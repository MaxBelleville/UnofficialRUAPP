package com.letgo.ruapp.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letgo.ruapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @OnClick(R.id.Schedule)
    public void click(View v){
        Navigation.findNavController(v).navigate(HomeFragmentDirections.actionHomeFragmentToScheduleFragment());
    }
    @OnClick(R.id.Assigments)
    public void click2(View v){
        Navigation.findNavController(v).navigate(HomeFragmentDirections.actionHomeFragmentToAssigmentsFragment());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.content_home, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

}
