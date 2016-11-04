module Message exposing (Msg(..))

import Login.Message
import Thanks.Thanks


type Msg
    = LoginMsg Login.Message.Msg
    | ThanksMsg Thanks.Thanks.Msg
