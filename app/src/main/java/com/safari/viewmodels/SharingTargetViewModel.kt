package com.safari.viewmodels

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SharingTargetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(){


}