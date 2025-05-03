package request

type CreateDeploymentReq struct {
    DeploymentName string `json:"deploymentName" binding:"required"`
    Version        string `json:"version" binding:"required,semver"`
}

type RemoveDeploymentReq struct {
    DeploymentName string `json:"deploymentName" binding:"required"`
}

type ScaleDeploymentReq struct {
    DeploymentName string `uri:"name" binding:"required"`
    Replicas       int    `uri:"replicas" binding:"min=0,max=10"`
}
