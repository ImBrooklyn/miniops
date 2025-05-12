package initialize

import (
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/router"
    "github.com/gin-gonic/gin"
    "net/http"
)

func Router() *gin.Engine {
    g := gin.Default()

    probeGrp := g.Group("probe")
    probeGrp.GET("liveness", func(c *gin.Context) {
        c.JSON(http.StatusOK, gin.H{
            "status": "OK",
        })
    })

    probeGrp.GET("readiness", func(c *gin.Context) {
        c.JSON(http.StatusOK, gin.H{
            "status": "OK",
        })
    })

    apiGrp := g.Group("/kube")

    router.InitDeploymentRouter(apiGrp)
    router.InitPodRouter(apiGrp)
    return g
}
