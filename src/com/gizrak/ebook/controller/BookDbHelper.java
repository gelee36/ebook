
package com.gizrak.ebook.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gizrak.ebook.controller.BookConstract.BookColumns;
import com.gizrak.ebook.controller.BookConstract.BookmarkColumns;
import com.gizrak.ebook.controller.BookConstract.TocColumns;
import com.gizrak.ebook.model.BookItem;
import com.gizrak.ebook.model.ChapterItem;

import java.util.ArrayList;
import java.util.List;

public class BookDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "book.db";

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates book table
        db.execSQL("CREATE TABLE " +
                BookConstract.TABLE_BOOK + " (" +
                BookColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BookColumns.TYPE + " INTEGER NOT NULL DEFAULT 0," +
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
        db.execSQL("CREATE TABLE " +
                BookConstract.TABLE_TOC + " (" +
                TocColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TocColumns.BID + " INTEGER NOT NULL," +
                TocColumns.ORDER + " INTEGER NOT NULL DEFAULT 0," +
                TocColumns.TITLE + " TEXT NOT NULL," +
                TocColumns.URL + " TEXT NOT NULL," +
                TocColumns.ANCHOR + " TEXT" +
                ");");

        // Creates book mark table
        db.execSQL("CREATE TABLE " +
                BookConstract.TABLE_BOOKMARK + " (" +
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
        ArrayList<BookItem> list = new ArrayList<BookItem>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(BookConstract.TABLE_BOOK, null, null, null, null, null,
                    BookColumns.TITLE);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                BookItem item;
                do {
                    item = new BookItem(cursor.getString(cursor.getColumnIndex(BookColumns._ID)));
                    item.setType(cursor.getInt(cursor.getColumnIndex(BookColumns.TYPE)));
                    item.setTitle(cursor.getString(cursor.getColumnIndex(BookColumns.TITLE)));
                    item.setAuthor(cursor.getString(cursor.getColumnIndex(BookColumns.AUTHOR)));
                    item.setPublisher(cursor.getString(cursor.getColumnIndex(BookColumns.PUBLISHER)));
                    item.setIsbn(cursor.getString(cursor.getColumnIndex(BookColumns.ISBN)));
                    item.setLanguage(cursor.getString(cursor.getColumnIndex(BookColumns.LANGUAGE)));
                    item.setCover(cursor.getBlob(cursor.getColumnIndex(BookColumns.COVER)));
                    item.setPath(cursor.getString(cursor.getColumnIndex(BookColumns.PATH)));
                    ArrayList<ChapterItem> chapterList = getTocList(item.getId());
                    item.setToc(chapterList);
                    list.add(item);

                    item.setToc(chapterList);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return list;
    }

    private ArrayList<ChapterItem> getTocList(String bid) {
        ArrayList<ChapterItem> list = new ArrayList<ChapterItem>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(BookConstract.TABLE_TOC, null,
                    TocColumns.BID + " = ?",
                    new String[] {
                        bid
                    },
                    null,
                    null, TocColumns.ORDER);
            if (cursor != null) {
                cursor.moveToFirst();

                ChapterItem chapter;
                do {
                    chapter = new ChapterItem(cursor.getString(cursor
                            .getColumnIndex(TocColumns._ID)));
                    chapter.setOrder(cursor.getInt(cursor.getColumnIndex(TocColumns.ORDER)));
                    chapter.setLabel(cursor.getString(cursor.getColumnIndex(TocColumns.TITLE)));
                    chapter.setContent(cursor.getString(cursor.getColumnIndex(TocColumns.URL)));
                    list.add(chapter);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return list;
    }

    public void importBook(BookItem book) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(BookColumns.TYPE, book.getType().toInteger());
            values.put(BookColumns.TITLE, book.getTitle());
            values.put(BookColumns.AUTHOR, book.getAuthor());
            values.put(BookColumns.PUBLISHER, book.getPublisher());
            values.put(BookColumns.ISBN, book.getIsbn());
            values.put(BookColumns.LANGUAGE, book.getLanguage());
            values.put(BookColumns.COVER, book.getCover());
            values.put(BookColumns.PATH, book.getPath());
            long id = db.insert(BookConstract.TABLE_BOOK, null, values);

            List<ChapterItem> list = book.getToc();
            for (ChapterItem chapter : list) {
                values = new ContentValues();
                values.put(TocColumns.BID, id);
                values.put(TocColumns.ORDER, chapter.getOrder());
                values.put(TocColumns.TITLE, chapter.getLabel());
                values.put(TocColumns.URL, chapter.getContent());
                db.insert(BookConstract.TABLE_TOC, null, values);
            }
        } finally {
            db.close();
        }
    }
}
