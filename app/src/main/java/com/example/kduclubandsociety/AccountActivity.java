package com.example.kduclubandsociety;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signoutbutton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        findViewById(R.id.signOutButton).setOnClickListener(this);

    }
    private void signOut() {
        mAuth.signOut();

    }

    @Override
    public void onClick(View view) {
        signOut();
    }
}
