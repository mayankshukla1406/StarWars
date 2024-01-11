package com.example.starwars.domain.repository

import com.example.starwars.domain.model.Character
import com.example.starwars.domain.model.Film
import com.example.starwars.util.ResponseState
import kotlinx.coroutines.flow.Flow

interface StarWarsRepository {

    fun getAllCharacters(page: String): Flow<ResponseState<List<Character>>>

    suspend fun getFilmFromId(id: String): Film?

}
