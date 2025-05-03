package confer

import (
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/cli"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/global"
    "go.uber.org/zap"
    "resty.dev/v3"
    "sync"
)

var (
    restyClient *resty.Client
    once        sync.Once
)

func client() *resty.Client {
    once.Do(func() {
        zap.S().Infof("Initializing confer client, url: %s", global.Config.Confer.URL)
        restyClient = resty.New().
            SetBaseURL(global.Config.Confer.URL).
            SetHeader(cli.MiniopsAuthKey, global.Config.Cli.AuthKey).
            SetHeader(cli.MiniopsAuthSecret, global.Config.Cli.AuthSecret)
    })
    return restyClient
}
