package com.frarisstudio.lavapiatti.system.drawmenu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.frarisstudio.lavapiatti.Home;
import com.frarisstudio.lavapiatti.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileF#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileF extends Fragment {

    private String uid;
    private FirebaseAuth mAuth;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileF() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileF.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileF newInstance(String param1, String param2) {
        ProfileF fragment = new ProfileF();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //txtName = getView().findViewById(R.id.txtNameProfile);
        // Inflate the layout for this fragment
        EditText txtName;

        View v = inflater.inflate(R.layout.activity_home, container, false);
        txtName = (EditText) v.findViewById(R.id.txtNameProfile);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        txtName.setText(uid);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}