package response

type DeploymentResp struct {
    Name    string            `json:"name"`
    Status  *DeploymentStatus `json:"status"`
    Image   string            `json:"image"`
    Service *ServiceInfo      `json:"service,omitempty"`
    Ingress *IngressInfo      `json:"ingress,omitempty"`
}

type DeploymentStatus struct {
    Replicas          int `json:"replicas"`
    ReadyReplicas     int `json:"readyReplicas"`
    AvailableReplicas int `json:"availableReplicas"`
}

type ServiceInfo struct {
    Name  string   `json:"name"`
    Ports []string `json:"ports"`
}

type IngressRoute struct {
    Path string `json:"path"`
    Port int    `json:"port"`
}

type IngressInfo struct {
    Name   string          `json:"name"`
    Host   string          `json:"host"`
    Routes []*IngressRoute `json:"routes"`
}
