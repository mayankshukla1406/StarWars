package com.example.starwars.data.data_source.remote.dto.CharacterDTO

import com.example.starwars.domain.model.Character
import com.example.starwars.domain.model.Film
import java.net.URL
import java.util.Random

data class Result(
    val birth_year: String?,
    val created: String?,
    val edited: String?,
    val eye_color: String?,
    val films: List<String?>?,
    val gender: String?,
    val hair_color: String?,
    val height: String?,
    val homeworld: String?,
    val mass: String?,
    val name: String?,
    val skin_color: String?,
    val species: List<String?>?,
    val starships: List<String?>?,
    val url: String?,
    val vehicles: List<String?>?
) {
    fun toCharacter() : Character {
        return Character(
            characterID = extractLastInteger(url)?:Random().ints().toString(),
            characterName = name?:"Nothing",
            birthYear = birth_year?:"Default Year",
            eye_color = eye_color?:"Nothing",
            hairColor = hair_color?:"Nothing",
            height = height?:"Not Provided",
            gender = gender?:"Not Provided",
            films = (films?: emptyList<String>()),
            mass = mass?:"Not Provided",
            skin_color = skin_color?:"Not Provided"
        )
    }

    private fun extractLastInteger(url: String?): String? {
        // Find the last index of '/'
        if(url == null)
            return null

        val urlObject = URL(url)
        val pathSegments = urlObject.path.split("/")
        return pathSegments[pathSegments.size - 2]
    }
}