package com.template.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.template.android.database.models.MessageObject;

public class DatabaseFactory {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "local.db";

    private final MessagesDatabase mMessagesDatabase;

    public DatabaseFactory(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

        mMessagesDatabase = new MessagesDatabase(context, databaseHelper);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(MessagesDatabase.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Added migration in future
        }
    }

    public Database<MessageObject> getMessages() {
        return mMessagesDatabase;
    }
}