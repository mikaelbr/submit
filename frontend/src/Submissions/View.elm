module Submissions.View exposing (view)

import Submissions.Model exposing (..)
import Submissions.Messages exposing (..)
import Html exposing (Html, div, h1, h2, text)
import Html.Attributes exposing (class)


view : Model -> Html Msg
view model =
    let
        years =
            List.map viewYear model.years
    in
        div []
            [ h1 [] [ text "Your Talks" ]
            , div [ class "submissions" ] years
            ]


viewYear : Year -> Html Msg
viewYear year =
    let
        submissions =
            List.map viewSubmission year.submissions
    in
        div [ class "submissions__year" ]
            [ h2 [] [ text <| toString year.year ]
            , div [] submissions
            ]


viewSubmission : Submission -> Html Msg
viewSubmission submission =
    div [ class "submissions__submission" ]
        [ text submission.name ]
