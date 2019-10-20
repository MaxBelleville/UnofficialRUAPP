package com.letgo.ruapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.letgo.ruapp.Handlers.ScheduleHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private String startTime = "";
    private String endTime = "";
    private View view;
    protected NotificationCompat.Builder builder;
    protected NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        builder = new NotificationCompat.Builder(this, "My Channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Next Class Reminder")
                .setContentText("You have class in x minutes")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager = NotificationManagerCompat.from(this);
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
        }, 0, 5000);
    }
    public void setCanGoBack(View v){
        view=v;
    }
    private void updateNextClass() {
        int c = new ScheduleHandler().nextClass();
        //TODO: make C a enum.
        //-2 means not today, -1 means all clases are finished.
        if (c != -2 && c != -1) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.CANADA);
                startTime = new SimpleDateFormat("h:mm a",Locale.CANADA).format(Objects.requireNonNull(sdf.parse(ScheduleHandler.timeStart.get(c))));
                endTime = new SimpleDateFormat("h:mm a", Locale.CANADA).format(Objects.requireNonNull(sdf.parse(ScheduleHandler.timeEnd.get(c))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String whenIsNext="";
            if (ScheduleHandler.diffHour > 0 && ScheduleHandler.diffMin > 0)
                whenIsNext =("In " + ScheduleHandler.diffHour + " Hour(s) & " + ScheduleHandler.diffMin + " Min(s)");
            else if (ScheduleHandler.diffHour > 0)
                whenIsNext =("In " + ScheduleHandler.diffHour + " Hour(s)");
            else if (ScheduleHandler.diffMin > 0)
                whenIsNext =("In " + ScheduleHandler.diffMin + " Minute(s)");
            if (ScheduleHandler.shouldBeInClass) {
                timeTillNext.setText("Currently");
                nextClass.setText(("Next Class: "+whenIsNext));
            }
            else {
                timeTillNext.setText(whenIsNext);
                nextClass.setText("");
                builder.setContentText(timeTillNext.getText() + " - Loc: " + ScheduleHandler.room.get(c));
                notificationManager.notify(1, builder.build());
            }
            sectionInfo.setText(ScheduleHandler.section.get(c));
            courseCode.setText(ScheduleHandler.courseCode.get(c));
            classInfo.setText(ScheduleHandler.courseName.get(c));
            profInfo.setText(ScheduleHandler.prof.get(c));
            classTime.setText((startTime + " - " + endTime));
            roomInfo.setText(ScheduleHandler.room.get(c));
            viewAssigned.setVisibility(View.VISIBLE);
            showNextClass.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,134));
        } else if (c != -2) {
            notificationManager.cancel(1);
            courseCode.setText("All done for the day.");
            classInfo.setText("No more classes, finish up at leave. Congrats!");
            showNextClass.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,90));
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
