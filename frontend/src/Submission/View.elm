module Submission.View exposing (view)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)
import Html
    exposing
        ( Html
        , div
        , text
        , input
        , textarea
        , button
        , img
        , h2
        , p
        , ul
        , li
        )
import Html.Attributes exposing (class, type_, value, src, placeholder)
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
            [ div [ class "input-section" ]
                [ h2 [] [ text "Title" ]
                , p [ class "input-description" ] [ text "Select an expressive and snappy title that captures the content of your talk without being too long. Remember that the title must be attractive and should make people curious." ]
                , input [ type_ "text", value submission.title, onInput Title ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Full Abstract" ]
                , p [ class "input-description" ] [ text "Give a concise description of the content and goals of your talk. Try not to exceed 300 words, as shorter and more to-the-point descriptions are more likely to be read by the participants." ]
                , textarea [ value submission.abstract, onInput Abstract ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Expected audience and code level" ]
                , p [ class "input-description" ] [ text "Who should attend this session? How will the participants benefit from attending? Explicitly state what your audience needs to know in order to get the most out of your presentation." ]
                , textarea [ value submission.intendedAudience, onInput IntendedAudience ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Presentation Format" ]
                , p [ class "input-description" ] [ text "In which format are you presenting your talk?" ]
                , input [ type_ "text", value submission.format, onInput Format ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Language" ]
                , p [ class "input-description" ] [ text "Which language will you be holding the talk in? Note it is permitted to use English in your slides, even though you may be talking in Norwegian. We generally recommend that you hold the talk in the language you are most comfortable with." ]
                , input [ type_ "text", value submission.language, onInput Language ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Outline (not public)" ]
                , p [ class "input-description" ] [ text "The information will be used by the Program Committee to review the details of your talk. The outline should be rough agenda for the talk, with a few keywords for each section, and with a rough estimate of the time spent on each." ]
                , textarea [ value submission.outline, onInput Outline ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Who are you?" ]
                , p [ class "input-description" ] [ text "Please give us a little bit of information about yourself. You can also add any additional speakers here. All of you will be shown in the program." ]
                , ul [] <|
                    List.map viewSpeaker submission.speakers
                , button [ onClick AddSpeaker ] [ text "Add speaker" ]
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Ready to save?" ]
                , p [ class "input-description" ] [ text "Push that button! Don't worry, you'll be able to come back and edit your talk at any time :)" ]
                , button [ onClick Save ] [ text "Save" ]
                ]
            ]
        ]


viewSpeaker : ( Int, Speaker ) -> Html Msg
viewSpeaker ( i, speaker ) =
    li [ class "speaker" ]
        [ input
            [ type_ "text"
            , value speaker.name
            , placeholder "Speaker name"
            , onInput <| SpeakerName i
            ]
            []
        , input
            [ type_ "text"
            , value speaker.email
            , placeholder "Speaker email"
            , onInput <| SpeakerEmail i
            ]
            []
        , textarea
            [ value speaker.bio
            , placeholder "Speaker bio"
            , onInput <| SpeakerBio i
            ]
            []
        , button [ onClick (RemoveSpeaker i) ] [ text "Remove speaker" ]
        ]


viewError : String -> Html Msg
viewError message =
    div [] [ text message ]
