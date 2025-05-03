package initialize

import (
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/global"
    "github.com/gin-gonic/gin/binding"
    "github.com/go-playground/locales/en"
    "github.com/go-playground/locales/zh"
    ut "github.com/go-playground/universal-translator"
    "github.com/go-playground/validator/v10"
    entrans "github.com/go-playground/validator/v10/translations/en"
    zhtrans "github.com/go-playground/validator/v10/translations/zh"
)

func Locale() {
    v, ok := binding.Validator.Engine().(*validator.Validate)
    if !ok {
        panic("register translator failed")
    }

    zhT := zh.New()
    enT := en.New()

    uni := ut.New(enT, zhT, enT)
    locale := global.Config.Locale
    translator, ok := uni.GetTranslator(locale)
    if !ok {
        panic("get translator failed, locale: " + locale)
    }
    var err error
    switch locale {
    case "en":
        err = entrans.RegisterDefaultTranslations(v, translator)
    case "zh":
        err = zhtrans.RegisterDefaultTranslations(v, translator)
    default:
        err = entrans.RegisterDefaultTranslations(v, translator)
    }

    if err != nil {
        panic(err)
    }

    global.Translator = &translator
}
