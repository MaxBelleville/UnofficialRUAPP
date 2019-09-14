package com.letgo.ruapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.letgo.ruapp.Adapters.ScheduleAdapter;
import com.letgo.ruapp.Handlers.ScheduleHandler;


import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
    private String startTime="";
    private String endTime="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateNextClass();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateNextClass();
                    }
                });
            }
        },0,5000);
        handleRecylcerView();
    }
    private void handleRecylcerView(){
       LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
        recyclerView.setAdapter(scheduleAdapter);


    }
    private void updateNextClass(){
        int c=new ScheduleHandler().nextClass();
        //TODO: make C a enum.
        //-2 means not today, -1 means all clases are finished.
                if(c!=-2&&c!=-1){
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        startTime = new SimpleDateFormat("h:mm a").format(sdf.parse(ScheduleHandler.timeStart.get(c)));
                        endTime = new SimpleDateFormat("h:mm a").format(sdf.parse(ScheduleHandler.timeEnd.get(c)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(!ScheduleHandler.classIsGoing) {
                            if (ScheduleHandler.diffHour > 0 && ScheduleHandler.diffMin > 0)
                                timeTillNext.setText("In " + ScheduleHandler.diffHour + " Hour(s) & " + ScheduleHandler.diffMin + " Min(s)");
                            else if (ScheduleHandler.diffHour > 0)
                                timeTillNext.setText("In " + ScheduleHandler.diffHour + " Hour(s)");
                            else if (ScheduleHandler.diffMin > 0)
                                timeTillNext.setText("In " + ScheduleHandler.diffMin + " Minute(s)");
                    }
                    else {
                        timeTillNext.setText("Currently");
                    }
                    sectionInfo.setText(ScheduleHandler.section.get(c));
                    courseCode.setText(ScheduleHandler.courseCode.get(c));
                    classInfo.setText(ScheduleHandler.courseName.get(c));
                    profInfo.setText(ScheduleHandler.prof.get(c));
                    classTime.setText(startTime+" - "+endTime);
                    roomInfo.setText(ScheduleHandler.room.get(c));
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
