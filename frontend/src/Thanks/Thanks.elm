module Thanks.Thanks exposing (..)

import Html exposing (..)
import Html.Attributes exposing (class, src)


type alias Model =
    String


type Msg
    = Update


initModel : Model
initModel =
    ""


update : Msg -> Model -> Model
update msg model =
    case msg of
        Update ->
            model


view : Model -> Html Msg
view model =
    div [ class "wrapper login " ]
        [ div [ class "logo-wrapper" ]
            [ img [ src "assets/logo.png", class "logo" ] []
            ]
        , h1 [] [ text "Awesome!", br [] [], text " We are on it!" ]
        , div [ class "success-image-wrapper" ]
            [ img [ src "/assets/plane2.png", class "success-image" ] []
            ]
        , div [ class "email-success" ]
            [ div [] [ text "Please check your email." ]
            , div [] [ text "Click the link to get started." ]
            ]
        ]
