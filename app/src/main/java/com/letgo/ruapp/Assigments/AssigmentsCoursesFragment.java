package com.letgo.ruapp.Assigments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letgo.ruapp.Adapters.AssigmentCourseAdapter;
import com.letgo.ruapp.MainActivity;
import com.letgo.ruapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AssigmentsCoursesFragment extends Fragment {
    @BindView(R.id.recyclerViewCourses) RecyclerView recyclerView;

    @OnClick(R.id.d2lview)
    public void click(View view){
        Bundle bundle = new Bundle();
        bundle.putString("Url","https://courses.ryerson.ca/d2l/home");
        Navigation.findNavController(view).navigate(R.id.action_assigmentsCourses_to_webActivity,bundle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.content_assignments_courses, container, false);
        ((MainActivity) requireActivity()).setCanGoBack(view);
        ButterKnife.bind(this,view);
        createRecyclerView();
        return view;
    }
    private void createRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        AssigmentCourseAdapter assigmentCourseAdapter = new AssigmentCourseAdapter();
        recyclerView.setAdapter(assigmentCourseAdapter);
    }

}
