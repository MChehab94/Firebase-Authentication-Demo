package mchehab.com.firebaseauthentication

data class User(val uid: String, val name: String, val dateOfBirth: String, val email: String) {
    constructor() : this("", "", "", "")
}