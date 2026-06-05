
// MBCS Browser Test - Simulates real user clicking through pages
const http = require('http');
const BASE = 'http://localhost:8080';

function request(method, path, data, token) {
  return new Promise((resolve, reject) => {
    const url = new URL(path, BASE);
    const opts = {
      hostname: url.hostname,
      port: url.port,
      path: url.pathname + url.search,
      method: method,
      headers: { 'Content-Type': 'application/json' },
      timeout: 10000
    };
    if (token) opts.headers['Authorization'] = token;
    const req = http.request(opts, (res) => {
      let body = '';
      res.on('data', chunk => body += chunk);
      res.on('end', () => {
        try {
          const json = JSON.parse(body);
          resolve({ status: res.statusCode, data: json, headers: res.headers });
        } catch(e) {
          resolve({ status: res.statusCode, raw: body.substring(0,200) });
        }
      });
    });
    req.on('error', e => reject(e.message));
    req.on('timeout', () => { req.destroy(); reject('timeout'); });
    if (data) req.write(JSON.stringify(data));
    req.end();
  });
}

async function run() {
  console.log('=== MBCS Browser Test ===');
  const results = { passed: [], failed: [], total: 0, errors: [] };
  const ok = (name) => { results.total++; results.passed.push(name); console.log('  OK ' + name); };
  const fail = (name, msg) => { results.total++; results.failed.push(name); results.errors.push(name + ': ' + msg); console.log('  FAIL ' + name + ': ' + msg); };

  // 1. Get institution list (like opening login page)
  try {
    var r1 = await request('GET', '/api/teller/institution/list');
    if (r1.status === 200 && r1.data && r1.data.data) {
      ok('GET institution list');
      var insts = r1.data.data;
      var instNo = insts.length > 0 ? insts[0].institutionNo : '100001';
      
      // 2. Get tellers for institution
      var r2 = await request('GET', '/api/teller/teller?institutionNo=' + instNo);
      if (r2.status === 200 && r2.data) {
        ok('GET teller list for ' + instNo);
        var tellers = r2.data.data || [];
        var tellerNo = tellers.length > 0 ? tellers[0].tellerNo : '1000010001';
        var tellerType = tellers.length > 0 ? (tellers[0].tellerType || '1') : '1';

        // 3. Login (simulating user clicking login button)
        var r3 = await request('POST', '/api/teller/auth/login', {
          institutionNo: instNo,
          tellerNo: tellerNo,
          password: '123456'
        });
        if (r3.status === 200 && r3.data && r3.data.data && r3.data.data.token) {
          ok('POST login as ' + tellerNo);
          var token = r3.data.data.token;
          
          // 4. Get current user info
          var r4 = await request('GET', '/api/teller/auth/me', null, token);
          if (r4.status === 200) { ok('GET current user info'); } else { fail('GET current user', r4.status); }

          // 5. Navigate to home page data - get accounts
          var r5 = await request('GET', '/api/liability/liability-account/accounts', null, token);
          if (r5.status === 200) { ok('GET liability accounts'); } else { fail('GET liability accounts', r5.status + ' ' + JSON.stringify(r5.data).substring(0,100)); }

          // 6. Get products
          var r6 = await request('GET', '/api/liability/product', null, token);
          if (r6.status === 200) { ok('GET products'); } else { fail('GET products', r6.status); }

          // 7. Get customers
          var r7 = await request('GET', '/api/customer/list', null, token);
          if (r7.status === 200) { ok('GET customer list'); } else { fail('GET customer list', r7.status); }

          // 8. Get transaction list
          var r8 = await request('GET', '/api/liability/transaction/list?page=1&size=10', null, token);
          if (r8.status === 200) { ok('GET transaction list'); } else { fail('GET transaction list', r8.status + ' ' + JSON.stringify(r8.data).substring(0,100)); }

          // 9. Get certificate deposit products
          var r9 = await request('GET', '/api/liability/certificate-deposit/product/list', null, token);
          if (r9.status === 200) { ok('GET CD products'); } else { fail('GET CD products', r9.status + ' ' + JSON.stringify(r9.data).substring(0,100)); }

          // 10. Get batch jobs
          var r10 = await request('GET', '/api/liability/batch/list', null, token);
          if (r10.status === 200) { ok('GET batch list'); } else { fail('GET batch list', r10.status + ' ' + JSON.stringify(r10.data).substring(0,100)); }

          // 11. Logout
          var r11 = await request('POST', '/api/teller/auth/logout', null, token);
          if (r11.status === 200) { ok('POST logout'); } else { fail('POST logout', r11.status); }

        } else {
          fail('POST login', r3.status + ' ' + JSON.stringify(r3.data).substring(0,100));
        }
      } else {
        fail('GET teller list', r2.status);
      }
    } else {
      fail('GET institution list', r1.status);
    }
  } catch(e) {
    fail('General error', String(e));
  }

  // Summary
  console.log('');
  console.log('=== Results ===');
  console.log('Tests: ' + results.total + ', Passed: ' + results.passed.length + ', Failed: ' + results.failed.length);
  results.errors.forEach(e => console.log('  ERROR: ' + e));
  
  // Output machine-readable JSON
  console.log('---RESULT_JSON---');
  console.log(JSON.stringify(results));
}

run().catch(e => console.error('Fatal:', e));
