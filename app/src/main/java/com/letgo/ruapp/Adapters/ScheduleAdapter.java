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
import com.letgo.ruapp.Schedule.ScheduleObject;

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
        if (i!=0)holder.borderLine.setBackgroundColor(Color.parseColor("#50000000"));
        ScheduleObject obj = new ScheduleHandler().getSchedule().get(i);
        holder.dateInfo.setText(obj.getVal("date"));
        holder.courseInfo.setText(obj.getVal("courseCode"));
        holder.classInfo.setText(obj.getVal("courseName"));
        holder.profInfo.setText(obj.getVal("instructor"));
        holder.roomInfo.setText(obj.getVal("location"));
        holder.sectionInfo.setText(obj.getVal("section"));
        holder.classTime.setText(obj.getVal("classStart")+" - "+ obj.getVal("classEnd"));
        holder.viewAssigned.setVisibility(View.VISIBLE);
        if (obj.getVal("courseName").isEmpty()) {
            holder.classTime.setText("");
            holder.viewAssigned.setVisibility(View.INVISIBLE);
            holder.classInfo.setText("No classes on this date.");
        }
        if (obj.getVal("date").isEmpty()) holder.borderLine.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
       return new ScheduleHandler().getSchedule().size();
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            itemView.setOnClickListener(this);
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

        @Override
        public void onClick(View view) {
            ScheduleHandler handler= new ScheduleHandler();
            handler.click(view,getAdapterPosition());
        }
    }
}