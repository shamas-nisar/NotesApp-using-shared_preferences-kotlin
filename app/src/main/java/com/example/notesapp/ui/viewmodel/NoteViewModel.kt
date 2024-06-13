package com.example.notesapp.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.database.NoteDatabase
import com.example.notesapp.data.model.Note
import com.example.notesapp.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(context: Context) : ViewModel() {
    private var repository: NoteRepository

    init {
        val noteDao = NoteDatabase.getNoteDatabaseInstance(context).noteDao()
        repository = NoteRepository(noteDao)
    }

    fun getNotesAll(): LiveData<List<Note>> {
        return  repository.getAllNotesFromDB()
    }

    fun addNotes(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNoteInDB(note)
        }
    }

    fun deleteNotes(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNoteFromDB(id)
        }
    }

    suspend fun shareNote(id: Int): Note? {

        return repository.shareNoteFromDB(id)
        /*viewModelScope.launch(Dispatchers.IO) {
            repository.shareNoteFromDB(id)
        }*/
    }

    fun updateNotes(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNoteInDB(note)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    class NoteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                return NoteViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}



/*
class NoteViewModel(application: Application, private val repository: NoteRepository) : AndroidViewModel(application) {

    suspend fun addNotes(note: Note) {
        repository.insertNoteInDB(note)
    }
    suspend fun deleteNotes(id : Int) {
        repository.deleteNoteFromDB(id)
    }

    suspend fun updateNotes(note: Note) {
        repository.updateNoteInDB(note)
    }

    suspend fun shareNote(id: Int) {
        repository.shareNoteFromDB(id)
    }

    fun getNotesAll(): LiveData<List<Note>> = repository.getAllNotesFromDB()

}*/
