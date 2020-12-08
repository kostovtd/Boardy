package com.kostovtd.boardy.data.repositories

import com.google.firebase.auth.*
import com.kostovtd.boardy.data.ErrorType
import com.kostovtd.boardy.data.Resource
import com.kostovtd.boardy.data.ResourceStatus
import com.kostovtd.boardy.data.models.User
import kotlinx.coroutines.tasks.await

/**
 * Created by tosheto on 14.11.20.
 */
class UserRepository {

    fun isSignedIn(): Boolean =
        FirebaseAuth.getInstance().currentUser?.let { true } ?: run { false }


    suspend fun signUpWithEmailAndPassword(email: String, password: String): Resource<User> {
        return try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            Resource(ResourceStatus.SUCCESS, null)
        } catch (weakPasswordException: FirebaseAuthWeakPasswordException) {
            Resource(ResourceStatus.ERROR, null, ErrorType.FIREBASE_AUTH_WEAK_PASSWORD)
        } catch (invalidCredentialsException: FirebaseAuthInvalidCredentialsException) {
            Resource(ResourceStatus.ERROR, null, ErrorType.FIREBASE_AUTH_INVALID_CREDENTIALS)
        } catch (userCollisionException: FirebaseAuthUserCollisionException) {
            Resource(ResourceStatus.ERROR, null, ErrorType.FIREBASE_AUTH_USER_COLLISION)
        } catch (e: Exception) {
            Resource(ResourceStatus.ERROR, null, ErrorType.UNKNOWN)
        }
    }


    fun signUpWithGoogle() {
        TODO("not yet implemented")
    }


    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<User> {
        return try {
            val authResult =
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                val user = User(it.displayName ?: "", it.email ?: "")
                Resource(ResourceStatus.SUCCESS, user)
            } ?: run {
                Resource(ResourceStatus.ERROR, null, ErrorType.UNKNOWN)
            }
        } catch (invalidUserException: FirebaseAuthInvalidUserException) {
            Resource(ResourceStatus.ERROR, null, ErrorType.FIREBASE_AUTH_INVALID_USER)
        } catch (invalidCredentialsException: FirebaseAuthInvalidCredentialsException) {
            Resource(ResourceStatus.ERROR, null, ErrorType.FIREBASE_AUTH_INVALID_CREDENTIALS)
        } catch (e: Exception) {
            Resource(ResourceStatus.ERROR, null, ErrorType.UNKNOWN)
        }
    }


    fun signInWithGoogle() {
        TODO("not yet implemented")
    }


    fun signOut() = FirebaseAuth.getInstance().signOut()
}