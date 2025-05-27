package com.example.task61d;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
// other imports...

public class Login extends Fragment {

    private EditText usernameEditText, passwordEditText;
    private TextView needAnAccount;
    private AppCompatButton loginButton;

    public Login() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize views
        usernameEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.submit); // using AppCompatButton
        needAnAccount = view.findViewById(R.id.needanaccount);


        DBHelper dbHelper = new DBHelper(getContext());

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                boolean valid = dbHelper.loginUser(username, password);
                if (valid) {
                    // 🔽 Retrieve and store user details
                    User loggedInUser = dbHelper.getUserDetails(username);

                    SharedPreferences sharedPref =requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("username", username); // store other user info if needed
                    editor.apply();

                    String userInterest= loggedInUser.getInterests();
                    Bundle bundle = new Bundle();
                    if( userInterest.length()<2){


                        loadFragment(new Intrests()); // Navigate to next screen
                    }else{

                        loginButton.setText("Loading...");

                        bundle.putString("user_interests", userInterest);
                        ContentDescriptionFetcher.fetch(getContext(), userInterest, new ContentDescriptionFetcher.ContentDescriptionCallback() {
                            @Override
                            public void onSuccess(String description) {
                                bundle.putString("desc", description);
                                Home homeFragment = new Home();
                                homeFragment.setArguments(bundle);
                                loginButton.setText("Login");
                                loadFragment(homeFragment); // move inside
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Log.e("ERROR", errorMessage);
                                // Optional: Navigate with default desc
                                bundle.putString("desc", "Default content");
                                Home homeFragment = new Home();
                                homeFragment.setArguments(bundle);
                                loginButton.setText("Login");
                                loadFragment(homeFragment);
                            }
                        });


                    }

                } else {
                    Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        needAnAccount.setOnClickListener(v->{
            loadFragment(new Signup());
        });

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Custom behavior, e.g. exit app or show toast
                requireActivity().finish(); // or moveTaskToBack(true);
            }
        });
    }


    private void loadFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)

                .commit();
    }
}
