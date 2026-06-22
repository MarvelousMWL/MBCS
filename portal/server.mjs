// MBCS 统一入口服务器
// 启动: node portal/server.mjs
// 访问: http://localhost:80
//   /      -> 门户 (portal/index.html)
//   /app/  -> 柜面 (localhost:3000 dev / bank-web/dist prod)
//   /api/  -> 后端 (localhost:8080)

import http from "node:http";
import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const PORT = 80;
const TELLER_DEV = "http://localhost:3000";
const BACKEND = "http://localhost:8080";

// MIME types
const MIME = {
  ".html": "text/html; charset=utf-8",
  ".css": "text/css",
  ".js": "application/javascript",
  ".mjs": "application/javascript",
  ".json": "application/json",
  ".png": "image/png",
  ".jpg": "image/jpeg",
  ".svg": "image/svg+xml",
  ".ico": "image/x-icon",
};

// Serve static file
function serveStatic(res, filePath) {
  try {
    const data = fs.readFileSync(filePath);
    const ext = path.extname(filePath);
    res.writeHead(200, { "Content-Type": MIME[ext] || "application/octet-stream" });
    res.end(data);
  } catch {
    // File not found → fallback to portal index.html (SPA)
    const data = fs.readFileSync(path.join(__dirname, "index.html"));
    res.writeHead(200, { "Content-Type": "text/html; charset=utf-8" });
    res.end(data);
  }
}

// Proxy request
function proxyRequest(targetBase, req, res) {
  const url = new URL(req.url, targetBase);
  const options = {
    hostname: url.hostname,
    port: url.port,
    path: url.pathname + url.search,
    method: req.method,
    headers: { ...req.headers, host: url.host },
  };

  const proxy = http.request(options, (proxyRes) => {
    res.writeHead(proxyRes.statusCode, proxyRes.headers);
    proxyRes.pipe(res);
  });
  proxy.on("error", () => {
    res.writeHead(502);
    res.end("Bad Gateway");
  });
  req.pipe(proxy);
}

// Main server
const server = http.createServer((req, res) => {
  const url = new URL(req.url, `http://localhost:${PORT}`);

  // API 代理
  if (url.pathname.startsWith("/api/")) {
    return proxyRequest(BACKEND, req, res);
  }

  // 柜面代理
  if (url.pathname.startsWith("/app/")) {
    return proxyRequest(TELLER_DEV, req, res);
  }

  // 门户静态文件
  let filePath = path.join(__dirname, url.pathname === "/" ? "index.html" : url.pathname);
  serveStatic(res, filePath);
});

server.listen(PORT, () => {
  console.log(`\n  MBCS Portal 运行中:`);
  console.log(`  ┌─────────────────────────────────┐`);
  console.log(`  │  门户: http://localhost:${PORT}         │`);
  console.log(`  │  柜面: http://localhost:${PORT}/app/    │`);
  console.log(`  │  API:  http://localhost:${PORT}/api/    │`);
  console.log(`  └─────────────────────────────────┘\n`);
});
