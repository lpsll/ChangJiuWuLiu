package com.htlc.cjwl.bean;

/**
 * Created by luochuan on 15/11/13.
 * "id": "1",
 "baseprice": "240",
 "basekilometre": "20",
 "overstep": "1",
 "overstepprice": "8",
 "type": "0公里接送"
 */
public class Method {
    private String id;
    private String baseprice;
    private String basekilometre;
    private String overstep;
    private String overstepprice;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBaseprice() {
        return baseprice;
    }

    public void setBaseprice(String baseprice) {
        this.baseprice = baseprice;
    }

    public String getBasekilometre() {
        return basekilometre;
    }

    public void setBasekilometre(String basekilometre) {
        this.basekilometre = basekilometre;
    }

    public String getOverstep() {
        return overstep;
    }

    public void setOverstep(String overstep) {
        this.overstep = overstep;
    }

    public String getOverstepprice() {
        return overstepprice;
    }

    public void setOverstepprice(String overstepprice) {
        this.overstepprice = overstepprice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Method{" +
                "id='" + id + '\'' +
                ", baseprice='" + baseprice + '\'' +
                ", basekilometre='" + basekilometre + '\'' +
                ", overstep='" + overstep + '\'' +
                ", overstepprice='" + overstepprice + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
