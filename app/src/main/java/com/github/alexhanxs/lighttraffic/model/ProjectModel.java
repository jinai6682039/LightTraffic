package com.github.alexhanxs.lighttraffic.model;

import com.github.alexhanxs.lighttraffic.base.RetrofitHelper;
import com.github.alexhanxs.lighttraffic.base.SubscribeHelper;
import com.github.alexhanxs.lighttraffic.base.observer.MainThreadObserver;
import com.github.alexhanxs.lighttraffic.base.service.TestService;
import com.github.alexhanxs.lighttraffic.model.entity.Project;
import com.github.jinai6682039.livedata_annotation.ObserverMethod;
import com.github.jinai6682039.livedata_annotation.ObserverModel;
import com.github.jinai6682039.livedata_annotation.ObserverParam;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * 下一步准备通过
 * Created by Alexhanxs on 2018/6/27.
 */
@ObserverModel
public class ProjectModel extends SubscribeHelper{

    public TestService getService() {
        return RetrofitHelper.getInstance().getService(TestService.BASE_URL, TestService.class,
                new HashMap<String, String>());
    }

    @ObserverMethod()
    public void getProjectList(@ObserverParam("userId") String userId, MainThreadObserver<List<Project>> observer) {
        Observable observable = getService().getProjectList(userId);
        doSimpleSubscribe(observable, observer);
    }

    @ObserverMethod()
    public void getProjectList2(@ObserverParam("userId") String userId, MainThreadObserver<List<List<Project>>> observer) {
        Observable observable = getService().getProjectList(userId);
        doSimpleSubscribe(observable, observer);
    }

    @ObserverMethod()
    public void getProjectDetails(@ObserverParam("userId") String userId, @ObserverParam("projectName") String projectName, MainThreadObserver<Project> observer) {
        Observable observable = getService().getProjectDetails(userId, projectName);
        doSimpleSubscribe(observable, observer);
    }
}
