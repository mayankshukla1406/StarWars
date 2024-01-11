package com.example.starwars.data.repository

import android.util.Log
import com.example.starwars.data.data_source.offline.CharactersDao
import com.example.starwars.data.data_source.offline.FilmsDao
import com.example.starwars.data.data_source.remote.dto.StarWarsApi
import com.example.starwars.domain.model.Character
import com.example.starwars.domain.model.Film
import com.example.starwars.domain.repository.StarWarsRepository
import com.example.starwars.util.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class StarWarsRepoImpl @Inject constructor(
    private val starWarsApi: StarWarsApi,
    private val characterDao : CharactersDao,
    private val filmDao : FilmsDao
) : StarWarsRepository {

    override fun getAllCharacters(page: String): Flow<ResponseState<List<Character>>> =  flow {
        try {
            emit(ResponseState.Loading<List<Character>>())
            val cachedCharacters = characterDao.getAllCharacters()
            if(cachedCharacters.isNotEmpty() && cachedCharacters.size>=(page.toInt()*10)) {
                emit(ResponseState.Success<List<Character>>(cachedCharacters))
            } else {
                val characters = starWarsApi.getAllCharacters(page).results?.let {results ->
                    results.map {character->
                        character?.let {
                            it.toCharacter()
                        }?: Character("","","","", emptyList(),"","","","","")
                    }
                }?: emptyList<Character>()
                characterDao.insertAllCharacters(characters)
                emit(ResponseState.Success<List<Character>>(characters))
            }
        } catch (e : retrofit2.HttpException) {
            emit(ResponseState.Error<List<Character>>(e.localizedMessage?:"An Unexpected Error"))
        } catch (e : IOException) {
            emit(ResponseState.Error<List<Character>>(e.localizedMessage?:"Internet Error"))
        }
    }

    override suspend fun getFilmFromId(id: String): Film? {
        return try {
            val cachedFilm = filmDao.getFilmFromId(id)
            if(cachedFilm != null) {
                cachedFilm
            } else {
                val film = starWarsApi.getFilmFromId(id).toFilm()
                filmDao.insertFilm(film)
                film
            }
        } catch (e : retrofit2.HttpException) {
            Log.d("HttpException",e.localizedMessage?:"Film Details not fetched due to HTTP Exception")
            null
        } catch (e : IOException) {
            Log.d("IOException",e.localizedMessage?:"Film Details not fetched due to IO Exception")
            null
        }
    }
}
