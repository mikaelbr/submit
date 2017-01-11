module Submission.View exposing (view)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)
import Html exposing (Html, div, text, input, textarea, button, img)
import Html.Attributes exposing (class, type_, value, src)
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
    div [ class "wrapper" ]
        [ div [ class "logo-wrapper" ] [ img [ src "assets/logo.png", class "logo" ] [] ]
        , div [ class "edit-submission" ]
            [ input [ type_ "text", value submission.title, onInput Title ] []
            , textarea [ value submission.abstract, onInput Abstract ] []
            , input [ type_ "text", value submission.format, onInput Format ] []
            , textarea [ value submission.intendedAudience, onInput IntendedAudience ] []
            , input [ type_ "text", value submission.language, onInput Language ] []
            , textarea [ value submission.outline, onInput Outline ] []
            , button [ onClick Save ] [ text "Save" ]
            ]
        ]


viewError : String -> Html Msg
viewError message =
    div [] [ text message ]
