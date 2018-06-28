package com.github.alexhanxs.lighttraffic.view.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.alexhanxs.lighttraffic.base.dialog.LoadingDialog;
import com.github.alexhanxs.lighttraffic.base.viewmodel.BaseViewModel;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public abstract class BaseMvvmActivity<T extends BaseViewModel> extends AppCompatActivity {

    private T mViewModel;

    public abstract Class<T> getViewModelClass();

    public T getmViewModel() {
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(getViewModelClass());
        }
        return mViewModel;
    }

    protected Dialog mProgressDialog;

    private Dialog getProgressDialog(String msg) {
        LoadingDialog loadingDialog = new LoadingDialog(this, msg);
        loadingDialog.setMessage(msg);
        return loadingDialog;
    }

    /**
     * 显示加载对话框
     *
     * @param msg
     */
    public void toShowProgressMsg(final String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (this == null || isDestroyed() || isFinishing()) {
                return;
            }
        } else {
            if (this == null || isFinishing()) {
                return;
            }
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            if (mProgressDialog instanceof ProgressDialog) {
                ((ProgressDialog) mProgressDialog).setMessage(msg);
            } else {
                try {
                    mProgressDialog.getClass()
                            .getMethod("setMessage", String.class)
                            .invoke(mProgressDialog, msg);
                } catch (Exception e) {
                    Log.e("", "toShowProgressMsg:", e);
                }
            }
        } else {
            mProgressDialog = getProgressDialog(msg);
            mProgressDialog.show();
        }
    }

    /**
     * 关闭加载对话框
     */
    public void toCloseProgressMsg() {
        if (this == null || isFinishing()) {
            return;
        }

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }
}
