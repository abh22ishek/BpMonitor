package database;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;
import com.model.BPMeasurementModel;

import java.util.ArrayList;
import java.util.List;

import static database.BPDatabaseHelper.COMMENT;
import static database.BPDatabaseHelper.DIA;
import static database.BPDatabaseHelper.PUL;
import static database.BPDatabaseHelper.SYS;
import static database.BPDatabaseHelper.TABLE_NAME;
import static database.BPDatabaseHelper.TESTING_TIME;
import static database.BPDatabaseHelper.TYPE_BP;
import static database.BPDatabaseHelper.USER_NAME;

public class BPDBManager {

    private final String TAG="BPDBManager.class";


    private int mOpenCounter=0;
    private static BPDBManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;



    private BPDBManager()
    {

    }

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new BPDBManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized BPDBManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(BPDBManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter++;
        if(mOpenCounter == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if(mOpenCounter == 0) {
            // Closing database
            mDatabase.close();

        }
    }






    // retreive list of BP Records submitted by userId
    public List<BPMeasurementModel> getBPRecords(String username)
    {

        List<BPMeasurementModel> recordsDetailList=new ArrayList<>();


        Cursor cursor=mDatabase.query(TABLE_NAME, null,
                USER_NAME+"=?",new String[]{username},null,null, TESTING_TIME+" "+"DESC"+" "+"LIMIT 100");


        Logger.log(Level.INFO, TAG,TABLE_NAME+ " records count=" + cursor.getCount());


        while(cursor.moveToNext())
        {
            BPMeasurementModel m=new BPMeasurementModel();
            m.setSysPressure(cursor.getFloat(cursor.getColumnIndex(SYS)));
            m.setDiabolicPressure(cursor.getFloat(cursor.getColumnIndex(DIA)));
            m.setPulsePerMin(cursor.getFloat(cursor.getColumnIndex(PUL)));
            m.setMeasurementTime(cursor.getString(cursor.getColumnIndex(TESTING_TIME)));
            m.setTypeBP(cursor.getString(cursor.getColumnIndex(TYPE_BP)));
            m.setComments(cursor.getString(cursor.getColumnIndex(COMMENT)));
            recordsDetailList.add(m);

        }

        Logger.log(Level.INFO, TAG, "Records detail length=" + recordsDetailList.size());
        cursor.close();
        return recordsDetailList;
    }







    public void deleteBPRecords(String username,String testing_time)
    {
        mDatabase.delete(TABLE_NAME, USER_NAME + "=?" + " " + "AND"
                + " " + TESTING_TIME + "=?", new String[]{username, testing_time});
    }





    public List<String> getListOfDate(){
        List<String> dateTimeList=new ArrayList<>();




        return dateTimeList;

    }




}
