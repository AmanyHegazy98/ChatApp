package com.amany.chat.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amany.chat.ui.Adapter.GroupsAdapter;
import com.amany.chat.R;



public class GroupsFragment extends Fragment {
    private GroupsAdapter groupsAdapter;
    private RecyclerView groupRecycleView;
    public GroupsFragment() { }

    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_groups,container,false);
        groupRecycleView=rootView.findViewById(R.id.recycler_view_groups);
        groupRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        groupsAdapter=new GroupsAdapter();
        groupRecycleView.setAdapter(groupsAdapter);


        return rootView;
    }
}