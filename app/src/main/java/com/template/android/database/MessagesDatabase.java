package com.template.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

import pro.topdigital.animeo.network.objects.MessageObject;
import pro.topdigital.animeo.tools.DateUtils;

public final class MessagesDatabase extends BaseDatabase<MessageObject> implements Database<MessageObject> {

    public static final String TABLE_NAME = "messages";

    public static final String COL_MESSAGE_ID = "message_id";
    public static final String COL_ID_TO = "user_id_to";
    public static final String COL_ID_FROM = "user_id_from";
    public static final String COL_MESSAGE = "message";
    public static final String COL_CREATED = "created";
    public static final String COL_IS_READ = "is_read";
    public static final String COL_STATUS = "status";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY, " +
                    COL_MESSAGE_ID + " INTEGER, " +
                    COL_ID_TO + " INTEGER, " +
                    COL_ID_FROM + " INTEGER, " +
                    COL_MESSAGE + " TEXT, " +
                    COL_CREATED + " INTEGER, " +
                    COL_IS_READ + " INTEGER, " +
                    COL_STATUS + " INTEGER " +
                    ");";

    public static final int STATUS_SEND = 1;
    public static final int STATUS_DELIVERED = 2;
    public static final int STATUS_ERROR = 3;

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
        object.setIdTo(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID_TO)));
        object.setIdFrom(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID_FROM)));
        object.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(COL_MESSAGE)));
        object.setCreatedUnix(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED)));
        object.setRead(intToBoolean(cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_READ))));
        object.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COL_STATUS)));

        return object;
    }

    @Override
    ContentValues toContentValues(MessageObject element) {
        ContentValues values = new ContentValues();
        values.put(COL_MESSAGE_ID, element.getId());
        values.put(COL_ID_TO, element.getIdTo());
        values.put(COL_ID_FROM, element.getIdFrom());
        values.put(COL_MESSAGE, element.getMessage());
        values.put(COL_CREATED, DateUtils.getInstance().toUnixTime(element.getCreated()));
        values.put(COL_IS_READ, booleanToInt(element.isRead()));
        values.put(COL_STATUS, element.getStatus());

        return values;
    }
}