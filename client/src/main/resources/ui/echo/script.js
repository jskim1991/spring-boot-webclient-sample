function log(msg) {
    const messagesDiv = document.getElementById('messages')
    const elem = document.createElement('div')
    const txt = document.createTextNode(msg)
    elem.appendChild(txt)
    messagesDiv.appendChild(elem)
}

let websocket = null

document.getElementById('close')
    .addEventListener('click', (e) => {
        e.preventDefault()
        websocket.close()
        return false
    })

window.addEventListener('load', (e) => {
    websocket = new WebSocket('ws://localhost:8080/ws/echo')
    websocket.addEventListener('message', (e) => {
        const msg = e.data
        log(msg)
        websocket.send(msg + ' reply from ui')
    })
})
