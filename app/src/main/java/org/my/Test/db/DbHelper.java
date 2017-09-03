package org.my.Test.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangj on 2016/8/27.
 */
public class DbHelper extends SQLiteOpenHelper {
    //private static final String DATABASE_NAME = "/storage/sdcard0/stock/stock.db";
    private static final String DATABASE_NAME = "/sdcard/stock/stock.db";
    private static int DATABASE_VERSION = 5;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists stock");
        db.execSQL("create table stock(code varchar,name varchar,trade varchar,region varchar,market varchar,opendate text)");
        db.execSQL("drop table if exists stockdaily");
        db.execSQL("create table stockdaily(code varchar,name varchar,date text,tclose decimal(10,2),high decimal(10,2),low decimal(10,2),topen decimal(10,2),lclose decimal(10,2),chg decimal(10,4),pchg decimal(10,4),turnover decimal(18,4),voturnover decimal(18,4),vaturnover decimal(18,4),tcap decimal(18,4),mcap decimal(18,4),ema12 decimal(18,6),ema26 decimal(18,6),dif decimal(10,2),dea decimal(10,2),bar decimal(10,2))");
        db.execSQL("drop table if exists history_transaction");
        db.execSQL("create table history_transaction(cjrq text,zqdm varchar,zqmc varchar,mmbz varchar,cjsl integer,cjjg decimal(10,2),cjje decimal(18,4),yhs decimal(10,2),sxf decimal(10,2),dsgf decimal(10,2),ghf decimal(10,2),jsf decimal(10,2),jyf decimal(10,2),jshl decimal(10,2),wtbh varchar,gddm varchar,jysmc varchar,beizhu varchar)");
        db.execSQL("drop table if exists capital_flow");
        db.execSQL("create table capital_flow(jsrq text,zqdm varchar,zqmc varchar,cjsl integer,cjjg decimal(10,2),zy varchar,fsje decimal(18,4),syje decimal(18,4),bizhong varchar,cjbh varchar,gddm varchar,cjrq text,beizhu varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //ema12 decimal(18,6),ema26 decimal(18,6),dif decimal(10,2),dea decimal(10,2),bar decimal(10,2)
        if(newVersion==5){
            db.execSQL("alter table stockdaily add ema12 decimal(18,6)");
            db.execSQL("alter table stockdaily add ema26 decimal(18,6)");
            db.execSQL("alter table stockdaily add dif decimal(10,2)");
            db.execSQL("alter table stockdaily add dea decimal(10,2)");
            db.execSQL("alter table stockdaily add bar decimal(10,2)");
        }
    }
}
