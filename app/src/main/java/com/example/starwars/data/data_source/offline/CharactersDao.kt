package com.example.starwars.data.data_source.offline

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starwars.domain.model.Character

@Dao
interface CharactersDao {

    @Query("SELECT * FROM star_wars_character")
    fun getAllCharacters() : List<Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCharacters(characters : List<Character>)

}