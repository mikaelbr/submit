module Usetoken.Model exposing (Model, init, initModel)

import Usetoken.Messages exposing (..)


initModel : String -> Model
initModel token =
    Model token


init : String -> ( Model, Cmd Msg )
init token =
    ( Model token, Cmd.none )


type alias Model =
    { token : String
    }
