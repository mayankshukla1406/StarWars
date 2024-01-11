package com.example.starwars.presentation.CharacterList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.starwars.databinding.ItemCharacterCardBinding
import com.example.starwars.domain.model.Character


class CharacterListAdapter(
    private var characters: List<Character>,
    private val onItemClick: (Character) -> Unit
) : RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {

    inner class CharacterViewHolder(private val binding: ItemCharacterCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(character: Character) {
            // Bind data to the views using view binding
            binding.characterName.text = character.characterName
            binding.characterGender.text = "Gender : ${character.gender}"
            binding.characterBirthYear.text = "Birth Year : ${character.birthYear}"
            binding.characterEyeColor.text = "Eye Color : ${character.eye_color}"
            binding.characterHairColor.text = "Hair Color : ${character.hairColor}"

            binding.root.setOnClickListener {
                onItemClick(character)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount(): Int = characters.size

    fun updateList(newCharacters: List<Character>) {
        val oldSize = characters.size
        (characters as ArrayList).addAll(newCharacters)
        val newSize = characters.size
        notifyItemRangeInserted(oldSize, newSize - oldSize)
    }

    fun getCharacterList() : List<Character> {
        return characters
    }
}

