package com.letgo.ruapp.Schedule;

import java.util.HashMap;

public class ScheduleObject {
    private HashMap<String, String> courseInfo = new HashMap<String, String>();
    public enum Status {
        INCLASS, NOTHING, DONE, WAITING
    }

    public String getVal(String key){
        return courseInfo.get(key);
    }

    public void setInfo(String key, String val){
        courseInfo.put(key,val);
    }

    public String save(){
        String collectedValues="";
        for (String  val: courseInfo.values())
            collectedValues += val + "\n";
        return collectedValues;
    }
}
