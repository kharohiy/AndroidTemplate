package com.template.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pro.topdigital.animeo.network.objects.MessageObject;
import pro.topdigital.animeo.network.objects.MissionObject;
import pro.topdigital.animeo.network.objects.MissionTypesObject;
import pro.topdigital.animeo.network.objects.ZoneObject;

public class DatabaseFactory {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "animeo.db";

    private final MissionsDatabase mMissions;
    private final ZonesDatabase mZones;
    private final MissionTypesDatabase mMissionTypes;
    private final MessagesDatabase mMessagesDatabase;

    public DatabaseFactory(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

        mMissions = new MissionsDatabase(context, databaseHelper);
        mZones = new ZonesDatabase(context, databaseHelper);
        mMissionTypes = new MissionTypesDatabase(context, databaseHelper);
        mMessagesDatabase = new MessagesDatabase(context, databaseHelper);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(MissionsDatabase.CREATE_TABLE);
            db.execSQL(ZonesDatabase.CREATE_TABLE);
            db.execSQL(MissionTypesDatabase.CREATE_TABLE);
            db.execSQL(MessagesDatabase.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Added migration in future
        }
    }

    public Database<MissionObject> getMissions() {
        return mMissions;
    }

    public Database<ZoneObject> getZones() {
        return mZones;
    }

    public Database<MissionTypesObject> getMissionTypes() {
        return mMissionTypes;
    }

    public Database<MessageObject> getMessages() {
        return mMessagesDatabase;
    }
}