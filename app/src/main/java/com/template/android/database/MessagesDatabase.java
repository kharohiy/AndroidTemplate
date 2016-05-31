package com.template.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.template.android.database.models.MessageObject;

import java.util.ArrayList;
import java.util.List;

public final class MessagesDatabase extends BaseDatabase<MessageObject> implements Database<MessageObject> {

    public static final String TABLE_NAME = "messages";

    public static final String COL_MESSAGE_ID = "message_id";
    public static final String COL_MESSAGE = "message";
    public static final String COL_CREATED = "created";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY, " +
                    COL_MESSAGE_ID + " INTEGER, " +
                    COL_MESSAGE + " TEXT, " +
                    COL_CREATED + " INTEGER" +
                    ");";

    public MessagesDatabase(Context context, SQLiteOpenHelper database) {
        super(context, TABLE_NAME, COL_MESSAGE_ID, database);
    }

    @Override
    public long addElement(MessageObject element) {
        if (hasElement(element.getId())) {
            return mDatabase.update(TABLE_NAME, toContentValues(element), COL_MESSAGE_ID + " = " + element.getId());
        } else {
            return mDatabase.insert(TABLE_NAME, toContentValues(element));
        }
    }

    @Override
    public List<Long> addElements(List<MessageObject> elements) {
        List<Long> ids = new ArrayList<>(elements.size());

        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            for (MessageObject element : elements) {
                ids.add(addElement(element));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }

        return ids;
    }

    @Override
    public void updateElement(MessageObject element) {
        mDatabase.update(TABLE_NAME, toContentValues(element), COL_MESSAGE_ID + " = ?", toString(element.getId()));
    }

    @Override
    public void updateElements(List<MessageObject> elements) {
        BriteDatabase.Transaction transaction = mDatabase.newTransaction();
        try {
            for (MessageObject element : elements) {
                updateElement(element);
            }

            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public MessageObject toObject(Cursor cursor) {
        MessageObject object = new MessageObject();
        object.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_MESSAGE_ID)));
        object.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(COL_MESSAGE)));
        object.setCreatedUnix(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED)));

        return object;
    }

    @Override
    ContentValues toContentValues(MessageObject element) {
        ContentValues values = new ContentValues();
        values.put(COL_MESSAGE_ID, element.getId());
        values.put(COL_MESSAGE, element.getMessage());
        values.put(COL_CREATED, System.currentTimeMillis());

        return values;
    }
}