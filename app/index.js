import React from 'react';
import ReactDOM from 'react-dom';

const Hello = React.createClass({
    displayName: 'app',

    render: function() {
        return React.createElement('h1', null, 'Hello!');
    }
});

ReactDOM.render(React.createElement(Hello), document.querySelector('.app'));
