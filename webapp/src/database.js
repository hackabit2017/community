import firebase from 'firebase';


var config = {
    apiKey: "AIzaSyCcucc8MVvNzik5USQHKc9Pr2hBXaibqPE",
    authDomain: "myapplication-d96fe.firebaseapp.com",
    databaseURL: "https://myapplication-d96fe.firebaseio.com",
    projectId: "myapplication-d96fe",
    storageBucket: "myapplication-d96fe.appspot.com",
    messagingSenderId: "802499495128"
};

firebase.initializeApp(config);
const database = firebase.database();

export default database;
