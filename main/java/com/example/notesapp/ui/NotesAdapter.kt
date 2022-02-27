package com.example.notesapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.databinding.NotesLayoutBinding
import com.example.notesapp.db.Note

class NotesAdapter(private val notes:List<Note>):RecyclerView.Adapter<NotesAdapter.NoteViewholder> (){

    class NoteViewholder(val myview: NotesLayoutBinding):RecyclerView.ViewHolder(myview.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewholder {
        return NoteViewholder(DataBindingUtil.inflate<NotesLayoutBinding>(LayoutInflater.from(parent.context), R.layout.notes_layout,parent,false))
    }

    override fun onBindViewHolder(holder: NoteViewholder, position: Int) {
        holder.myview.textViewTitle.text=notes[position].title
        holder.myview.textViewNote.text=notes[position].note
        holder.myview.root.setOnClickListener{
            val action=HomeFragmentDirections.actionAddNote()
            action.note=notes[position]
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount()=notes.size
    }
