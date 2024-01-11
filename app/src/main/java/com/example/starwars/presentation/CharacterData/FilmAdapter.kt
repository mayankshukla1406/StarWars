package com.example.starwars.presentation.CharacterData

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.databinding.ItemFilmCardBinding
import com.example.starwars.domain.model.Film

class FilmAdapter : ListAdapter<Film, FilmAdapter.FilmViewHolder>(FilmDiffCallback()) {

    inner class FilmViewHolder(private val binding: ItemFilmCardBinding ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(film: Film) {
            binding.filmTitle.text = film.filmTitle
            binding.filmCreated.text = film.created
            binding.filmDirector.text = film.director
            binding.filmEdited.text = film.edited
            binding.filmProducer.text = film.producer
            binding.filmReleaseDate.text = film.release_date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = ItemFilmCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class FilmDiffCallback : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.filmId == newItem.filmId
        }
    }
}
