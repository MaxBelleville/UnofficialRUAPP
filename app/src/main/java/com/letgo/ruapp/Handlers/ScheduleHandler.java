package com.letgo.ruapp.Handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.navigation.Navigation;

import com.letgo.ruapp.MainActivity;
import com.letgo.ruapp.R;
import com.letgo.ruapp.Requests.ScheduleRequest;
import com.letgo.ruapp.Requests.SimpleCallback;
import com.letgo.ruapp.Schedule.ScheduleObject;
import com.letgo.ruapp.Schedule.ScheduleObject.Status;
import com.letgo.ruapp.Services.RetrofitClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ScheduleHandler {

    private static ArrayList<ScheduleObject> courseObjs = new ArrayList<ScheduleObject>();

    public ArrayList<ScheduleObject> getSchedule() {
        return courseObjs;
    }
    private String readWeek;
    private String diffTime;
    private ScheduleObject nextObj;

    public Status getStatus() {
        Boolean classOnDate=false;
        for (int i =0; i<courseObjs.size();i++){
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            SimpleDateFormat daySdf = new SimpleDateFormat("EEEE, MMM dd");
            SimpleDateFormat timeSdf = new SimpleDateFormat("h:mm aa");
            try {
                Date date = daySdf.parse(courseObjs.get(i).getVal("classDate"));
                cal2.setTime(date);
                if (compareDate(cal,cal2)&&!courseObjs.get(i).getVal("classStart").isEmpty()) {
                    Log.d("Special",courseObjs.get(i).getVal("classDate"));
                    cal.set(1970,0,1);//Default date
                    Date curr=cal.getTime();
                    classOnDate=true;
                    nextObj=courseObjs.get(i);
                    Date start = timeSdf.parse(courseObjs.get(i).getVal("classStart"));
                    Date end = timeSdf.parse(courseObjs.get(i).getVal("classEnd"));
                    Log.d("Special",start.getTime()+" "+curr.getTime());
                    calcDiff(start.getTime()-curr.getTime());
                    if (start.getTime()>curr.getTime()) return Status.WAITING;
                    if (end.getTime()>curr.getTime()&&start.getTime()<=curr.getTime())  {
                        if(i+1<courseObjs.size()) {
                            start = timeSdf.parse(courseObjs.get(i+1).getVal("classStart"));
                            calcDiff(start.getTime()-curr.getTime());
                        }
                        return Status.INCLASS;
                    }
                }
            }
            catch (ParseException e) { e.printStackTrace(); }
        }
        if (classOnDate) return Status.DONE;
        return Status.NOTHING;
    }

    private boolean compareDate(Calendar currDate, Calendar classDate){
        return currDate.get(Calendar.DAY_OF_YEAR)==classDate.get(Calendar.DAY_OF_YEAR);
    }

    private void calcDiff(long diffMs){
        long diffSec = diffMs/1000;
        long min= diffSec % 60;
        long hour= diffSec / 3600;
        diffTime="";
        if (hour>0 && min>0) diffTime= hour+" Hour(s) & "+ min+ " Min(s)";
        else if (hour>0) diffTime= hour+" Hour(s)";
        else if (min>0) diffTime= min+" Min(s)";
    }

    public String getDifference(){
        return diffTime;
    }
    public ScheduleObject getNextObj(){
        return nextObj;
    }

    public void readSchedule(Context context){
        courseObjs.clear();
        ScheduleRequest request=RetrofitClient.getRetrofit().create(ScheduleRequest.class);
        Calendar cal = Calendar.getInstance();
        Calendar week = Calendar.getInstance();
        week.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        readWeek=sdf.format(week.getTime());
        Call<ResponseBody> call =request.initSchedule(
                "https://m.ryerson.ca/core_apps/schedule/index.cfm");
        call.enqueue(new SimpleCallback<ResponseBody>(){
            @Override
            public void getResponse(ResponseBody response) throws IOException {
                Document document = Jsoup.parse(response.string());
                Element elem = document.getElementsByClass("schedBrowserInformation").get(0);
                sdf.applyPattern("EEEE, MMMM dd, yyyy");
                try {
                    Date date= sdf.parse(elem.text());
                    sdf.applyPattern("yyyy-MM-dd");
                    String dateText=sdf.format(date);
                    if (date.compareTo(week.getTime())>0) readWeek=dateText;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Call<ResponseBody> call =request.getSchedule(
                        "https://m.ryerson.ca/core_apps/schedule/index.cfm",readWeek);
                call.enqueue(new SimpleCallback<ResponseBody>(){
                    @Override
                    public void getResponse(ResponseBody response) throws IOException {
                        Document document = Jsoup.parse(response.string());
                        Elements elems = document.getElementsByClass("schedBrowserInformation");
                        for (Element elem : elems){
                            try {
                                parse(elem.outerHtml());
                            }
                            catch (ParseException e) { e.printStackTrace(); }
                        }
                        save(context);
                        Intent intent = new Intent(context,MainActivity.class);
                        context.startActivity(intent);
                    }
                });
            }
        });
    }
    public void parse(String str) throws ParseException {
        String[] split=str.split("<br>");
        String[] courseInfo = Arrays.copyOfRange(split,2,split.length);
        String date = split[0].replaceAll("\\<.*?\\>", "").trim();
        SimpleDateFormat daySdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        Date d = daySdf.parse(date);
        daySdf.applyPattern("EEEE, MMM dd");
        String formattedDate= daySdf.format(d);
        String[] formatted= new String[courseInfo.length];
        for (int i =0; i<courseInfo.length;i++)
            formatted[i]=courseInfo[i].replaceAll("\\<.*?\\>", "").replaceAll("\\s"," ").trim();
        for (int i =0; i<formatted.length;i+=6){
            ScheduleObject obj = startEmpty();
            if (!formatted[i].isEmpty()) {
                String[] firstItems = formatted[i].split(" ");
                String[] time = firstItems[1].replaceAll("[()-]", " ").trim().split(" ");
                SimpleDateFormat timeSdf = new SimpleDateFormat("kk:mm");
                Date classStart = timeSdf.parse(time[0]);
                Date classEnd = timeSdf.parse(time[1]);
                timeSdf.applyPattern("h:mm aa");

                if(i==0)obj.setInfo("date", formattedDate);
                obj.setInfo("classDate", formattedDate);
                obj.setInfo("courseName", formatted[i + 1]);
                obj.setInfo("courseCode", firstItems[0]);
                obj.setInfo("classStart", timeSdf.format(classStart));
                obj.setInfo("classEnd", timeSdf.format(classEnd));
                obj.setInfo("section", formatted[i + 2]);
                obj.setInfo("location", formatted[i + 3]);
                obj.setInfo("instructor", formatted[i + 4].replace("assigned, Instructor","TA"));
            }
            else {
                obj.setInfo("date", formattedDate);
                obj.setInfo("classDate", formattedDate);
            }
            courseObjs.add(obj);
            AssigmenetHandler handler = new AssigmenetHandler();
            handler.addObject(obj);
        }

    }
    private ScheduleObject startEmpty(){
        ScheduleObject obj = new ScheduleObject();
        obj.setInfo("date", "");
        obj.setInfo("classDate", "");
        obj.setInfo("courseName", "");
        obj.setInfo("courseCode", "");
        obj.setInfo("classStart", "");
        obj.setInfo("classEnd", "");
        obj.setInfo("section", "");
        obj.setInfo("location", "");
        obj.setInfo("instructor", "");
        return obj;
    }

    public void save(Context context) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput("scheduleOffline", Context.MODE_PRIVATE);
           for (ScheduleObject objs : courseObjs) {
                outputStream.write(objs.save().getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean load(Context context) {
        courseObjs.clear();
        try {
            FileInputStream fileInputStream = context.openFileInput("scheduleOffline");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                  ScheduleObject obj= new ScheduleObject();
                  obj.setInfo("instructor",line);
                  obj.setInfo("location",br.readLine());
                  obj.setInfo("section",br.readLine());
                  obj.setInfo("courseName",br.readLine());
                  obj.setInfo("classEnd",br.readLine());
                  obj.setInfo("classDate",br.readLine());
                  obj.setInfo("date",br.readLine());
                  obj.setInfo("courseCode",br.readLine());
                  obj.setInfo("classStart",br.readLine());

                courseObjs.add(obj);
                AssigmenetHandler handler = new AssigmenetHandler();
                handler.addObject(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Log.d("Special","True");
        return true;
    }

    public int find(int pos){
    int pos2=0;
    ArrayList<ScheduleObject> course = new AssigmenetHandler().getSchedule();

    for(int i =0; i<course.size();i++){
        if(courseObjs.get(pos).getVal("courseCode")==course.get(i).getVal("courseCode"))
            pos2=i;
    }
    return pos2;
    }

    public void click(View view, int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("ID", find(pos));
        Navigation.findNavController(view).navigate(R.id.action_scheduleFragment_to_assigmentBlank,bundle);
    }
}
