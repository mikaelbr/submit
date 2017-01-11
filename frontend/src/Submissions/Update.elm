module Submissions.Update exposing (update)

import Submissions.Model exposing (..)
import Submissions.Messages exposing (..)
import Navigation
import Nav.Nav exposing (toHash)
import Nav.Model
import Nav.Requests exposing (createSubmission, getSubmissions, deleteLoginToken)
import Backend.Network exposing (RequestStatus(..))
import LocalStorage
import Task
import Lazy


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Message ->
            ( model, Cmd.none )

        CreateTalk ->
            ( model, Lazy.force createSubmission )

        Get (Err message) ->
            ( { model | submissions = Error <| toString message }, Cmd.none )

        Get (Ok submissions) ->
            ( { model | submissions = Complete submissions }, Cmd.none )

        Created (Err _) ->
            ( model, Lazy.force getSubmissions )

        Created (Ok submission) ->
            ( model, Navigation.newUrl << toHash <| Nav.Model.Submission submission.id )

        Logout ->
            ( model, Lazy.force deleteLoginToken )

        LoggedOut (Err err) ->
            ( model, Lazy.force removeLocalToken )

        LoggedOut (Ok _) ->
            ( model, Lazy.force removeLocalToken )

        TokenRemoved _ ->
            ( model, Navigation.newUrl << toHash <| Nav.Model.Register )


removeLocalToken : Lazy.Lazy (Cmd Msg)
removeLocalToken =
    Lazy.lazy <| \() -> Task.perform TokenRemoved <| LocalStorage.remove "login_token"
