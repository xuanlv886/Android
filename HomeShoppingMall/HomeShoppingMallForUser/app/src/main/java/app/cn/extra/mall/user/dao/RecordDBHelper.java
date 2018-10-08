package app.cn.extra.mall.user.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class RecordDBHelper extends SQLiteOpenHelper {
    public static final String BLACK_NUMBER = "record";
    public static final String MARKET_SEARCH = "search";

    public RecordDBHelper(Context context) {
        super(context, "record_number.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table record(_id integer primary key autoincrement,number varchar,loge integer)");
        db.execSQL("create table search(_id integer primary key autoincrement,number varchar)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
