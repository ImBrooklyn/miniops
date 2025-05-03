package confer

import (
    "context"
    "errors"
    "fmt"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/model/errs"
    "go.uber.org/zap"
    "net/http"
)

func GetAppConfig(ctx context.Context, appName, namespace, deployment string) (*AppConfig, error) {

    response, err := client().R().
        SetContext(ctx).
        SetResult(&AppConfig{}).
        SetQueryParam("appName", appName).
        SetQueryParam("namespace", namespace).
        SetQueryParam("deployment", deployment).
        Get(fmt.Sprintf("/configuration/app"))
    if err != nil {
        zap.S().Errorf("failed to get app config: %s", err.Error())
        return nil, errs.ErrRestReq
    }

    if response.IsError() {
        zap.S().Errorf("failed to get app config with code [%d]: %s", response.StatusCode(), string(response.Bytes()))
        if response.StatusCode() == http.StatusNotFound {
            return nil, errs.ErrNotFound
        }
        return nil, errors.New(string(response.Bytes()))
    }

    return response.Result().(*AppConfig), nil
}
