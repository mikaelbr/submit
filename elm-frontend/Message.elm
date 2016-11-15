module Message exposing (Msg(..))

import Login.Message
import Thanks.Thanks
import Nav.Model exposing (Page)


type Msg
    = UpdateUrl Page
    | LoginMsg Login.Message.Msg
    | ThanksMsg Thanks.Thanks.Msg
