package org.my.Test.stock;

import org.my.Test.utils.DateUtil;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    //cjrq text,zqdm varchar,zqmc varchar,mmbz varchar,cjsl float,cjjg float,cjje float,yhs float,sxf float,dsgf float,ghf float,jsf float,jyf float,jshl float,wtbh varchar,gddm varchar,jysmc varchar,beizhu varchar

    private Date cjrq;
    private String zqdm;
    private String zqmc;
    private String mmbz;
    private float cjsl;
    private float cjjg;
    private float cjje;
    private float yhs;
    private float sxf;
    private float dsgf;
    private float ghf;
    private float jsf;
    private float jyf;
    private float jshl;
    private String wtbh;
    private String gddm;
    private String jysmc;
    private String beizhu;

    public Transaction(){}

    public Date getCjrq() {
        return cjrq;
    }

    public void setCjrq(Date cjrq) {
        this.cjrq = cjrq;
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

    public String getMmbz() {
        return mmbz;
    }

    public void setMmbz(String mmbz) {
        this.mmbz = mmbz;
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

    public float getCjje() {
        return cjje;
    }

    public void setCjje(float cjje) {
        this.cjje = cjje;
    }

    public float getYhs() {
        return yhs;
    }

    public void setYhs(float yhs) {
        this.yhs = yhs;
    }

    public float getSxf() {
        return sxf;
    }

    public void setSxf(float sxf) {
        this.sxf = sxf;
    }

    public float getDsgf() {
        return dsgf;
    }

    public void setDsgf(float dsgf) {
        this.dsgf = dsgf;
    }

    public float getGhf() {
        return ghf;
    }

    public void setGhf(float ghf) {
        this.ghf = ghf;
    }

    public float getJsf() {
        return jsf;
    }

    public void setJsf(float jsf) {
        this.jsf = jsf;
    }

    public float getJyf() {
        return jyf;
    }

    public void setJyf(float jyf) {
        this.jyf = jyf;
    }

    public float getJshl() {
        return jshl;
    }

    public void setJshl(float jshl) {
        this.jshl = jshl;
    }

    public String getWtbh() {
        return wtbh;
    }

    public void setWtbh(String wtbh) {
        this.wtbh = wtbh;
    }

    public String getGddm() {
        return gddm;
    }

    public void setGddm(String jddm) {
        this.gddm = gddm;
    }

    public String getJysmc() {
        return jysmc;
    }

    public void setJysmc(String jysmc) {
        this.jysmc = jysmc;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "cjrq=" + DateUtil.format(cjrq) +
                ", zqdm='" + zqdm + '\'' +
                ", zqmc='" + zqmc + '\'' +
                ", cjsl=" + cjsl +
                ", cjjg=" + cjjg +
                ", cjje=" + cjje +
                '}';
    }
}
