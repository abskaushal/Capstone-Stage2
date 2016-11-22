package abhi.com.mobitest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abhishek on 03-Nov-16.
 */
public class UserData implements Parcelable, IBaseData {
    private String userName;
    private String email;
    private String imageUrl;
    private String parentAccount;
    private String password;
    private String gcmId;
    private int role;
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
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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
        this.userName = data[0];
        this.email = data[1];
        this.imageUrl = data[2];
        this.parentAccount = data[3];
        this.gcmId = data[4];
        this.role = Integer.parseInt(data[5]);
        this.userId = Integer.parseInt(data[6]);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.userName, this.email, this.imageUrl, this.parentAccount, this.gcmId, this.role + "", this.userId + ""});
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