package com.example.starwars.presentation.CharacterData

import com.example.starwars.domain.model.Film

data class FilmState(
    var isLoading : Boolean = false,
    var film : List<Film> = emptyList(),
    var error : String = ""
)
