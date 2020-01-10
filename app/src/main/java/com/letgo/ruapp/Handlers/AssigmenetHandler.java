package com.letgo.ruapp.Handlers;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import com.letgo.ruapp.R;
import com.letgo.ruapp.Schedule.ScheduleObject;

import java.util.ArrayList;


public class AssigmenetHandler {
    private static ArrayList<ScheduleObject> assignments = new ArrayList<ScheduleObject>();
    public AssigmenetHandler(){}
    public ArrayList<ScheduleObject> getSchedule() {
        return assignments;
    }

    public void addObject(ScheduleObject scheduleObject) {
        boolean found=false;
        for (ScheduleObject obj: assignments){
            if (obj.getVal("courseCode")==scheduleObject.getVal("courseCode"))
                    found=true;
        }
        if(!found) {
            ScheduleObject schedule = new ScheduleObject();
            schedule.setInfo("section", scheduleObject.getVal("section"));
            schedule.setInfo("courseCode", scheduleObject.getVal("courseCode"));
            assignments.add(schedule);
        }
    }

    public void click(View view, int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("ID", pos);
        Navigation.findNavController(view).navigate(R.id.action_assigmentsCourses_to_assigmentBlank,bundle);
    }
}
