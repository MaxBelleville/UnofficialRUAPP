package com.letgo.ruapp.Assigments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letgo.ruapp.MainActivity;
import com.letgo.ruapp.R;


public class AssigmentFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.content_assigment, container, false);
        ((MainActivity) requireActivity()).setCanGoBack(view);
        return view;
    }

}
