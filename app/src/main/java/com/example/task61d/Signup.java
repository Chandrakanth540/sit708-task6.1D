package com.example.task61d;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Signup extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Signup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Signup.
     */
    // TODO: Rename and change types and number of parameters
    public static Signup newInstance(String param1, String param2) {
        Signup fragment = new Signup();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DBHelper dbhelper=new DBHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // Initialize views
        EditText username = view.findViewById(R.id.username);
        EditText email = view.findViewById(R.id.email);
        EditText confirmEmail = view.findViewById(R.id.confirm_email);
        EditText phone = view.findViewById(R.id.phone);
        EditText password = view.findViewById(R.id.password);
        EditText confirmPassword = view.findViewById(R.id.confirm_password);
        AppCompatButton signupBtn = view.findViewById(R.id.signup_submit);

        // Set onClickListener for the signup button
        signupBtn.setOnClickListener(v -> {
            String usernameText = username.getText().toString().trim();
            String emailText = email.getText().toString().trim();
            String confirmEmailText = confirmEmail.getText().toString().trim();
            String phoneText = phone.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            String confirmPasswordText = confirmPassword.getText().toString().trim();

            // Basic validation
            if (usernameText.isEmpty() || emailText.isEmpty() || confirmEmailText.isEmpty() ||
                    phoneText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!emailText.equals(confirmEmailText)) {
                Toast.makeText(getContext(), "Emails do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                Toast.makeText(getContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passwordText.length() < 6) {
                Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phoneText.matches("\\d{10}")) {
                Toast.makeText(getContext(), "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            // If all validations pass

            Toast.makeText(getContext(), "Signup successful!", Toast.LENGTH_SHORT).show();
            boolean registerId=dbhelper.registerUser(usernameText,emailText,passwordText,phoneText,"");
            if (registerId){
                loadFragment(new Login());
            }else{
                Toast.makeText(getContext(),"Signup Failed",Toast.LENGTH_SHORT).show();
            }



            // You can now proceed to save the user data or navigate to another fragment/activity
        });

        return view;
    }
    private void loadFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }

}