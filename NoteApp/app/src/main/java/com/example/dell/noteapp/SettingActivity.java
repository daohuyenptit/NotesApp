package com.example.dell.noteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    ToggleButton toggleButton;
    EditText edtPin;
    boolean check = false;
    public SharedPreferences sharedPreferences;
    String myShared = "Share";
    Toolbar toolbar;
    String pincode;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView txtlock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        addControl();
        ActionBar();
        toggleButton.setOnClickListener(SettingActivity.this);
        sharedPreferences = getSharedPreferences(myShared, MODE_PRIVATE);
    }

    public void addControl() {
        toggleButton = findViewById(R.id.toggle);
        toolbar = findViewById(R.id.toobar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        txtlock = findViewById(R.id.txtlock);
        txtlock.setOnClickListener(this);


    }

    private void ActionBar() {
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (toggleButton.isChecked()) {
            setLock();


        } else if (!toggleButton.isChecked()) {
            Utils.savePinCode(SettingActivity.this, "savePin", "");
        }

    }

    public void setLock() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("Enter PIN code");
        final EditText input = new EditText(SettingActivity.this);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Utils.savePinCode(SettingActivity.this, "savePin", input.getText().toString());
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_lock:
                String pincode = Utils.getPinCode(SettingActivity.this);
                Utils.setPinCode(SettingActivity.this, pincode);

                break;
            case R.id.nav_home:
                startActivity(new Intent(this, NoteListActivity.class));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.txtlock:
                setLock();
                break;

        }
        drawerLayout.closeDrawers();
        return true;
    }

}
