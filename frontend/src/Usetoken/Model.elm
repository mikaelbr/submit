module Usetoken.Model exposing (Model, init, initModel)

import Usetoken.Messages exposing (..)
import Flags exposing (Flags)


initModel : Flags -> String -> Model
initModel flags token =
    Model flags token


init : Flags -> String -> ( Model, Cmd Msg )
init flags token =
    ( Model flags token, Cmd.none )


type alias Model =
    { flags : Flags
    , token : String
    }
