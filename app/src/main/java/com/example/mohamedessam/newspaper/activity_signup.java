package com.example.mohamedessam.newspaper;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class activity_signup extends AppCompatActivity {
    EditText name ,email , pass , passagain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = (EditText)findViewById(R.id.user_name);
        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);
        passagain = (EditText)findViewById(R.id.passagain);
    }

    public void signup(View view) {
        final String uname;
         String uemail , upass = null;
        if(name.getText().toString().isEmpty() && pass.getText().toString().isEmpty() && passagain.getText().toString().isEmpty() && email.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Complete Enter data",Toast.LENGTH_LONG).show();
        }else
        {
            if(pass.getText().toString().equals(passagain.getText().toString()))
            {
                upass = email.getText().toString();
            }else
            {
                Toast.makeText(this,"Password isn't match",Toast.LENGTH_LONG).show();
            }
            uname = name.getText().toString();
            uemail = email.getText().toString();

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(uemail,upass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        task.getResult().getUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(uname).build());
                        Intent i = new Intent(getBaseContext(), Show_news.class);
                        startActivity(i);

                    }else
                    {
                        Toast.makeText(getBaseContext(),task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name",name.getText().toString());
        outState.putString("pass",pass.getText().toString());
        outState.putString("passagain",passagain.getText().toString());
        outState.putString("email",email.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if(savedInstanceState != null)
        {
            name.setText(savedInstanceState.getString("name"));
            email.setText(savedInstanceState.getString("email"));
            pass.setText(savedInstanceState.getString("pass"));
            passagain.setText(savedInstanceState.getString("passagain"));
        }
    }
}
