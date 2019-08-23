package com.letgo.ruapp.Adapters;

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
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMMM dd yyyy");
        Date d = null;
        try {
            d = sdf.parse(ScheduleHandler.courseDate.get(i));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern("EEEE, MMM dd");
        String date = sdf.format(d);
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
            holder.dateInfo.setText(date);
            holder.courseInfo.setText(ScheduleHandler.courseCode.get(i));
            holder.classInfo.setText(ScheduleHandler.courseName.get(i));
            holder.profInfo.setText(ScheduleHandler.prof.get(i));
            holder.roomInfo.setText(ScheduleHandler.room.get(i));
            holder.sectionInfo.setText(ScheduleHandler.section.get(i));
            holder.classTime.setText(ScheduleHandler.timeStart.get(i)+" - "+ ScheduleHandler.timeEnd.get(i));
            holder.viewAssigned.setVisibility(View.VISIBLE);
        }
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
        }
    }
}