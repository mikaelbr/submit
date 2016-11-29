module Login.Model exposing (Model)

import Flags exposing (Flags)


type alias Model =
    { flags : Flags
    , email : String
    }
