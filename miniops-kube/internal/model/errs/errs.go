package errs

import "errors"

var (
    ErrNotModified = errors.New("not modified")
    ErrNotFound    = errors.New("not found")
    ErrRestReq     = errors.New("bad request")
    ErrBadRequest  = errors.New("bad request")
)
