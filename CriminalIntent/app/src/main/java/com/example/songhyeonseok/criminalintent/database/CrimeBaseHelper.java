package com.example.songhyeonseok.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CrimeBaseHelper extends SQLiteOpenHelper {

    public enum SchemaVersion {
        SCHEMA_VERSION_1
        , SCHEMA_VERSION_dev_2
        , SCHEMA_VERSION_ADD_SUSPECT
        , SCHEMA_VERSION_CURRENT
        ;
        public int version(){
            return this.ordinal();
        }
    };

    private static final int VERSION = SchemaVersion.SCHEMA_VERSION_CURRENT.version();
    public static final String DATABASE_NAME = "crimeBase0.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeDbSchema.CrimeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeDbSchema.CrimeTable.Cols.UUID + " not null, " +
                CrimeDbSchema.CrimeTable.Cols.TITLE + " , " +
                CrimeDbSchema.CrimeTable.Cols.DATE + " , " +
                CrimeDbSchema.CrimeTable.Cols.SOLVED + ", " +
                CrimeDbSchema.CrimeTable.Cols.SUSPECT +
                ") "
        );
        db.execSQL("create index " + "idx_uuid" + " on "+
                CrimeDbSchema.CrimeTable.NAME + "("+
                CrimeDbSchema.CrimeTable.Cols.UUID+")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < SchemaVersion.SCHEMA_VERSION_dev_2.version()){
            db.execSQL("DROP TABLE IF EXISTS "+ CrimeDbSchema.CrimeTable.NAME);
            onCreate(db);
        }else {
            if(oldVersion <= SchemaVersion.SCHEMA_VERSION_ADD_SUSPECT.version()){
                db.execSQL("ALTER TABLE "
                        + CrimeDbSchema.CrimeTable.NAME + " ADD COLUMN "+
                        CrimeDbSchema.CrimeTable.Cols.SUSPECT+" ; ");
            }
        }
    }
}