module Login.Message exposing (Msg(..))

import Http exposing (Error)


type Msg
    = Email String
    | SubmitEmail
    | Submit (Result Error String)
