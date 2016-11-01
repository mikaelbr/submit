import r from 'r-dom';
import request from 'superagent';

const text = {
};

const submitEmail = () => {
    var emailAddress = document.getElementById('email-address').value;
    request.get('http://localhost:8081/users/authtoken?email=' + emailAddress)
        .end(function(err, res){
            if (err || !res.ok) {
                alert('Couldn\' send login link to ' + emailAddress + '. Please contact program@java.no to get help.');
            } else {
                window.location.hash = 'registerThanks';
            }
        });
};

const register = () => (
    r.div({className: 'welcome'}, [
        r.div({className: 'logo-wrapper'}, [
            r.img({src: 'assets/logo.png', className: 'logo'})
        ]),
        r.h1('Got something interesting to say?'),
        r.div({className: 'email-wrapper'}, [
            r.input({type: 'email', className: 'email', id: 'email-address', placeholder: 'Your email address'}),
            r.button({className: 'submit', type: 'submit', onClick: submitEmail})
        ]),
        r.div({className: 'explanation'}, [
            r.div({className: 'arrow'}),
            r.div({className: 'text'}, [
                r.div({}, 'We\'ll email you a unique login link.'),
                r.div({}, 'Then you can start creating your talk.')
            ])
        ])
    ])
);

export default register;
