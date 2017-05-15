/*global require, process */

/*
 * Submit Proxy (localhost:9000)
 * Setup: cd proxy && ./setup.sh
 */

var fs        = require("fs"),
    util      = require("util"),
    url       = require("url"),
    path      = require("path"),
    exec      = require('child_process').exec,
    http      = require("http");

try {
    var colors    = require("colors"),
        request   = require("request"),
        httpProxy = require("http-proxy");
} catch (e) {
    if(e.code === "MODULE_NOT_FOUND") {
        console.error("Fant ikke moduler for node proxy. Prøv deg på en npm install!");
        process.exit(1);
    } else {
        throw e;
    }
}

/* ----------------------------------- */

var proxy = httpProxy.createProxyServer({});

http.createServer(function(req, res) {
    var pathname = url.parse(req.url).pathname;

    var proxyAndLogRequest = function(proxyTo, appName) {
        console.log(pathname, '=>', 'proxying to', appName, '=>', url.parse(req.url).pathname);
        proxy.web(req, res, { target: proxyTo });
    };

    if(pathname.startsWith('/api')) {
        proxyAndLogRequest("http://submit-prod-bekk.eu-central-1.elasticbeanstalk.com", 'backend');
    } else {
        proxyAndLogRequest("http://localhost:8000", 'frontend');
    }
}).listen(9000, function() {
    console.log('proxy listening on port 9000');
});