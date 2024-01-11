package com.example.starwars.util

import com.example.starwars.domain.model.Character

data class CharacterListState(
    val isLoading : Boolean = true,
    val characters : List<Character> = emptyList(),
    val error : String = ""
)
