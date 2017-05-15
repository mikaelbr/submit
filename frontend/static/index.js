(function() {
    function redirectUrl() {
        if (window.location.host.indexOf('localhost') >= 0) {
            return 'http://localhost:8000';
        } else {
            return 'https://cfp.bekk.no';
        }
    }

    var token = localStorage.getItem('login_token');
    if (!token) {
        if (window.location.hash.indexOf('access_token=') >= 0) {
            token = window.location.hash.substr(1).split('&').map(function(query) {
                return query.split('=');
            }).reduce(function(acc, val) {
                if (acc) {
                    return acc;
                } else {
                    if (val[0] === 'id_token') {
                        return val[1];
                    } else {
                        return acc;
                    }
                }
            }, null);
            localStorage.setItem('login_token', token);
        } else {
            window.location = 'https://bekk.eu.auth0.com/authorize/?client_id=o49bHOfUDukdCku6Ak4DngHc2wSwv3CT&scope=openid email&response_type=token&redirect_uri=' + redirectUrl() + '&connection=Bekk';
            return;
        }
    }

    var app = Elm.Main.fullscreen({
        host: window.location.host,
        token: token
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
