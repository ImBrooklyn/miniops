package handler

import (
    "context"
    "errors"
    "fmt"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/cli/confer"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/cli/warehouse"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/model/errs"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/model/request"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/model/response"
    "github.com/ImBrooklyn/miniops/miniops-kube/pkg/kube"
    "github.com/gin-gonic/gin"
    "go.uber.org/zap"
    "net/http"
    "time"
)

func Deploy(c *gin.Context) {
    req := &request.CreateDeploymentReq{}
    err := c.ShouldBindJSON(req)
    if err != nil {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"message": err.Error()})
        return
    }

    ctx, cancel := context.WithTimeout(c.Request.Context(), time.Second*500)
    defer cancel()

    deployment, err := getKubeDeployment(ctx, req.DeploymentName, req.Version)
    if err != nil {
        c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{"message": err.Error()})
        return
    }

    zap.S().Infof("Deploying deployment: %#v", deployment)
    if deployment.AppConfig != nil {
        err := kube.CreateConfig(ctx, deployment)
        if err != nil {
            c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
                "message": "failed to apply config",
            })
            return
        }
    }

    if err := kube.ApplyDeployment(ctx, deployment); err != nil {
        zap.S().Errorf("failed to apply deployment: %s", err.Error())
        c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
            "message": "failed to apply deployment",
        })
        return
    }

    c.JSON(http.StatusOK, gin.H{
        "message": "deployment created",
    })
}

func DeleteDeployment(c *gin.Context) {
    name := c.Param("name")
    if name == "" {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
            "message": "invalid deployment name",
        })
    }

    ctx, cancel := context.WithTimeout(c.Request.Context(), time.Second*500)
    defer cancel()
    d, err := getKubeDeployment(ctx, name, "")
    if err != nil {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
            "message": err.Error(),
        })
        return
    }

    err = kube.DeleteDeployment(ctx, d)
    if err != nil {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
            "message": "failed to delete deployment",
        })
        return
    }

    c.JSON(http.StatusOK, gin.H{
        "message": "deployment removed",
    })
}

func Expose(c *gin.Context) {
    name := c.Param("name")
    if name == "" {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
            "message": "invalid deployment name",
        })
        return
    }
    ctx, cancel := context.WithTimeout(c.Request.Context(), time.Second*500)
    defer cancel()
    deployment, err := getKubeDeployment(ctx, name, "")
    if err != nil {
        c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
            "message": err.Error(),
        })
        return
    }
    if err := kube.ExposeDeployment(ctx, deployment); err != nil {
        c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
            "message": "failed to expose deployment",
        })

        return
    }

    c.JSON(http.StatusOK, gin.H{
        "message": "deployment exposed",
    })
}

func DeleteExposure(c *gin.Context) {
    name := c.Param("name")
    if name == "" {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
            "message": "invalid deployment name",
        })
        return
    }

    ctx, cancel := context.WithTimeout(c.Request.Context(), time.Second*500)
    defer cancel()
    deployment, err := getKubeDeployment(ctx, name, "")
    if err != nil {
        c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
            "message": err.Error(),
        })
        return
    }

    if err := kube.DeleteDeploymentSvc(ctx, deployment); err != nil {
        c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
            "message": "failed to delete exposure",
        })
        return
    }
    c.JSON(http.StatusOK, gin.H{
        "message": "exposure deleted",
    })
}

func Scale(c *gin.Context) {
    req := &request.ScaleDeploymentReq{}

    err := c.ShouldBindUri(req)

    if err != nil {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"message": err.Error()})
        return
    }

    ctx, cancel := context.WithTimeout(c.Request.Context(), time.Second*5)
    defer cancel()
    d, err := getKubeDeployment(ctx, req.DeploymentName, "")
    if err != nil {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
            "message": "failed to get deployment",
        })
        return
    }
    err = kube.ScaleDeployment(ctx, d, req.Replicas)
    if err != nil {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
            "message": "failed to scale deployment",
        })
        return
    }

    c.JSON(http.StatusOK, gin.H{
        "msg": "deployment successfully scaled",
    })
}

func DeploymentInfo(c *gin.Context) {
    name := c.Param("name")
    if name == "" {
        c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
            "msg": "deployment name is empty",
        })
        return
    }

    ctx, cancel := context.WithTimeout(c.Request.Context(), time.Second*500)
    defer cancel()

    d, err := getKubeDeployment(ctx, name, "")
    if err != nil {
        c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
            "message": err.Error(),
        })
        return
    }

    deployment, err := kube.GetDeployment(ctx, d)
    if err != nil {
        c.JSON(http.StatusBadRequest, gin.H{
            "message": err.Error(),
        })
        return
    }

    var serviceInfo *response.ServiceInfo
    service, err := kube.GetService(ctx, d)
    if err == nil {
        var ports []string
        servicePorts := service.Spec.Ports
        for _, port := range servicePorts {
            ports = append(ports, fmt.Sprintf("%d:%d/%s", port.Port, port.TargetPort.IntVal, port.Protocol))
        }
        serviceInfo = &response.ServiceInfo{
            Name:  service.Name,
            Ports: ports,
        }
    }

    var ingressInfo *response.IngressInfo
    ingress, err := kube.GetIngress(ctx, d)
    if err == nil {
        var routes []*response.IngressRoute
        paths := ingress.Spec.Rules[0].HTTP.Paths
        for _, path := range paths {
            routes = append(routes, &response.IngressRoute{
                Path: path.Path,
                Port: int(path.Backend.Service.Port.Number),
            })
        }
        ingressInfo = &response.IngressInfo{
            Name:   ingress.Name,
            Host:   ingress.Spec.Rules[0].Host,
            Routes: routes,
        }
    }

    c.JSON(http.StatusOK, &response.DeploymentResp{
        Name:  deployment.Name,
        Image: deployment.Spec.Template.Spec.Containers[0].Image, // TODO
        Status: &response.DeploymentStatus{
            Replicas:          int(deployment.Status.Replicas),
            ReadyReplicas:     int(deployment.Status.ReadyReplicas),
            AvailableReplicas: int(deployment.Status.AvailableReplicas),
        },
        Service: serviceInfo,
        Ingress: ingressInfo,
    })
}

func getKubeDeployment(ctx context.Context, name, version string) (*kube.Deployment, error) {
    deployment, err := warehouse.GetDeployment(ctx, name)
    if err != nil {
        return nil, errors.New("failed to get deployment")
    }

    application, err := warehouse.GetApp(ctx, deployment.AppName)
    if err != nil {
        return nil, errors.New("failed to get application")
    }

    var artifact *warehouse.Artifact
    if version != "" {
        artifact, err = warehouse.GetArtifact(ctx, deployment.AppName, version)
        if err != nil {
            return nil, errors.New("failed to get artifact")
        }
    }

    appConfig, err := confer.GetAppConfig(ctx, application.Name, deployment.Namespace, deployment.Name)
    if err != nil {
        if errors.Is(err, errs.ErrNotFound) {
            appConfig = nil
        } else {
            return nil, errors.New("failed to get app config")
        }
    }

    return &kube.Deployment{
        App:        application,
        Deployment: deployment,
        Artifact:   artifact,
        AppConfig:  appConfig,
    }, nil
}
