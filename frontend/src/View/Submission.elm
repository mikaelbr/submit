module View.Submission exposing (view)

import Model.Submission exposing (..)
import Messages exposing (..)
import Messages exposing (..)
import Html exposing (..)
import Html.Attributes exposing (class, style, type_, value, src, placeholder, href, name, checked, selected, id, for, disabled)
import Html.Events exposing (onInput, onClick, on)
import Nav.Nav exposing (toHash)
import Nav.Model
import Backend.Network exposing (RequestStatus(..))
import Time
import Date
import String
import Json.Decode exposing (succeed)


view : Model -> Html Msg
view model =
    case model.submission of
        Initial ->
            div [] []

        Loading ->
            viewLoading

        Complete submission ->
            viewSubmission submission model

        Error message ->
            viewError message


viewLoading : Html Msg
viewLoading =
    div [ class "wrapper" ]
        [ div [ class "logo-wrapper" ] [ div [ class "logo" ] [] ]
        , div [ class "edit-submission" ]
            [ div [ class "edit-submission loading" ] [ text "Loading ..." ] ]
        ]


viewSubmission : Submission -> Model -> Html Msg
viewSubmission submission model =
    div [ class "wrapper" ]
        [ viewFooter submission model
        , Html.map UpdateSubmission <| viewSubmissionDetails submission model
        ]


viewFooter : Submission -> Model -> Html Msg
viewFooter submission model =
    div [ class "sticky-footer" ]
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
                        [ input [ id "autosave", type_ "checkbox", onClick ToggleAutosaveSubmission, checked model.autosave ] []
                        ]
                    ]
                , div []
                    [ button [ class "button-save", onClick (SaveSubmission 0) ] [ text "Save now" ] ]
                ]
            ]
        ]


viewSubmissionDetails : Submission -> Model -> Html SubmissionField
viewSubmissionDetails submission model =
    div []
        [ div [ class "logo-wrapper" ] [ div [ class "logo" ] [] ]
        , div [ class <| "edit-intro " ++ hideIfNotEditable submission.editable ]
            [ h1 [] [ text "Let's make this talk happen!" ]
            , p [ class "ingress" ] [ text "Fill in the form below to create your talk. Don't forget to mark it as submitted at the end of the form when you are happy with your draft." ]
            , div [ class "help-part" ]
                [ strong [] [ span [] [ text "Month XXth" ], text "Deadline for submissions" ]
                , p [] [ text "Start creating your talk by filling in all the fields. We'll auto-save the talk for you as you edit, making sure you don't lose your great ideas. When you are done, mark the talk as ready for review." ]
                ]
            , div [ class "help-part" ]
                [ strong [] [ span [] [ text "Month YYth" ], text "Deadline for reviews" ]
                , p [] [ text "We'll give you a notice by this date about whether your talk is selected, meaning you'll have plenty of time to prepare." ]
                ]
            , div [ class "help-part" ]
                [ strong [] [ span [] [ text "Month ZZth" ], text "Let's present!" ]
                , p [] [ text "Get everything in order by this date, then you can enter the stage and start presenting! :)" ]
                ]
            ]
        , div [ class <| "comments-wrapper " ++ hideIfNoComments submission ]
            [ h2 [] [ text "Comments from the program committee" ]
            , p [] [ text "The program committee has reviewed your talk and have the following comments. Please review them, and respond either by your own comment or by updating your talk accordingly." ]
            , viewComments submission model
            ]
        , div [ class "edit-submission" ]
            [ div [ class <| "cant-edit-message " ++ hideIfEditable submission.editable ] [ text "You can't edit this talk. Only talks from the current event is editable." ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Title" ]
                , p [ class "input-description" ] [ text "Select an expressive and snappy title that captures the content of your talk without being too long. Remember that the title must be attractive and should make people curious." ]
                , input [ type_ "text", value submission.title, onInput Title, placeholder "A short and snappy title catching people's interest" ] []
                ]
            , div [ class "input-section" ]
                [ h2 [] [ text "Description" ]
                , p [ class "input-description" ] [ text "Give a concise description of the content and goals of your talk. Try not to exceed 300 words, as shorter and more to-the-point descriptions are more likely to be read by the participants." ]
                , textarea [ value submission.abstract, onInput Abstract, placeholder "Try to sell your presentation as best as possible to the audience. \n\nShort and to the point is often a good starting point!" ] []
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
                [ h2 [] [ text "Equipment (not public)" ]
                , p [ class "input-description" ] [ text "Please specify any additional special equipment you may need. Note that all get access to WiFi and a projector." ]
                , textarea [ class "small-textarea", value submission.equipment, onInput Equipment, placeholder "Let us know if your talk or workshop depends on us providing you with anything to ensure it's success." ] []
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
                    List.map (viewSpeaker submission <| List.length submission.speakers) submission.speakers
                ]
            , div [ class <| "input-section " ++ hideIfNotEditable submission.editable ]
                [ h2 [] [ text "How finished are you with your abstract?" ]
                , p [ class "input-description" ] [ text "Keep it as a draft until you have filled in everything. Don't worry, you can still make changes after marking it as ready for review." ]
                , p [ class "input-description input-description-strong" ] [ text "Make sure you mark it as ready by the submission deadline to have your talk considered for inclusion!" ]
                , radio "Not ready: Keep it as my personal draft" "status" "DRAFT" (Status "DRAFT") <| submission.status == "DRAFT"
                , radio "Ready: Let the program committee look at it" "status" "SUBMITTED" (Status "SUBMITTED") <| submission.status == "SUBMITTED"
                ]
            , div [ class "sticky-footer-filler" ] []
            ]
        ]


viewLength : Submission -> Html SubmissionField
viewLength s =
    case s.format of
        "presentation" ->
            select [ onInput Length ]
                [ option [ value "40", selected <| s.length == "40" ] [ text "40 minutes" ]
                , option [ value "20", selected <| s.length == "20" ] [ text "20 minutes" ]
                ]

        "lightning-talk" ->
            select [ onInput Length ]
                [ option [ value "10", selected <| s.length == "10" ] [ text "10 minutes" ]
                ]

        _ ->
            select [ onInput Length ]
                [ option [ value "120", selected <| s.length == "120" ] [ text "2 hours" ]
                , option [ value "240", selected <| s.length == "240" ] [ text "4 hours" ]
                , option [ value "480", selected <| s.length == "480" ] [ text "8 hours" ]
                ]


viewSpeaker : Submission -> Int -> ( Int, Speaker ) -> Html SubmissionField
viewSpeaker submission n ( i, speaker ) =
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
                , textarea [ value speaker.bio, placeholder "Tell the audience who this speaker is, and why she/he is the perfect person to hold this talk.", onInput <| SpeakerBio i ] []
                ]
            , div [ class "speaker-input-section" ]
                [ h3 [] [ text "Speakers image" ]
                , p [ class "input-description" ]
                    [ text "Please upload a good image of yourself."
                    , b [] [ text " Max 500 KB. " ]
                    , text "If you don't upload a picture, we'll try to use the gravatar image connected to your email address."
                    ]
                , input [ class "speaker-image-input", type_ "file", id <| "SpeakerImage" ++ toString i, on "change" (succeed <| FileSelected speaker i <| "SpeakerImage" ++ toString i) ] []
                , speakerImage speaker
                ]
            ]


viewComments : Submission -> Model -> Html SubmissionField
viewComments submission model =
    div [ class "comment-section" ]
        [ ul [ class "comments" ] <|
            List.map (\comment -> viewComment comment) submission.comments
        , viewCommentSubmission model
        ]


viewComment : Comment -> Html SubmissionField
viewComment comment =
    li [ class "comment" ]
        [ h3 [] [ text comment.name ]
        , p [ class "comment-text" ] [ text comment.comment ]
        ]


viewCommentSubmission : Model -> Html SubmissionField
viewCommentSubmission model =
    div [ class "send-comment" ]
        [ h2 [] [ text "Reply" ]
        , textarea [ onInput NewComment, class "comment-area", value model.comment ] []
        , button [ onClick SaveComment, disabled <| String.isEmpty model.comment ] [ text "Send" ]
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


hideIfNoComments : Submission -> String
hideIfNoComments submission =
    if List.isEmpty submission.comments then
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
            "Please select the length of the presentation (in minutes)."

        "lightning-talk" ->
            "Please select the length of the lightning talk (in minutes)."

        _ ->
            "Please select the length of the workshop (in minutes)."


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
                            << List.map (zeroPad << toString)
                        <|
                            [ Date.hour date, Date.minute date, Date.second date ]
                       )

        Nothing ->
            "Not edited yet"


zeroPad : String -> String
zeroPad n =
    if String.length n == 1 then
        "0" ++ n
    else
        n


speakerImage : Speaker -> Html SubmissionField
speakerImage speaker =
    if speaker.hasPicture then
        div [ style <| [ ( "background-image", "url(" ++ speaker.pictureUrl ++ ")" ) ], class "speaker-image" ] []
    else
        img [ src "assets/robot_padded_arm.png", class "speaker-image" ] []
