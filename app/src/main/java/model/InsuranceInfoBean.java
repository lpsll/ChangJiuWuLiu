package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sks on 2016/4/13.
 */
public class InsuranceInfoBean implements Parcelable {
    public String id;//汽车id
    public String insurePrice;//汽车投保价格


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.insurePrice);
    }

    public InsuranceInfoBean() {
    }

    protected InsuranceInfoBean(Parcel in) {
        this.id = in.readString();
        this.insurePrice = in.readString();
    }

    public static final Parcelable.Creator<InsuranceInfoBean> CREATOR = new Parcelable.Creator<InsuranceInfoBean>() {
        public InsuranceInfoBean createFromParcel(Parcel source) {
            return new InsuranceInfoBean(source);
        }

        public InsuranceInfoBean[] newArray(int size) {
            return new InsuranceInfoBean[size];
        }
    };
}
