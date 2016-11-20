package abhi.com.mobitest.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import abhi.com.mobitest.R;

/**
 * Created by Abhishek on 19-Nov-16.
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
    }


}
