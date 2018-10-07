package com.github.alexhanxs.lighttraffic.view;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.github.alexhanxs.lighttraffic.R;
import com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData;
import com.github.alexhanxs.lighttraffic.model.entity.Project;
import com.github.alexhanxs.lighttraffic.view.base.BaseMvvmActivity;
import com.github.alexhanxs.lighttraffic.viewmodel.ProjectViewModelSouce;

import static com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData.TYPE_DATA_ERROR;
import static com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData.TYPE_DATA_FINISH;
import static com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData.TYPE_DATA_LOADING;
import static com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData.TYPE_DATA_SUCCESS;

public class Main2Activity extends BaseMvvmActivity {

    public static final void toHere(Context context, String userId, String projectId) {
        Intent intent = new Intent(context, Main2Activity.class);
        intent.putExtra("UserId", userId);
        intent.putExtra("ProjectId", projectId);
        context.startActivity(intent);
    }

    public ProjectViewModelSouce viewModel;
    private String userId;
    private String projectId;

    TextView tv_project_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tv_project_detail = findViewById(R.id.tv_project_detail);

        if (getIntent() != null) {
            userId = getIntent().getStringExtra("UserId");
            projectId = getIntent().getStringExtra("ProjectId");
        }

        if (ProjectViewModelSouce.mInstance != null) {
            viewModel = ProjectViewModelSouce.mInstance;
        } else {
            viewModel = (ProjectViewModelSouce) getmViewModel();
        }


        initLiveData();

        if (viewModel.getPreLoadedData() != null && viewModel.getPreLoadedData().name.equals(projectId)) {
            makeProjectDetail(viewModel.getPreLoadedData());
        } else {
            viewModel.getProjectDetails(userId, projectId);
        }
    }

    @Override
    public Class getViewModelClass() {
        return ProjectViewModelSouce.class;
    }

    private void initLiveData() {
        viewModel.getProjectLiveData().observe(this, new Observer<WrapLiveData<Project>>() {
            @Override
            public void onChanged(@Nullable WrapLiveData<Project> projectWrapLiveData) {
                postLiveData(projectWrapLiveData);
            }
        });
    }

    public void postLiveData(WrapLiveData<Project> listWrapLiveData) {
        switch (listWrapLiveData.type) {
            case TYPE_DATA_SUCCESS:
                makeProjectDetail(listWrapLiveData.data);
                break;

            case TYPE_DATA_ERROR:
                Toast.makeText(this,
                        listWrapLiveData.requestException.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
                break;

            case TYPE_DATA_FINISH:

                break;

            case TYPE_DATA_LOADING:
                if (listWrapLiveData.showLoading) {
                    toShowProgressMsg("load data..");
                } else {
                    toCloseProgressMsg();
                }
                break;
        }
    }

    public void makeProjectDetail(Project project) {
        StringBuilder builder = new StringBuilder();
        builder.append(project.name).append("\n")
                .append(project.full_name).append("\n")
                .append(project.owner.name).append("\n")
                .append(project.language).append("\n")
                .append(project.watchers).append("\n")
                .append(project.description).append("\n")
                .append(project.url).append("\n");

        tv_project_detail.setText(builder.toString());
    }
}
