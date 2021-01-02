var express = require('express')
var router = express.Router()
var aws = require('aws-sdk')
const { Console } = require('console')

aws.config.update({
    region: 'ap-southeast-1',
    accessKeyId: 'AKIA3LBQEI65LVPVS2PR',
    secretAccessKey: '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'
})

var client = new aws.DynamoDB.DocumentClient()

router.get('/login', (req, res) => {
    res.render('login')
})

router.get('/trangchu', (req, res) => {
    res.render('index');
})

router.get('/users', (req, res) => {
    var params = {
        TableName: 'NguoiDung'
    }
    client.scan(params, function (err, data) {
        if (err) {
            console.log('get users error')
        } else {
            console.log('get users')
            res.json(data.Items)
        }
    })
})

router.post('/addUser', (req, res) => {
    var { id, email, password, ngayTao, status } = req.body;
    var User = {
        TableName: 'NguoiDung',
        Item: {
            "id": String(id),
            "email": String(email),
            "password": String(password),
            "ngayTao": String(ngayTao),
            "status": String(status)
        },
    };
    client.put(User, function (err, data) {
        if (err) {
            console.error("Lỗi khi thêm:", JSON.stringify(err, null, 2));
        } else {
            console.log("Đã thêm:", JSON.stringify(data, null, 2));
        }
    });
})

router.delete('/deleteuser/:userID', (req, res) => {
    var userID = req.params.userID;
    var user = {
        TableName: 'NguoiDung',
        Key: {
            id: String(userID)
        }
    };
    client.delete(user, function (err, data) {
        if (err) {
            console.error("Lỗi khi xóa: ", JSON.stringify(err, null, 2));
        } else {
            console.log("Đã xóa:", JSON.stringify(data, null, 2));
        }
    });
})

router.put('/modifyuser', (req, res) => {
    const { userID, status } = req.body;
    console.log(status)
    var params = {
        TableName: 'NguoiDung',
        Key:{
            'id': userID
        }, 
        UpdateExpression: "set #s = :s",
        ExpressionAttributeNames: {
            "#s": "status"
        },
        ExpressionAttributeValues: {
            ":s": String(status)
        }
    };
    client.update(params, function (err, data) {
        if (err) {
            console.error("Error modify: ", JSON.stringify(err, null, 2));
        } else {
            console.log("Modify:", JSON.stringify(data, null, 2));
        }
    });
})

router.put('/getusersby/:email', (req, res) => {
    var email = req.params.email;
    var params;
    console.log('tim: '+email)
    if(email === '-1'){
        params = {
            TableName: 'NguoiDung'
        }
        console.log('ten tai khoan rỗng')
    }else{
        params = {
            TableName : "NguoiDung",
            FilterExpression: "#e = :email",
            ExpressionAttributeNames: {
                '#e': 'email',
            },
            ExpressionAttributeValues: {
                ":email": email
            }
        };
    }
    client.scan(params, function(err, data){ 
        if (err) {
            console.error("get users by tenTaiKhoan error:", JSON.stringify(err, null, 2));
        } else {
            console.log("get users by tenTaiKhoan sucsess");
            res.json(data.Items);

        }
    });
    
})


module.exports = router;







