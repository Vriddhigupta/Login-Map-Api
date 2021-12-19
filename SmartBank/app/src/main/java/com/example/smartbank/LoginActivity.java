package com.example.smartbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static String PREFS_NAME = "MyPrefsFile";
    EditText user_email,user_password;
    Button login;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_email = findViewById(R.id.edtEmaillogin);
        user_password = findViewById(R.id.edtPasslogin);
        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email.getText().toString();
                String password = user_password.getText().toString();

                if(email.equals("")||password.equals(""))
                {
                    Toast.makeText(LoginActivity.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    Boolean checkuserpass = DB.checkcredentials(email,password);
//                    Boolean checkuserpass2 = DB2.checkusernamepassword(email,password);
//                    if(checkuserpass==true||checkuserpass2==true)
//                    {
//                        SharedPreferences sharedPreferences = getSharedPreferences(login_activity.PREFS_NAME,0);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean("hasLoggedIn",true);
//                        editor.commit();
//                        Toast.makeText(login_activity.this,"Sign in successful",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(login_activity.this,dashboard.class);
//                        startActivity(intent);
//                    }
//                    else
//                    {
//                        Toast.makeText(LoginActivity.this,"Invalid credentials",Toast.LENGTH_SHORT).show();
//                    }
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setEmail(user_email.getText().toString());
                    loginRequest.setPassword(user_password.getText().toString());
                    loginUser(loginRequest);
                }
            }
        });

        signup = findViewById(R.id.btnSignUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    public void loginUser(LoginRequest loginRequest)
    {
        Call<LoginResponse> loginResponseCall = LoginApi.getService().loginuser(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful())
                {
                    LoginResponse loginResponse = response.body();
                    String message = "Logged in";
                    Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this,MapsActivity.class).putExtra("data",loginResponse));
                    finish();
                }
                else
                {
                    String message = "Unable to login. An error occured";
                    Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });
    }
}