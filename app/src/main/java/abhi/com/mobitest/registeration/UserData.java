package abhi.com.mobitest.registeration;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abhishek on 03-Nov-16.
 */
public class UserData implements Parcelable {
    public String mDisplayName;
    public String mEmailId;
    public String mPassword;
    public String mzipcode;
    public String mImageUrl;
    public String mImageLocalPath;

    public UserData() {

    }

    public UserData(String mImageUrl, String mDisplayName, String mEmailId, String mPassword,
                    String mzipcode, String imageLocalPath) {
        this.mImageUrl = mImageUrl;
        this.mDisplayName = mDisplayName;
        this.mEmailId = mEmailId;
        this.mPassword = mPassword;
        this.mzipcode = mzipcode;
        this.mImageLocalPath = imageLocalPath;
    }

    public UserData(Parcel in) {
        String[] data = new String[6];
        in.readStringArray(data);
        this.mDisplayName = data[0];
        this.mEmailId = data[1];
        this.mPassword = data[2];
        this.mzipcode = data[3];
        this.mImageUrl = data[4];
        this.mImageLocalPath = data[5];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.mDisplayName, this.mEmailId, this.mPassword, this.mzipcode, this.mImageUrl, this.mImageLocalPath});
    }
    public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel source) {
            return new UserData(source);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
}