package com.android.notes

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_note.view.*
import java.util.*

/**
 * Created by Apurva on 28-03-2018.
 * Class is an adapter to show list of notes we have in database
 */
class NotesAdapter(private var mContext: Context, private var notesArrayList: ArrayList<Map<String, Any?>>, var database: MyDatabaseOpenHelper) :
        RecyclerView.Adapter<NotesAdapter.ASViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ASViewHolder? {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return ASViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notesArrayList.size
    }

    override fun onBindViewHolder(holder: ASViewHolder, position: Int) {
        holder.bindItem(position)
    }

    //view holder to bind views and assign listeners
    inner class ASViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(position: Int) = with(itemView)
        {
            itemView.setTag(R.string.key0, notesArrayList[position]["text"])
            itemView.setTag(R.string.key1, notesArrayList[position]["title"])
            itemView.note_title.text = notesArrayList[position]["title"].toString()
            itemView.note_date.text = notesArrayList[position]["date"].toString()
            itemView.imgvw_del.setTag(R.string.key0, notesArrayList[position]["date"])
            itemView.imgvw_del.setTag(R.string.key1, position)
            itemView.imgvw_del.setOnClickListener {
                if (database.delete(itemView.imgvw_del.getTag(R.string.key0).toString()) > 0) {
                    notesArrayList.removeAt(Integer.parseInt(itemView.imgvw_del.getTag(R.string.key1).toString()))
                    notifyDataSetChanged()
                }
            }
            itemView.setOnClickListener {
                val note = itemView.getTag(R.string.key0).toString()
                val note_title = itemView.getTag(R.string.key1).toString()
                showNote(note,note_title)
            }
        }
    }

    //Show note on click of item
    fun showNote( note:String,note_title:String) {
        val showNoteDialog = AlertDialog.Builder(mContext).create()
        showNoteDialog.setTitle(note_title)
        showNoteDialog.setMessage(note)
        showNoteDialog.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getString(R.string.ok), {
            dialogInterface, i ->
            showNoteDialog.cancel()
        })
        showNoteDialog.show()
    }
}