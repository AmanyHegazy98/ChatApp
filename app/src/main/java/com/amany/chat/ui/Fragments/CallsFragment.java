package com.amany.chat.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amany.chat.data.model.UserModel;
import com.amany.chat.ui.Adapter.CallAdapter;
import com.amany.chat.R;

import java.util.List;


public class CallsFragment extends Fragment {
    private CallAdapter callAdapter;
    private RecyclerView recyclerView;
    private List<UserModel> userModelList;


    public CallsFragment() {}


    public static CallsFragment newInstance() {
        CallsFragment fragment = new CallsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_calls, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_calls);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        callAdapter=new CallAdapter(getContext(),userModelList);
        recyclerView.setAdapter(callAdapter);


        return rootView;
    }
}