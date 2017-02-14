module Model exposing (Model, Flags, initModel)

import Model.Login
import Nav.Model exposing (Page(..))
import Model.Submissions
import Model.Submission


type alias Flags =
    { host : String }


type alias Model =
    { login : Model.Login.Model
    , submissions : Model.Submissions.Model
    , submission : Model.Submission.Model
    , page : Page
    , flags : Flags
    }


initModel : Flags -> Page -> Model
initModel flags page =
    Model
        Model.Login.init
        Model.Submissions.initModel
        Model.Submission.initModel
        page
        flags
