package com.example.starwars.data.data_source.remote.dto.FilmDTO

import com.example.starwars.domain.model.Film
import java.net.URL

data class FilmDTO(
    val characters: List<String?>?,
    val created: String?,
    val director: String?,
    val edited: String?,
    val episode_id: Int?,
    val opening_crawl: String?,
    val planets: List<String?>?,
    val producer: String?,
    val release_date: String?,
    val species: List<String?>?,
    val starships: List<String?>?,
    val title: String?,
    val url: String?,
    val vehicles: List<String?>?,
) {
    fun toFilm(): Film {
        return Film(
            filmTitle = title ?: "",
            filmId = url?.let { extractFilmId(it) } ?: "",
            created = created ?: "",
            director = director ?: "",
            edited = edited ?: "",
            producer = producer ?: "",
            release_date = release_date ?: "",
            title = title ?: ""
        )
    }

    private fun extractFilmId(url: String?): String? {
        // Find the last index of '/'
        if (url == null)
            return null

        val urlObject = URL(url)
        val pathSegments = urlObject.path.split("/")
        return pathSegments[pathSegments.size - 2]
    }
}