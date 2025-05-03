package main

import (
    "fmt"
    "github.com/ImBrooklyn/miniops/miniops-kube/initialize"
    "github.com/ImBrooklyn/miniops/miniops-kube/pkg/logging"
    "go.uber.org/zap"
    "os"
    "os/signal"
    "syscall"
)

func main() {
    logging.InitLogger()
    config := initialize.Config()
    router := initialize.Router()
    initialize.Locale()
    initialize.Validator()

    zap.S().Infof("Starting server at: %d", config.Port)
    go func() {
        err := router.Run(fmt.Sprintf(":%d", config.Port))
        if err != nil {
            zap.S().Panic("boot failed, err: %s", err.Error())
        }
    }()

    quit := make(chan os.Signal)
    signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
    <-quit
    zap.S().Info("shutting down")
}
