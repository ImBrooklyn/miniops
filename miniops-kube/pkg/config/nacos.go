package config

import (
    "fmt"
    "github.com/nacos-group/nacos-sdk-go/v2/clients"
    "github.com/nacos-group/nacos-sdk-go/v2/common/constant"
    "github.com/nacos-group/nacos-sdk-go/v2/vo"
    "github.com/spf13/viper"
    "gopkg.in/yaml.v3"
)

func loadNacosConfig(env string, cfg any) {
    configFileName := fmt.Sprintf("configs/nacos-%s.yaml", env)
    v := viper.New()
    v.SetConfigFile(configFileName)
    if err := v.ReadInConfig(); err != nil {
        panic(err)
    }

    nacosConfig := &NacosConfig{}
    if err := v.Unmarshal(&nacosConfig); err != nil {
        panic(err)
    }

    sc := []constant.ServerConfig{
        {IpAddr: nacosConfig.Host, Port: nacosConfig.Port},
    }

    cc := constant.ClientConfig{
        NamespaceId:         nacosConfig.Namespace,
        TimeoutMs:           5000,
        NotLoadCacheAtStart: true,
        LogDir:              "/tmp/nacos/log",
        CacheDir:            "/tmp/nacos/cache",
        LogLevel:            "debug",
    }

    configClient, err := clients.CreateConfigClient(map[string]any{
        "serverConfigs": sc,
        "clientConfig":  cc,
    })

    if err != nil {
        panic(err)
    }

    content, err := configClient.GetConfig(vo.ConfigParam{
        DataId: nacosConfig.DataId,
        Group:  nacosConfig.Group,
    })

    if err != nil {
        panic(err)
    }

    err = yaml.Unmarshal([]byte(content), cfg)
    if err != nil {
        panic(err)
    }
}
