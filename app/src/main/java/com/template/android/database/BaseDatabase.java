package com.template.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDatabase<T> {

    protected static final String COL_ID = "_id";
    protected static final String COL_UPDATED = "updated";

    protected final Context mContext;
    protected final BriteDatabase mDatabase;
    private final String mTableName;
    private final String mOwnId;

    public BaseDatabase(Context context, String tableName, String ownId, SQLiteOpenHelper database) {
        mContext = context;
        mTableName = tableName;
        mOwnId = ownId;
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabase = sqlBrite.wrapDatabaseHelper(database);
    }

    abstract T toObject(Cursor cursor);

    abstract ContentValues toContentValues(T element);

    public T getElement(long id) {
        T element = null;
        Cursor cursor = null;

        try {
            cursor = mDatabase.query("SELECT * FROM " + mTableName + " WHERE " + mOwnId + " = ?", toString(id));

            if (cursor.moveToFirst()) {
                element = toObject(cursor);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return element;
    }

    public List<T> getElements() {
        return getElements(null);
    }

    public List<T> getElements(String parameters) {
        String query = "SELECT * FROM " + mTableName;
        if (parameters != null) {
            query += " " + parameters;
        }

        return queryElements(query);
    }

    public long deleteElement(long id) {
        return mDatabase.delete(mTableName, mOwnId + " = ?", toString(id));
    }

    public void deleteElements() {
        mDatabase.delete(mTableName, null);
    }

    public void deleteElements(String where) {
        mDatabase.delete(mTableName, where);
    }

    public List<T> queryElements(String query) {
        List<T> elements = null;
        Cursor cursor = null;

        try {
            cursor = mDatabase.query(query);
            elements = new ArrayList<>(cursor.getCount());

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                elements.add(toObject(cursor));
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return elements;
    }

    public Cursor queryCursor(String where) {
        return mDatabase.query(where);
    }

    public boolean hasElement(long id) {
        String query = "SELECT " + COL_ID + " FROM " + mTableName + " WHERE " + mOwnId + " = " + id;
        Cursor cursor = mDatabase.query(query);

        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();

            cursor.close();
        }

        return count > 0;
    }

    public int getCount() {
        Cursor cursor = mDatabase.query("SELECT " + COL_ID + " FROM " + mTableName);
        return cursor.getCount();
    }

    protected String toString(long value) {
        return Long.toString(value);
    }

    protected boolean intToBoolean(int value) {
        return value > 0;
    }

    protected int booleanToInt(boolean value) {
        return value ? 1 : 0;
    }
}