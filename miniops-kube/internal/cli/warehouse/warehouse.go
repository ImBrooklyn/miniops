package warehouse

import (
    "context"
    "fmt"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/model/errs"
    "go.uber.org/zap"
)

func GetApp(ctx context.Context, appName string) (*Application, error) {
    response, err := client().R().
        SetContext(ctx).
        SetResult(&Application{}).
        Get(fmt.Sprintf("/application/%s", appName))
    if err != nil {
        zap.S().Errorf("failed to get application: %s", err.Error())
        return nil, errs.ErrRestReq
    }

    if response.IsError() {
        zap.S().Errorf("failed to get application: %s", response.Error())
        return nil, errs.ErrBadRequest
    }

    return response.Result().(*Application), nil
}

func GetArtifact(ctx context.Context, appName, version string) (*Artifact, error) {
    response, err := client().R().
        SetContext(ctx).
        SetResult(&Artifact{}).
        Get(fmt.Sprintf("/artifact/%s/%s", appName, version))
    if err != nil {
        zap.S().Errorf("failed to get artifact: %s", err.Error())
        return nil, errs.ErrRestReq
    }

    if response.IsError() {
        zap.S().Errorf("failed to get artifact: %s", response.Error())
        return nil, errs.ErrBadRequest
    }

    return response.Result().(*Artifact), nil
}

func GetDeployment(ctx context.Context, deploymentName string) (*Deployment, error) {
    response, err := client().R().
        SetContext(ctx).
        SetResult(&Deployment{}).
        Get(fmt.Sprintf("/deployment/%s", deploymentName))
    if err != nil {
        zap.S().Errorf("failed to get deployment: %s", err.Error())
        return nil, errs.ErrRestReq
    }

    if response.IsError() {
        zap.S().Errorf("failed to get deployment: %s", response.Error())
        return nil, errs.ErrBadRequest
    }

    return response.Result().(*Deployment), nil
}
