package com.letgo.ruapp.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.letgo.ruapp.Adapters.ScheduleAdapter;
import com.letgo.ruapp.MainActivity;
import com.letgo.ruapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ScheduleFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_schedule, container, false);
        ButterKnife.bind(this, view);
        handleRecylcerView();
        ((MainActivity) requireActivity()).setCanGoBack(view);
        return view;
    }
    private void handleRecylcerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
        recyclerView.setAdapter(scheduleAdapter);
    }

}
