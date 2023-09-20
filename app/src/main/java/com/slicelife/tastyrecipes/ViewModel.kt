package com.slicelife.tastyrecipes

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ViewModel(service: NetworkService): ViewModel() {

    val items = mutableStateOf(ItemList())

    init {
        viewModelScope.launch{
            items.value = service.getRecipes()
        }
    }
}