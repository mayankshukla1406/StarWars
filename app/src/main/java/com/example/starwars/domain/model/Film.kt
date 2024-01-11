package com.example.starwars.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "star_wars_film")
data class Film(
    @PrimaryKey
    val filmId : String,
    val filmTitle : String,
    val created: String,
    val director: String,
    val edited: String,
    val producer: String,
    val release_date: String,
    val title: String,
)
