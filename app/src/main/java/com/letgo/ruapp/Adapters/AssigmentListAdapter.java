package com.letgo.ruapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.letgo.ruapp.Handlers.AssigmenetHandler;
import com.letgo.ruapp.R;
import com.letgo.ruapp.Schedule.ScheduleObject;

public class AssigmentListAdapter extends RecyclerView.Adapter<AssigmentListAdapter.viewHolder> {
    @NonNull
    @Override
    public AssigmentListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.assignment_list_item, parent, false);
        return new AssigmentListAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssigmentListAdapter.viewHolder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class viewHolder extends RecyclerView.ViewHolder {

        public viewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}