import r from 'r-dom';

const text = {
};

const submitEmail = () => {
    window.location.hash = 'registerThanks';
}

const register = () => (
    r.div({className: 'welcome'}, [
        r.div({className: 'logo-wrapper'}, [
            r.img({src: 'assets/logo.png', className: 'logo'})
        ]),
        r.h1('Got something interesting to say?'),
        r.div({className: 'email-wrapper'}, [
            r.input({type: 'email', className: 'email', placeholder: 'Your email address'}),
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
