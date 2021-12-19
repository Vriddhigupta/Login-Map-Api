package com.example.smartbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText passworduser,emailuser,userid;
    Button signup;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userid = findViewById(R.id.userid);
        passworduser = findViewById(R.id.edtPassword);
        emailuser = findViewById(R.id.edtEmail);
        signup = findViewById(R.id.btnRegister);
        back = findViewById(R.id.btnBack);

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String id = userid.getText().toString();
                String email = emailuser.getText().toString();
                String pass = passworduser.getText().toString();

                if(email.equals("")||pass.equals("")||id.equals(""))
                {
                    Toast.makeText(RegisterActivity.this,"Please enter all the fields",Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    Boolean checkcomp = DB.checkusername(user);
//                    if(checkcomp == false)
//                    {
//                        Boolean checkmail = DB.checkemail(useremail);
//                        if(checkmail==false)
//                        {
//                            Boolean insert = DB.insertData(user,useremail,userpass);
//                            if(insert == true)
//                            {
//                                Toast.makeText(sign_up.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(sign_up.this,login_activity.class);
//                                startActivity(intent);
//                            }
//                            else
//                                Toast.makeText(sign_up.this,"Registration failed",Toast.LENGTH_SHORT).show();
//
//                        }
//                        else
//                        {
//                            Toast.makeText(sign_up.this,"User email already exists",Toast.LENGTH_SHORT).show();
//                        }
//              }
//                    else
//                    {
//                        Toast.makeText(RegisterActivity.this,"Username already exists",Toast.LENGTH_SHORT).show();
//                    }
                    RegisterRequest registerRequest = new RegisterRequest(emailuser.getText().toString(),passworduser.getText().toString(),Integer.parseInt(userid.getText().toString()));
//                    registerRequest.setEmail(emailuser.getText().toString());
//                    registerRequest.setPassword(passworduser.getText().toString());
//                    registerRequest.setUserid(Integer.parseInt(userid.getText().toString()));
                    registerUser(registerRequest);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
    public void registerUser(RegisterRequest registerRequest)
    {
        Call<RegisterResponse> registerResponseCall = LoginApi.getService().registerUsers(registerRequest);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if(response.isSuccessful())
                {
                    String message = "Registered successfully";
                    Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    finish();
                }
                else
                {
                    String message = "Unable to register. An error occured";
                    Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();

            }
        });

    }
}