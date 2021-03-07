let headers = {'content-type': 'application/json'}

async function requestCore(method, url, body) {
  if (typeof body == 'object') body = JSON.stringify(body)
  return await fetch(url, {method, headers, body})
}

async function request(method, url, body) {
  let response = await requestCore(method, url, body)
  let text = await response.text()
  try {
    text = JSON.stringify(JSON.parse(text), null, 2)
  } catch(e) {}
  if (body) {
    console.log(method.toUpperCase(), url, body)
  } else {
    console.log(method.toUpperCase(), url)
  }
  console.log(response.status, text)
}

let get = (url) => request('GET', url)
let post = (url, body) => request('POST', url, body)
let put = (url, body) => request('PUT', url, body)
let patch = (url, body) => request('PATCH', url, body)
let del = (url, body) => request('DELETE', url, body)


get('/api/channels')
.then(() => post('/api/channels', 'Комната'))
.then(() => post('/api/channels', 'Ещё одна'))
.then(() => get('/api/channels'))
.then(() => get('/api/channels/0/messages'))
.then(() => put('/api/channels/0/messages', {username: 'vasya', text: 'Всем привет'}))
.then(() => put('/api/channels/0/messages', {username: 'petya', text: 'Тут кто-то есть?'}))
.then(() => get('/api/channels/0/messages'))
.then(() => put('/api/channels/1/messages', {username: 'vasya', text: 'Зачем тут ещё одна комната?'}))
.then(() => get('/api/channels/1/messages'))
