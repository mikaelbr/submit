module Model.Login exposing (Model, init)


type alias Model =
    { email : String
    , loading : Bool
    }


init : Model
init =
    Model "" False
