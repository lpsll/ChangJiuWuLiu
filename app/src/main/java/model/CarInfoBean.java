package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sks on 2016/4/6.
 */
public class CarInfoBean implements Parcelable {
    public String carName;
    public String carId;
    public String carNum = "1";


    public CarInfoBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.carName);
        dest.writeString(this.carId);
        dest.writeString(this.carNum);
    }

    protected CarInfoBean(Parcel in) {
        this.carName = in.readString();
        this.carId = in.readString();
        this.carNum = in.readString();
    }

    public static final Parcelable.Creator<CarInfoBean> CREATOR = new Parcelable.Creator<CarInfoBean>() {
        public CarInfoBean createFromParcel(Parcel source) {
            return new CarInfoBean(source);
        }

        public CarInfoBean[] newArray(int size) {
            return new CarInfoBean[size];
        }
    };
}
