package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sks on 2016/4/6.
 */
public class CarInfoBean implements Parcelable {
    public String name;
    public String id;
    public String price;
    public String type;
    public String num = "1";


    public CarInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.price);
        dest.writeString(this.type);
        dest.writeString(this.num);
    }

    protected CarInfoBean(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.price = in.readString();
        this.type = in.readString();
        this.num = in.readString();
    }

    public static final Creator<CarInfoBean> CREATOR = new Creator<CarInfoBean>() {
        public CarInfoBean createFromParcel(Parcel source) {
            return new CarInfoBean(source);
        }

        public CarInfoBean[] newArray(int size) {
            return new CarInfoBean[size];
        }
    };
}
