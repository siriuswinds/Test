package org.my.Test.reader;

import android.os.Message;

import org.my.Test.db.DbManager;
import org.my.Test.stock.Stock;
import org.my.Test.stock.StockDaily;
import org.my.Test.utils.DateUtil;
import org.my.Test.utils.Global;
import org.my.Test.utils.NumberUtil;
import org.my.Test.utils.ThreadPoolUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.os.Handler;

public class HttpReader {
    //"http://quotes.money.163.com/service/chddata.html?code=1300036&start=20091216&end=20160811&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP";
    private final String STOCK_DAILY_URL = "http://quotes.money.163.com/service/chddata.html?code=%1$s%2$s&start=%3$s&end=%4$s&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP";
    private Handler handler;
    private DbManager dbManager;
    private URL url;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public void setDbManager(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    public void downloadStockDaily(Stock stock, Date start, Date end) throws IOException {
        //URL url = new URL("http://quotes.money.163.com/service/chddata.html?code=1300036&start=20091216&end=20160811&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP");
        url = initStockDailyURL(stock,start,end);
        ThreadPoolUtils.execute(new HttpRunner());
    }

    private URL initStockDailyURL(Stock stock, Date start, Date end) throws MalformedURLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String market = "0";
        if(stock.getMarket().equals("SZ")){
            market = "1";
        }

        return new URL(String.format(STOCK_DAILY_URL,market,stock.getCode(),sdf.format(start), sdf.format(end)));
    }

    class HttpRunner implements Runnable{
        @Override
        public void run() {
            Message msg = handler.obtainMessage();

            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                int code = conn.getResponseCode();

                if (code == 200) {
                    InputStream is = conn.getInputStream();
                    InputStreamReader inputReader = new InputStreamReader(is, "GB2312");
                    BufferedReader buffReader = new BufferedReader(inputReader);
                    String line = buffReader.readLine();
                    List<StockDaily> stockDailys = new ArrayList<>();

                    while ((line = buffReader.readLine()) != null) {
                        String[] stock_item = line.split(",");

                        if (stock_item == null || stock_item.length < 12) continue;

                        StockDaily stockdaily = new StockDaily();
                        stockdaily.setDate(DateUtil.parse(stock_item[0].trim()));
                        stockdaily.setCode(stock_item[1].trim().replace("'",""));
                        stockdaily.setName(stock_item[2].trim());
                        stockdaily.setTclose(NumberUtil.parseFloat(stock_item[3].trim()));
                        stockdaily.setHigh(NumberUtil.parseFloat(stock_item[4].trim()));
                        stockdaily.setLow(NumberUtil.parseFloat(stock_item[5].trim()));
                        stockdaily.setTopen(NumberUtil.parseFloat(stock_item[6].trim()));
                        stockdaily.setLclose(NumberUtil.parseFloat(stock_item[7].trim()));
                        stockdaily.setChg(NumberUtil.parseFloat(stock_item[8].trim()));
                        stockdaily.setPchg(NumberUtil.parseFloat(stock_item[9].trim()));
                        stockdaily.setTurnover(NumberUtil.parseFloat(stock_item[10].trim()));
                        stockdaily.setVoturnover(NumberUtil.parseFloat(stock_item[11].trim()));
                        stockdaily.setVaturnover(NumberUtil.parseFloat(stock_item[12].trim()));
                        stockdaily.setTcap(NumberUtil.parseFloat(stock_item[13].trim()));
                        stockdaily.setMcap(NumberUtil.parseFloat(stock_item[14].trim()));

                        stockDailys.add(stockdaily);
                    }
                    buffReader.close();
                    inputReader.close();
                    is.close();

                    msg.what = Global.STOCK_DOWNLOAD_SUCCESS;
                    msg.obj = stockDailys;
                    handler.sendMessage(msg);

                    addStockDailys(stockDailys);
                } else {
                    msg.what = Global.STOCK_DOWNLOAD_FAILED;
                    handler.sendMessage(msg);
                }
                conn.disconnect();
            }catch (Exception e){
                e.printStackTrace();
                msg.what = Global.STOCK_DOWNLOAD_FAILED;
                handler.sendMessage(msg);
            }
        }

        private void addStockDailys(List<StockDaily> stockdailys) {
            if(dbManager != null && stockdailys != null){
                dbManager.beginTransaction();
                for (StockDaily stockdaily:stockdailys) {
                    dbManager.addStockDaily(stockdaily);
                }
                dbManager.commitTransaction();
            }
        }
    }
}
