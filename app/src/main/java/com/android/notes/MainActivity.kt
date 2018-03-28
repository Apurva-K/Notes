package com.android.notes

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Apurva on 27-03-2018.
 *
 * Class will be used as main launcher class
 * To create new note
 * clear that note or save it
 * also contains button to show list of notes saved in database
 * */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mContext: Context
    private val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_main)
        mContext = this

        fab_show_saved.setOnClickListener {
            val intent = Intent(this, ShowSavedNotes::class.java)
            startActivity(intent)
        }
        edt_note.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 500) {
                    toast(getString(R.string.alert))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when (s.length) {
                    in 1..499 -> {
                        imgw_save.visibility = View.VISIBLE
                        imgvw_clear.visibility = View.VISIBLE
                    }
                    0 -> {
                        imgw_save.visibility = View.GONE
                        imgvw_clear.visibility = View.GONE
                    }
                    500 -> Toast.makeText(mContext, getString(R.string.alert), Toast.LENGTH_SHORT).show()
                }
            }
        })
        imgw_save.setOnClickListener(this)
        imgvw_clear.setOnClickListener(this)


    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgw_save -> showSaveDialog()
            R.id.imgvw_clear -> edt_note.text.clear()
        }
    }

    /*
    Show dialog to edit note name while saving
     */
    private fun showSaveDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle(getString(R.string.save_note))
        val b = dialogBuilder.create()
        val edt_title = dialogView.findViewById<View>(R.id.edt_title) as EditText
        val btn_cancel = dialogView.findViewById<View>(R.id.btn_cancel) as Button
        val btn_save = dialogView.findViewById<View>(R.id.btn_save) as Button
        btn_cancel.setOnClickListener {
            b.cancel()
        }
        btn_save.setOnClickListener {

            var values = ContentValues()
            values.put("text", edt_note.text.toString())
            values.put("title", edt_title.text.toString())
            values.put("date", sdf.format(Calendar.getInstance().time))
            if (database.insertValues(values) > 0) {
                b.cancel()
                edt_note.text.clear()
                toast(getString(R.string.note_saved_sucessfully))
            }
        }
        b.show()
    }


}
