package org.my.Test.stock;

import org.my.Test.utils.DateUtil;

import java.io.Serializable;
import java.util.Date;

public class Capitalflow implements Serializable {
    //jsrq text,zqdm varchar,zqmc varchar,cjsl float,cjjg float,zy varchar,fsje float,syje float,bizhong varchar,cjbh varchar,gddm varchar,cjrq text,beizhu varchar
    private Date jsrq;
    private String zqdm;
    private String zqmc;
    private float cjsl;
    private float cjjg;
    private String zy;
    private float fsje;
    private float profits;
    private float syje;
    private String bizhong;
    private String cjbh;
    private String gddm;
    private Date cjrq;
    private String beizhu;
    private StockDaily stockDaily;

    public Capitalflow() {
    }

    public Date getJsrq() {
        return jsrq;
    }

    public void setJsrq(Date jsrq) {
        this.jsrq = jsrq;
    }

    public String getZqdm() {
        return zqdm;
    }

    public void setZqdm(String zqdm) {
        this.zqdm = zqdm;
    }

    public String getZqmc() {
        return zqmc;
    }

    public void setZqmc(String zqmc) {
        this.zqmc = zqmc;
    }

    public float getCjsl() {
        return cjsl;
    }

    public void setCjsl(float cjsl) {
        this.cjsl = cjsl;
    }

    public float getCjjg() {
        return cjjg;
    }

    public void setCjjg(float cjjg) {
        this.cjjg = cjjg;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public float getFsje() {
        return fsje;
    }

    public void setFsje(float fsje) {
        this.fsje = fsje;
    }

    public float getProfits() {
        return profits;
    }

    public void setProfits(float profits) {
        this.profits = profits;
    }

    public float getSyje() {
        return syje;
    }

    public void setSyje(float syje) {
        this.syje = syje;
    }

    public String getBizhong() {
        return bizhong;
    }

    public void setBizhong(String bizhong) {
        this.bizhong = bizhong;
    }

    public String getCjbh() {
        return cjbh;
    }

    public void setCjbh(String cjbh) {
        this.cjbh = cjbh;
    }

    public String getGddm() {
        return gddm;
    }

    public void setGddm(String gddm) {
        this.gddm = gddm;
    }

    public Date getCjrq() {
        return cjrq;
    }

    public void setCjrq(Date cjrq) {
        this.cjrq = cjrq;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public StockDaily getStockDaily() {
        return stockDaily;
    }

    public void setStockDaily(StockDaily stockDaily) {
        this.stockDaily = stockDaily;
    }

    @Override
    public String toString() {
        return "Capitalflow{" +
                "jsrq=" + DateUtil.format(jsrq) +
                ", zqdm='" + zqdm + '\'' +
                ", cjsl=" + cjsl +
                ", cjjg=" + cjjg +
                '}';
    }
}
