import r from 'r-dom';

const text = {
};

const registerThanks = () => (
    r.div({className: 'welcome'}, [
        r.div({className: 'logo-wrapper'}, [
            r.img({src: 'assets/logo.png', className: 'logo'})
        ]),
        r.h1('Yay! We are on it!'),
        r.div({className: 'success-image-wrapper'}, [
            r.img({className: 'success-image', src: '/assets/plane2.png'})
        ]),
        r.div({className: 'email-success'}, [
            r.div({}, 'Please check your email.'),
            r.div({}, 'Click the link to get started')
        ])
    ])
);

export default registerThanks;
