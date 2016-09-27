import r from 'r-dom';

const text = {
    whoareyou: 'Start by telling us who you are.',
    explanation: 'We’ll email you a login link, so you can start editing your talk'
};

const register = () => (
    r.div({className: 'welcome'}, [
        r.div({className: 'logo-wrapper'}, [
            r.img({src: 'assets/logo.png', className: 'logo'})
        ]),
        r.h1('Got something interesting to say?'),
        r.p({className: 'whoareyou'}, text.whoareyou),
        r.p({className: 'whoareyou-explanation'}, text.explanation),
        r.div({className: 'email-wrapper'}, [
            r.input({type: 'email', placeholder: 'Your email address'})
        ])
    ])
);

export default register;
