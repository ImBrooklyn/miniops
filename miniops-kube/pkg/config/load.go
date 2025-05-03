package config

import (
    "github.com/spf13/viper"
    "go.uber.org/zap"
)

func LoadConfig(cfg any) {
    viper.AutomaticEnv()

    env := viper.GetString("MINIOPS_ENV")
    if env == "" {
        env = "dev"
    }
    zap.S().Infof("%s: %s", "MINIOPS_ENV", env)

    src := viper.GetString("MINIOPS_CFG_SRC")
    switch src {
    case "nacos":
        loadNacosConfig(env, cfg)
    case "local":
        loadLocalConfig(env, cfg)
    default:
        loadLocalConfig(env, cfg)
    }

}
