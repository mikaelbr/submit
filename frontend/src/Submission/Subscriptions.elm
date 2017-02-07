module Submission.Subscriptions exposing (subscriptions)

import Submission.Model exposing (..)
import Submission.Messages exposing (..)
import Time exposing (every, second)
import Backend.Network exposing (RequestStatus(..))
import Ports exposing (fileUploadSucceeded, UploadedImageData)


subscriptions : Model -> Sub Msg
subscriptions model =
    let
        saveSub =
            every (30 * second) Save

        uploadSub =
            fileUploadSucceeded FileUploaded
    in
        case model.submission of
            Complete submission ->
                if model.autosave && model.dirty && submission.editable then
                    Sub.batch [ saveSub, uploadSub ]
                else
                    uploadSub

            _ ->
                uploadSub
