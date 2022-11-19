const express = require("express");
const bodyParser = require("body-parser");
const app = express();
app.use(bodyParser.json());
const path = require("path");
const db = require("./db");
const mongoose = require("mongoose");
const collection = "restaurants";
const assert = require("assert");
const http = require("http");
var orders = "orders";
var users = "users";
var objectId = require("mongodb").ObjectID;
var crypto = require("crypto");
app.use(bodyParser.urlencoded({ extended: true }));

app.use(express.static(__dirname));
app.set("views", __dirname + "/views");
app.engine("html", require("ejs").renderFile);
app.set("view engine", "hbs");
var port = process.env.PORT || 3000;

app.get("/login", (req, res) => {
  res.render("login");
});
app.get("/autentificare", (req, res) => {
  res.render("login");
});
app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "index.html"));
  //res.render("login");
});
app.get("/home", (req, res) => {
  res.sendFile(path.join(__dirname, "index.html"));
  //res.render("login");
});

app.get("/restaurant", (req, res) => {
  res.render("restaurant");
});

app.get("/orders", (req, res) => {
  res.render("orders");
});

app.post("/placeOrder", (req, res, net) => {
  var post_data = JSON.parse(req.body.order);
  console.log(post_data);

  var insertJSON = {
    userId: objectId(post_data.userId),
    restaurantId: objectId(post_data.restaurantId),
    order: post_data.order,
    status: post_data.status,
    address: post_data.address,
    totalAmount: post_data.totalAmount,
    phone: post_data.phone,
    dateOrder: new Date(post_data.dateOrder),
  };
  console.log(insertJSON);
  db.getDB()
    .collection(orders)
    .insertOne(insertJSON, function (err, response) {
      res.json("Order placed");
      console.log(response);
    });
});

app.post("/insert", function (req, res) {
  var menu = {
    food: "",
    description: "",
    price: "",
  };
  var MenuArray = [];
  var foodArray = req.body.food;
  var descriptionArray = req.body.description;
  var priceArray = req.body.price;

  for (var i = 0; i < foodArray.length; i++) {
    var menu = {
      food: foodArray[i],
      description: descriptionArray[i],
      price: priceArray[i],
    };
    MenuArray.push(menu);
  }

  var item = {
    name: req.body.name,
    address: req.body.address,
    menu: MenuArray,
  };
  // console.log(item);
  db.getDB()
    .collection(collection)
    .insertOne(item, function (err, result) {
      assert.equal(null, err);
      console.log("Item inserted");
    });
  res.redirect("/home");
});

app.get("/getRestaurantForAndroid", (req, res) => {
  var result = [];
  var cursor = db.getDB().collection(collection).find();
  cursor.forEach(
    function (doc, err) {
      assert.equal(null, err);
      result.push(doc);
    },
    function () {
      //console.log(result);
      var listMenu = result;
      //console.log(listMenu);
      // res.render('restaurant', { items: listMenu });
      res.send("{restaurants:" + JSON.stringify(listMenu) + "}");
      // res.json(listMenu);
    }
  );
});

app.get("/getUserOrders", function (req, res) {
  var result = [];
  var cursor = db.getDB().collection(orders).find();
  cursor.forEach(
    function (doc, err) {
      assert.equal(null, err);
      result.push(doc);
    },
    function () {
      res.send("{'orders':" + JSON.stringify(result) + "}");
    }
  );
});

app.get("/getRestaurants", (req, res) => {
  var result = [];
  var cursor = db.getDB().collection(collection).find();
  cursor.forEach(
    function (doc, err) {
      assert.equal(null, err);
      result.push(doc);
    },
    function () {
      //console.log(result);
      var listMenu = result;
      //console.log(listMenu);
      res.render("restaurant", { items: listMenu });
    }
  );
});

app.get("/getAllOrders", (req, res) => {
  var result = [];
  var cursor = db.getDB().collection(orders).find();
  cursor.forEach(
    function (doc, err) {
      assert.equal(null, err);
      result.push(doc);
    },
    function () {
      //console.log(result);
      var listMenu = result;
      //console.log(listMenu);
      res.render("orders", { items: listMenu });
    }
  );
});
//update the selected order
app.post("/updateOrder", (req, res) => {
  var orderId = req.body.orderId;
  var orderStatus = req.body.orderStatus;

  var myquery = { _id: objectId(orderId) };
  var newvalues = { $set: { status: orderStatus } };
  db.getDB()
    .collection(orders)
    .updateOne(myquery, newvalues, function (err, res) {
      console.log("Item updated");
    });
  res.redirect("/getAllOrders");
});

//password utils
//create function to random salt
var genRandomString = function (length) {
  return crypto
    .randomBytes(Math.ceil(length / 2))
    .toString("hex")
    .slice(0, length);
};

var sha512 = function (password, salt) {
  var hash = crypto.createHmac("sha512", salt);
  hash.update(password);
  var value = hash.digest("hex");
  return {
    salt: salt,
    passwordHash: value,
  };
};

function saltHashPassword(userPassword) {
  var salt = genRandomString(16); //create 16 random caracters
  var passwordData = sha512(userPassword, salt);
  return passwordData;
}
function chechHashPassword(userPassword, salt) {
  var passwordData = sha512(userPassword, salt);
  return passwordData;
}

app.post("/register", (req, res, next) => {
  var post_data = req.body;

  var password = post_data.password;

  var name = post_data.name;
  var email = post_data.email;
  var address = post_data.address;
  var phone = post_data.phone;

  var insertJson = {
    name: name,
    email: email,
    password: password,
    address: address,
    phone: phone,
  };
  db.getDB()
    .collection(users)
    .find({ phone: phone })
    .count(function (err, number) {
      if (number != 0) {
        res.json("Phone already exist !");
        console.log("Phone already exist !");
      } else {
        db.getDB()
          .collection(users)
          .insertOne(insertJson, function (err, response) {
            res.json("register success  !");
            console.log("regsiter success !");
          });
      }
    });
});

app.post("/login", (req, res, next) => {
  var post_data = req.body;

  var phone = post_data.phone;
  var userPassword = post_data.password;
  //console.log(phone);
  var query = { phone: phone };
  db.getDB()
    .collection(users)
    .find({ phone: phone })
    .count(function (err, number) {
      if (number == 0) {
        res.json("Phone not exist !");
        console.log("Phone not exist !");
      } else {
        db.getDB()
          .collection(users)
          .findOne(query, function (err, document) {
            //var salt = user.salt; //get salt from user,salt
            //var hashed_password = chechHashPassword(userPassword, salt).passwordHash; // hash password with hash
            //var encrypted_password = user.password;

            if (document.password == userPassword) {
              res.json("Login success!");

              console.log("Login success!");
            } else {
              res.json("Wrong password");
              console.log("Wrong password");
            }
          });
      }
    });
});
app.post("/loginWeb", (req, res, next) => {
  var post_data = req.body;

  var phone = post_data.phone;
  var userPassword = post_data.password;
  //console.log(phone);
  var query = { phone: phone };
  db.getDB()
    .collection(users)
    .find({ phone: phone })
    .count(function (err, number) {
      if (number == 0) {
        res.json("Phone not exist !");
        console.log("Phone not exist !");
      } else {
        db.getDB()
          .collection(users)
          .findOne(query, function (err, document) {
            if (document.password == userPassword) {
              //res.json("Login success!");
              console.log("Login success!");
              const token = jwt.sign(
                {
                  userId: document._id,
                  userName: document.name,
                  userEmail: document.email,
                  userPhone: document.phone,
                },
                process.env.JWT_TOKEN,
                {
                  expiresIn: "1h",
                }
              );
              //console.log(token);
              // res.status(200).header('auth-token',token);
              // res.header('token', token).redirect('/getRestaurants');
              /*  res.status(200).json({
                    message: "Auth successful",
                    token: token
                  });*/
              console.log("Auth successful");
              res.cookie("auth", token).redirect("/");

              //res.send(token);
              //res.redirect('/');
            } else {
              res.json("Wrong password");
              console.log("Wrong password");
            }
          });
      }
    });
});

db.connect((err) => {
  if (err) {
    console.log("unable to connect to database");
    process.exit(1);
  } else
    app.listen(port, () => {
      console.log("Connect to database, app listening on port " + port + " !");
    });
});
