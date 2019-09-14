package com.letgo.ruapp.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.letgo.ruapp.Handlers.ScheduleHandler;
import com.letgo.ruapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.viewHolder> {
    @NonNull
    @Override
    public ScheduleAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.schedule_item, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.viewHolder holder, int i) {
        holder.borderLine.setBackgroundColor(Color.parseColor("#50000000"));
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMMM dd yyyy");
        Date d = null;
        String date="";
        try {
            d = sdf.parse(ScheduleHandler.courseDate.get(i));
            sdf.applyPattern("EEEE, MMM dd");
            date= sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(ScheduleHandler.courseCode.get(i).equals("Nothing")){
            holder.dateInfo.setText(date);
            holder.courseInfo.setText("");
            holder.classInfo.setText("No courses on this day.");
            holder.profInfo.setText("");
            holder.roomInfo.setText("");
            holder.sectionInfo.setText("");
            holder.classTime.setText("");
            holder.viewAssigned.setVisibility(View.INVISIBLE);
        }
        else{
            if(!ScheduleHandler.date.get(i).equals("Nothing")) {
                holder.dateInfo.setText(date);
                holder.courseInfo.setText(ScheduleHandler.courseCode.get(i));
            }
            else {
                holder.courseInfo.setText(ScheduleHandler.courseCode.get(i));
                holder.dateInfo.setText("");
                holder.borderLine.setBackgroundColor(Color.parseColor("#1F000000"));
            }
            String startTime="";
            String endTime="";
            try {
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                startTime=new SimpleDateFormat("h:mm a").format(sdf2.parse(ScheduleHandler.timeStart.get(i)));
                endTime=new SimpleDateFormat("h:mm a").format(sdf2.parse(ScheduleHandler.timeEnd.get(i)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.classInfo.setText(ScheduleHandler.courseName.get(i));
            holder.profInfo.setText(ScheduleHandler.prof.get(i));
            holder.roomInfo.setText(ScheduleHandler.room.get(i));
            holder.sectionInfo.setText(ScheduleHandler.section.get(i));
            holder.classTime.setText(startTime+" - "+ endTime);
            holder.viewAssigned.setVisibility(View.VISIBLE);
        }
        if(i==0)holder.borderLine.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return ScheduleHandler.courseCode.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        TextView profInfo;
        TextView classInfo;
        TextView courseInfo;
        TextView dateInfo;
        TextView classTime;
        TextView roomInfo;
        TextView sectionInfo;
        TextView viewAssigned;
        View borderLine;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profInfo = itemView.findViewById(R.id.profInfo2);
            classInfo = itemView.findViewById(R.id.classInfo2);
            courseInfo = itemView.findViewById(R.id.courseCode2);
            dateInfo = itemView.findViewById(R.id.dateInfo);
            roomInfo = itemView.findViewById(R.id.roomInfo2);
            sectionInfo = itemView.findViewById(R.id.sectionInfo2);
            viewAssigned = itemView.findViewById(R.id.viewAssigned2);
            classTime =itemView.findViewById(R.id.classTime2);
            borderLine=itemView.findViewById(R.id.borderLine);
        }
    }
}