package initialize

import (
    "github.com/ImBrooklyn/miniops/miniops-kube/internal/global"
    "github.com/ImBrooklyn/semver-go/semver"
    "github.com/gin-gonic/gin/binding"
    ut "github.com/go-playground/universal-translator"
    "github.com/go-playground/validator/v10"
)

func registerSemverValidator(v *validator.Validate) {
    _ = v.RegisterValidation("semver", func(fld validator.FieldLevel) bool {
        version := fld.Field().String()
        _, err := semver.Parse(version)
        return err == nil
    })
    _ = v.RegisterTranslation("semver", *global.Translator,
        func(ut ut.Translator) error {
            return ut.Add("semver", "{0} Invalid semver", true) // see universal-translator for details
        },
        func(ut ut.Translator, fe validator.FieldError) string {
            t, _ := ut.T("semver", fe.Field())
            return t
        },
    )
}

func Validator() {
    if v, ok := binding.Validator.Engine().(*validator.Validate); ok {
        registerSemverValidator(v)
    }
}
