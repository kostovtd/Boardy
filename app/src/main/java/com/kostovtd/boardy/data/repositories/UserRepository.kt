package com.kostovtd.boardy.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.kostovtd.boardy.data.Resource
import com.kostovtd.boardy.data.ResourceStatus
import com.kostovtd.boardy.data.models.User

/**
 * Created by tosheto on 14.11.20.
 */
class UserRepository {


    fun signUpWithEmailAndPassword(
        listener: IUserRepositoryListener,
        email: String,
        password: String
    ) {
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    firebaseUser?.let { firebaseUser ->
                        val user = User(
                            firebaseUser.displayName ?: "",
                            firebaseUser.email ?: ""
                        )
                        val data = Resource(ResourceStatus.SUCCESS, user, "")
                        listener.handleSignInWithEmailAndPassword(data)
                    }
                } else {
                    val data = Resource(ResourceStatus.ERROR, null, message = "")
                    listener.handleSignInWithEmailAndPassword(data)
                }
            }
    }


    fun signUpWithGoogle() {
        TODO("not yet implemented")
    }


    fun signInWithEmailAndPassword(
        listener: IUserRepositoryListener,
        email: String,
        password: String
    ) {
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    firebaseUser?.let { firebaseUser ->
                        val user = User(
                            firebaseUser.displayName ?: "",
                            firebaseUser.email ?: ""
                        )
                        val data = Resource(ResourceStatus.SUCCESS, user, "")
                        listener.handleSignInWithEmailAndPassword(data)
                    }
                } else {
                    val data = Resource(ResourceStatus.ERROR, null, message = "")
                    listener.handleSignInWithEmailAndPassword(data)
                }
            }
    }


    fun signInWithGoogle() {
        TODO("not yet implemented")
    }
}