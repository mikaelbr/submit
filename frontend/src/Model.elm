module Model exposing (Model)

import Login.Model as Login
import Nav.Model exposing (Page(..))
import Thanks.Thanks as Thanks
import Usetoken.Model
import Flags exposing (Flags)


type alias Model =
    { flags : Flags
    , login : Login.Model
    , thanks : Thanks.Model
    , usetoken : Usetoken.Model.Model
    , page : Page
    }
