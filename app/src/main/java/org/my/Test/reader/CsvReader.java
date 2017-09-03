package org.my.Test.reader;

import org.my.Test.db.DbManager;
import org.my.Test.stock.Capitalflow;
import org.my.Test.stock.Stock;
import org.my.Test.stock.Transaction;
import org.my.Test.utils.DateUtil;
import org.my.Test.utils.NumberUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    private static final String SH_STOCKFILE = "/storage/sdcard0/stock/sh.xls";
    private static final String SZ_STOCKFILE = "/storage/sdcard0/stock/sz.xls";
    /*
        资金流水
     */
    private static final String FILE_ZJLS = "/storage/sdcard0/stock/ZJLS.csv";
    /*
        历史成交
     */
    private static final String FILE_LSCJ = "/storage/sdcard0/stock/LSCJ.csv";

    /*
        读取历史资金流水文件
     */
    public static List<Capitalflow> readCapitalflows(DbManager dbManager){
        List<Capitalflow> capitalflows = getCapitalflowsFromCVS(FILE_ZJLS,dbManager);
        return capitalflows;
    }

    private static List<Capitalflow> getCapitalflowsFromCVS(String filename, DbManager dbManager) {
        List<Capitalflow> capitalflows = null;

        File fs = new File(filename);

        try{
            FileInputStream inputStream = new FileInputStream(fs);
            if(inputStream != null){
                capitalflows = new ArrayList<>();
                InputStreamReader intputReader = new InputStreamReader(inputStream,"GB2312");
                BufferedReader bufferReader = new BufferedReader(intputReader);
                String line = bufferReader.readLine();
                while ((line = bufferReader.readLine())!= null){
                    String[] capitalflow_item = line.split(",");
                    if (capitalflow_item == null || capitalflow_item.length < 4) continue;

                    Capitalflow capitalflow = new Capitalflow();
                    capitalflow.setJsrq(DateUtil.parse(capitalflow_item[0],"yyyyMMdd"));
                    capitalflow.setZqdm(capitalflow_item[1]);
                    capitalflow.setZqmc(capitalflow_item[2]);

                    capitalflow.setCjsl(NumberUtil.parseFloat(capitalflow_item[3]));
                    capitalflow.setCjjg(NumberUtil.parseFloat(capitalflow_item[4]));
                    capitalflow.setZy(capitalflow_item[5]);
                    capitalflow.setFsje(NumberUtil.parseFloat(capitalflow_item[6]));
                    capitalflow.setSyje(NumberUtil.parseFloat(capitalflow_item[7]));
                    capitalflow.setBizhong(capitalflow_item[8]);
                    capitalflow.setCjbh(capitalflow_item[9]);
                    capitalflow.setGddm(capitalflow_item[10]);
                    capitalflow.setCjrq(DateUtil.parse(capitalflow_item[11],"yyyyMMdd"));
                    //capitalflow.setBeizhu(capitalflow_item[12]);
                    capitalflows.add(capitalflow);

                    //jsrq text,zqdm varchar,zqmc varchar,cjsl float,cjjg float,zy varchar,fsje float,syje float,bizhong varchar,cjbh varchar,gddm varchar,cjrq text,beizhu varchar
                }
                dbManager.addCapitalflows(capitalflows);
                bufferReader.close();
                intputReader.close();
            }
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return capitalflows;
    }

    /*
        读取历史成交文件
     */
    public static List<Transaction> readTransactions(DbManager dbManager){
        List<Transaction> transactions = getTransactionsFromCVS(FILE_LSCJ,dbManager);
        return transactions;
    }

    private static List<Transaction> getTransactionsFromCVS(String filename, DbManager dbManager) {
        List<Transaction> transactions = null;

        File fs = new File(filename);

        try{
            FileInputStream inputStream = new FileInputStream(fs);
            if(inputStream != null){
                transactions = new ArrayList<>();
                InputStreamReader intputReader = new InputStreamReader(inputStream,"GB2312");
                BufferedReader bufferReader = new BufferedReader(intputReader);
                String line = bufferReader.readLine();
                while ((line = bufferReader.readLine())!= null){
                    String[] transaction_item = line.split(",");
                    if (transaction_item == null || transaction_item.length < 4) continue;

                    Transaction transaction = new Transaction();
                    transaction.setCjrq(DateUtil.parse(transaction_item[0],"yyyyMMdd"));
                    transaction.setZqdm(transaction_item[1]);
                    transaction.setZqmc(transaction_item[2]);
                    transaction.setMmbz(transaction_item[3]);
                    transaction.setCjsl(NumberUtil.parseFloat(transaction_item[4]));
                    transaction.setCjjg(NumberUtil.parseFloat(transaction_item[5]));
                    transaction.setCjje(NumberUtil.parseFloat(transaction_item[6]));
                    transaction.setYhs(NumberUtil.parseFloat(transaction_item[7]));
                    transaction.setSxf(NumberUtil.parseFloat(transaction_item[8]));
                    transaction.setDsgf(NumberUtil.parseFloat(transaction_item[9]));
                    transaction.setGhf(NumberUtil.parseFloat(transaction_item[10]));
                    transaction.setJsf(NumberUtil.parseFloat(transaction_item[11]));
                    transaction.setJyf(NumberUtil.parseFloat(transaction_item[12]));
                    transaction.setJshl(NumberUtil.parseFloat(transaction_item[13]));
                    transaction.setWtbh(transaction_item[14]);
                    transaction.setGddm(transaction_item[15]);
                    transaction.setJysmc(transaction_item[16]);
                    //transaction.setBeizhu(transaction_item[17]);
                    transactions.add(transaction);
                    //cjrq text,zqdm varchar,zqmc varchar,mmbz varchar,cjsl float,cjjg float,cjje float,yhs float,sxf float,dsgf float,ghf float,jsf float,jyf float,jshl float,wtbh varchar,gddm varchar,jysmc varchar,beizhu varchar)");
                }
                dbManager.addTransactions(transactions);
                bufferReader.close();
                intputReader.close();
            }
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return transactions;
    }

    /*
        读取股票列表
     */
    public static List<Stock> readStocks() {
        List<Stock> stocks = getStocksFromCVS(SH_STOCKFILE, "SH");
        stocks.addAll(getStocksFromCVS(SZ_STOCKFILE, "SZ"));
        return stocks;
    }

    private static List<Stock> getStocksFromCVS(String filename, String market) {
        List<Stock> stocks = null;
        File fs = new File(filename);
        try {
            FileInputStream inputStream = new FileInputStream(fs);

            if (inputStream != null) {
                stocks = new ArrayList<Stock>();
                InputStreamReader inputReader = new InputStreamReader(inputStream, "GB2312");
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line;
                while ((line = buffReader.readLine()) != null) {
                    String[] stock_item = line.split("\t");
                    if (stock_item == null || stock_item.length < 4) continue;

                    Stock stock = new Stock();
                    stock.setCode(stock_item[0].trim());
                    stock.setName(stock_item[1].trim());
                    stock.setTrade(stock_item[2].trim());
                    stock.setRegion(stock_item[3].trim());
                    stock.setMarket(market);
                    stocks.add(stock);
                }
                buffReader.close();
                inputReader.close();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stocks;
    }
}
