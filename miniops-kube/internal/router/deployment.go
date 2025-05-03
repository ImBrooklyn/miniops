package router

import (
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/handler"
    "github.com/gin-gonic/gin"
)

func InitDeploymentRouter(router *gin.RouterGroup) {
    group := router.Group("/deployment")

    group.POST("", handler.Deploy)
    group.PUT("", handler.Deploy)
    group.DELETE("/:name", handler.DeleteDeployment)
    group.POST("/expose/:name", handler.Expose)
    group.DELETE("/expose/:name", handler.DeleteExposure)
    group.PUT("/scale/:name/:replicas", handler.Scale)
    group.GET("/:name", handler.DeploymentInfo)
}
