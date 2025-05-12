package router

import (
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/handler"
    "github.com/gin-gonic/gin"
)

func InitPodRouter(router *gin.RouterGroup) {
    group := router.Group("/pod")
    group.GET("/log", handler.PodLog)
}
