package org.my.Test.stock;

import org.my.Test.utils.DateUtil;

import java.io.Serializable;
import java.util.Date;

public class StockDaily implements Serializable {
    private String code;
    private String name;
    private Date date;
    private float tclose;
    private float high;
    private float low;
    private float topen;
    private float lclose;
    private float chg;
    private float pchg;
    private float turnover;
    private float voturnover;
    private float vaturnover;
    private float tcap;
    private float mcap;
    private float ema12;
    private float ema26;
    private float dif;
    private float dea;
    private float macd;
    private float bar;

    public StockDaily(){}

    public StockDaily(String code){
        this.code = code;
    }

    public StockDaily(String code, Date date) {
        this.code = code;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getTclose() {
        return tclose;
    }

    public void setTclose(float tclose) {
        this.tclose = tclose;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getTopen() {
        return topen;
    }

    public void setTopen(float topen) {
        this.topen = topen;
    }

    public float getLclose() {
        return lclose;
    }

    public void setLclose(float lclose) {
        this.lclose = lclose;
    }

    public float getChg() {
        return chg;
    }

    public void setChg(float chg) {
        this.chg = chg;
    }

    public float getPchg() {
        return pchg;
    }

    public void setPchg(float pchg) {
        this.pchg = pchg;
    }

    public float getTurnover() {
        return turnover;
    }

    public void setTurnover(float turnover) {
        this.turnover = turnover;
    }

    public float getVoturnover() {
        return voturnover;
    }

    public void setVoturnover(float voturnover) {
        this.voturnover = voturnover;
    }

    public float getVaturnover() {
        return vaturnover;
    }

    public void setVaturnover(float vaturnover) {
        this.vaturnover = vaturnover;
    }

    public float getTcap() {
        return tcap;
    }

    public void setTcap(float tcap) {
        this.tcap = tcap;
    }

    public float getMcap() {
        return mcap;
    }

    public void setMcap(float mcap) {
        this.mcap = mcap;
    }

    public float getEma12() {
        return ema12;
    }

    public void setEma12(float ema12) {
        this.ema12 = ema12;
    }

    public float getEma26() {
        return ema26;
    }

    public void setEma26(float ema26) {
        this.ema26 = ema26;
    }

    public float getDif() {
        return dif;
    }

    public void setDif(float dif) {
        this.dif = dif;
    }

    public float getDea() {
        return dea;
    }

    public void setDea(float dea) {
        this.dea = dea;
    }

    public float getMacd() {
        return macd;
    }

    public void setMacd(float macd) {
        this.macd = macd;
    }

    public float getBar() {
        return bar;
    }

    public void setBar(float bar) {
        this.bar = bar;
    }

    @Override
    public String toString() {
        return "StockDaily{" +
                "code='" + code + '\'' +
                ", date=" + DateUtil.format(date) +
                ", tclose=" + tclose +
                '}';
    }

    public float getAmplitude() {
        if(low != 0) {
            return (high - low)*100 / low;
        }else
        {
            return 0;
        }
    }
}
