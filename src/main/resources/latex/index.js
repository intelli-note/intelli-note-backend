const http = require('http'),
    url = require('url'),
    qs = require('qs'),
    mathjax = require('mathjax-node')

mathjax.start()

const app = http.createServer((req, res) => {
    let {tex} = qs.parse(
        url.parse(req.url).query
    );
    mathjax.typeset({
        math: tex,
        format: 'TeX',
        svg: true
    }, data => {
        res.writeHead(
            200,
            {'Content-type': 'image/svg+xml;charset=utf-8'}
        );
        res.write(data.svg);
        res.end();
    });
});

app.listen(8081);