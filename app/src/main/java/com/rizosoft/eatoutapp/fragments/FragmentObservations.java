package com.rizosoft.eatoutapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rizosoft.eatoutapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentObservations extends Fragment {

    private String observations;
    private Context context;

    public FragmentObservations() {
        // Required empty public constructor
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static FragmentObservations newInstance(String observations, Context context) {
        FragmentObservations fragmentFirst = new FragmentObservations();
        fragmentFirst.setObservations(observations);
        fragmentFirst.setContext(context);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_observations, container, false);
        TextView tvcObservations = view.findViewById(R.id.tvcObservations);
        tvcObservations.setText(observations);
        return view;
    }

}
