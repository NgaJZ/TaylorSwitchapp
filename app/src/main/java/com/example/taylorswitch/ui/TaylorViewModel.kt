package com.example.taylorswitch.ui

import androidx.lifecycle.ViewModel
import com.example.taylorswitch.data.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Declaration variable **/




class TaylorViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()


    //declaration function

}