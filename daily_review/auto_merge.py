import json, urllib.request, base64, sys

AUTH = base64.b64encode(b'mbcsadmin:fff92a248a1d50edcaeed7e8fb78f0e030bc847a').decode()
BASE = 'http://localhost:3001/api/v1/repos/mbcsadmin/MBCS'
HEADERS = {'Content-Type': 'application/json', 'Authorization': 'Basic ' + AUTH}

def api(path, data=None, method='GET'):
    if data is not None:
        data = json.dumps(data).encode()
    req = urllib.request.Request(BASE + path, data=data, headers=HEADERS, method=method)
    try:
        resp = urllib.request.urlopen(req)
        return json.loads(resp.read())
    except urllib.error.HTTPError as e:
        body = e.read().decode()
        print(f'API error {e.code}: {body}', file=sys.stderr)
        sys.exit(1)

# Check if PR already exists for this branch
existing = api('/pulls?state=open&head=mbcs-mwl-001')
if existing:
    pr_num = existing[0]['number']
    print(f'PR #{pr_num} already exists, merging...')
else:
    pr = api('/pulls', {'title': 'Auto merge: mbcs-mwl-001 to master', 'head': 'mbcs-mwl-001', 'base': 'master'}, 'POST')
    pr_num = pr['number']
    print(f'PR #{pr_num} created')

result = api(f'/pulls/{pr_num}/merge', {'Do': 'merge'}, 'POST')
print(f'PR #{pr_num} merged: {result.get("message", "OK")}')
