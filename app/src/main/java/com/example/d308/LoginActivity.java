package com.example.d308;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.example.d308.dao.UserDao;
import com.example.d308.database.AppDatabase;
import com.example.d308.entities.User;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private Button signIn;
    private Button signUp;
    private TextView welcomeText;
    private UserDao userDao;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        userDao = db.userDao();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        welcomeText = findViewById(R.id.welcomeText);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckUserTask().execute(username.getText().toString(), password.getText().toString());
            }
        });
        final Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
                confirmPassword.setText("");
                confirmPassword.setVisibility(View.GONE);
                welcomeText.setText("Welcome to Vacation Tracker! Please sign in.");
                signIn.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.VISIBLE);
                findViewById(R.id.confirm).setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
                confirmPassword.setText("");
                confirmPassword.setVisibility(View.VISIBLE);
                welcomeText.setText("Create an Account");
                signIn.setVisibility(View.GONE);
                signUp.setVisibility(View.GONE);
                findViewById(R.id.confirm).setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordText = password.getText().toString();
                String confirmPasswordText = confirmPassword.getText().toString();
                if (!passwordText.equals(confirmPasswordText)) {
                    Toast.makeText(LoginActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    new RegisterUserTask().execute(new User(username.getText().toString(), passwordText));
                }
            }
        });
        final Button confirmButton = findViewById(R.id.confirm);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                confirmButton.setEnabled(!username.getText().toString().trim().isEmpty() &&
                        !password.getText().toString().trim().isEmpty() &&
                        !confirmPassword.getText().toString().trim().isEmpty());
            }
        };

        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        confirmPassword.addTextChangedListener(textWatcher);
    }

    private class CheckUserTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            return userDao.checkUser(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(User user) {
            if(user != null){
                // Navigate to MainActivity if the user is valid
                MainActivity.currentUsername = user.getUsername();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class RegisterUserTask extends AsyncTask<User, Void, Boolean> {
        @Override
        protected Boolean doInBackground(User... users) {
            User existingUser = userDao.getUserByUsername(users[0].getUsername());
            if (existingUser != null) {
                // A user with the entered username already exists
                return false;
            } else {
                userDao.registerUser(users[0]);
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(LoginActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                username.setText("");
                password.setText("");
                confirmPassword.setText("");
                confirmPassword.setVisibility(View.GONE);
                welcomeText.setText("Welcome to Vacation Tracker! Please sign in.");
                signIn.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.VISIBLE);
                findViewById(R.id.confirm).setVisibility(View.GONE);
            } else {
                Toast.makeText(LoginActivity.this, "A user with this username already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }
}