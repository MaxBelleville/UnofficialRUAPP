package com.letgo.ruapp.Assigments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.letgo.ruapp.Adapters.AssigmentCourseAdapter;
import com.letgo.ruapp.Adapters.AssigmentListAdapter;
import com.letgo.ruapp.MainActivity;
import com.letgo.ruapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssigmentsList extends Fragment {
    @BindView(R.id.recyclerViewList)
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.content_assignments_list, container, false);
        ((MainActivity) requireActivity()).setCanGoBack(view);
        ButterKnife.bind(this,view);
        createRecyclerView();
        return view;
    }

    private void createRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        AssigmentListAdapter assigmentListAdapter = new AssigmentListAdapter();
        recyclerView.setAdapter(assigmentListAdapter);
        if(recyclerView.getChildCount()==0) {
            int pos = requireArguments().getInt("ID");
            Bundle bundle = new Bundle();
            bundle.putInt("ID",pos);
            Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment)).popBackStack(R.id.homeFragment,false);
            Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.action_homeFragment_to_assigmentBlank,bundle);
        }
    }
}
