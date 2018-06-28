package com.github.alexhanxs.lighttraffic.base.livedata;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class TypeLiveData<T extends WrapLiveData> extends MutableLiveData<T> {

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<T> observer) {
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (t.type > 0) {
                    observer.onChanged(t);
                }
            }
        });
    }
}
