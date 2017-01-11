module Usetoken.Update exposing (update, saveToken)

import Usetoken.Model exposing (..)
import Usetoken.Messages exposing (..)
import Navigation
import Nav.Model exposing (Page(..))
import Nav.Nav exposing (toHash)
import LocalStorage
import Task


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Update ->
            ( model, Cmd.none )

        TokenSaved _ ->
            ( model, Navigation.newUrl <| toHash Submissions )


saveToken : String -> Cmd Msg
saveToken token =
    Task.perform TokenSaved <|
        LocalStorage.set "login_token" token
