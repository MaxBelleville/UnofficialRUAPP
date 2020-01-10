package com.letgo.ruapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import com.letgo.ruapp.Handlers.ScheduleHandler;
import com.letgo.ruapp.Schedule.ScheduleObject;
import com.letgo.ruapp.Schedule.ScheduleObject.Status;
import com.letgo.ruapp.Services.NotificationService;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.courseCode)
    TextView courseCode;
    @BindView(R.id.classInfo)
    TextView classInfo;
    @BindView(R.id.roomInfo)
    TextView roomInfo;
    @BindView(R.id.sectionInfo)
    TextView sectionInfo;
    @BindView(R.id.profInfo)
    TextView profInfo;
    @BindView(R.id.classTime)
    TextView classTime;
    @BindView(R.id.viewAssigned)
    TextView viewAssigned;
    @BindView(R.id.timeTillClass)
    TextView timeTillNext;
    @BindView(R.id.timeTillNext)
    TextView nextClass;
    @BindView(R.id.showNextClass)
    ConstraintLayout showNextClass;
    private int c=0;
    @OnClick(R.id.showNextClass)
    void click(View v){
        ScheduleHandler handler=new ScheduleHandler();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", handler.find(c));
        if(viewAssigned.getVisibility()==View.VISIBLE){
            Navigation.findNavController(findViewById(R.id.nav_host_fragment)).popBackStack(R.id.homeFragment,false);
            Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigate(R.id.action_homeFragment_to_assigmentBlank,bundle);
        }

    }
    private String startTime = "";
    private String endTime = "";
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i= new Intent(this, NotificationService.class);
        startService(i);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
       // updateNextClass();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // updateNextClass();
                    }
                });
            }
        }, 0, 5000);
    }
    public void setCanGoBack(View v){
        view=v;
    }
    private void updateNextClass() {
        ScheduleHandler handler=new ScheduleHandler();
        Status status = handler.getStatus();
        ScheduleObject obj=handler.getNextObj();
        if (status==Status.INCLASS || status==Status.WAITING){
            if (status==Status.INCLASS) {
                timeTillNext.setText("Currently");
                nextClass.setText(("Next class: "+handler.getDifference()));
                if(!handler.getDifference().isEmpty())nextClass.setVisibility(View.VISIBLE);
            }
            else {
                timeTillNext.setText("In: "+handler.getDifference());
                nextClass.setText("");
                nextClass.setVisibility(View.GONE);
            }
            sectionInfo.setText(obj.getVal("section"));
            courseCode.setText(obj.getVal("courseCode"));
            classInfo.setText(obj.getVal("courseName"));
            profInfo.setText(obj.getVal("instructor"));
            classTime.setText((obj.getVal("classStart") + " - " + obj.getVal("classEnd")));
            roomInfo.setText(obj.getVal("location"));
            viewAssigned.setVisibility(View.VISIBLE);
            profInfo.setVisibility(View.VISIBLE);
            roomInfo.setVisibility(View.VISIBLE);
            classTime.setVisibility(View.VISIBLE);
            sectionInfo.setVisibility(View.VISIBLE);
        }
        if (status==Status.DONE){
            courseCode.setText("All done for the day.");
            classInfo.setText("No more classes, finish up at leave. Congrats!");
            viewAssigned.setVisibility(View.GONE);
            profInfo.setVisibility(View.GONE);
            roomInfo.setVisibility(View.GONE);
            classTime.setVisibility(View.GONE);
            sectionInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if(view== null)
        this.moveTaskToBack(true);
        else {
        Navigation.findNavController(view).popBackStack();
        view=null;
        }
    }
}
