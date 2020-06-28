package net.xblacky.animexstream.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.xblacky.animexstream.R
import net.xblacky.animexstream.utils.constants.Const
import net.xblacky.animexstream.utils.model.LoadingModel2
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class CommonViewModel2 : ViewModel() {

    private val _loadingModel: MutableLiveData<LoadingModel2> = MutableLiveData()
    val loadingModel: LiveData<LoadingModel2> = _loadingModel
    private var errorCode: Int = Const.ERROR_CODE_DEFAULT
    private var errorMsgId = R.string.something_went_wrong

    private fun updateErrorModel(e: Throwable?) {
        e?.let {
            if (e is HttpException) {
                errorCode = when (e.code()) {
                    HttpURLConnection.HTTP_BAD_REQUEST -> Const.RESPONSE_UNKNOWN
                    else -> Const.ERROR_CODE_DEFAULT
                }
            } else if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException) {
                errorCode = Const.NO_INTERNET_CONNECTION
                errorMsgId = R.string.no_internet
            } else {
                errorCode = Const.ERROR_CODE_DEFAULT
            }
        }
    }

    protected fun isLoading(): Boolean {
        _loadingModel.value?.let {
            return it.loading == Loading.LOADING
        } ?: return false
    }

    protected fun updateLoadingState(loading: Loading, e: Throwable?, isListEmpty: Boolean = true) {
        updateErrorModel(e)
        _loadingModel.value = LoadingModel2(
            loading = loading,
            errorCode = errorCode,
            errorMsg = errorMsgId,
            isListEmpty = isListEmpty
        )

    }

    enum class Loading {
        LOADING, COMPLETED, ERROR
    }
}