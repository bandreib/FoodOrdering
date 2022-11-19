const MongoClient = require("mongodb").MongoClient;
const ObjectId = require("mongodb").ObjectID;
const dbName = "licenta";
const url = "mongodb://bandrei:licenta@cluster0-shard-00-00-qntqe.mongodb.net:27017,cluster0-shard-00-01-qntqe.mongodb.net:27017,cluster0-shard-00-02-qntqe.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";
const mongoOption = {useNewUrlParser: true,useUnifiedTopology: true};

const state = {
    db : null
};

const connect = (cb) => {

    if(state.db)
        cb();
    else {

        MongoClient.connect(url,mongoOption,(err,client)=>{
            if(err)
            cb(err);
            else
                state.db = client.db(dbName);
                cb();
        });
    }    
}

const getPrimaryKey = (_id)=>{
        return ObjectID(_id);

}

const getDB =()=>{
    return state.db;
}

module.exports={getDB,connect,getPrimaryKey};