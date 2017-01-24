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
        , h3
        , p
        , a
        , ul
        , li
        )
import Html.Attributes exposing (class, type_, value, src, placeholder, href)
import Html.Events exposing (onInput, onClick)
import Nav.Nav exposing (toHash)
import Nav.Model
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
        [ div [ class "sticky-footer" ]
            [ div [ class "sticky-footer-content" ]
                [ div [] [ a [ href << toHash <| Nav.Model.Submissions ] [ button [ class "button-back" ] [ text "Back to list" ] ] ]
                , div [] [ button [ class <| "button-save " ++ hideIfNotEditable submission.editable, onClick Save ] [ text "Save now" ] ]
                ]
            ]
        , div [ class "logo-wrapper" ] [ img [ src "assets/logo.png", class "logo" ] [] ]
        , div [ class "edit-submission" ]
            [ div [ class <| "cant-edit-message " ++ hideIfEditable submission.editable ] [ text "You can't edit this talk. Only talks from this year's JavaZone is editable." ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Title" ]
                , p [ class "input-description" ] [ text "Select an expressive and snappy title that captures the content of your talk without being too long. Remember that the title must be attractive and should make people curious." ]
                , input [ type_ "text", value submission.title, onInput Title ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Language" ]
                , p [ class "input-description" ] [ text "Which language will you be holding the talk in? Note it is permitted to use English in your slides, even though you may be talking in Norwegian. We generally recommend that you hold the talk in the language you are most comfortable with." ]
                , input [ type_ "text", value submission.language, onInput Language ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Description" ]
                , p [ class "input-description" ] [ text "Give a concise description of the content and goals of your talk. Try not to exceed 300 words, as shorter and more to-the-point descriptions are more likely to be read by the participants." ]
                , textarea [ value submission.abstract, onInput Abstract ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Expected audience and code level" ]
                , p [ class "input-description" ] [ text "Who should attend this session?  How will the participants benefit from attending?  Please indicate how code will factor into your presentation (for example \"no code\", \"code in slides\" or \"live coding\")." ]
                , textarea [ class "small-textarea", value submission.intendedAudience, onInput IntendedAudience ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Presentation format" ]
                , p [ class "input-description" ] [ text "In which format are you presenting your talk?" ]
                , input [ type_ "text", value submission.format, onInput Format ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Presentation length" ]
                , p [ class "input-description" ] [ text "Please select the length of the presentation (in minutes). Presentations can have a length of 45 or 60 minutes. Including Q&A" ]
                , input [ type_ "text", value submission.length, onInput Length ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Experience level" ]
                , p [ class "input-description" ] [ text "Who is your talk pitched at?  Beginners, Experts or perhaps those in between?" ]
                , input [ type_ "text", value submission.level, onInput Level ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Suggested keywords" ]
                , p [ class "input-description" ] [ text "Suggest up to five keywords that describe your talk. These will be used by the program committee to group the talks into subjects and categories. We reserve the right to reclassify talks to make then fit into this years selected categories." ]
                , input [ type_ "text", value submission.suggestedKeywords, onInput SuggestedKeywords, placeholder "Keyword 1, Keyword 2, ..." ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Outline (not public)" ]
                , p [ class "input-description" ] [ text "The information will be used by the Program Committee to review the details of your talk. The outline should be rough agenda for the talk, with a few keywords for each section, and with a rough estimate of the time spent on each. Omitting this section will reduce the chances of your submission being accepted." ]
                , textarea [ value submission.outline, onInput Outline ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Equipment (not public)" ]
                , p [ class "input-description" ] [ text "Please specify any additional special equipment you may need. Note that all get access to WiFi and a projector." ]
                , textarea [ class "small-textarea", value submission.equipment, onInput Equipment ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Additional information for the Program Committee" ]
                , p [ class "input-description" ] [ text "Please include any information relevant to the Program Committee. Here you can write a few words about your motivation for speaking at JavaZone, and optionally include links to videos and slides from previous speaker engagements, or other links that tell us about you (e.g. your GitHub profile)." ]
                , textarea [ class "small-textarea", value submission.infoToProgramCommittee, onInput InfoToProgramCommittee ] []
                ]
            , div [ class "input-section" ]
                [ div [ class "flex-header" ]
                    [ h2 [ class "flex-header-element" ] [ text "Who are you?" ]
                    , div [ class "flex-header-element" ]
                        [ button [ onClick AddSpeaker, class "button-new" ] [ text "Add second speaker" ]
                        ]
                    ]
                , p [ class "input-description" ] [ text "Please give us a little bit of information about yourself. You can also add any additional speakers here. All of you will be shown in the program." ]
                , ul [ class "speakers" ] <|
                    List.map viewSpeaker submission.speakers
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "That's it! You're done!" ]
                , p [ class "input-description" ] [ text "Feel free to come back anytime and edit your talk!" ]
                , div [ class "sticky-footer-filler" ] []
                ]
            ]
        ]


viewSpeaker : ( Int, Speaker ) -> Html Msg
viewSpeaker ( i, speaker ) =
    li [ class "speaker" ]
        [ div [ class "flex-header" ]
            [ h2 [ class "flex-header-element" ] [ text ("Speaker " ++ toString (i + 1)) ]
            , div [ class "flex-header-element" ]
                [ button [ onClick (RemoveSpeaker i), class "button-delete" ] [ text "Remove speaker" ]
                ]
            ]
        , div [ class "speaker-input-section" ]
            [ h3 [] [ text "Speakers name" ]
            , input [ type_ "text", value speaker.name, placeholder "Speaker name", onInput <| SpeakerName i ] []
            ]
        , div [ class "speaker-input-section" ]
            [ h3 [] [ text "Speakers email (not public)" ]
            , input [ type_ "text", value speaker.email, placeholder "Speaker email", onInput <| SpeakerEmail i ] []
            ]
        , div [ class "speaker-input-section" ]
            [ h3 [] [ text "Short description of the speaker (try not to exceed 150 words)" ]
            , textarea [ value speaker.bio, placeholder "Speaker bio", onInput <| SpeakerBio i ] []
            ]
        , div [ class "speaker-input-section" ]
            [ h3 [] [ text "Your Twitter handle (optional)" ]
            , input [ type_ "text", value speaker.twitter, placeholder "@YourTwitterName", onInput <| SpeakerTwitter i ] []
            ]
        , div [ class "speaker-input-section" ]
            [ h3 [] [ text "Your norwegian ZIP Code (optional)" ]
            , input [ type_ "text", value speaker.zipCode, placeholder "Zip Code", onInput <| SpeakerZipCode i ] []
            ]
        ]


viewError : String -> Html Msg
viewError message =
    div [] [ text message ]


hideIfEditable : Bool -> String
hideIfEditable editable =
    if editable then
        "hide"
    else
        ""


hideIfNotEditable : Bool -> String
hideIfNotEditable editable =
    if not editable then
        "hide"
    else
        ""
