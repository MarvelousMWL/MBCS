import http from 'node:http';
import fs from 'node:fs';
import path from 'node:path';
import zlib from 'node:zlib';
import { fileURLToPath } from 'node:url';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const DIST = path.join(__dirname, 'dist');
const PORT = 3000;
const API_TARGET = 'http://localhost:8080';

const MIME = {
  '.html': 'text/html;charset=utf-8',
  '.js': 'application/javascript;charset=utf-8',
  '.css': 'text/css;charset=utf-8',
  '.json': 'application/json',
  '.png': 'image/png', '.jpg': 'image/jpeg', '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon', '.woff2': 'font/woff2', '.woff': 'font/woff',
};

function proxyAPI(req, res) {
  const cleanHeaders = {};
  const skipHeaders = ['host', 'connection', 'transfer-encoding', 'accept-encoding'];
  for (const [key, val] of Object.entries(req.headers)) {
    if (!skipHeaders.includes(key.toLowerCase())) {
      cleanHeaders[key] = val;
    }
  }
  cleanHeaders['host'] = 'localhost:8080';

  const options = {
    hostname: 'localhost',
    port: 8080,
    path: req.url,
    method: req.method,
    headers: cleanHeaders,
  };

  const proxyReq = http.request(options, (proxyRes) => {
    const resHeaders = { ...proxyRes.headers };
    res.writeHead(proxyRes.statusCode, resHeaders);
    proxyRes.pipe(res);
  });

  proxyReq.on('error', (err) => {
    res.writeHead(502, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ code: 502, message: '\u540e\u7aef\u670d\u52a1\u4e0d\u53ef\u7528' }));
  });

  req.pipe(proxyReq);
}

function serveStatic(req, res) {
  let filePath = path.join(DIST, req.url === '/' ? 'index.html' : req.url);
  if (!fs.existsSync(filePath)) {
    filePath = path.join(DIST, 'index.html');
  }

  const ext = path.extname(filePath);
  const contentType = MIME[ext] || 'application/octet-stream';

  fs.readFile(filePath, (err, data) => {
    if (err) { res.writeHead(500); res.end('Error'); return; }

    const acceptEncoding = req.headers['accept-encoding'] || '';
    const headers = { 'Content-Type': contentType };

    // index.html never cache; hashed assets get 1-year cache
    if (filePath.endsWith('index.html')) {
      headers['Cache-Control'] = 'no-cache, no-store, must-revalidate';
      headers['Pragma'] = 'no-cache';
      headers['Expires'] = '0';
    } else {
      headers['Cache-Control'] = 'public, max-age=31536000, immutable';
    }

    if (acceptEncoding.includes('gzip')) {
      headers['Content-Encoding'] = 'gzip';
      zlib.gzip(data, (_, compressed) => {
        res.writeHead(200, headers);
        res.end(compressed);
      });
    } else {
      res.writeHead(200, headers);
      res.end(data);
    }
  });
}

const server = http.createServer((req, res) => {
  if (req.url.startsWith('/api/')) {
    proxyAPI(req, res);
  } else {
    serveStatic(req, res);
  }
});

server.listen(PORT, '0.0.0.0', () => {
  console.log('Production server on http://0.0.0.0:' + PORT + ', API -> ' + API_TARGET);
});

process.on('SIGTERM', () => { server.close(); process.exit(0); });
process.on('SIGINT', () => { server.close(); process.exit(0); });
