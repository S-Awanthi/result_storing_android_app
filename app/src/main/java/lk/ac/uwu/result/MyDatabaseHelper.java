package lk.ac.uwu.result;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.muddzdev.styleabletoast.StyleableToast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ExamResult.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_result";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_SEMESTER = "semester";
    private static final String COLUMN_NAME = "course_name";
    private static final String COLUMN_CODE = "course_code";
    private static final String COLUMN_GRADE = "course_grade";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ***************** CREATE TABLE ********************************
        String tableResult = "create table "
                + TABLE_NAME + " ("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_YEAR + " integer not null, "
                + COLUMN_SEMESTER + " integer not null, "
                + COLUMN_NAME + " text not null, "
                + COLUMN_CODE + " text not null, "
                + COLUMN_GRADE + " text not null)";

        db.execSQL(tableResult);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // *************************  METHOD TO ADD A NEW RESULT  **************************************
    void addResult(int year, int semester, String name, String code, String grade){

        SQLiteDatabase db = this.getWritableDatabase();     //database object "db"
        ContentValues cv = new ContentValues();     //Store values inside this object

        cv.put(COLUMN_YEAR, year);
        cv.put(COLUMN_SEMESTER, semester);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_CODE, code);
        cv.put(COLUMN_GRADE, grade);

        long result = db.insert(TABLE_NAME, null, cv);   //Insert

        if (result == -1){
            StyleableToast.makeText(context, "Failed!", Toast.LENGTH_SHORT, R.style.myToastFail).show();
        }
        else {
            StyleableToast.makeText(context, "Successfully saved!", Toast.LENGTH_SHORT, R.style.myToastSuccess).show();
        }
    }


    // *************************  METHOD TO READ ALL DATA WHICH RETURNS A CURSOR OBJECT  **********************
    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();   //database object "db"

        Cursor cursor = null;   //cursor object
        if (db != null){
            cursor = db.rawQuery(query, null);   //Execute the row and store inside the cursor
        }
        return cursor;
    }


    // *************************  METHOD TO UPDATE RESULT  *********************************************************
    void updateData(String row_id, String year, String semester, String name, String code, String grade){
        SQLiteDatabase db = this.getWritableDatabase();   //database object "db"
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_YEAR, year);
        cv.put(COLUMN_SEMESTER, semester);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_CODE, code);
        cv.put(COLUMN_GRADE, grade);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

        if (result == -1){
            StyleableToast.makeText(context, "Failed!", Toast.LENGTH_SHORT, R.style.myToastFail).show();
        }
        else{
            StyleableToast.makeText(context, "Successfully updated!", Toast.LENGTH_SHORT, R.style.myToastSuccess).show();
        }
    }


    // *************************  METHOD TO DELETE RESULT  *************************************************
    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});

        if (result == -1){
            StyleableToast.makeText(context, "Failed!", Toast.LENGTH_SHORT, R.style.myToastFail).show();
        }
        else{
            StyleableToast.makeText(context, "Successfully deleted!", Toast.LENGTH_SHORT, R.style.myToastSuccess).show();
        }
    }


    // *************************  METHOD TO DELETE ALL RESULTS  ****************************************
    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
