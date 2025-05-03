package confer

type AppConfig struct {
    AppName          string          `json:"appName"`
    Namespace        string          `json:"namespace"`
    Deployment       string          `json:"deployment"`
    ConfigProperties []*PropertyItem `json:"configProperties"`
    Resources        []*Resource     `json:"resources"`
}

type PropertyItem struct {
    PropKey   string `json:"propKey"`
    PropValue string `json:"propValue"`
    IsSecret  bool   `json:"isSecret"`
}

type Resource struct {
    AppName            string          `json:"appName"`
    Namespace          string          `json:"namespace"`
    Type               string          `json:"type"`
    ResourceProperties []*PropertyItem `json:"resourceProperties"`
}
