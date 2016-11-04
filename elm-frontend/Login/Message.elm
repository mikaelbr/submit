module Login.Message exposing (Msg(..))

import HttpBuilder exposing (..)


type Msg
    = Email String
    | SubmitEmail
    | SubmitFailed (Error ())
    | SubmitSucceeded (Response ())
