package com.htlc.cjwl.bean;

import java.util.List;

/**
 * Created by luochuan on 15/11/5.
 */
public class HotListCity {
    private List<HotCity> hotCitys;
    private Citys list;

    public List<HotCity> getHotCitys() {
        return hotCitys;
    }

    public void setHotCitys(List<HotCity> hotCitys) {
        this.hotCitys = hotCitys;
    }

    public Citys getList() {
        return list;
    }

    public void setList(Citys list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "HotListCity{" +
                "hotCitys=" + hotCitys +
                ", list=" + list +
                '}';
    }
}
