package com.example.task61d;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        }
    }
    private void loadFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null) // Add this line
                .commit();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        User currentUser = User.getCurrentUser();
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView description = view.findViewById(R.id.desc);
        AppCompatButton historyBtn=view.findViewById(R.id.historyBtn);
        AppCompatButton upgradeBtn=view.findViewById(R.id.upgradeBtn);
        ImageView profile =view.findViewById(R.id.profile_icon);
        profile.setOnClickListener(v->{
            loadFragment(new Profile());

        });
historyBtn.setOnClickListener(v->{
    loadFragment(new History());
});
upgradeBtn.setOnClickListener(v->{
    Intent intent =new Intent(getContext(),CheckoutActivity.class);
    startActivity(intent);
});
        if (bundle != null && bundle.containsKey("desc")) {
            String desc = bundle.getString("desc");
            Log.d("TAG", "onCreateView: " + desc); // Moved inside
            desc=desc.replace("*","");
            desc = desc.split("\n\n")[1];
            description.setText(desc);

        } else {
            Toast.makeText(getContext(), "Not found ", Toast.LENGTH_SHORT).show();
        }

        TextView name = view.findViewById(R.id.name);
        name.setText(currentUser.getUsername());

        LinearLayout linearLayout = view.findViewById(R.id.generated_task_card);
        linearLayout.setOnClickListener(v -> {
            Bundle b = getArguments();
            if (b == null) b = new Bundle(); // Defensive coding

            b.putString("user_interests", currentUser.getInterests());
            b.putString("desc",description.getText().toString());
            Questions questionFragment = new Questions();
            questionFragment.setArguments(b);

            loadFragment(questionFragment);
        });


        return view;
    }




}
