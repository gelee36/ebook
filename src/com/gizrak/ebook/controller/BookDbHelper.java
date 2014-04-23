
package com.gizrak.ebook.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gizrak.ebook.controller.BookConstract.BookColumns;
import com.gizrak.ebook.controller.BookConstract.BookmarkColumns;
import com.gizrak.ebook.controller.BookConstract.TocColumns;
import com.gizrak.ebook.model.BookItem;

import java.util.ArrayList;

public class BookDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "book.db";

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates book table
        db.execSQL("CREATE TABLE book (" +
                BookColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BookColumns.TYPE + " TEXT NOT NULL," +
                BookColumns.TITLE + " TEXT NOT NULL," +
                BookColumns.AUTHOR + " TEXT," +
                BookColumns.PUBLISHER + " TEXT," +
                BookColumns.ISBN + " TEXT," +
                BookColumns.LANGUAGE + " TEXT," +
                BookColumns.COVER + " BLOB," +
                BookColumns.PATH + " TEXT," +
                BookColumns.CUR_CHAP + " INTEGER NOT NULL DEFAULT 1," +
                BookColumns.CUR_PAGE + " INTEGER NOT NULL DEFAULT 1," +
                BookColumns.CUR_PCNT + " INTEGER NOT NULL DEFAULT 0" +
                ");");

        // Creates toc table
        db.execSQL("CREATE TABLE toc (" +
                TocColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TocColumns.BID + " INTEGER NOT NULL," +
                TocColumns.SEQ + " INTEGER NOT NULL DEFAULT 0," +
                TocColumns.TITLE + " TEXT NOT NULL," +
                TocColumns.URL + " TEXT NOT NULL," +
                TocColumns.ANCHOR + " TEXT" +
                ");");

        // Creates book mark table
        db.execSQL("CREATE TABLE bookmark (" +
                BookmarkColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BookmarkColumns.BID + " INTEGER NOT NULL," +
                BookmarkColumns.CHAPTER + " INTEGER NOT NULL DEFAULT 1," +
                BookmarkColumns.PERCENTAGE + " INTEGER NOT NULL DEFAULT 0" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing

    }

    public ArrayList<BookItem> getBookList() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("book", new String[] {

                }, null, null, null, null, BookColumns.TITLE);

        if (cursor != null) {
            cursor.moveToFirst();

            ArrayList<BookItem> list = new ArrayList<BookItem>(cursor.getCount());
            BookItem item;
            while (cursor.moveToNext()) {
                item = new BookItem(cursor.getString(cursor.getColumnIndex(BookColumns._ID)));
                // item.setco
                list.add(item);
            }
            return list;
        }
        return null;
    }
}
