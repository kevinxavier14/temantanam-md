package com.capstone.temantanam.ui.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.temantanam.response.GetPlantData

class PlantCollectionIdViewModel : ViewModel() {

    private val _plantIdList = MutableLiveData<List<Int>>()
    val plantIdList: LiveData<List<Int>> = _plantIdList

    fun addPlantId(id: Int) {
        val currentList = _plantIdList.value ?: emptyList() // Get the current list of plant IDs or an empty list if it's null
        val updatedList = currentList.toMutableList() // Create a mutable copy of the list
        updatedList.add(id) // Add the new ID to the list
        _plantIdList.value = updatedList // Update the value of _plantIdList with the updated list
    }

    fun deletePlantId(id: Int) {
        val currentList = _plantIdList.value.orEmpty().toMutableList()
        currentList.removeAll { it == id }
        _plantIdList.value = currentList
    }

    fun inCollection(data: GetPlantData?): Boolean {
        val plantIdListValue = _plantIdList.value
        return plantIdListValue?.contains(data?.id) ?: false
    }

    fun clearAllPlantId() {
        _plantIdList.value = emptyList() // Set the value of _plantIdList to an empty list
    }
}
