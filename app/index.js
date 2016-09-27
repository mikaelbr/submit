import React from 'react';
import ReactDOM from 'react-dom';
import r from 'r-dom';

const Hello = React.createClass({
    render: () => r.h1('Hello')
});

ReactDOM.render(React.createElement(Hello), document.querySelector('.app'));
