package com.htlc.cjwl.bean;

public class CityInfoBean implements Comparable<CityInfoBean>{

        public String cityname;
        public String id;
        public String name;
        public String group;

    @Override
    public String toString() {
        return "CityInfoBean{" +
                "cityname='" + cityname + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                '}';
    }

    @Override
    public int compareTo(CityInfoBean another) {
        if(group!=null){
            return group.compareTo(another.group);
        }
        return 0;
    }
}