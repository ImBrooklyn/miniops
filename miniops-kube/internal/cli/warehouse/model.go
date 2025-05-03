package warehouse

type Application struct {
    ID          int            `json:"id,omitempty"`
    Name        string         `json:"name,omitempty"`
    Liveness    *AppExposure   `json:"liveness,omitempty"`
    Readiness   *AppExposure   `json:"readiness,omitempty"`
    Services    []*AppExposure `json:"services,omitempty"`
    Deployments []*Deployment  `json:"deployments,omitempty"`
}

type AppExposure struct {
    Path   string         `json:"path,omitempty"`
    Port   int            `json:"port,omitempty"`
    Params map[string]any `json:"params,omitempty"`
}

type Deployment struct {
    Name      string `json:"name,omitempty"`
    AppName   string `json:"appName,omitempty"`
    Namespace string `json:"namespace,omitempty"`
    Suffix    string `json:"suffix,omitempty"`
    Replicas  int    `json:"replicas,omitempty"`
}

type Artifact struct {
    ID            int    `json:"artifactId,omitempty"`
    AppName       string `json:"appName,omitempty"`
    Version       string `json:"version,omitempty"`
    Major         int    `json:"major,omitempty"`
    Minor         int    `json:"minor,omitempty"`
    Patch         int    `json:"patch,omitempty"`
    Prerelease    string `json:"prerelease,omitempty"`
    BuildMetadata any    `json:"buildMetadata,omitempty"`
}
