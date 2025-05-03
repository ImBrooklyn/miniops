package warehouse

import (
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/cli"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/global"
    "resty.dev/v3"
    "sync"
)

var (
    restyClient *resty.Client
    once        sync.Once
)

func client() *resty.Client {
    once.Do(func() {
        restyClient = resty.New().
            SetBaseURL(global.Config.Warehouse.URL).
            SetHeader(cli.MiniopsAuthKey, global.Config.Cli.AuthKey).
            SetHeader(cli.MiniopsAuthSecret, global.Config.Cli.AuthSecret)
    })
    return restyClient
}
