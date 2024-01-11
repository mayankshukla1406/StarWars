package com.example.starwars.di

import android.content.Context
import androidx.room.Room
import com.example.starwars.data.data_source.offline.CharactersDao
import com.example.starwars.data.data_source.offline.FilmsDao
import com.example.starwars.data.data_source.offline.StarWarsDatabase
import com.example.starwars.data.data_source.remote.dto.StarWarsApi
import com.example.starwars.data.repository.StarWarsRepoImpl
import com.example.starwars.domain.repository.StarWarsRepository
import com.example.starwars.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StarWarsModule {

    @Provides
    @Singleton
    fun provideStarWarsApi(): StarWarsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(StarWarsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStarWarsDatabase(@ApplicationContext context: Context): StarWarsDatabase {
        return Room.databaseBuilder(
            context,
            StarWarsDatabase::class.java,
            Constants.ROOM_DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharactersDao(database: StarWarsDatabase): CharactersDao {
        return database.charactersDao()
    }

    @Provides
    @Singleton
    fun provideFilmsDao(database: StarWarsDatabase): FilmsDao {
        return database.filmsDao()
    }

    @Provides
    @Singleton
    fun provideStarWarsRepository(starWarsApi: StarWarsApi,charactersDao: CharactersDao,filmsDao: FilmsDao): StarWarsRepository {
        return StarWarsRepoImpl(starWarsApi = starWarsApi, characterDao = charactersDao,filmsDao)
    }

}