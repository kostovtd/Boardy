package com.kostovtd.boardy.ui.viewModels

import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.*
import com.google.gson.Gson
import com.kostovtd.boardy.data.models.*
import com.kostovtd.boardy.data.repositories.GameSessionRepository
import com.kostovtd.boardy.data.repositories.MessageType
import com.kostovtd.boardy.data.repositories.ResourceStatus
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.util.Constants
import com.kostovtd.boardy.util.generateQRCodeBitmap
import kotlinx.coroutines.launch

open class BaseGameViewModel : ViewModel() {

    private val gameSessionRepository = GameSessionRepository()
    private val userRepository = UserRepository()

    private val _isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isGameSessionCreated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val isGameSessionCreated: LiveData<Boolean> = _isGameSessionCreated

    private val _isCurrentPlayerAdded: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val isCurrentPlayerAdded: LiveData<Boolean> = _isCurrentPlayerAdded

    private val _errorMessageId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val errorMessageId: LiveData<Int> = _errorMessageId

    protected val _gameSessionFirestore: MutableLiveData<GameSessionFirestore> by lazy {
        MutableLiveData<GameSessionFirestore>()
    }
    val gameSessionFirestore: LiveData<GameSessionFirestore> = _gameSessionFirestore

    protected val _gameSessionDatabase: MutableLiveData<GameSessionDatabase> by lazy {
        MutableLiveData<GameSessionDatabase>()
    }
    val gameSessionDatabase: LiveData<GameSessionDatabase> = _gameSessionDatabase

    private val _playerType: MutableLiveData<PlayerType> by lazy {
        MutableLiveData<PlayerType>()
    }
    val playerType: LiveData<PlayerType> = _playerType

    private val _boardGameGameSession: MutableLiveData<BoardGameGameSession> by lazy {
        MutableLiveData<BoardGameGameSession>()
    }
    val boardGameGameSession: LiveData<BoardGameGameSession> = _boardGameGameSession

    private val _generatedQRCode: MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
    }
    val generatedQRCode: LiveData<Bitmap> = _generatedQRCode

    val gameSessionStatus: LiveData<GameSessionStatus> =
        Transformations.map(_gameSessionFirestore) { it.status }

    val players: LiveData<ArrayList<String>> = Transformations.map(_gameSessionFirestore) {
        it.players
    }

    val currentPlayer = userRepository.getCurrentUser()

    val isCurrentPlayerRemoved: LiveData<Boolean> = Transformations.map(_gameSessionFirestore) {
        !it.players.contains(currentPlayer?.uid + Constants.FIRESTORE_VALUE_SEPARATOR + currentPlayer?.email)
    }

    val points: LiveData<HashMap<String, Int>> = Transformations.map(_gameSessionDatabase) {
        it.points
    }

    fun generateQRCode() {
        viewModelScope.launch {
            _boardGameGameSession.value?.let {
                val gameSessionQRJson = Gson().toJson(it)
                val gameSessionQRJsonBase64 =
                    Base64.encodeToString(gameSessionQRJson.toByteArray(), Base64.DEFAULT)
                val bitmap = generateQRCodeBitmap(gameSessionQRJsonBase64)
                bitmap?.let {
                    _generatedQRCode.value = bitmap
                } ?: run {
                    _errorMessageId.value = MessageType.CREATE_QR_CODE_ERROR.resourceId
                }
            } ?: run {
                _errorMessageId.value = MessageType.CREATE_QR_CODE_ERROR.resourceId
            }
        }
    }


    fun createGameSession(startingPoints: Int) {
        viewModelScope.launch {
            currentPlayer?.let { currentUser ->
                boardGameGameSession.value?.boardGameId?.let { boardGameId ->
                    val adminId = currentUser.uid
                    val losers = ArrayList<String>()

                    val playerArrEntry = adminId +
                            Constants.FIRESTORE_VALUE_SEPARATOR + currentUser.email
                    val players = arrayListOf(playerArrEntry)

                    val teamArrEntry = adminId +
                            Constants.FIRESTORE_VALUE_SEPARATOR + currentUser.email
                    val teams = arrayListOf(teamArrEntry)

                    val winners = ArrayList<String>()
                    val status = GameSessionStatus.NOT_STARTED_YET

                    val gameSessionFirestoreObj = GameSessionFirestore(
                        "", adminId, boardGameId, null, null,
                        startingPoints, players, teams, winners, losers, status
                    )

                    _isLoading.value = true

                    val response =
                        gameSessionRepository.createGameSession(gameSessionFirestoreObj)

                    _isLoading.value = false

                    when (response.status) {
                        ResourceStatus.SUCCESS -> {
                            _boardGameGameSession.value?.gameSessionId =
                                response.data?.gameSessionId
                            _isGameSessionCreated.value = true
                        }
                        ResourceStatus.ERROR -> {
                            _errorMessageId.value = response.message?.resourceId
                        }
                    }
                } ?: run {
                    _errorMessageId.value =
                        MessageType.CREATE_GAME_SESSION_ERROR.resourceId
                }
            } ?: run {
                _errorMessageId.value =
                    MessageType.CREATE_GAME_SESSION_ERROR.resourceId
            }
        }
    }


    fun startGameSession() {
        viewModelScope.launch {
            _gameSessionFirestore.value?.id?.let { gameSessionId ->
                _isLoading.value = true

                val response = gameSessionRepository.changeGameSessionStatus(
                    gameSessionId,
                    GameSessionStatus.ACTIVE
                )

                _isLoading.value = false

                if (response.status == ResourceStatus.ERROR) {
                    _errorMessageId.value = response.message?.resourceId
                }
            }
        }
    }


    fun suspendGameSession() {
        viewModelScope.launch {
            _gameSessionFirestore.value?.id?.let { gameSessionId ->
                _isLoading.value = true

                val response = gameSessionRepository.changeGameSessionStatus(
                    gameSessionId,
                    GameSessionStatus.SUSPENDED
                )

                _isLoading.value = false

                if (response.status == ResourceStatus.ERROR) {
                    _errorMessageId.value = response.message?.resourceId
                }
            }
        }
    }


    fun endGameSession() {
        viewModelScope.launch {
            _gameSessionFirestore.value?.id?.let { gameSessionId ->
                _isLoading.value = true

                val response = gameSessionRepository.changeGameSessionStatus(
                    gameSessionId,
                    GameSessionStatus.FINISHED
                )

                _isLoading.value = false

                if (response.status == ResourceStatus.ERROR) {
                    _errorMessageId.value = response.message?.resourceId
                }
            }
        }
    }


    fun getGameSessionById(gameSessionId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val response = gameSessionRepository.getGameSessionByIdV2(gameSessionId)

            _isLoading.value = false

            when (response.status) {
                ResourceStatus.SUCCESS -> {
                    _gameSessionFirestore.value = response.data?.gameSession
                    _gameSessionDatabase.value = response.data?.realTimeGameSession
                }
                ResourceStatus.ERROR -> {
                    _errorMessageId.value = response.message?.resourceId
                }
            }
        }
    }


    fun addCurrentPlayerToGameSession(points: Int) {
        viewModelScope.launch {
            val currentUser = currentPlayer
            currentUser?.let {
                currentUser.email?.let { email ->
                    _boardGameGameSession.value?.gameSessionId?.let { gameSessionId ->
                        _isLoading.value = true

                        val response =
                            gameSessionRepository.addPlayerToGameSession(
                                gameSessionId,
                                email,
                                currentUser.uid,
                                points
                            )

                        _isLoading.value = false

                        if (response.status == ResourceStatus.ERROR) {
                            _errorMessageId.value = response.message?.resourceId
                        } else {
                            _isCurrentPlayerAdded.value = true
                        }
                    }
                }
            }
        }
    }


    fun removePlayerFromGameSession(playerData: String) {
        viewModelScope.launch {
            _boardGameGameSession.value?.gameSessionId?.let { gameSessionId ->
                _isLoading.value = true

                val playerId = playerData.split(Constants.FIRESTORE_VALUE_SEPARATOR)[0]
                val playerEmail = playerData.split(Constants.FIRESTORE_VALUE_SEPARATOR)[1]

                val response = gameSessionRepository.removePlayerFromGameSession(gameSessionId, playerId, playerEmail)

                _isLoading.value = false

                if (response.status == ResourceStatus.ERROR) {
                    _errorMessageId.value = response.message?.resourceId
                }
            }
        }
    }


    fun removeCurrentPlayerFromGameSession() {
        viewModelScope.launch {
            _boardGameGameSession.value?.gameSessionId?.let { gameSessionId ->
                currentPlayer?.let {
                    it.email?.let { email ->
                        _isLoading.value = true

                        val response = gameSessionRepository.removePlayerFromGameSession(gameSessionId, it.uid, email)

                        _isLoading.value = false

                        if (response.status == ResourceStatus.ERROR) {
                            _errorMessageId.value = response.message?.resourceId
                        }
                    }
                }
            }
        }
    }


    fun changePoints(
        points: Int
    ) {
        viewModelScope.launch {
            _boardGameGameSession.value?.gameSessionId?.let { gameSessionId ->
                currentPlayer?.uid?.let {
                    gameSessionRepository.changePoints(gameSessionId, it, points)
                }
            }
        }
    }


    fun subscribeToGameSession() {
        _boardGameGameSession.value?.gameSessionId?.let { gameSessionId ->
            viewModelScope.launch {
                subscribeToGameSessionFirestore(gameSessionId)
            }
            viewModelScope.launch {
                subscribeToGameSessionDatabase(gameSessionId)
            }
        }
    }


    fun unsubscribeFromGameSession() {
        _boardGameGameSession.value?.gameSessionId?.let { gameSessionId ->
            gameSessionRepository.unsubscribeGameSessionFirestore()
            gameSessionRepository.unsubscribeGameSessionDatabase(gameSessionId)
        }
    }


    fun setPlayerType(playerType: PlayerType) {
        _playerType.value = playerType
    }


    fun setBoardGameGameSession(boardGameGameSession: BoardGameGameSession) {
        _boardGameGameSession.value = boardGameGameSession
    }


    fun setGameSessionFirestore(gameSessionFirestore: GameSessionFirestore) {
        _gameSessionFirestore.value = gameSessionFirestore
    }


    fun setGameSessionDatabase(gameSessionDatabase: GameSessionDatabase) {
        _gameSessionDatabase.value = gameSessionDatabase
    }


    private suspend fun subscribeToGameSessionFirestore(gameSessionId: String) {
        gameSessionRepository.subscribeGameSessionFirestore(gameSessionId).collect {
            this._gameSessionFirestore.value = it
        }
    }


    private suspend fun subscribeToGameSessionDatabase(gameSessionId: String) {
        gameSessionRepository.subscribeGameSessionDatabase(gameSessionId).collect {
            this._gameSessionDatabase.value = it
        }
    }
}
