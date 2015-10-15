package chicxulub.nagaram;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trollham on 10/13/15.
 */
public class SavedDataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nagaram.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "scores";
    public static final String _ID = BaseColumns._ID;
    public static final String NAME = "name";
    public static final String SCORE = "score";
    private static SavedDataHelper instance = null;

    public static SavedDataHelper getInstance(Context context){
        if (instance == null) instance = new SavedDataHelper(context);
        return instance;
    }

    private SavedDataHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT NOT NULL, " + SCORE + " INTEGER" + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(String name, int score){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(SCORE, score);
        db.insertOrThrow(TABLE_NAME, null, values);
        System.out.println("New entry: " + name + ", " + score);
    }
    public List<String> all (Activity activity){
        List<String> list = new ArrayList<String>();
        String[] from = {_ID, NAME, SCORE};
        String order = SCORE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, from, null, null, null, null, order);
        if (cursor.moveToFirst()){
            do{
                // changed list add
                 list.add(cursor.getString(1) + " " + String.valueOf(cursor.getInt(2)));
                //list.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }
        return list;
    }
    public long count(){
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public void truncate(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME;
        db.execSQL(sql);
    }
}
