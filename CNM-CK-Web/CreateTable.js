var aws = require('aws-sdk')
aws.config.update({
    region: 'ap-southeast-1',
    accessKeyId: 'AKIA3LBQEI65LVPVS2PR',
    secretAccessKey: '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'
})

var dynamodb = new aws.DynamoDB()

var Users = {
    TableName: 'Users',
    KeySchema: [
        {AttributeName: 'userID', KeyType: 'HASH'}
    ],
    AttributeDefinitions: [
        {AttributeName: 'userID', AttributeType: 'S'}
    ],
    ProvisionedThroughput:{
        ReadCapacityUnits: 10,
        WriteCapacityUnits: 10
    }
}

dynamodb.createTable(Users, (err, data)=>{
    if(err){
        console.log('loi', JSON.stringify(err, null, 2))
    }else{
        console.log('da tao bang', JSON.stringify(data, null, 2))
    }
})