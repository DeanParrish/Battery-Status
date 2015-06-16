import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Parrish on 6/16/2015.
 */
public static abstract class FeedEntry implements BaseColumns {
    //Table Name
    public static final String TABLE_NAME = "Batteries";
    //Battery Name
    public static final String COLUMN_ID_BATTERY_NAME = "Battery_Name";
    public static final String COLUMN_TITLE_BATTERY_NAME = "Battery Name";
    public static final String COLUMN_SUBTITLE_BATTERY_NAME = "Battery Name";
    //Cells of Battery
    public static final String COLUMN_ID_BATTERY_CELL = "Battery_Cell";
    public static final String COLUMN_TITLE_CELL ="Battery Cell";
    public static final String COLUMN_SUBTITLE_CELL = "Battery Cell";
    //Milliamp Hours of Battery
    public static final String COLUMN_ID_MAH = "MAH";
    public static final String COLUMN_TITLE_MAH = "Milliamp Hours";
    public static final String COLUMN_SUBTITLE_MAH = "Milliamp Hours";
    //Cycles Battery is rated for
    public static final String COLUMN_ID_CYCLES = "Cycles";
    public static final String COLUMN_TITLE_CYCLES = "Cycles";
    public static final String COLUMN_SUBTITLE_CYCLES = "Cycles";
    //Battery Composition
    public static final String COLUMN_ID_BATTERY_TYPE = "Battery_Type";
    public static final String COLUMN_TITLE_BATTERY_TYPE = "Battery Type";
    public static final String COLUMN_SUBTITLE_BATTERY_TYPE = "Battery Type";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    //Query
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE" + TABLE_NAME + " (" + _ID + "INTEGER PRIMARY KEY," +

                    COLUMN_ID_BATTERY_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_TITLE_BATTERY_NAME+ TEXT_TYPE + COMMA_SEP +
                    COLUMN_SUBTITLE_BATTERY_NAME + TEXT_TYPE + COMMA_SEP +

                    COLUMN_ID_BATTERY_CELL + TEXT_TYPE + COMMA_SEP +
                    COLUMN_TITLE_CELL + TEXT_TYPE + COMMA_SEP +
                    COLUMN_SUBTITLE_CELL + TEXT_TYPE + COMMA_SEP +

                    COLUMN_ID_MAH + TEXT_TYPE + COMMA_SEP +
                    COLUMN_TITLE_MAH + TEXT_TYPE + COMMA_SEP +
                    COLUMN_SUBTITLE_MAH + TEXT_TYPE + COMMA_SEP +

                    COLUMN_ID_CYCLES + TEXT_TYPE + COMMA_SEP +
                    COLUMN_TITLE_CYCLES + TEXT_TYPE + COMMA_SEP +
                    COLUMN_SUBTITLE_CYCLES + TEXT_TYPE + COMMA_SEP +

                    COLUMN_ID_BATTERY_TYPE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_TITLE_BATTERY_TYPE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_SUBTITLE_BATTERY_TYPE + TEXT_TYPE + " )";

}


public class   extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Battery_Status.db";

    public FeedReaderDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(FeedEntry.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
