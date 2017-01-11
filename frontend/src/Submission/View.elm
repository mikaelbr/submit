module Submission.View exposing (view)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)
import Html exposing (Html, div, text, input, textarea, button)
import Html.Attributes exposing (class, type_, value)
import Html.Events exposing (onInput, onClick)
import Backend.Network exposing (RequestStatus(..))


view : Model -> Html Msg
view model =
    case model.submission of
        Initial ->
            div [] []

        Loading ->
            div [] [ text "Loading" ]

        Complete submission ->
            viewSubmission submission

        Error message ->
            viewError message


viewSubmission : Submission -> Html Msg
viewSubmission submission =
    div [ class "submission" ]
        [ input [ type_ "text", value submission.title ] []
        , textarea [ value submission.abstract, onInput Abstract ] []
        , input [ type_ "text", value submission.format ] []
        , textarea [ value submission.intendedAudience ] []
        , input [ type_ "text", value submission.language ] []
        , textarea [ value submission.outline ] []
        , button [ onClick Save ] [ text "Save" ]
        ]


viewError : String -> Html Msg
viewError message =
    div [] [ text message ]
