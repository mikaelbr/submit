module Login.Login exposing (..)

import Html exposing (..)
import Html.Attributes exposing (class, src, type_, id, placeholder, value)
import Html.Events exposing (onClick, onInput)
import Login.Model exposing (Model)
import Login.Message exposing (Msg(..))


init : ( Model, Cmd Msg )
init =
    ( initModel, Cmd.none )


initModel : Model
initModel =
    Model ""


view : Model -> Html Msg
view model =
    div [ class "welcome" ]
        [ div [ class "logo-wrapper" ]
            [ img [ src "assets/logo.png", class "logo" ] [] ]
        , h1 [] [ text "Got something interesting to say?" ]
        , div [ class "email-wrapper" ]
            [ input [ value model.email, onInput Email, type_ "email", class "email", id "email-address", placeholder "Your email address" ] []
            , button [ class "submit", type_ "submit", onClick SubmitEmail ] []
            ]
        , div [ class "explanation" ]
            [ div [ class "arrow" ] []
            , div [ class "text" ]
                [ div [] [ text "We’ll email you a unique login link." ]
                , div [] [ text "Then you can start creating your talk." ]
                ]
            ]
        ]
