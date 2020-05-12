package com.letgo.ruapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.letgo.ruapp.Handlers.AssigmenetHandler;
import com.letgo.ruapp.Handlers.ScheduleHandler;
import com.letgo.ruapp.R;
import com.letgo.ruapp.Schedule.ScheduleObject;

public class AssigmentCourseAdapter extends RecyclerView.Adapter<AssigmentCourseAdapter.viewHolder> {
    @NonNull
    @Override
    public AssigmentCourseAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.assignment_course_item, parent, false);
        return new AssigmentCourseAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssigmentCourseAdapter.viewHolder holder, int i) {
        ScheduleObject obj = new AssigmenetHandler().getSchedule().get(i);
        holder.courseCode.setText(obj.getVal("courseCode"));
        String msg = obj.getVal("section").replace("Section:","");
        holder.courseSection.setText(msg.substring(0,msg.length()-1)+"1");
    }

    @Override
    public int getItemCount() {
        return new AssigmenetHandler().getSchedule().size();
    }


    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView courseCode;
        TextView courseSection;
        TextView assignmentTitle;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            courseCode= itemView.findViewById(R.id.aCourseCode);
            courseSection= itemView.findViewById(R.id.aCourseSection);
            assignmentTitle= itemView.findViewById(R.id.aCourseTitle);
        }

        @Override
        public void onClick(View view) {
            AssigmenetHandler handler= new AssigmenetHandler();
            handler.click(view,getAdapterPosition());
        }
    }

}