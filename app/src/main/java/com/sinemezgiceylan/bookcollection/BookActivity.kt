package com.sinemezgiceylan.bookcollection

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.sinemezgiceylan.bookcollection.databinding.ActivityBookBinding

class BookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookBinding
    private lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        database = this.openOrCreateDatabase("Books", MODE_PRIVATE,null)


        val intent = intent
        val info = intent.getStringExtra("info")
        if (info.equals("new")) {
            binding.bookName.setText("")
            binding.writerName.setText("")
            binding.type.setText("")
            binding.page.setText("")
            binding.button.visibility = View.VISIBLE


        } else {
            binding.button.visibility = View.INVISIBLE
            val selectedId = intent.getIntExtra("id",1)

            val cursor = database.rawQuery("SELECT * FROM books WHERE id = ?",arrayOf(selectedId.toString()))

            val bookNameIx = cursor.getColumnIndex("bookname")
            val writerNameIx = cursor.getColumnIndex("writername")
            val typeIx = cursor.getColumnIndex("type")
            val pageIx = cursor.getColumnIndex("page")

            while(cursor.moveToNext()) {
                binding.bookName.setText(cursor.getString(bookNameIx))
                binding.writerName.setText(cursor.getString(writerNameIx))
                binding.type.setText(cursor.getString(typeIx))
                binding.page.setText(cursor.getString(pageIx))
            }

            cursor.close()


        }
    }

    fun save(view: View) {

        val bookName = binding.bookName.text.toString()
        val writerName = binding.writerName.text.toString()
        val type = binding.type.text.toString()
        val page = binding.page.text.toString()


        try {

            database.execSQL("CREATE TABLE IF NOT EXISTS books (id INTEGER PRIMARY KEY, bookname VARCHAR, writername VARCHAR, type VARCHAR, page VARCHAR)")

            val sqlString = "INSERT INTO books (bookname, writername, type, page) VALUES (?, ?, ?, ?)"
            val statement = database.compileStatement(sqlString)

            statement.bindString(1,bookName)
            statement.bindString(2,writerName)
            statement.bindString(3,type)
            statement.bindString(4,page)
            statement.execute()


        } catch (e: Exception) {
            e.printStackTrace()
        }

        Toast.makeText(this@BookActivity,"Saved!",Toast.LENGTH_LONG).show()

        val intent = Intent(this@BookActivity,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)


    }

}