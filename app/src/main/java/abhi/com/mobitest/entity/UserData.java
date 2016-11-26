package abhi.com.mobitest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abhishek on 03-Nov-16.
 */
public class UserData implements Parcelable, IBaseData {
    private String displayname;
    private String email;
    private String imageurl;
    private String parentaccount;
    private String password;
    private String gsmid;
    private int category;
    private int userId;

    public UserData() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return displayname;
    }

    public void setUserName(String userName) {
        this.displayname = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageurl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageurl = imageUrl;
    }

    public String getParentAccount() {
        return parentaccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentaccount = parentAccount;
    }

    public String getGcmId() {
        return gsmid;
    }

    public void setGcmId(String gcmId) {
        this.gsmid = gcmId;
    }

    public int getRole() {
        return category;
    }

    public void setRole(int role) {
        this.category = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserData(Parcel in) {
        String[] data = new String[7];
        in.readStringArray(data);
        this.displayname = data[0];
        this.email = data[1];
        this.imageurl = data[2];
        this.parentaccount = data[3];
        this.gsmid = data[4];
        this.category = Integer.parseInt(data[5]);
        this.userId = Integer.parseInt(data[6]);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.displayname, this.email, this.imageurl, this.parentaccount, this.gsmid, this.category + "", this.userId + ""});
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