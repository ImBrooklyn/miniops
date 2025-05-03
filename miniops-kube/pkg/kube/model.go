package kube

import (
    "fmt"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/cli/confer"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/cli/warehouse"
    "github.com/ImBrooklyn/miniops/miniops-kube/pkg/util"
)

type Deployment struct {
    App        *warehouse.Application
    Deployment *warehouse.Deployment
    Artifact   *warehouse.Artifact
    AppConfig  *confer.AppConfig
}

func (d *Deployment) AppName() string {
    if d == nil || d.App == nil {
        return ""
    }
    return d.App.Name
}

func (d *Deployment) Name() string {
    if d == nil || d.Deployment == nil {
        return ""
    }
    return d.Deployment.Name
}

func (d *Deployment) Namespace() string {
    if d == nil || d.Deployment == nil {
        return ""
    }
    return d.Deployment.Namespace
}

func (d *Deployment) ConfigmapName() string {
    if d == nil || d.Deployment == nil {
        return ""
    }
    return fmt.Sprintf("%s-configmap", d.Deployment.Name)
}

func (d *Deployment) SecretName() string {
    if d == nil || d.Deployment == nil {
        return ""
    }
    return fmt.Sprintf("%s-secret", d.Deployment.Name)
}

func (d *Deployment) ResourceName() string {
    if d == nil || d.Deployment == nil {
        return ""
    }
    return fmt.Sprintf("%s-resource", d.Deployment.Name)
}

func (d *Deployment) ServiceName() string {
    if d == nil || d.Deployment == nil {
        return ""
    }
    return fmt.Sprintf("%s-service", d.Deployment.Name)
}

func (d *Deployment) IngressName() string {
    if d == nil || d.Deployment == nil {
        return ""
    }
    return fmt.Sprintf("%s-ingress", d.Deployment.Name)
}

func (d *Deployment) IngressHost() string {
    if d == nil || d.Deployment == nil {
        return ""
    }
    return fmt.Sprintf("%s.miniops-k8s.com", d.Deployment.Name)
}

func ParseLivenessParams(params map[string]any) *LivenessProbeParams {
    return &LivenessProbeParams{
        InitialDelaySeconds: int32(util.GetOrDefault[int](params, "InitialDelaySeconds", 15)),
        TimeoutSeconds:      int32(util.GetOrDefault[int](params, "TimeoutSeconds", 5)),
        PeriodSeconds:       int32(util.GetOrDefault[int](params, "PeriodSeconds", 10)),
        SuccessThreshold:    int32(util.GetOrDefault[int](params, "SuccessThreshold", 1)),
        FailureThreshold:    int32(util.GetOrDefault[int](params, "FailureThreshold", 3)),
    }
}

func ParseReadinessParams(params map[string]any) *ReadinessProbeParams {
    return &ReadinessProbeParams{
        InitialDelaySeconds: int32(util.GetOrDefault[int](params, "InitialDelaySeconds", 5)),
        PeriodSeconds:       int32(util.GetOrDefault[int](params, "PeriodSeconds", 5)),
    }
}

type LivenessProbeParams struct {
    InitialDelaySeconds int32
    TimeoutSeconds      int32
    PeriodSeconds       int32
    SuccessThreshold    int32
    FailureThreshold    int32
}
type ReadinessProbeParams struct {
    InitialDelaySeconds int32
    PeriodSeconds       int32
}
