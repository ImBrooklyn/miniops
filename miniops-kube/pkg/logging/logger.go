package logging

import "go.uber.org/zap"

func InitLogger() {
    cfg := zap.NewDevelopmentConfig()
    cfg.OutputPaths = []string{
        // adds local out
        "stdout",
    }
    logger, err := cfg.Build()
    if err != nil {
        panic(err)
    }
    zap.ReplaceGlobals(logger)
}
