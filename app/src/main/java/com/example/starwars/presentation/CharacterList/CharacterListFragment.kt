package com.example.starwars.presentation.CharacterList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import android.widget.Toolbar.OnMenuItemClickListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.MainActivity
import com.example.starwars.R
import com.example.starwars.databinding.FragmentCharacterListBinding
import com.example.starwars.domain.model.Character
import com.example.starwars.domain.repository.StarWarsRepository
import com.example.starwars.presentation.StarWarsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class CharacterListFragment : Fragment(), SortOptionsListener, FilterOptionsBottomSheetFragment.FilterOptionsListener {

    private val starWarsViewModel: StarWarsViewModel by viewModels()

    private lateinit var binding: FragmentCharacterListBinding
    private var characters = arrayListOf<Character>()
    private lateinit var characterAdapter: CharacterListAdapter
    private var page: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCharacterListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarTitle.text = "Star Wars Characters"
        binding.toolbar.setOnMenuItemClickListener(object : OnMenuItemClickListener,
            androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(p0: MenuItem?): Boolean {

                if (p0 != null) {
                    when (p0.itemId) {
                        R.id.btSort -> {
                            val sortBottomSheetFragment = SortOptionsBottomSheetFragment()
                            sortBottomSheetFragment.setListener(this@CharacterListFragment)
                            sortBottomSheetFragment.show(
                                parentFragmentManager,
                                "sort_bottom_sheet_fragment"
                            )
                        }

                        R.id.btFilter -> {
                            val filterOptionsBottomSheetFragment = FilterOptionsBottomSheetFragment()
                            filterOptionsBottomSheetFragment.setFilterOptionsListener(this@CharacterListFragment)
                            filterOptionsBottomSheetFragment.show(
                                parentFragmentManager,
                                "filter_bottom_sheet_fragment"
                            )
                        }
                    }
                }

                return true
            }
        })

        characterAdapter = CharacterListAdapter(characters) { character ->
            navigateToCharacterFragment(character as Character)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = characterAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount

                    if (lastVisibleItemPosition == totalItemCount - 1) {
                        // User has reached the end, load the next page
                        starWarsViewModel.loadNextPage()
                    }
                }
            })
        }
        starWarsViewModel.getListOfCharacters()
        observeCharacterList()
    }


    private fun observeCharacterList() {
        CoroutineScope(Dispatchers.Main).launch {
            starWarsViewModel.characterList.collect { characterListState ->
                if (characterListState.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else if (characterListState.error.isNotBlank()) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        characterListState.error,
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    binding.progressBar.visibility = View.GONE
                    characterAdapter.updateList(characterListState.characters)
                }
            }
        }
    }


    private fun navigateToCharacterFragment(character: Character) {
        val bundle = Bundle().apply {
            putString("characterId",character.characterID)
            putString("characterName", character.characterName)
            putString("birthYear", character.birthYear)
            putString("eyeColor", character.eye_color)
            putString("hairColor", character.hairColor)
            putString("skinColor", character.skin_color)
            putString("gender", character.gender)
            putString("height", character.height)
            putString("mass", character.mass)
            putStringArrayList("films", ArrayList(character.films))
        }

        findNavController().navigate(R.id.action_characterListFragment_to_characterFragment,bundle)

    }

    override fun onSortOptionSelected(attribute: SortAttribute, sortOrder: SortOrder) {
        when (attribute) {
            SortAttribute.NAME -> sortByName(sortOrder)
            SortAttribute.HEIGHT -> sortByHeight(sortOrder)
            SortAttribute.MASS -> sortByMass(sortOrder)
        }
    }

    private fun sortByName(sortOrder: SortOrder) {
        (characterAdapter.getCharacterList() as ArrayList<Character>).sortBy { it.characterName }
        if (sortOrder == SortOrder.DESCENDING) {
            (characterAdapter.getCharacterList() as ArrayList<Character>).reverse()
        }
        characterAdapter.notifyDataSetChanged()
    }

    private fun sortByHeight(sortOrder: SortOrder) {
        (characterAdapter.getCharacterList() as ArrayList<Character>).sortBy { it.height.toDoubleOrNull() ?: Double.MAX_VALUE }
        if (sortOrder == SortOrder.DESCENDING) {
            (characterAdapter.getCharacterList() as ArrayList<Character>).reverse()
        }
        characterAdapter.notifyDataSetChanged()
    }

    private fun sortByMass(sortOrder: SortOrder) {
        (characterAdapter.getCharacterList() as ArrayList<Character>).sortBy { it.mass.toDoubleOrNull() ?: Double.MAX_VALUE }
        if (sortOrder == SortOrder.DESCENDING) {
            (characterAdapter.getCharacterList() as ArrayList<Character>).reverse()
        }
        characterAdapter.notifyDataSetChanged()
    }

    override fun onFilterOptionsSelected(gender: String, eyeColor: String) {
        val charactersFilteredList = filterCharacters(gender, eyeColor, characterAdapter.getCharacterList())
        (characterAdapter.getCharacterList() as ArrayList).clear()
        characterAdapter.updateList(charactersFilteredList)
    }

    private fun filterCharacters(gender: String, eyeColor: String, characters: List<Character>): List<Character> {
        return characters.filter { character ->
            (gender.isBlank() || character.gender.equals(gender, ignoreCase = true)) &&
                    (eyeColor.isBlank() || character.eye_color.equals(eyeColor, ignoreCase = true))
        }
    }
}