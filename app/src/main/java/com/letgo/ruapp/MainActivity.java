package com.letgo.ruapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.letgo.ruapp.Adapters.ScheduleAdapter;
import com.letgo.ruapp.Handlers.ScheduleHandler;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.courseCode) TextView courseCode;

    @BindView(R.id.classInfo) TextView classInfo;
    @BindView(R.id.roomInfo) TextView roomInfo;
    @BindView(R.id.sectionInfo) TextView sectionInfo;
    @BindView(R.id.profInfo) TextView profInfo;
    @BindView(R.id.classTime) TextView classTime;
    @BindView(R.id.viewAssigned) TextView viewAssigned;
    @BindView(R.id.timeTillNext) TextView timeTillNext;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateNextClass();
        handleRecylcerView();
    }
    private void handleRecylcerView(){
       LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
        recyclerView.setAdapter(scheduleAdapter);


    }
    private void updateNextClass(){
        int c=ScheduleHandler.nextClass();
        //TODO: make C a enum.
        //-2 means not today, -1 means all clases are finished.
                if(c!=-2&&c!=-1){
                    String startTime="";
                    String endTime="";
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        startTime=new SimpleDateFormat("h:mm a").format(sdf.parse(ScheduleHandler.timeStart.get(c)));
                        endTime=new SimpleDateFormat("h:mm a").format(sdf.parse(ScheduleHandler.timeEnd.get(c)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    courseCode.setText(ScheduleHandler.courseCode.get(c));
                    classInfo.setText(ScheduleHandler.courseName.get(c));
                    profInfo.setText(ScheduleHandler.prof.get(c));
                    classTime.setText(startTime+" -"+endTime);
                    roomInfo.setText(ScheduleHandler.room.get(c));
                    sectionInfo.setText(ScheduleHandler.section.get(c));
                    if(ScheduleHandler.diffHour>0&&ScheduleHandler.diffMin>0)
                        timeTillNext.setText("In " +ScheduleHandler.diffHour+ " Hour(s) & "+ScheduleHandler.diffMin+" Min(s)");
                    else if(ScheduleHandler.diffHour>0)
                        timeTillNext.setText("In " +ScheduleHandler.diffHour+ " Hour(s)");
                    else if(ScheduleHandler.diffMin>0)
                        timeTillNext.setText("In " +ScheduleHandler.diffMin+ " Minute(s)");
                    viewAssigned.setVisibility(View.VISIBLE);
                }
                else if(c!=-2) {
                    courseCode.setText("All done for the day.");
                    classInfo.setText("No more classes, finish up at leave. Congrats!");
                }
            }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
