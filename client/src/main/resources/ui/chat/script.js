const messages = document.getElementById('messages')
const button = document.getElementById('send')
const message = document.getElementById('message')

var websocket = null

function send() {
    const value = message.value
    message.value = ''
    websocket.send(value.trim())
}


window.addEventListener('load', (e) => {
    websocket = new WebSocket('ws://localhost:8080/ws/chat')
    websocket.addEventListener('message', (e) => {
        const element = document.createElement('div')
        console.log(e.data)
        element.innerText = e.data
        messages.appendChild(element)
    })
})


message.addEventListener('keydown', (e) => {
    const key = e.key
    if (key === 'Enter') {
        send()
    }
})

button.addEventListener('click', (e) => {
    e.preventDefault()
    send()
    return false
})