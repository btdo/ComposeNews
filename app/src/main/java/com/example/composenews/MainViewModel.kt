package com.example.composenews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenews.repository.ComposeChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ComposeChatRepository) : ViewModel(){


    fun testing(){
        viewModelScope.launch {
            repository.getEverything("testing")
        }
    }
}