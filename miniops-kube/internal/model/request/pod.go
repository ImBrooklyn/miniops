package request

type GetPodLogReq struct {
    Namespace string `json:"namespace" form:"namespace" binding:"required"`
    Pod       string `json:"pod" form:"pod" binding:"required"`
    Container string `json:"container" form:"container"`
}
