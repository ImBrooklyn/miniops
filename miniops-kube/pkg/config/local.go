package config

import (
    "fmt"
    "github.com/spf13/viper"
)

func loadLocalConfig(env string, cfg any) {
    configFileName := fmt.Sprintf("configs/local-%s.yaml", env)
    v := viper.New()
    v.SetConfigFile(configFileName)
    if err := v.ReadInConfig(); err != nil {
        panic(err)
    }

    if err := v.Unmarshal(cfg); err != nil {
        panic(err)
    }
}
