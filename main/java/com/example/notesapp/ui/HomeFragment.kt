package com.example.notesapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentHomeBinding
import com.example.notesapp.db.NoteDatabase
import kotlinx.coroutines.launch

private lateinit var binding:FragmentHomeBinding

class HomeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
                DataBindingUtil.inflate<ViewDataBinding>(
                        inflater,
                        R.layout.fragment_home,
                        container,
                        false
                ) as FragmentHomeBinding
        val rootView = binding.getRoot()
        return rootView
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.recyclerViewNotes.setHasFixedSize(true)
        binding.recyclerViewNotes.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)

        launch {

            context?.let {
                val notes=NoteDatabase(it).getNoteDao().getAllNotes()

                //set adapter
                binding.recyclerViewNotes.adapter=NotesAdapter(notes)
            }
        }
        binding.add.setOnClickListener(View.OnClickListener {

            val action=HomeFragmentDirections.actionAddNote()
            Navigation.findNavController(it).navigate(action)
        })

    }
}