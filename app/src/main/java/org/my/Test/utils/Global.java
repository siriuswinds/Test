package org.my.Test.utils;

import android.content.Context;

import org.my.Test.db.DbManager;

public class Global {
    public static final int STOCK_DOWNLOAD_SUCCESS = 0;
    public static final int STOCK_DOWNLOAD_FAILED = -1;

    private static DbManager dbManager;
    public static DbManager getDbManager(Context context){
        if(dbManager == null){
            dbManager = new DbManager(context);
        }
        return dbManager;
    }
}
