package app.cn.extra.mall.user.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.vo.SelfMarketSearch;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class SelfMarketSearchDAO {
    private RecordDBHelper dbHelper;

    public SelfMarketSearchDAO(Context context){
        dbHelper = new RecordDBHelper(context);
    }

    /**
     *
     * @return  返回数据表中所有的数据
     */
    public List<SelfMarketSearch> getAll(){
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        List<SelfMarketSearch> list = new ArrayList<>();

        Cursor cursor = database.query(RecordDBHelper.MARKET_SEARCH, null, null, null, null, null, null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String number = cursor.getString(cursor.getColumnIndex("number"));
            SelfMarketSearch blackNumber = new SelfMarketSearch(id,number);

            list.add(blackNumber);
        }
        cursor.close();
        database.close();

        return list;
    }
    /**
     * 添加一个元素到数据库中
     * @param record
     */
    public void add(SelfMarketSearch record){

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("number",record.getNumber());
        database.insert(RecordDBHelper.MARKET_SEARCH, null, values);
        database.close();
    }
    /**
     * 删除所以的数据
     *
     */

    public void deleteadd(){
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        database.execSQL("delete from search");
        database.close();

    }
    //判断搜索的名字是否相同
    public boolean hasisData(String record) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                " select * from search where number=?", new String[]{record});
        //判断是否有下一个
        return cursor.moveToNext();


    }
}
