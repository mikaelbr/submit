module Login.Login exposing (..)

import Html exposing (..)
import Html.App exposing (program)
import Html.Attributes exposing (class, src, type', id, placeholder, value)
import Html.Events exposing (onClick)


type alias Model =
    { email : String }


init : ( Model, Cmd Msg )
init =
    ( initModel, Cmd.none )


initModel : Model
initModel =
    Model ""


type Msg
    = Update
    | SubmitEmail


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Update ->
            ( model, Cmd.none )

        SubmitEmail ->
            ( model, Cmd.none )


view : Model -> Html Msg
view model =
    div [ class "welcome" ]
        [ div [ class "logo-wrapper" ]
            [ img [ src "assets/logo.png", class "logo" ] [] ]
        , h1 [] [ text "Got something interesting to say?" ]
        , div [ class "email-wrapper" ]
            [ input [ value model.email, type' "email", class "email", id "email-address", placeholder "Your email address" ] []
            , button [ class "submit", type' "submit", onClick SubmitEmail ] []
            ]
        , div [ class "explanation" ]
            [ div [ class "arrow" ] []
            , div [ class "text" ]
                [ div [] [ text "We’ll email you a unique login link." ]
                , div [] [ text "Then you can start creating your talk." ]
                ]
            ]
        ]


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none


main : Program Never
main =
    program { init = init, update = update, view = view, subscriptions = subscriptions }
