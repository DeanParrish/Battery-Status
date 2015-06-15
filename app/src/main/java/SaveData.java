import android.provider.BaseColumns;

/**
 * Created by Parrish on 6/15/2015.
 */
public class SaveData {


    public void SaveData(){

    }

    public static abstract class FeedEntry implements BaseColumns{
        public static final String TABLE_NAME = "Batteries";

        public static final String COLUMN_ID_BATTERY_NAME = "Battery_Name";
        public static final String COLUMN_TITLE_BATTERY_NAME = "Battery Name";
        public static final String COLUMN_SUBTITLE_BATTERY_NAME = "Battery Name";

        public static final String COLUMN_ID_BATTERY_CELL = "Battery_Cell";

    }
}
