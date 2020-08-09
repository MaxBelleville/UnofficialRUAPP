package com.letgo.ruapp.Assigments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.letgo.ruapp.Handlers.AssigmenetHandler;
import com.letgo.ruapp.MainActivity;
import com.letgo.ruapp.R;
import com.letgo.ruapp.Schedule.ScheduleObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AssigmentBlank extends Fragment {

    @BindView(R.id.aCourseAssigments)
    TextView titleText;

    @OnClick(R.id.aCreateNew)
    public void click(View view) {
        Bundle bundle = new Bundle();
        int pos= requireArguments().getInt("ID");
        bundle.putInt("ID",pos);
        Navigation.findNavController(view).navigate(R.id.action_assigmentBlank_to_assignmentCreate,bundle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.content_assignment_blank, container, false);
        ((MainActivity) requireActivity()).setCanGoBack(view);
        ButterKnife.bind(this,view);
        int pos= requireArguments().getInt("ID");
        ScheduleObject obj = new AssigmenetHandler().getSchedule().get(pos);
        titleText.setText(obj.getVal("courseCode")+" - Assignments");
        return view;
    }

}
