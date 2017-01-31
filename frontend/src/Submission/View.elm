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
        , h1
        , h2
        , h3
        , p
        , strong
        , span
        , a
        , ul
        , li
        , label
        , select
        , option
        )
import Html.Attributes exposing (class, type_, value, src, placeholder, href, name, checked, selected, id, for)
import Html.Events exposing (onInput, onClick)
import Nav.Nav exposing (toHash)
import Nav.Model
import Backend.Network exposing (RequestStatus(..))
import Time
import Date
import String


view : Model -> Html Msg
view model =
    case model.submission of
        Initial ->
            div [] []

        Loading ->
            div [] [ text "Loading" ]

        Complete submission ->
            viewSubmission submission model

        Error message ->
            viewError message


viewSubmission : Submission -> Model -> Html Msg
viewSubmission submission model =
    div [ class "wrapper" ]
        [ div [ class "sticky-footer" ]
            [ div [ class "sticky-footer-content" ]
                [ div []
                    [ a [ href << toHash <| Nav.Model.Submissions ]
                        [ button [ class "button-back" ] [ text "Back to list" ] ]
                    ]
                , div [ class <| "save-controls " ++ hideIfNotEditable submission.editable ]
                    [ div [ class "autosave" ]
                        [ div []
                            [ label [ for "autosave" ] [ text "Autosave changes" ]
                            , div [ class "lastsaved" ] [ text <| viewLastSaved model.lastSaved ]
                            ]
                        , div [ class "autosave-checkbox" ]
                            [ input [ id "autosave", type_ "checkbox", onClick ToggleAutosave, checked model.autosave ] []
                            ]
                        ]
                    , div []
                        [ button [ class "button-save", onClick (Save 0) ] [ text "Save now" ] ]
                    ]
                ]
            ]
        , div [ class "logo-wrapper" ] [ img [ src "assets/logo.png", class "logo" ] [] ]
        , div [ class <| "edit-intro " ++ hideIfNotEditable submission.editable ]
            [ h1 [] [ text "Let's make this talk happen!" ]
            , p [ class "ingress" ] [ text "JavaZone takes place in Oslo, Norway, on September 13th-14th 2017. Do YOU want to be one of the great speakers at our conference? Fantastic! That's what's this thing is for! Let's get you started!" ]
            , div [ class "help-part" ]
                [ strong [] [ span [] [ text "February 13th" ], text "Create your talk" ]
                , p [] [ text "Start creating your talk by filling in all the fields. We'll auto-save the talk for you as you edit, making sure you not lose your great ideas." ]
                ]
            , div [ class "help-part" ]
                [ strong [] [ span [] [ text "March 27th" ], text "Get some feedback" ]
                , p [] [ text "If you mark your talk for review before March 27th, we'll try to give you some personal feedback. We receive hundreds of talks, so we can't promise the world, but we'll do our best." ]
                ]
            , div [ class "help-part" ]
                [ strong [] [ span [] [ text "April 24th" ], text "Submission deadline" ]
                , p [] [ text "Get all your last touches in by April 24th. After that, the program committee will go through all talks and select the one that will be a part of JavaZone 2017." ]
                ]
            , div [ class "help-part" ]
                [ strong [] [ span [] [ text "June 26th" ], text "Know your result" ]
                , p [] [ text "By the end of June, all speakers will be get info about whether their talk is selected or not. Fingers crossed! If you are selected, you get to talk at JavaZone 2017!" ]
                ]
            ]
        , div [ class "edit-submission" ]
            [ div [ class <| "cant-edit-message " ++ hideIfEditable submission.editable ] [ text "You can't edit this talk. Only talks from the current year is editable." ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Title" ]
                , p [ class "input-description" ] [ text "Select an expressive and snappy title that captures the content of your talk without being too long. Remember that the title must be attractive and should make people curious." ]
                , input [ type_ "text", value submission.title, onInput Title ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Language" ]
                , p [ class "input-description" ] [ text "Which language will you be holding the talk in? It is permitted to use English in your slides, even though you may be talking in Norwegian, but you should write the rest of the abstract in the language you will speak in. We generally recommend that you hold the talk in the language you are most comfortable with." ]
                , radio "Norwegian" "language" "no" (Language "no") <| submission.language == "no"
                , radio "English" "language" "en" (Language "en") <| submission.language == "en"
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
                , radio "Presentation" "format" "presentation" (Format "presentation") <| submission.format == "presentation"
                , radio "Lightning Talk" "format" "lightning-talk" (Format "lightning-talk") <| submission.format == "lightning-talk"
                , radio "Workshop" "format" "workshop" (Format "workshop") <| submission.format == "workshop"
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Presentation length" ]
                , p [ class "input-description" ] [ text <| formatText submission.format ]
                , viewLength submission
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Experience level" ]
                , p [ class "input-description" ] [ text "Who is your talk pitched at?  Beginners, Experts or perhaps those in between?" ]
                , radio "Beginner" "experience" "beginner" (Level "beginner") <| submission.level == "beginner"
                , radio "Intermediate" "experience" "intermediate" (Level "intermediate") <| submission.level == "intermediate"
                , radio "Advanced" "experience" "advanced" (Level "advanced") <| submission.level == "advanced"
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Suggested keywords" ]
                , p [ class "input-description" ] [ text "Suggest up to five keywords that describe your talk. These will be used by the program committee to group the talks into categories. We reserve the right to edit these suggestions to make them fit into this years selected categories." ]
                , input [ type_ "text", value submission.suggestedKeywords, onInput SuggestedKeywords, placeholder "Keyword 1, Keyword 2, ..." ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Outline (not public)" ]
                , p [ class "input-description" ] [ text "The information will be used by the Program Committee to review the details of your talk. The outline should be a rough agenda for the talk, with a short description for each section, and with a rough estimate of the time spent on each. Omitting this section will reduce the chances of your submission being accepted." ]
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
                        [ if List.length submission.speakers > 1 then
                            div [] []
                          else
                            button [ onClick AddSpeaker, class "button-new" ] [ text "Add second speaker" ]
                        ]
                    ]
                , p [ class "input-description" ] [ text "Please give us a little bit of information about yourself. You can also add any additional speakers here. All of you will be shown in the program." ]
                , ul [ class "speakers" ] <|
                    List.map (viewSpeaker <| List.length submission.speakers) submission.speakers
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "That's it! You're done!" ]
                , p [ class "input-description" ] [ text "Feel free to come back anytime and edit your talk!" ]
                , div [ class "sticky-footer-filler" ] []
                ]
            ]
        ]


viewLength : Submission -> Html Msg
viewLength s =
    case s.format of
        "presentation" ->
            select [ onInput Length ]
                [ option [ value "45", selected <| s.length == "45" ] [ text "45 minutes" ]
                , option [ value "60", selected <| s.length == "60" ] [ text "60 minutes" ]
                ]

        "lightning-talk" ->
            select [ onInput Length ]
                [ option [ value "10", selected <| s.length == "10" ] [ text "10 minutes" ]
                , option [ value "20", selected <| s.length == "20" ] [ text "20 minutes" ]
                ]

        _ ->
            select [ onInput Length ]
                [ option [ value "120", selected <| s.length == "120" ] [ text "2 hours" ]
                , option [ value "240", selected <| s.length == "240" ] [ text "4 hours" ]
                , option [ value "480", selected <| s.length == "480" ] [ text "8 hours" ]
                ]


viewSpeaker : Int -> ( Int, Speaker ) -> Html Msg
viewSpeaker n ( i, speaker ) =
    let
        removeButton =
            if n == 1 then
                div [] []
            else if not speaker.deletable then
                div [] []
            else
                div [ class "flex-header-element" ]
                    [ button [ onClick (RemoveSpeaker i), class "button-delete" ] [ text "Remove speaker" ]
                    ]
    in
        li [ class "speaker" ]
            [ div [ class "flex-header" ]
                [ h2 [ class "flex-header-element" ] [ text ("Speaker " ++ toString (i + 1)) ]
                , removeButton
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


radio : String -> String -> String -> msg -> Bool -> Html msg
radio l group val msg selected =
    div [ class "radio-input" ]
        [ label []
            [ input [ type_ "radio", name group, value val, onClick msg, checked selected ] []
            , text l
            ]
        ]


formatText : String -> String
formatText format =
    case format of
        "presentation" ->
            "Please select the length of the presentation (in minutes). Presentations can have a length of 45 or 60 minutes. Including Q&A"

        "lightning-talk" ->
            "Please select the length of the presentation (in minutes). Lightning talks can be 10 or 20 minutes long. The time limit is strictly enforced"

        _ ->
            "Please select the length of the presentation (in minutes). Workshops last 2, 4 or 8 hours (120, 240 or 480 minutes)"


viewLastSaved : Maybe Time.Time -> String
viewLastSaved time =
    case time of
        Just t ->
            let
                date =
                    Date.fromTime t
            in
                "Last saved "
                    ++ (String.join ":"
                            << List.map toString
                        <|
                            [ Date.hour date, Date.minute date, Date.second date ]
                       )

        Nothing ->
            "Not edited yet"
