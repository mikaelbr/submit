(function() {
    var app = Elm.Main.fullscreen({
        host: window.location.host
    });

    function checkStatus(response) {
        if (response.status >= 200 && response.status < 300) {
            return response;
        } else {
            var error = new Error(response.statusText);
            error.response = response;
            throw error;
        }
    }

    function parseJson(response) {
        return response.json();
    }

    app.ports.fileSelected.subscribe(function(props) {
        var id = props.id;
        var submission = props.submission;
        var speaker = props.speaker;
        var i = props.i;
        var input = document.getElementById(id);
        if (input == null) {
            return;
        }

        var data = new FormData();
        data.append('image', input.files[0]);

        var token = localStorage.getItem("login_token");
        var url = '/api/submissions/' +
                  submission + '/speakers/' +
                  speaker + '/picture';

        var myHeaders = new Headers();
        myHeaders.append("x-token", token);

        fetch(url, {
                method: 'POST',
                body: data,
                headers: myHeaders
            })
            .then(checkStatus)
            .then(parseJson)
            .then(function(data) {
                app.ports.fileUploadSucceeded.send({
                    url: data.pictureUrl + '?t=' + Date.now(),
                    submission: submission,
                    speaker: speaker,
                    i : i
                });
            })
            .catch(function(error) {
                app.ports.fileUploadFailed.send(error.message);
            });
    });
})();
