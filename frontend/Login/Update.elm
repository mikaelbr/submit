module Login.Update exposing (update)

import Login.Model exposing (Model)
import Login.Message exposing (Msg(..))
import Nav.Nav exposing (toHash)
import Nav.Model exposing (Page(..))
import Http exposing (Error, Response)
import String exposing (join)
import Navigation


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Email email ->
            ( { model | email = email }, Cmd.none )

        SubmitEmail ->
            ( model, register model )

        Submit (Err _) ->
            ( model, Cmd.none )

        Submit (Ok _) ->
            ( model, Navigation.newUrl <| toHash Thanks )


register : Model -> Cmd Msg
register model =
    Http.send Submit <|
        emptyPost <|
            join "" [ model.flags.url, "/users/authtoken?email=", model.email ]


emptyPost : String -> Http.Request String
emptyPost url =
    Http.request
        { method = "POST"
        , headers = []
        , url = url
        , body = Http.emptyBody
        , expect = Http.expectString
        , timeout = Nothing
        , withCredentials = False
        }
