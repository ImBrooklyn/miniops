package global

import "github.com/ImBrooklyn/miniops/miniops-kube/internal/conf"
import ut "github.com/go-playground/universal-translator"

var (
    Translator *ut.Translator
    Config     *conf.AppConfig
)
