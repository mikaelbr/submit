import React from 'react';
import ReactDOM from 'react-dom';
import r from 'r-dom';
import register from './register';
import submit from './submit';

const routes = {
    'register': register,
    'submit': submit
};

function getPage(pageName) {
    console.log(pageName);
    const page = routes[pageName];
    if (!page) {
        return register;
    }

    return page;
}

const app = React.createClass({
    componentDidMount() {
        window.onhashchange = function() {
            const page = window.location.hash.substr(1);
            this.setState({
                page: page
            });
        }.bind(this);
    },

    getInitialState() {
        return {
            page: window.location.hash.substr(1)
        };
    },

    render() {
        const page = getPage(this.state.page);
        return r(page);
    }
});

ReactDOM.render(r(app), document.querySelector('.app'));
