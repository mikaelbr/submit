module Submission.View exposing (view)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)
import Html exposing (Html, div, text)


view : Model -> Html Msg
view model =
    div [] [ text model.entry ]
