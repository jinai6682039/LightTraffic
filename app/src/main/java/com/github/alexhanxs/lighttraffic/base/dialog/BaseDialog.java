package com.github.alexhanxs.lighttraffic.base.dialog;

import android.app.Dialog;
import android.content.Context;

import com.github.alexhanxs.lighttraffic.R;

/**
 * Created by Alexhanxs on 2018/6/28.
 */

public class BaseDialog extends Dialog {

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public BaseDialog(Context context) {
        super(context, R.style.baseDialog);
    }
}
