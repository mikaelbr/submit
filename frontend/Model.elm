module Model exposing (Model)

import Login.Model as Login
import Nav.Model exposing (Page(..))
import Thanks.Thanks as Thanks


type alias Model =
    { login : Login.Model
    , thanks : Thanks.Model
    , page : Page
    }
