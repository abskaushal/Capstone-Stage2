package abhi.com.mobitest.webservices;

import java.util.List;

import abhi.com.mobitest.entity.IBaseData;

/**
 * Wrapper class to hold data from WebServices.
 * <p/>
 * Created by Abhishek on 20-Nov-16.
 */
public class WebData {
    private boolean success;
    private int statusCode;
    private String message;
    private IBaseData data;
    private List<IBaseData> testData;
    private int apiCode;

    public List<IBaseData> getTestData() {
        return testData;
    }

    public void setTestData(List<IBaseData> testData) {
        this.testData = testData;
    }

    public int getApiCode() {
        return apiCode;
    }

    public void setApiCode(int apiCode) {
        this.apiCode = apiCode;
    }

    public IBaseData getData() {
        return data;
    }

    public void setData(IBaseData data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
