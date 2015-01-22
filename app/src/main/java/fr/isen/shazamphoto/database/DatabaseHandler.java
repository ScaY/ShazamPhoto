package fr.isen.shazamphoto.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    //		****************table LOCALISATION*******************
    public static final String LOCALIZATION_KEY = "id";
    public static final String LOCALIZATION_LATITUDE = "latitude";
    public static final String LOCALIZATION_LONGITUDE = "longitude";
    public static final String LOCALIZATION_ALL_COLUMNS[] = {LOCALIZATION_KEY, LOCALIZATION_LATITUDE, LOCALIZATION_LONGITUDE};
    public static final String LOCALIZATION_TABLE_NAME = "localisation";
    public static final String LOCALIZATION_TABLE_CREATE =
            "CREATE TABLE " + LOCALIZATION_TABLE_NAME + " (" +
                    LOCALIZATION_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOCALIZATION_LATITUDE + " INTEGER, " +
                    LOCALIZATION_LONGITUDE + " INTEGER);";
    public static final String LOCALIZATION_TABLE_DROP = "DROP TABLE IF EXISTS " + LOCALIZATION_TABLE_NAME + ";";

    //				****************table MONUMENTS*******************
    //listes des colomnes de la table monument
    public static final String MONUMENTS_KEY = "id";
    public static final String MONUMENTS_NAME = "name";
    public static final String MONUMENTS_PHOTO_PATH = "photoPath";
    public static final String MONUMENTS_DESCRIPTION = "description";
    public static final String MONUMENTS_YEAR = "year";
    public static final String MONUMENTS_NB_VISITORS = "nbVisitors";
    public static final String MONUMENTS_NB_VISITED = "nbVisited";
    public static final String MONUMENTS_LOCALISATION_KEY = "localisationKey";

    //tableau content toutes les colomnes de la table
    public static final String MONUMENTS_ALL_COLUMNS[] = {MONUMENTS_KEY, MONUMENTS_NAME, MONUMENTS_PHOTO_PATH, MONUMENTS_DESCRIPTION, MONUMENTS_YEAR, MONUMENTS_NB_VISITORS, MONUMENTS_NB_VISITED, MONUMENTS_LOCALISATION_KEY};
    public static final String MONUMENTS_TABLE_NAME = "monuments";
    //script de création de la table
    public static final String MONUMENTS_TABLE_CREATE =
            "CREATE TABLE " + MONUMENTS_TABLE_NAME + " (" +
                    MONUMENTS_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MONUMENTS_NAME + " TEXT NOT NULL, " +
                    MONUMENTS_PHOTO_PATH + " TEXT, " +
                    MONUMENTS_DESCRIPTION + " TEXT, " +
                    MONUMENTS_YEAR + " TEXT +" +
                    MONUMENTS_NB_VISITORS + " INTEGER, " +
                    MONUMENTS_NB_VISITED + " INTEGER, " +
                    MONUMENTS_LOCALISATION_KEY + " INTEGER, " +
                    "FOREIGN KEY(" + MONUMENTS_LOCALISATION_KEY + ") REFERENCES " + LOCALIZATION_TABLE_NAME + "(" + LOCALIZATION_KEY + "));";

    //script de suppression
    public static final String MONUMENTS_TABLE_DROP = "DROP TABLE IF EXISTS " + MONUMENTS_TABLE_NAME + ";";

    //		****************table FAVOURITE_MONUMENTS********************
    public static final String FAVOURITE_MONUMENTS_KEY = "id";
    public static final String FAVOURITE_MONUMENTS_MONUMENT_KEY = "monumentId";
    public static final String FAVOURITE_MONUMENTS_ALL_COLUMNS[] = {FAVOURITE_MONUMENTS_KEY, FAVOURITE_MONUMENTS_MONUMENT_KEY};
    public static final String FAVOURITE_MONUMENTS_TABLE_NAME = "favouriteMonuments";
    public static final String FAVOURITE_MONUMENTS_TABLE_CREATE =
            "CREATE TABLE " + FAVOURITE_MONUMENTS_TABLE_NAME + " (" +
                    FAVOURITE_MONUMENTS_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FAVOURITE_MONUMENTS_MONUMENT_KEY + " INTEGER, " +
                    "FOREIGN KEY(" + FAVOURITE_MONUMENTS_MONUMENT_KEY + ") REFERENCES " + MONUMENTS_TABLE_NAME + "(" + MONUMENTS_KEY + "));";
    public static final String FAVOURITE_MONUMENTS_TABLE_DROP = "DROP TABLE IF EXISTS " + FAVOURITE_MONUMENTS_TABLE_NAME + ";";

    //		****************table VISITED_MONUMENTS*******************
    public static final String VISITED_MONUMENTS_KEY = "id";
    public static final String VISITED_MONUMENTS_MONUMENT_KEY = "monumentId";
    public static final String VISITED_MONUMENTS_VISIT_DATE = "visitDate";
    public static final String VISITED_MONUMENTS_ALL_COLUMNS[] = {VISITED_MONUMENTS_KEY, VISITED_MONUMENTS_MONUMENT_KEY, VISITED_MONUMENTS_VISIT_DATE};
    public static final String VISITED_MONUMENTS_TABLE_NAME = "visitedMonuments";
    public static final String VISITED_MONUMENTS_TABLE_CREATE =
            "CREATE TABLE " + VISITED_MONUMENTS_TABLE_NAME + " (" +
                    VISITED_MONUMENTS_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    VISITED_MONUMENTS_MONUMENT_KEY + " INTEGER, " +
                    VISITED_MONUMENTS_VISIT_DATE + " DATETIME, " +
                    "FOREIGN KEY(" + VISITED_MONUMENTS_MONUMENT_KEY + ") REFERENCES " + MONUMENTS_TABLE_NAME + "(" + MONUMENTS_KEY + "));";
    public static final String VISITED_MONUMENTS_TABLE_DROP = "DROP TABLE IF EXISTS " + VISITED_MONUMENTS_TABLE_NAME + ";";

    //		****************table TO_IDENTIFY_MONUMENTS*******************
    public static final String TO_IDENTIFY_MONUMENTS_KEY = "id";
    public static final String TO_IDENTIFY_MONUMENTS_PHOTO_PATH = "photoPath";
    public static final String TO_IDENTIFY_MONUMENTS_LOCALISATION_KEY = "localisationId";
    public static final String TO_IDENTIFY_MONUMENTS_ALL_COLUMNS[] = {TO_IDENTIFY_MONUMENTS_KEY, TO_IDENTIFY_MONUMENTS_PHOTO_PATH, TO_IDENTIFY_MONUMENTS_LOCALISATION_KEY};
    public static final String TO_IDENTIFY_MONUMENTS_TABLE_NAME = "toIdentifyMonuments";
    public static final String TO_IDENTIFY_MONUMENTS_TABLE_CREATE =
            "CREATE TABLE " + TO_IDENTIFY_MONUMENTS_TABLE_NAME + " (" +
                    TO_IDENTIFY_MONUMENTS_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TO_IDENTIFY_MONUMENTS_PHOTO_PATH + " TEXT, " +
                    TO_IDENTIFY_MONUMENTS_LOCALISATION_KEY + " INTEGER, " +
                    "FOREIGN KEY(" + TO_IDENTIFY_MONUMENTS_LOCALISATION_KEY + ") REFERENCES " + LOCALIZATION_TABLE_NAME + "(" + LOCALIZATION_KEY + "));";
    public static final String TO_IDENTIFY_MONUMENTS_TABLE_DROP = "DROP TABLE IF EXISTS " + TO_IDENTIFY_MONUMENTS_TABLE_NAME + ";";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //création de tables : exécution des différents scripts correspondant à chaque table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MONUMENTS_TABLE_CREATE);
        db.execSQL(FAVOURITE_MONUMENTS_TABLE_CREATE);
        db.execSQL(VISITED_MONUMENTS_TABLE_CREATE);
        db.execSQL(TO_IDENTIFY_MONUMENTS_TABLE_CREATE);
        db.execSQL(LOCALIZATION_TABLE_CREATE);
    }

    //mise à jour des tables : suppression des tables en executant les scripts correspondant
    //puis recréation avec la fonction ci-dessus
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MONUMENTS_TABLE_DROP);
        db.execSQL(FAVOURITE_MONUMENTS_TABLE_DROP);
        db.execSQL(VISITED_MONUMENTS_TABLE_DROP);
        db.execSQL(TO_IDENTIFY_MONUMENTS_TABLE_DROP);
        db.execSQL(LOCALIZATION_TABLE_DROP);
        onCreate(db);
    }
}