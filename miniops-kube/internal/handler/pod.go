package handler

import (
    "bufio"
    "context"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/model/request"
    "github.com/ImBrooklyn/miniops/miniops-kube/pkg/kube"
    "github.com/gin-gonic/gin"
    "go.uber.org/zap"
    "io"
    "net/http"
)

func PodLog(c *gin.Context) {
    req := &request.GetPodLogReq{}
    err := c.ShouldBind(req)
    if err != nil {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"message": err.Error()})
        return
    }

    ctx, cancel := context.WithCancel(c.Request.Context())
    defer cancel()

    stream, err := kube.PodLog(ctx, req.Namespace, req.Pod, req.Container)
    if err != nil {
        c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{"message": err.Error()})
        return
    }

    defer func(stream io.ReadCloser) {
        err := stream.Close()
        if err != nil {
            zap.S().Errorf("error closing stream: %v", err)
        }
    }(stream)

    logsCh := make(chan string)
    errCh := make(chan error)
    clientDone := c.Writer.CloseNotify()
    go func() {
        defer close(logsCh)
        defer close(errCh)
        scanner := bufio.NewScanner(stream)
        for scanner.Scan() {
            select {
            case <-clientDone:
                zap.L().Debug("client closed, stopping reading logs")
                return
            default:
                logsCh <- scanner.Text()
            }
        }
        zap.L().Info("scanner closed, stopping reading logs")
        if err := scanner.Err(); err != nil {
            zap.S().Debugf("error scanning logs: %v", err)
            errCh <- err
        } else {
            zap.L().Debug("stopped reading logs")
        }
    }()

    for {
        select {
        case <-clientDone:
            zap.L().Debug("client closed, closing sse")
            return
        case line, ok := <-logsCh:
            if !ok {
                zap.L().Debug("log chan closed, closing sse")
                return
            }
            c.SSEvent("log", line)
            c.Writer.Flush()
        case err, ok := <-errCh:
            if !ok {
                return
            }
            c.SSEvent("error", err.Error())
            c.Writer.Flush()
            return
        }
    }

}
