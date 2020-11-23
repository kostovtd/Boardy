package com.kostovtd.boardy.data.repositories

import com.kostovtd.boardy.data.Resource
import com.kostovtd.boardy.data.models.User

/**
 * Created by tosheto on 17.11.20.
 */
interface IUserRepositoryListener {
    fun handleSignInWithEmailAndPassword(resource: Resource<User>) {}
}