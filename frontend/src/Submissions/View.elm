module Submissions.View exposing (view)

import Submissions.Model exposing (..)
import Submissions.Messages exposing (..)
import Html exposing (Html, div, text)


view : Model -> Html Msg
view model =
    div [] [ text "Submissions" ]
