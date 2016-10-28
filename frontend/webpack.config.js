const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: path.join(__dirname, 'app', 'index.js'),

    output: {
        path: path.join(__dirname, 'dest'),
        filename: 'app.js'
    },

    module: {
        loaders: [
            {
                test: /\.js$/,
                loader: 'babel',
                exlude: 'node_modules',
                query: {
                    presets: ['es2015', 'react']
                }
            },
            {
                test: /\.json$/,
                loader: 'json'
            }
        ]
    },

    devServer: {
        contentBase: path.join(__dirname, 'dest')
    }
};
