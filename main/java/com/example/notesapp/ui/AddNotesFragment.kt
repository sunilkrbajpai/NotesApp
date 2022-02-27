package com.example.notesapp.ui

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentAddNotesBinding
import com.example.notesapp.db.Note
import com.example.notesapp.db.NoteDatabase
import kotlinx.coroutines.launch

private lateinit var binding: FragmentAddNotesBinding

class AddNotesFragment : BaseFragment() {

    private var note:Note?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                inflater,
                R.layout.fragment_add_notes,
                container,
                false
            ) as FragmentAddNotesBinding
        val rootView = binding.getRoot()
        return rootView
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_notes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Update saved items
        arguments?.let {
            note=AddNotesFragmentArgs.fromBundle(it).note
            binding.editTextNote.setText(note?.note)
            binding.editTextTitle.setText(note?.title)
        }

        binding.btnSave.setOnClickListener(View.OnClickListener {view->

            val noteTitle= binding.editTextTitle.text.toString().trim()
            val noteBody= binding.editTextNote.text.toString().trim()

            if(noteTitle.isEmpty()){
                binding.editTextTitle.error="Title Required!"
                binding.editTextTitle.requestFocus()
                return@OnClickListener
            }

            if(noteBody.isEmpty()){
                binding.editTextNote.error="Please add content in note!"
                binding.editTextNote.requestFocus()
                return@OnClickListener
            }

            //using kotlin coroutines

            launch {

                context?.let {
                    val mNote= Note(noteTitle,noteBody)

                    if(note==null){
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        it.toast("Note Saved")
                    }
                    else{
                        mNote.id=note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        it.toast("Note Updated")
                    }


                    val action=AddNotesFragmentDirections.actionSaveNotes()
                     Navigation.findNavController(view).navigate(action)
                }
            }

            //call async function
//            saveNote(note)

        })


    }

    //menu item select
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.delete->if(note!=null) deleteNode() else context?.toast("Cannot delete unsaved note!")

        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNode() {
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("You cannot undo this operation once deleted!")
            setPositiveButton("Yes"){_,_->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    //move to Home
                    val action=AddNotesFragmentDirections.actionSaveNotes()
                    Navigation.findNavController(requireView()).navigate(action)
                    context.toast("Note deleted!")
                }
            }
            setNegativeButton("No"){_,_->

            }
        }.create().show()
    }

    //create option in menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)
    }

    //depreciated
//    private fun saveNote(note:Note)
//    {
//        class SaveNote:AsyncTask<Void,Void,Void>(){
//            override fun doInBackground(vararg params: Void?): Void ?{
//                NoteDatabase(requireActivity()).getNoteDao().addNote(note)
//                return null
//            }
//
//            override fun onPostExecute(result: Void?) {
//                super.onPostExecute(result)
//                Toast.makeText(activity,"Note Saved",Toast.LENGTH_SHORT).show()
//            }
//        }
//SaveNote().execute()
//    }


}