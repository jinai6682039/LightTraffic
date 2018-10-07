package com.github.alexhanxs.lighttraffic.view;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.alexhanxs.lighttraffic.R;
import com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData;
import com.github.alexhanxs.lighttraffic.model.entity.Project;
import com.github.alexhanxs.lighttraffic.view.base.BaseMvvmActivity;
import com.github.alexhanxs.lighttraffic.viewmodel.ProjectViewModelSouce;

import java.util.ArrayList;
import java.util.List;

import static com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData.TYPE_DATA_ERROR;
import static com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData.TYPE_DATA_FINISH;
import static com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData.TYPE_DATA_LOADING;
import static com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData.TYPE_DATA_SUCCESS;

public class MainActivity extends BaseMvvmActivity<ProjectViewModelSouce> {

    EditText et_search;
    TextView tv_search;
    RecyclerView rcy_project_list;

    ProjectItemAdapter projectItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectItemAdapter = new ProjectItemAdapter();

        rcy_project_list = findViewById(R.id.rcy_project_view);
        rcy_project_list.setLayoutManager(new LinearLayoutManager(this));
        rcy_project_list.setAdapter(projectItemAdapter);

        et_search = findViewById(R.id.et_search);
        tv_search = findViewById(R.id.tv_search);

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_search.getText().toString();
                getProjectList(TextUtils.isEmpty(s) ? "Google" : s
                );
            }
        });

        initLiveData();
//        getProjectList("Google");
    }

    @Override
    public Class<ProjectViewModelSouce> getViewModelClass() {
        return ProjectViewModelSouce.class;
    }

    public void initLiveData() {
        if (getmViewModel() != null) {
            getmViewModel().getProjectListLiveData().observe(this,
                    new Observer<WrapLiveData<List<Project>>>() {
                        @Override
                        public void onChanged(@Nullable WrapLiveData<List<Project>> listWrapLiveData) {
                            postLiveData(listWrapLiveData);
                        }
                    });

            getmViewModel().getProjectLiveData().observe(this,
                    new Observer<WrapLiveData<Project>>() {
                        @Override
                        public void onChanged(@Nullable WrapLiveData<Project> projectWrapLiveData) {
                            postDetailLiveData(projectWrapLiveData);
                        }
                    });
        }
    }

    public void getProjectList(String userId) {
        if (getmViewModel() != null) {
            getmViewModel().getProjectList(userId);
        }
    }

    public void getPreProjectDetail(String userId, String projectId) {
        if (getmViewModel() != null) {
            getmViewModel().getProjectDetails(userId, projectId);
        }
    }

    public void postLiveData(WrapLiveData<List<Project>> listWrapLiveData) {
        switch (listWrapLiveData.type) {
            case TYPE_DATA_SUCCESS:
                projectItemAdapter.addData(listWrapLiveData.data);
                projectItemAdapter.notifyDataSetChanged();

                Project project = listWrapLiveData.data.get(0);
                getPreProjectDetail(TextUtils.isEmpty(et_search.getText().toString()) ? "Google"
                        : et_search.getText().toString(), project.name);
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

    public void postDetailLiveData(WrapLiveData<Project> listWrapLiveData) {
        switch (listWrapLiveData.type) {
            case TYPE_DATA_SUCCESS:
                if (getmViewModel() != null) {
                    getmViewModel().setPreLoadedData(listWrapLiveData.data);
                    getmViewModel().getProjectLiveData().removeObservers(this);
                }
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

    class ProjectItemAdapter extends RecyclerView.Adapter {

        List<Project> projects;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final Project project = getItem(position);

            ((MyViewHolder) holder).name.setText(project.name);
            ((MyViewHolder) holder).languages.setText(project.language);
            ((MyViewHolder) holder).project_watchers.setText("Watchers count :" + project.watchers);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Main2Activity.toHere(MainActivity.this, TextUtils.isEmpty(et_search.getText().toString()) ? "Google"
                            : et_search.getText().toString(), project.name);
                }
            });
        }

        @Override
        public int getItemCount() {
            return projects != null ? projects.size() : 0;
        }

        public void addData(List<Project> projects) {
            if (this.projects == null)
                this.projects = new ArrayList<>();

            this.projects.clear();

            if (projects != null)
                this.projects.addAll(projects);
        }

        public Project getItem(int index) {
            if (this.projects == null)
                return null;

            if (index > projects.size())
                return null;

            return projects.get(index);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView languages;
        private TextView project_watchers;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            languages = itemView.findViewById(R.id.languages);
            project_watchers = itemView.findViewById(R.id.project_watchers);
        }
    }
}
