package com.example.starwars.data.data_source.remote.dto

import com.example.starwars.data.data_source.remote.dto.CharacterDTO.CharactersDTO
import com.example.starwars.data.data_source.remote.dto.FilmDTO.FilmDTO
import com.example.starwars.domain.model.Character
import com.example.starwars.domain.model.Film
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StarWarsApi {

    @GET("api/people/")
    suspend fun getAllCharacters(@Query("page")page : String) : CharactersDTO

    @GET("api/films/")
    suspend fun getFilmFromId(@Path(":id")filmId : String) : FilmDTO

}