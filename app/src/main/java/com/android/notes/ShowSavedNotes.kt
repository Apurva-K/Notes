package com.android.notes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_show_saved_notes.*

/**
 * Created by Apurva on 27-03-2018.
 *
 * Class to show lis of notes
 */
class ShowSavedNotes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_saved_notes)
        val hashMapArrayList = database.fetchNotes()
        recycler_notes.layoutManager = LinearLayoutManager(this)
        recycler_notes.adapter = NotesAdapter(this, hashMapArrayList, database)

        if(hashMapArrayList.isEmpty()) {
            txtvw_no_notes.visibility = View.VISIBLE
            recycler_notes.visibility = View.GONE
        }
        else
        {
            txtvw_no_notes.visibility = View.GONE
            recycler_notes.visibility = View.VISIBLE
        }

    }
}