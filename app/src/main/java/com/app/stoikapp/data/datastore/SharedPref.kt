package com.app.stoikapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class SharedPref(private val context: Context) {

    private val userId = stringPreferencesKey("userId")
    private val fullName = stringPreferencesKey("fullName")
    private val email = stringPreferencesKey("email")
    private val birthDate = stringPreferencesKey("birthDate")
    private val gender = stringPreferencesKey("gender")
    private val phoneNumber = stringPreferencesKey("phoneNumber")
    private val password = stringPreferencesKey("password")
    private val profilePicture = stringPreferencesKey("profilePicture")

    suspend fun saveUserData(
        userId: String,
        fullName: String,
        email: String,
        birthDate: String,
        gender: String,
        phoneNumber: String,
        password: String,
        profilePicture: String
    ) {
        context.dataStore.edit {
            it[this.userId] = userId
            it[this.fullName] = fullName
            it[this.email] = email
            it[this.birthDate] = birthDate
            it[this.gender] = gender
            it[this.phoneNumber] = phoneNumber
            it[this.password] = password
            it[this.profilePicture] = profilePicture
        }
    }

    val getUserId: Flow<String> = context.dataStore.data
        .map { it[userId] ?: "Undefined" }

    val getFullName: Flow<String> = context.dataStore.data
        .map { it[fullName] ?: "Undefined" }

    val getEmail: Flow<String> = context.dataStore.data
        .map { it[email] ?: "Undefined" }

    val getBirthDate: Flow<String> = context.dataStore.data
        .map { it[birthDate] ?: "Undefined" }

    val getGender: Flow<String> = context.dataStore.data
        .map { it[gender] ?: "Undefined" }

    val getPhoneNumber: Flow<String> = context.dataStore.data
        .map { it[phoneNumber] ?: "Undefined" }

    val getPassword: Flow<String> = context.dataStore.data
        .map { it[password] ?: "Undefined" }

    val getProfilePicture: Flow<String> = context.dataStore.data
        .map { it[profilePicture] ?: "Undefined" }

    val getAllUserData: Flow<String> = context.dataStore.data
        .map {
            "User ID: ${it[userId] ?: "Undefined"}\n" +
                    "Full Name: ${it[fullName] ?: "Undefined"}\n" +
                    "Email: ${it[email] ?: "Undefined"}\n" +
                    "Birth Date: ${it[birthDate] ?: "Undefined"}\n" +
                    "Gender: ${it[gender] ?: "Undefined"}\n" +
                    "Phone Number: ${it[phoneNumber] ?: "Undefined"}\n" +
                    "Password: ${it[password] ?: "Undefined"}\n" +
                    "Profile Picture: ${it[profilePicture] ?: "Undefined"}"
        }

    suspend fun removeSession(){
        context.dataStore.edit {
            it.remove(userId)
            it.remove(fullName)
            it.remove(email)
            it.remove(birthDate)
            it.remove(gender)
            it.remove(phoneNumber)
            it.remove(password)
            it.remove(profilePicture)
        }
    }
}
