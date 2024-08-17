package com.isar.memeshare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isar.memeshare.retrofit.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _memeUrl = MutableLiveData<String>()
    val memeUrl     : LiveData<String> get() = _memeUrl
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading  : LiveData<Boolean> get() = _isLoading

    private val _list = MutableLiveData<MutableList<String>>(mutableListOf())
    val list: LiveData<MutableList<String>> get() = _list


    fun loadMeme(){
        _isLoading.value = true
        viewModelScope.launch {
            repository.getMemes { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = {url -> _memeUrl.value = url},
                    onFailure = { error ->  }
                )
            }
        }

    }



    fun onDoubleTapLike(currentUrl: String) {
        val newList = _list.value ?: mutableListOf()
        if (currentUrl.isNotEmpty()) {
            newList.add(currentUrl)
        }
        _list.value = newList
    }
}