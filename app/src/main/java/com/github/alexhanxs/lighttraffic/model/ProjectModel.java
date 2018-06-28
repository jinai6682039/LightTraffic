package com.github.alexhanxs.lighttraffic.model;

import com.github.alexhanxs.lighttraffic.base.RetrofitHelper;
import com.github.alexhanxs.lighttraffic.base.SubscribeHelper;
import com.github.alexhanxs.lighttraffic.base.observer.MainThreadObserver;
import com.github.alexhanxs.lighttraffic.base.service.TestService;
import com.github.alexhanxs.lighttraffic.model.entity.Project;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class ProjectModel extends SubscribeHelper{

    public TestService getService() {
        return RetrofitHelper.getInstance().getService(TestService.BASE_URL, TestService.class,
                new HashMap<String, String>());
    }

    public void getProjectList(String userId, MainThreadObserver<List<Project>> observer) {
        Observable observable = getService().getProjectList(userId);
        doSimpleSubscribe(observable, observer);
    }

    public void getProjectDetails(String userId, String projectName, MainThreadObserver<Project> observer) {
        Observable observable = getService().getProjectDetails(userId, projectName);
        doSimpleSubscribe(observable, observer);
    }
}
