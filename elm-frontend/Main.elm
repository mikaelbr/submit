import Html exposing (..)
import Html.App exposing (program)

type alias Model = String

init  : (Model, Cmd Msg)
init  = ("Hello", Cmd.none)

type Msg = Update

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    case msg of
        Update -> (model, Cmd.none)

view : Model -> Html Msg
view model =
    div [] [ text model ]


subscriptions : Model -> Sub Msg
subscriptions model = Sub.none

main : Program Never
main = program { init = init, update = update, view = view, subscriptions = subscriptions}
