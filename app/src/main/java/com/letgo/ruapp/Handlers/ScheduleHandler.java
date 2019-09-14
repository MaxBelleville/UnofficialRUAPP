package com.letgo.ruapp.Handlers;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.letgo.ruapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleHandler extends Handler {
    @BindView(R.id.webView)
    WebView webView;

    private static boolean isDone = false;
    //Todo put this all in one object.
    public static ArrayList<String> date = new ArrayList<String>();
    public static ArrayList<String> courseDate = new ArrayList<String>();
    public static ArrayList<String> timeStart = new ArrayList<String>();
    public static ArrayList<String> timeEnd = new ArrayList<String>();
    public static ArrayList<String> courseCode = new ArrayList<String>();
    public static ArrayList<String> courseName = new ArrayList<String>();
    public static ArrayList<String> prof = new ArrayList<String>();
    public static ArrayList<String> room = new ArrayList<String>();
    public static ArrayList<String> section = new ArrayList<String>();
    public static long diffHour = 0;
    public static long diffMin = 0;
    public static boolean classIsGoing = false;

    public ScheduleHandler() {
    }


    public int nextClass() {
        long closestTime = Long.MAX_VALUE;
        int closestIndx = -2;
        for (int i = 0; i < courseCode.size(); i++) {
            if (!courseCode.get(i).equals("Nothing")) {
                Calendar cal = Calendar.getInstance();
                Calendar oldCal = Calendar.getInstance();
                Calendar finalCal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMMM dd yyyy", Locale.ENGLISH);
                try {
                    oldCal.setTime(sdf.parse(courseDate.get(i)));
                    finalCal.setTime(sdf.parse(courseDate.get(i)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                oldCal.set(Calendar.HOUR, Integer.parseInt(timeStart.get(i).split(":")[0]));
                oldCal.set(Calendar.MINUTE, Integer.parseInt(timeStart.get(i).split(":")[1]));
                finalCal.set(Calendar.HOUR, Integer.parseInt(timeEnd.get(i).split(":")[0]));
                finalCal.set(Calendar.MINUTE, Integer.parseInt(timeEnd.get(i).split(":")[1]));
                if (oldCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && oldCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
                        oldCal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
                    if(closestIndx==-2)closestIndx=-1;
                    long diffMs = oldCal.getTime().getTime() - cal.getTime().getTime();
                    long diffEndMs = finalCal.getTime().getTime() - cal.getTime().getTime();
                    if (closestTime > diffEndMs) {
                        if(diffMs>0) {
                            diffHour = diffMs / (1000 * 3600); //2:30 -> 3600+3600+1800
                            diffMin = (diffMs / (1000 * 60)) % 60;
                        }
                        else if(diffEndMs>0){
                            classIsGoing=true;
                        }
                        if(diffEndMs>0){
                            closestIndx =i;
                            closestTime = diffEndMs;
                        }
                    }
                }
            }
        }
        return closestIndx;
    }

    public boolean isDone() {
        return isDone;
    }

    @Override
    public void update(Object... object) {
        if (Build.VERSION.SDK_INT >= 19) {
            view.evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    if (!s.equals("true")) {
                        String[] day = s.replace("\"", "").split(";");
                        for (int d = 0; d < day.length; d++) {
                            String[] info = day[d].split("&");
                            date.add(info[0]);
                            if (info.length > 1) {
                                for (int i = 1; i < info.length; i += 5) {
                                    String codeAndTime = info[i];
                                    String course = info[i + 1];
                                    String section = info[i + 2];
                                    String room = info[i + 3];
                                    String instructor = info[i + 4].substring(info[i + 4].indexOf(' ') + 1, info[i + 4].length());
                                    String[] splitCode = codeAndTime.split("\\(");
                                    String code = splitCode[0];
                                    String time1 = splitCode[1].substring(0, 2) + ":" + splitCode[1].substring(2, 4);
                                    String time2 = splitCode[1].substring(4, 6) + ":" + splitCode[1].substring(6, 8);
                                    courseCode.add(code);
                                    courseDate.add(info[0]);
                                    if (i != 1) date.add("Nothing");
                                    courseName.add(course);
                                    prof.add(instructor);
                                    ScheduleHandler.room.add(room);
                                    ScheduleHandler.section.add(section);
                                    timeStart.add(time1);
                                    timeEnd.add(time2);
                                }
                            } else {
                                courseDate.add(info[0]);
                                courseCode.add("Nothing");
                                courseName.add("Nothing");
                                prof.add("Nothing");
                                room.add("Nothing");
                                section.add("Nothing");
                                timeStart.add("Nothing");
                                timeEnd.add("Nothing");
                                Log.d("Special", info[0]);
                            }
                        }
                        webView.loadUrl("about:blank");
                        ScheduleHandler.isDone = true;
                    }
                }
            });
        }
    }

    public void save() {
        FileOutputStream outputStream;
        Log.d("Special", "saving");
        try {
            outputStream = context.openFileOutput("scheduleOffline", Context.MODE_PRIVATE);
            for (int i = 0; i < date.size(); i++) {
                outputStream.write(date.get(i).getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(courseDate.get(i).getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(courseName.get(i).getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(courseCode.get(i).getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(timeStart.get(i).getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(timeEnd.get(i).getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(prof.get(i).getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(room.get(i).getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(section.get(i).getBytes());
                outputStream.write("\n".getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean load(Context context) {
        try {
            FileInputStream fileInputStream = context.openFileInput("scheduleOffline");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                date.add(line);
                courseDate.add(br.readLine());
                courseName.add(br.readLine());
                courseCode.add(br.readLine());
                timeStart.add(br.readLine());
                timeEnd.add(br.readLine());
                prof.add(br.readLine());
                room.add(br.readLine());
                section.add(br.readLine());
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void logLoad(Context context) {
        String str = "";
        try {
            FileInputStream fin = context.openFileInput("scheduleOffline");
            int c;
            while ((c = fin.read()) != -1) {
                str += (char) c;
            }
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Special", str);
    }

    public ScheduleHandler(Activity activity, WebView view, String jsLoc) {
        super(activity.getApplicationContext(), view, jsLoc);
        ButterKnife.bind(this, activity);
    }


}
