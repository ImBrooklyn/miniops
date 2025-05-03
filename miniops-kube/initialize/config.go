package initialize

import (
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/conf"
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/global"
    "github.com/ImBrooklyn/miniops/miniops-kube/pkg/config"
)

func Config() *conf.AppConfig {
    config.LoadConfig(&global.Config)
    return global.Config
}
