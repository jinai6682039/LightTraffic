package com.github.alexhanxs.lighttraffic.base.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.alexhanxs.lighttraffic.R;

/**
 * Created by Alexhanxs on 2018/6/28.
 */

public class LoadingDialog extends BaseDialog {

    private Context mContext;

    private String mMsg;

    public LoadingDialog(Context context, String msg) {
        super(context, R.style.baseDialog);
        this.mContext = context;
        this.mMsg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    public void init() {
        View view = View.inflate(mContext, R.layout.dialog_loading_layout, null);

        TextView tvMessage = (TextView) view.findViewById(R.id.tv_loading_data_message);

        if (!TextUtils.isEmpty(mMsg)) {
            tvMessage.setText(mMsg);
        }

        setContentView(view);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);

        Window win = getWindow();
        WindowManager m = win.getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (dm.widthPixels / 2.5);
        p.alpha = 0.7f;
        win.setAttributes(p);
    }

    /**
     * 设置相关信息.
     *
     * @param message 信息内容.
     */
    public void setMessage(String message) {
        mMsg = message;
    }


}
