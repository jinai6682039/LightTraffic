package com.github.alexhanxs.lighttraffic.viewmodel;

import com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData;
import com.github.alexhanxs.lighttraffic.base.livedata.TypeLiveData;
import com.github.alexhanxs.lighttraffic.base.observer.MainThreadObserver;
import com.github.alexhanxs.lighttraffic.base.viewmodel.BaseViewModel;
import com.github.alexhanxs.lighttraffic.model.ProjectModel;
import com.github.alexhanxs.lighttraffic.model.entity.Project;

import java.util.List;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class ProjectViewModel extends BaseViewModel {

    protected ProjectModel projectModel;

    protected MainThreadObserver<List<Project>> projectListObserver;
    protected MainThreadObserver<Project> projectObserver;

    protected TypeLiveData<WrapLiveData<List<Project>>> projectListLiveData;
    protected TypeLiveData<WrapLiveData<Project>> projectLiveData;

    public ProjectViewModel() {

        projectModel = new ProjectModel();

        projectListLiveData = new TypeLiveData<>();
        projectLiveData = new TypeLiveData<>();

        projectListObserver = new MainThreadObserver<>(projectListLiveData);
        projectObserver = new MainThreadObserver<>(projectLiveData);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void getProjectList(String userId) {
        projectModel.getProjectList(userId, projectListObserver);
    }

    public void getProjectDetails(String userId, String projectId) {
        projectModel.getProjectDetails(userId, projectId, projectObserver);
    }

    public TypeLiveData<WrapLiveData<List<Project>>> getProjectListLiveData() {
        return projectListLiveData;
    }

    public void setProjectListLiveData(TypeLiveData<WrapLiveData<List<Project>>> projectListLiveData) {
        this.projectListLiveData = projectListLiveData;
    }

    public TypeLiveData<WrapLiveData<Project>> getProjectLiveData() {
        return projectLiveData;
    }

    public void setProjectLiveData(TypeLiveData<WrapLiveData<Project>> projectLiveData) {
        this.projectLiveData = projectLiveData;
    }

}
