package com.letgo.ruapp.Schedule;

import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.letgo.ruapp.Adapters.ScheduleAdapter;
import com.letgo.ruapp.Handlers.ScheduleHandler;
import com.letgo.ruapp.MainActivity;
import com.letgo.ruapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ScheduleFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    public ScheduleFragment() {
        // Required empty public constructor
    }


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
