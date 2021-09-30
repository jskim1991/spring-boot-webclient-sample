function log(msg) {
    const messages = document.getElementById('messages')
    const elem = document.createElement('div')
    const text = document.createTextNode(msg)
    elem.appendChild(text)
    messages.append(elem)
}

// require CORS
window.addEventListener('load', (e) => {
    const eventSource = new EventSource('http://localhost:8080/sse/10')
    eventSource.addEventListener('message', (e) => {
        e.preventDefault()
        log(e.data)
    })
    eventSource.addEventListener('error', (e) => {
        e.preventDefault()
        eventSource.close()
    })
})