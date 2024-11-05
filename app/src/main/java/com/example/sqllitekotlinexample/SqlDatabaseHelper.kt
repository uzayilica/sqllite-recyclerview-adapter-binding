package com.example.sqllitekotlinexample

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqlDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "userdatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "usertable"
        private const val COLUMN_id = "id"
        private const val COLUMN_username = "username"
        private const val COLUMN_password = "password"
        private const val COLUMN_photo = "photo"

        private const val SQL_CREATE_TABLE_KODU =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_username TEXT," +
                    "$COLUMN_password TEXT," +
                    "$COLUMN_photo INT" +
                    ")"

        private const val SQL_DELETE_TABLE_KODU =
            "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_KODU)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_TABLE_KODU)
        onCreate(db)
    }

    fun insertUser(username: String, password: String, photo: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_username, username)
            put(COLUMN_password, password)
            put(COLUMN_photo, photo)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllUser(): List<User> {
        val listeUser = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_id))
                val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_username))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_password))
                val photo = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_photo))
                listeUser.add(User(id, username, password, photo))
            }
        }

        db.close()
        return listeUser
    }

    fun getUser(id: Int): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, "$COLUMN_id = ?", arrayOf(id.toString()), null, null, null
        )

        var user: User? = null
        cursor.use {
            if (cursor.moveToFirst()) {
                val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_username))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_password))
                val photo = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_photo))
                user = User(id, username, password, photo)
            }
        }

        db.close()
        return user
    }

    fun deleteUser(id: Int) {
        val db = this.writableDatabase
        try {
            db.delete(TABLE_NAME, "$COLUMN_id=?", arrayOf(id.toString()))
        } finally {
            db.close()
        }
    }

    fun updateUser(id: Int, username: String, password: String, photo: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_username, username)
            put(COLUMN_password, password)
            put(COLUMN_photo, photo)
        }
        try {
            db.update(TABLE_NAME, values, "$COLUMN_id=?", arrayOf(id.toString()))
        } finally {
            db.close()
        }
    }
}
