package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sks on 2016/4/13.
 */
public class VinInfoBean implements Parcelable {
    public String id;//汽车id
    public String vinnumId;//汽车车架号


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.vinnumId);
    }

    public VinInfoBean() {
    }

    public VinInfoBean(String vinnumId) {
        this.vinnumId = vinnumId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VinInfoBean that = (VinInfoBean) o;

        return !(vinnumId != null ? !vinnumId.equals(that.vinnumId) : that.vinnumId != null);

    }

    @Override
    public int hashCode() {
        return vinnumId != null ? vinnumId.hashCode() : 0;
    }

    protected VinInfoBean(Parcel in) {
        this.id = in.readString();
        this.vinnumId = in.readString();
    }

    public static final Parcelable.Creator<VinInfoBean> CREATOR = new Parcelable.Creator<VinInfoBean>() {
        public VinInfoBean createFromParcel(Parcel source) {
            return new VinInfoBean(source);
        }

        public VinInfoBean[] newArray(int size) {
            return new VinInfoBean[size];
        }
    };
}
