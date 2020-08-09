package com.letgo.ruapp.Assigments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.letgo.ruapp.Handlers.AssigmenetHandler;
import com.letgo.ruapp.MainActivity;
import com.letgo.ruapp.R;
import com.letgo.ruapp.Schedule.ScheduleObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class AssignmentCreate extends Fragment implements TextWatcher {

    @BindView(R.id.aCourseAssigments2)
    TextView titleText;

    @BindView(R.id.editTextDescription)
    EditText desc;


    @BindView(R.id.editTextToDo)
    EditText todo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.content_assignment_create, container, false);
        ((MainActivity) requireActivity()).setCanGoBack(view);
        ButterKnife.bind(this,view);
        int pos= requireArguments().getInt("ID");
        ScheduleObject obj = new AssigmenetHandler().getSchedule().get(pos);
        titleText.setText(obj.getVal("courseCode")+" - New Assignment");
        desc.addTextChangedListener(this);
        todo.addTextChangedListener(this);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(desc.getLayout().getLineCount()>desc.getMaxLines()){
            desc.getText().delete(desc.getText().length()-1,desc.getText().length());
        }
        if(todo.getLayout().getLineCount()>todo.getMaxLines()){
            todo.getText().delete(todo.getText().length()-1,todo.getText().length());
        }
    }
}