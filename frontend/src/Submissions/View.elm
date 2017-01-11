module Submissions.View exposing (view)

import Submissions.Model exposing (..)
import Submissions.Messages exposing (..)
import Html exposing (Html, div, h1, h2, text, a, button, img)
import Html.Attributes exposing (class, href, src)
import Html.Events exposing (onClick)
import Nav.Nav exposing (toHash)
import Nav.Model
import Backend.Network exposing (RequestStatus(..))


view : Model -> Html Msg
view model =
    case model.submissions of
        Initial ->
            div [] []

        Loading ->
            div [] [ text "Loading" ]

        Complete submissions ->
            viewSubmissions submissions

        Error message ->
            viewError message


viewError : String -> Html Msg
viewError message =
    div [] [ text message ]


viewSubmissions : Submissions -> Html Msg
viewSubmissions submissions =
    let
        years =
            List.map viewYear submissions.years
    in
        div [ class "wrapper" ]
            [ div [ class "logo-wrapper" ] [ img [ src "assets/logo.png", class "logo" ] [] ]
            , div [ class "submissions-list" ]
                [ button [ onClick CreateTalk, class "new-talk" ] [ text "New talk" ]
                , h1 [] [ text "Your Talks" ]
                , div [ class "submissions" ] years
                ]
            ]


viewYear : Year -> Html Msg
viewYear year =
    let
        submissions =
            List.map viewSubmission year.submissions
    in
        div [ class "submissions__year" ]
            [ h2 [] [ text year.year ]
            , div [] submissions
            ]


viewSubmission : Submission -> Html Msg
viewSubmission submission =
    div [ class "submissions__submission" ]
        [ a [ href << toHash <| Nav.Model.Submission submission.id ]
            [ text submission.name ]
        ]
