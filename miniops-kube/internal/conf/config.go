package conf

type AppConfig struct {
    AppName   string          `mapstructure:"app_name" yaml:"app_name" json:"app_name"`
    Port      int             `mapstructure:"port" yaml:"port" json:"port"`
    Locale    string          `mapstructure:"locale" yaml:"locale" json:"locale"`
    Cli       CliConfig       `mapstructure:"cli" yaml:"cli" json:"cli"`
    Warehouse WarehouseConfig `mapstructure:"warehouse" yaml:"warehouse" json:"warehouse"`
    Confer    ConferConfig    `mapstructure:"confer" yaml:"confer" json:"confer"`
}

type CliConfig struct {
    AuthKey    string `mapstructure:"auth_key" yaml:"auth_key" json:"auth_key"`
    AuthSecret string `mapstructure:"auth_secret" yaml:"auth_secret" json:"auth_secret"`
}

type WarehouseConfig struct {
    URL string `mapstructure:"url" yaml:"url" json:"url"`
}

type ConferConfig struct {
    URL string `mapstructure:"url" yaml:"url" json:"url"`
}
