import Html exposing (..)
import Html.App exposing (program, map)
import Html.Attributes exposing (class)
import Login.Login as Login

type alias Model = { login : Login.Model }

initModel : Model
initModel = Model (Login.initModel)

init  : (Model, Cmd Msg)
init  = (initModel, Cmd.none)

type Msg = Login Login.Msg

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    case msg of
        Login loginMsg ->
            let
                (newLogin, loginCmd) = Login.update loginMsg model.login
                mappedCmd = Cmd.map Login loginCmd
            in
                ({ model | login = newLogin}, mappedCmd)

view : Model -> Html Msg
view model =
    div [ class "app" ] [ map Login (Login.view model.login) ]


subscriptions : Model -> Sub Msg
subscriptions model = Sub.none

main : Program Never
main = program { init = init, update = update, view = view, subscriptions = subscriptions}
