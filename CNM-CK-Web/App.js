var express = require('express')
var app = express()

app.use(express.urlencoded({extended: false}))
app.use(express.json())

app.set('view engine', 'hbs')
app.set('views', 'D:/CNM-CK/views')

app.use('/', require('./Controller'))

app.listen(8000, ()=>{
    console.log('localhost:8000')
})