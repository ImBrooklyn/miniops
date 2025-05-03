package router

import (
    "github.com/gin-gonic/gin"
    "net/http"
)

func InitProbeRouter(router *gin.RouterGroup) {
    group := router.Group("probe")

    group.GET("liveness", func(c *gin.Context) {
        c.JSON(http.StatusOK, gin.H{
            "status": "OK",
        })
    })

    group.GET("readiness", func(c *gin.Context) {
        c.JSON(http.StatusOK, gin.H{
            "status": "OK",
        })
    })
}
