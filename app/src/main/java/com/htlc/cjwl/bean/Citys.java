package com.htlc.cjwl.bean;

import java.util.List;

/**
 * Created by luochuan on 15/11/5.
 */
public class Citys {

    private List<City> A;
    private List<City> B;
    private List<City> C;
    private List<City> D;
    private List<City> S;

    public List<City> getA() {
        return A;
    }

    public void setA(List<City> a) {
        A = a;
    }

    public List<City> getB() {
        return B;
    }

    public void setB(List<City> b) {
        B = b;
    }

    public List<City> getC() {
        return C;
    }

    public void setC(List<City> c) {
        C = c;
    }

    public List<City> getD() {
        return D;
    }

    public void setD(List<City> d) {
        D = d;
    }

    public List<City> getS() {
        return S;
    }

    public void setS(List<City> s) {
        S = s;
    }

    @Override
    public String toString() {
        return "Citys{" +
                "A=" + A +
                ", B=" + B +
                ", C=" + C +
                ", D=" + D +
                ", S=" + S +
                '}';
    }
}
