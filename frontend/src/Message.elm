module Message exposing (Msg(..))

import Login.Message
import Thanks.Thanks
import Usetoken.Messages
import Submissions.Messages
import Nav.Model exposing (Page)


type Msg
    = UpdateUrl Page
    | LoginMsg Login.Message.Msg
    | ThanksMsg Thanks.Thanks.Msg
    | UsetokenMsg Usetoken.Messages.Msg
    | SubmissionsMsg Submissions.Messages.Msg
