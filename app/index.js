import React from 'react';
import ReactDOM from 'react-dom';
import r from 'r-dom';
import register from './register';
import submit from './submit';
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import registerStore from './submit/store';

const routes = {
    'register': register,
    'submit': submit
};

function getPage(pageName) {
    const page = routes[pageName];
    if (!page) {
        return register;
    }

    return page;
}

const store = createStore(registerStore);

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

ReactDOM.render(r(Provider, {store: store}, r(app)), document.querySelector('.app'));
