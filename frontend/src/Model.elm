module Model exposing (Model, Flags)

import Login.Model as Login
import Nav.Model exposing (Page(..))
import Thanks.Thanks as Thanks
import Usetoken.Model
import Submissions.Model
import Submission.Model


type alias Flags =
    { host : String }


type alias Model =
    { login : Login.Model
    , thanks : Thanks.Model
    , usetoken : Usetoken.Model.Model
    , submissions : Submissions.Model.Model
    , submission : Submission.Model.Model
    , page : Page
    , flags : Flags
    }
