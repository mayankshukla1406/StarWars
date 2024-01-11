package com.example.starwars.presentation

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwars.domain.model.Film
import com.example.starwars.domain.repository.StarWarsRepository
import com.example.starwars.presentation.CharacterData.FilmState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.starwars.util.CharacterListState
import com.example.starwars.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class StarWarsViewModel @Inject constructor(
    private val starWarsRepository: StarWarsRepository,
) : ViewModel() {

    private val _characterList = MutableStateFlow(CharacterListState())
    val characterList: StateFlow<CharacterListState> = _characterList

    private val _film = MutableStateFlow(FilmState())
    val film: StateFlow<FilmState> = _film


    private var currentPage = 1

    fun getListOfCharacters() {
        viewModelScope.launch() {
            // Used Dispatchers.IO for the network request
            withContext(Dispatchers.IO) {
                starWarsRepository.getAllCharacters(currentPage.toString())
                    .collect { response ->
                        // Switch back to the main thread for updating Data
                        withContext(Dispatchers.Main) {
                            when (response) {
                                is ResponseState.Success -> {
                                    _characterList.value =
                                        CharacterListState(false, response.data ?: emptyList())
                                }

                                is ResponseState.Loading -> {
                                    _characterList.value = CharacterListState(true)
                                }

                                is ResponseState.Error -> {
                                    _characterList.value =
                                        CharacterListState(
                                            false,
                                            emptyList(),
                                            response.errorMessage ?: "Error Occurred"
                                        )
                                }
                            }
                        }
                    }
            }
        }
    }

    // Function to fetch film details for a list of film URLs
    fun fetchFilmListDetails(filmUrls: List<String?>) {
        val filmList = arrayListOf<Film>()
        viewModelScope.launch {
            _film.value = FilmState(true)
            // Create a list of deferred tasks for parallel network calls
            filmUrls.map { filmUrl ->
                async(Dispatchers.IO) {
                    try {
                        // Fetch film details using getFilmFromId function (replace with your actual implementation)
                        if (!filmUrl.isNullOrBlank()) {
                            var filmId = extractFilmIdFromUrl(filmUrl)
                            val film = starWarsRepository.getFilmFromId(filmId)
                            film?.let {
                                filmList.add(it)
                            }
                        } else {
                            Log.d("exception", "Film Url is null or blank")
                        }
                    } catch (e: Exception) {
                        // Handle exceptions for individual network calls
                        Log.d("exception", e.localizedMessage ?: "Film Details not fetched")
                    }
                }
            }.awaitAll()
            _film.value = FilmState(false, filmList)
        }
    }

    private fun extractFilmIdFromUrl(filmUrl: String): String {
        // Find the last index of '/'
        val urlObject = URL(filmUrl)
        val pathSegments = urlObject.path.split("/")
        return pathSegments[pathSegments.size - 2]
    }


    // Call this function when reaching the end of the list to load the next page
    fun loadNextPage() {
        currentPage++
        getListOfCharacters()
    }
}