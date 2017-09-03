package org.my.Test.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.my.Test.stock.Capitalflow;
import org.my.Test.stock.Stock;
import org.my.Test.stock.StockDaily;
import org.my.Test.stock.Transaction;
import org.my.Test.utils.DateUtil;
import org.my.Test.utils.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DbManager {
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    public DbManager(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public Stock getStock(String code) {
        Stock stock = null;
        Cursor c = db.rawQuery("select * from stock where code=?", new String[]{code});

        while (c.moveToNext()) {
            stock = new Stock();
            stock.setCode(c.getString(c.getColumnIndex("code")));
            stock.setName(c.getString(c.getColumnIndex("name")));
            stock.setTrade(c.getString(c.getColumnIndex("trade")));
            stock.setRegion(c.getString(c.getColumnIndex("region")));
            stock.setMarket(c.getString(c.getColumnIndex("market")));
            stock.setOpendate(DateUtil.parse(c.getString(c.getColumnIndex("opendate"))));
        }
        c.close();

        return stock;
    }

    public List<Stock> getStocks(int limit, int offset,String condition) {
        List<Stock> stocks = new ArrayList<>();
        String queryString = String.format("select * from stock %s Limit ? Offset ?",condition);

        Cursor c = db.rawQuery(queryString, new String[]{String.valueOf(limit), String.valueOf(offset)});

        while (c.moveToNext()) {
            Stock stock = new Stock();
            stock.setCode(c.getString(c.getColumnIndex("code")));
            stock.setName(c.getString(c.getColumnIndex("name")));
            stock.setTrade(c.getString(c.getColumnIndex("trade")));
            stock.setRegion(c.getString(c.getColumnIndex("region")));
            stock.setMarket(c.getString(c.getColumnIndex("market")));
            stock.setOpendate(DateUtil.parse(c.getString(c.getColumnIndex("opendate"))));

            stocks.add(stock);
        }
        c.close();

        return stocks;
    }

    public boolean addStock(Stock stock) {
        db.beginTransaction();
        try {
            db.execSQL("insert into stock values(?,?,?,?,?,?)", new Object[]{
                    stock.getCode(),
                    stock.getName(),
                    stock.getTrade(),
                    stock.getRegion(),
                    stock.getMarket(),
                    stock.getOpendate()});
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(DbManager.class.toString(), ex.getMessage());
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public void deleteStock(String code) {
        db.delete("stock", "code=?", new String[]{code});
    }

    public void clearStocks() {
        db.beginTransaction();
        db.delete("stock", "", null);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public StockDaily getStockDaily(String code, String date) {
        StockDaily stockdaily = null;

        Cursor c = db.rawQuery("select * from stockdaily where code=? and date=?", new String[]{code, date});

        while (c.moveToNext()) {
            stockdaily = getStockDailyFromCursor(c);
        }
        c.close();
        return stockdaily;
    }

    public boolean addStockDaily(StockDaily stockdaily) {
        if(stockDailyExists(stockdaily)){
            return true;
        }
        db.execSQL("insert into stockdaily values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                stockdaily.getCode(),
                stockdaily.getName(),
                DateUtil.format(stockdaily.getDate()),
                stockdaily.getTclose(),
                stockdaily.getHigh(),
                stockdaily.getLow(),
                stockdaily.getTopen(),
                stockdaily.getLclose(),
                stockdaily.getChg(),
                stockdaily.getPchg(),
                stockdaily.getTurnover(),
                stockdaily.getVoturnover(),
                stockdaily.getVaturnover(),
                stockdaily.getTcap(),
                stockdaily.getMcap(),
                stockdaily.getEma12(),
                stockdaily.getEma26(),
                stockdaily.getDif(),
                stockdaily.getDea(),
                stockdaily.getBar()});

        return true;
    }

    private boolean stockDailyExists(StockDaily stockdaily) {
        Cursor c = db.rawQuery("select count(*) from stockdaily where code=? and date=date(?)",new String[]{stockdaily.getCode(), DateUtil.format(stockdaily.getDate())});
        c.moveToNext();
        int count = c.getInt(0);
        c.close();
        return (count==1);
    }

    public void deleteStockDaily(String code, String date) {
        db.beginTransaction();
        db.delete("stockdaily", "code=? and date=?", new String[]{code, date});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void clearStockDaily() {
        db.beginTransaction();
        db.delete("stockdaily", "", null);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void closeDb() {
        db.close();
    }

    public List<StockDaily> getStockDailys(String code,int limit, int offset) {
        List<StockDaily> stockdailys = new ArrayList<>();

        Cursor c = db.rawQuery("select * from stockdaily where code = ? order by date desc Limit ? Offset ?", new String[]{code,String.valueOf(limit), String.valueOf(offset)});
        //Cursor c = db.rawQuery("select * from stockdaily",null);

        while (c.moveToNext()) {
            StockDaily stockdaily = getStockDailyFromCursor(c);

            stockdailys.add(stockdaily);
        }
        c.close();

        return stockdailys;
    }

    public List<StockDaily> getStockDailys(String code) {
        List<StockDaily> stockdailys = new ArrayList<>();

        Cursor c = db.rawQuery("select * from stockdaily where code = ? order by date asc", new String[]{code});
        //Cursor c = db.rawQuery("select * from stockdaily",null);

        while (c.moveToNext()) {
            StockDaily stockdaily = getStockDailyFromCursor(c);

            stockdailys.add(stockdaily);
        }
        c.close();

        return stockdailys;
    }

    public Collection<? extends Stock> searchStocks(int limit,int offset, String condition) {
        List<Stock> stocks = new ArrayList<>();
        Cursor c = db.rawQuery("select * from stock where code like ? or name like ? Limit ? Offset ?", new String[]{"%".concat(condition.concat("%")),"%".concat(condition.concat("%")),String.valueOf(limit),String.valueOf(offset)});

        while (c.moveToNext()) {
            Stock stock = new Stock();
            stock.setCode(c.getString(c.getColumnIndex("code")));
            stock.setName(c.getString(c.getColumnIndex("name")));
            stock.setTrade(c.getString(c.getColumnIndex("trade")));
            stock.setRegion(c.getString(c.getColumnIndex("region")));
            stock.setMarket(c.getString(c.getColumnIndex("market")));
            stock.setOpendate(DateUtil.parse(c.getString(c.getColumnIndex("opendate"))));
            stocks.add(stock);
        }
        c.close();

        return stocks;
    }

    public void beginTransaction() {
        db.beginTransaction();
    }

    public void commitTransaction() {
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public List<String> getTrades() {
        List<String> tradeList = new ArrayList<>();

        Cursor c = db.rawQuery("select trade from stock group by trade",null);

        while(c.moveToNext()){
            tradeList.add(c.getString(0));
        }

        return tradeList;
    }

    public List<String> getRegions() {
        List<String> regionList = new ArrayList<>();
        Cursor c = db.rawQuery("select region from stock group by region",null);

        while(c.moveToNext()){
            regionList.add(c.getString(0));
        }
        return regionList;
    }

    public void clearTransactions() {
        db.delete("history_transaction", "", null);
    }

    public void clearCapitalflows() {
        db.delete("capital_flow", "", null);
    }

    public void addTransactions(List<Transaction> transactions) {
        db.beginTransaction();
        try {
            for (Transaction transaction :transactions) {
                //cjrq text,zqdm varchar,zqmc varchar,mmbz varchar,cjsl float,cjjg float,cjje float,yhs float,sxf float,dsgf float,ghf float,jsf float,jyf float,jshl float,wtbh varchar,gddm varchar,jysmc varchar,beizhu varchar)");
                db.execSQL("insert into history_transaction values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                        DateUtil.format(transaction.getCjrq()),
                        transaction.getZqdm(),
                        transaction.getZqmc(),
                        transaction.getMmbz(),
                        transaction.getCjsl(),
                        transaction.getCjjg(),
                        transaction.getCjje(),
                        transaction.getYhs(),
                        transaction.getSxf(),
                        transaction.getDsgf(),
                        transaction.getGhf(),
                        transaction.getJsf(),
                        transaction.getJyf(),
                        transaction.getJshl(),
                        transaction.getWtbh(),
                        transaction.getGddm(),
                        transaction.getJysmc(),
                        transaction.getBeizhu()
                });
            }
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(DbManager.class.toString(), ex.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void addCapitalflows(List<Capitalflow> capitalflows) {
        db.beginTransaction();
        try {
            for (Capitalflow capitalflow :capitalflows) {
                //jsrq text,zqdm varchar,zqmc varchar,cjsl float,cjjg float,zy varchar,fsje float,syje float,bizhong varchar,cjbh varchar,gddm varchar,cjrq text,beizhu varchar)");
                db.execSQL("insert into capital_flow values(?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                        DateUtil.format(capitalflow.getJsrq()),
                        capitalflow.getZqdm(),
                        capitalflow.getZqmc(),
                        capitalflow.getCjsl(),
                        capitalflow.getCjjg(),
                        capitalflow.getZy(),
                        capitalflow.getFsje(),
                        capitalflow.getSyje(),
                        capitalflow.getBizhong(),
                        capitalflow.getCjbh(),
                        capitalflow.getGddm(),
                        DateUtil.format(capitalflow.getCjrq()),
                        capitalflow.getBeizhu()
                });
            }
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(DbManager.class.toString(), ex.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public List<Capitalflow> getCapitalflows(int limit,int offset) {
        List<Capitalflow> capitalflows = new ArrayList<>();

        Cursor c = db.rawQuery("select * from capital_flow Limit ? Offset ?",new String[]{String.valueOf(limit),String.valueOf(offset)});
        try {
            while (c.moveToNext()) {
                Capitalflow capitalflow = getCapitalflowsFromCursor(c);
                capitalflows.add(capitalflow);
                //jsrq text,zqdm varchar,zqmc varchar,cjsl float,cjjg float,zy varchar,fsje float,syje float,bizhong varchar,cjbh varchar,gddm varchar,cjrq text,beizhu varchar
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            c.close();
        }

        return capitalflows;
    }

    public List<Transaction> getTransactions(int limit,int offset) {
        List<Transaction> transactions = new ArrayList<>();

        Cursor c = db.rawQuery("select * from history_transaction Limit ? Offset ?",new String[]{String.valueOf(limit),String.valueOf(offset)});

        try {
            while (c.moveToNext()) {
                Transaction transaction = getTransactionFromCursor(c);
                transactions.add(transaction);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            c.close();
        }
        return transactions;
    }

    public List<Capitalflow> getCapitalflows(Capitalflow stocktransaction,int limit,int offset) {
        List<Capitalflow> capitalflows = new ArrayList<>();
        String zqdm = stocktransaction.getZqdm();

        Cursor c = db.rawQuery("select a.*,b.tclose,b.topen,b.high,b.low from capital_flow a left join stockdaily b on b.code = a.zqdm and date(a.jsrq) = date(b.date) where a.zqdm = ? Limit ? Offset ?",new String[]{zqdm,String.valueOf(limit),String.valueOf(offset)});

        try {
            while (c.moveToNext()) {
                Capitalflow capitalflow = getCapitalflowsFromCursor(c);
                capitalflows.add(capitalflow);
                //jsrq text,zqdm varchar,zqmc varchar,cjsl float,cjjg float,zy varchar,fsje float,syje float,bizhong varchar,cjbh varchar,gddm varchar,cjrq text,beizhu varchar
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            c.close();
        }

        return capitalflows;
    }

    public void addStocks(List<Stock> stocks) {
        try {
            db.beginTransaction();
            for (Stock stock : stocks) {
                addStock(stock);
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    public void endTransaction() {
        db.endTransaction();
    }

    public List<Capitalflow> getStockTransactions(Capitalflow stocktransaction, int limit, int offset) {
        List<Capitalflow> capitalflows = new ArrayList<>();
        String zqdm = stocktransaction.getZqdm();

        Cursor c = db.rawQuery("select a.*,b.* from capital_flow a left join stockdaily b on b.code = a.zqdm and date(a.jsrq) = date(b.date) where a.zqdm = ? Limit ? Offset ?",new String[]{zqdm,String.valueOf(limit),String.valueOf(offset)});

        try {
            while (c.moveToNext()) {
                Capitalflow capitalflow = getCapitalflowsFromCursor(c);
                StockDaily stockdaily = getStockDailyFromCursor(c);

                capitalflow.setStockDaily(stockdaily);
                capitalflows.add(capitalflow);
                //jsrq text,zqdm varchar,zqmc varchar,cjsl float,cjjg float,zy varchar,fsje float,syje float,bizhong varchar,cjbh varchar,gddm varchar,cjrq text,beizhu varchar
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            c.close();
        }

        return capitalflows;
    }

    private Capitalflow getCapitalflowsFromCursor(Cursor c) {
        if(c == null || c.isClosed()) return null;

        Capitalflow capitalflow = new Capitalflow();
        capitalflow.setJsrq(DateUtil.parse(c.getString(c.getColumnIndex("jsrq"))));
        capitalflow.setZqdm(c.getString(c.getColumnIndex("zqdm")));
        capitalflow.setZqmc(c.getString(c.getColumnIndex("zqmc")));
        capitalflow.setCjsl(NumberUtil.parseFloat(c.getString(c.getColumnIndex("cjsl"))));
        capitalflow.setCjjg(NumberUtil.parseFloat(c.getString(c.getColumnIndex("cjjg"))));
        capitalflow.setZy(c.getString(c.getColumnIndex("zy")));
        capitalflow.setFsje(NumberUtil.parseFloat(c.getString(c.getColumnIndex("fsje"))));
        capitalflow.setSyje(NumberUtil.parseFloat(c.getString(c.getColumnIndex("syje"))));
        capitalflow.setBizhong(c.getString(c.getColumnIndex("bizhong")));
        capitalflow.setCjbh(c.getString(c.getColumnIndex("cjbh")));
        capitalflow.setGddm(c.getString(c.getColumnIndex("gddm")));
        capitalflow.setCjrq(DateUtil.parse(c.getString(c.getColumnIndex("cjrq"))));
        capitalflow.setBeizhu(c.getString(c.getColumnIndex("beizhu")));

        return capitalflow;
    }

    private StockDaily getStockDailyFromCursor(Cursor c) {
        if(c == null || c.isClosed()) return null;

        StockDaily stockdaily = new StockDaily();
        stockdaily.setCode(c.getString(c.getColumnIndex("code")));
        stockdaily.setName(c.getString(c.getColumnIndex("name")));
        String date = c.getString(c.getColumnIndex("date"));
        stockdaily.setDate(DateUtil.parse(date));
        stockdaily.setTclose(NumberUtil.parseFloat(c.getString(c.getColumnIndex("tclose"))));
        stockdaily.setHigh(NumberUtil.parseFloat(c.getString(c.getColumnIndex("high"))));
        stockdaily.setLow(NumberUtil.parseFloat(c.getString(c.getColumnIndex("low"))));
        stockdaily.setTopen(NumberUtil.parseFloat(c.getString(c.getColumnIndex("topen"))));
        stockdaily.setLclose(NumberUtil.parseFloat(c.getString(c.getColumnIndex("lclose"))));
        stockdaily.setChg(NumberUtil.parseFloat(c.getString(c.getColumnIndex("chg"))));
        stockdaily.setPchg(NumberUtil.parseFloat(c.getString(c.getColumnIndex("pchg"))));
        stockdaily.setTurnover(NumberUtil.parseFloat(c.getString(c.getColumnIndex("turnover"))));
        stockdaily.setVoturnover(NumberUtil.parseFloat(c.getString(c.getColumnIndex("voturnover"))));
        stockdaily.setVaturnover(NumberUtil.parseFloat(c.getString(c.getColumnIndex("vaturnover"))));
        stockdaily.setTcap(NumberUtil.parseFloat(c.getString(c.getColumnIndex("tcap"))));
        stockdaily.setMcap(NumberUtil.parseFloat(c.getString(c.getColumnIndex("mcap"))));
        stockdaily.setEma12(NumberUtil.parseFloat(c.getString(c.getColumnIndex("ema12"))));
        stockdaily.setEma26(NumberUtil.parseFloat(c.getString(c.getColumnIndex("ema26"))));
        stockdaily.setDif(NumberUtil.parseFloat(c.getString(c.getColumnIndex("dif"))));
        stockdaily.setDea(NumberUtil.parseFloat(c.getString(c.getColumnIndex("dea"))));
        stockdaily.setBar(NumberUtil.parseFloat(c.getString(c.getColumnIndex("bar"))));

        return stockdaily;
    }

    private Transaction getTransactionFromCursor(Cursor c) {
        if(c==null || c.isClosed()) return null;

        Transaction transaction = new Transaction();
        transaction.setCjrq(DateUtil.parse(c.getString(c.getColumnIndex("cjrq"))));
        transaction.setZqdm(c.getString(c.getColumnIndex("zqdm")));
        transaction.setZqmc(c.getString(c.getColumnIndex("zqmc")));
        transaction.setMmbz(c.getString(c.getColumnIndex("mmbz")));
        transaction.setCjsl(NumberUtil.parseFloat(c.getString(c.getColumnIndex("cjsl"))));
        transaction.setCjjg(NumberUtil.parseFloat(c.getString(c.getColumnIndex("cjjg"))));
        transaction.setCjje(NumberUtil.parseFloat(c.getString(c.getColumnIndex("cjje"))));
        transaction.setYhs(NumberUtil.parseFloat(c.getString(c.getColumnIndex("yhs"))));
        transaction.setSxf(NumberUtil.parseFloat(c.getString(c.getColumnIndex("sxf"))));
        transaction.setDsgf(NumberUtil.parseFloat(c.getString(c.getColumnIndex("dsgf"))));
        transaction.setGhf(NumberUtil.parseFloat(c.getString(c.getColumnIndex("ghf"))));
        transaction.setJsf(NumberUtil.parseFloat(c.getString(c.getColumnIndex("jsf"))));
        transaction.setJyf(NumberUtil.parseFloat(c.getString(c.getColumnIndex("jyf"))));
        transaction.setJshl(NumberUtil.parseFloat(c.getString(c.getColumnIndex("jshl"))));
        transaction.setWtbh(c.getString(c.getColumnIndex("wtbh")));
        transaction.setGddm(c.getString(c.getColumnIndex("gddm")));
        transaction.setJysmc(c.getString(c.getColumnIndex("jysmc")));
        transaction.setBeizhu(c.getString(c.getColumnIndex("beizhu")));

        return transaction;
    }

    public String execQuery(String sql,String[] args) {
        Cursor cursor = db.rawQuery(sql,args);
        String result = null;

        try{
            if(cursor == null || cursor.isClosed()) return result;

            while (cursor.moveToNext()){
                result = cursor.getString(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }

        return result;
    }


    /**
     * 保存MACD计算结果
     * @param dailys
     */
    public void updateStockDailyMACD(List<StockDaily> dailys) {
        db.beginTransaction();
        try {
            for (StockDaily daily :dailys) {
                //jsrq text,zqdm varchar,zqmc varchar,cjsl float,cjjg float,zy varchar,fsje float,syje float,bizhong varchar,cjbh varchar,gddm varchar,cjrq text,beizhu varchar)");
                db.execSQL("update stockdaily set ema12=?,ema26=?,dif=?,dea=?,bar=? where code=? and date=?", new Object[]{
                        daily.getEma12(),
                        daily.getEma26(),
                        daily.getDif(),
                        daily.getDea(),
                        daily.getBar(),
                        daily.getCode(),
                        DateUtil.format(daily.getDate())
                });
            }
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(DbManager.class.toString(), ex.getMessage());
        } finally {
            db.endTransaction();
        }
    }
}
