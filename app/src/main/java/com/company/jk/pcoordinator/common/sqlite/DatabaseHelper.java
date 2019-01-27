package com.company.jk.pcoordinator.common.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "GiantBaby";

    private static final String TABLE_COMMENT = "Comment";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "comment";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_COMMENT + "(" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_NAME + " TEXT NOT NULL" +
                        ");";

        db.execSQL(CREATE_TABLE);

        add(db,"잘먹었어요.");
        add(db,"소고기이유식");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_COMMENT;
        db.execSQL(DROP_TABLE);

        onCreate(db);
    }

    public void add(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, comment.getComment());

        db.insert(TABLE_COMMENT, null, values);
        db.close();
    }

    public void add(String comment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, comment);

        db.insert(TABLE_COMMENT, null, values);
        db.close();
    }

    public void add(SQLiteDatabase database,String comment) {  //최초 기본 코멘트 넣을 때만 사용

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, comment);

        database.insert(TABLE_COMMENT, null, values);
//        database.close();
    }

    public void delete(String comment){
        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, id);
        db.delete(TABLE_COMMENT, KEY_NAME + " = '" + comment + "'", null);
    }
    public List<Comment> getAll() {
        List<Comment> drinkList = new ArrayList<Comment>();

        String SELECT_ALL = "SELECT * FROM " + TABLE_COMMENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if (cursor.moveToFirst()) {
            do {
                Comment comment = new Comment();
                comment.setId(Integer.parseInt(cursor.getString(0)));
                comment.setComment(cursor.getString(1));
                drinkList.add(comment);
            } while (cursor.moveToNext());
        }

        return drinkList;
    }
}


