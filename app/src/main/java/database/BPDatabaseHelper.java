package database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;

public class BPDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    private static final String TAG="BPDatabaseHelper";

    // Database Name
    public static final String DATABASE_NAME = "UserDatabase.db";

    // Table Names
    public static final String TABLE_NAME = "BpUsers";



    // Common column names
    public static final String KEY_ID = "id";
    public static final String USER_NAME ="user_name";

    public static final String SYS="systolic_pressure";
    public static final String DIA="diabolic_pressure";
    public static final String PUL="pulse_per_min";
    public static final String TESTING_TIME="testing_time";
    public static final String COMMENT="comment";
    public static final String TYPE_BP="type_bp";



    public static final String CREATE_TABLE_BP="CREATE TABLE " +TABLE_NAME+"("+KEY_ID+" " +
            "integer primary key autoincrement"+","
            +USER_NAME+" "+"REAL"+","+SYS+" "+"REAL"+","
            +DIA+" "+"REAL"+","+PUL+" "+"REAL"+","+TESTING_TIME+" "+"TEXT"+","+TYPE_BP+" "+"TEXT"+","+COMMENT+" "+"TEXT"+ ")";







    public BPDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





    private BPDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    // It is called only once when database is created for first time
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_BP);
        Logger.log(Level.INFO,TAG,CREATE_TABLE_BP);


    }

    // called when database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        Logger.log(Level.DEBUG,TAG,"Downgrade the SQlite() version");
    }


    @Override
    public synchronized void close() {
        super.close();
    }
}

