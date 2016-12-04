module Usetoken.View exposing (view)

import Html exposing (Html, div, text)
import Usetoken.Model exposing (..)
import Usetoken.Messages exposing (..)


view : Model -> Html Msg
view model =
    div [] [ text model.token ]
