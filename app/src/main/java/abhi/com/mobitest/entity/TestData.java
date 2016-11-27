package abhi.com.mobitest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abhishek on 26-Nov-16.
 */
public class TestData implements IBaseData, Parcelable {
    private String user_id;
    private String question;
    private String title;
    private String description;
    private String duration;
    private String testId;

    public TestData() {

    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public TestData(Parcel in) {
        String[] data = new String[6];
        in.readStringArray(data);
        this.user_id = data[0];
        this.question = data[1];
        this.title = data[2];
        this.description = data[3];
        this.duration = data[4];
        this.testId = data[5];

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.user_id, this.question, this.title, this.description, this.duration, this.testId + ""});
    }

    public static final Parcelable.Creator<TestData> CREATOR = new Parcelable.Creator<TestData>() {
        @Override
        public TestData createFromParcel(Parcel source) {
            return new TestData(source);
        }

        @Override
        public TestData[] newArray(int size) {
            return new TestData[size];
        }
    };
}