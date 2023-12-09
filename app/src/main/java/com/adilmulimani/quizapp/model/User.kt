package com.adilmulimani.quizapp.model

//data classes do not work in firebase because firebase needs an empty constructor
 class User
{           var name=""
            var age=0
            var email=""
            var password=""

    //empty constructor for firebase
        constructor()

       constructor(name: String, age: Int, email: String, password: String) {
        this.name = name
        this.age = age
        this.email = email
        this.password = password
    }


}
