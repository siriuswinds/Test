package org.my.Test.stock;

import android.os.Handler;

import org.my.Test.db.DbManager;
import org.my.Test.utils.StockUtil;

import java.io.Serializable;
import java.util.Date;

public class Stock implements Serializable {
    private String code;
    private String name;
    private String trade;
    private String region;
    private String market;
    private Date opendate;

    public Stock() {
    }

    public Stock(String _code, String _name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Date getOpendate() {
        return opendate;
    }

    public void setOpendate(Date opendate) {
        this.opendate = opendate;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * 更新股票日交易数据
     * @param dbManager
     * @param handler
     */
    public void updateStockDaily(DbManager dbManager, Handler handler) {
        StockUtil.updateStockDaily(this,dbManager,handler);
    }

    /**
     * 计算MACD指标
     * @param dbManager
     * @param handler
     */
    public void calcStockMACD(DbManager dbManager, Handler handler) {
        StockUtil.calcStockMACD(this,dbManager,handler);
    }
}
