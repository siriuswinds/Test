package org.my.Test.utils;

import android.os.Handler;

import org.my.Test.db.DbManager;
import org.my.Test.reader.HttpReader;
import org.my.Test.stock.Capitalflow;
import org.my.Test.stock.Stock;
import org.my.Test.stock.StockDaily;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class StockUtil {
    public static Collection<? extends Capitalflow> computeProfits(Collection<? extends Capitalflow> capitalflows) {
        for (Capitalflow capitalflow:capitalflows) {

        }
        return capitalflows;
    }

    public static void updateStockDaily(Stock stock, DbManager dbManager, Handler handler) {
        Date start = getStockLastDay(stock,dbManager);
        Date end = new Date();

        HttpReader reader = new HttpReader();
        reader.setHandler(handler);
        reader.setDbManager(dbManager);
        try {
            reader.downloadStockDaily(stock,start,end);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Date getStockLastDay(Stock stock, DbManager dbManager) {
        String maxDay = dbManager.execQuery("select max(datetime(date)) from stockdaily where code = ?",new String[]{stock.getCode()});

        if(maxDay != null && !maxDay.isEmpty()){
            return DateUtil.parse(maxDay);
        }else
            return DateUtil.parse("1990-01-01");
    }

    public static void calcStockMACD(Stock stock, DbManager dbManager, Handler handler) {
        List<StockDaily> stockDailys =  dbManager.getStockDailys(stock.getCode());

        StockDaily daily1 = stockDailys.get(1);
        daily1.setEma12(daily1.getTclose());
        daily1.setEma26(daily1.getTclose());

        daily1.setDif(0);
        daily1.setDea(0);
        daily1.setBar(0);
        daily1.setMacd(0);

        float preEma12 = 0.0f;
        float preEma26 = 0.0f;
        float preDea = 0.0f;

        for(int i = 2;i< stockDailys.size();i++){
            StockDaily daily = stockDailys.get(i);

            if(daily.getTclose()>0){
                daily.setEma12(preEma12 + (daily.getTclose()-preEma12)*2/13.0f);
                daily.setEma26(preEma26 + (daily.getTclose()-preEma26)*2/27.0f);
                daily.setDif(daily.getEma12()-daily.getEma26());
                daily.setDea(preDea*8/10.0f + daily.getDif()*2/10.0f);
                daily.setBar(2.0f*(daily.getDif()-daily.getDea()));
                preEma12 = daily.getEma12();
                preEma26 = daily.getEma26();
                preDea = daily.getDea();
            }
            //daily.setMacd(daily.getBar());
        }

        dbManager.updateStockDailyMACD(stockDailys);
    }
}
