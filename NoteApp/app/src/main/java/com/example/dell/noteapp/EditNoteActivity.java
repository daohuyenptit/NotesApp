package com.example.dell.noteapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener, com.bumptech.glide.module.GlideModule {
    NoteController controller;
    ImageView imgSelect, imgdelete;
    ImageView imgcamera, imggallery;
    EditText edtDate, edtTime, edtContent;
    Note note;
    int REQUEST_CODE = 20;
    int REQUEST_CODE_GALLERY = 21;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView txtlock;
    String newpath, oldpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        addControl();
        ActionBar();
        checkPermission();
        getNote();
        datePicker();
        timePicker();
        imgdelete.setOnClickListener(this);
        imgSelect.setOnClickListener(this);
        controller = new NoteController(EditNoteActivity.this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getNote() {
        Intent i = getIntent();
        note = (Note) i.getSerializableExtra("note");
        oldpath = note.getImage();
        Glide.with(this).load(oldpath).into(imgSelect);
        Date date = note.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String ngay = df.format(date);
        edtDate.setText(ngay.substring(0, 10));
        edtTime.setText(ngay.substring(11));
        edtContent.setText(note.getContent());

    }

    public void addControl() {
        edtContent = findViewById(R.id.edtContent);
        edtTime = findViewById(R.id.edtTime);
        edtDate = findViewById(R.id.edtDate);
        imgSelect = findViewById(R.id.imgSelect);
        imgdelete = findViewById(R.id.imageDelete);
        toolbar = findViewById(R.id.toobar);
        drawerLayout = findViewById(R.id.drawer);
        imgcamera = findViewById(R.id.imgcamera);
        imggallery = findViewById(R.id.imggallery);
        imggallery.setOnClickListener(this);
        imgcamera.setOnClickListener(this);

    }

    public void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void getimgCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);

    }

    public void getimgGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgSelect.setImageBitmap(bitmap);


        }
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgSelect.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1001);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                checkPermission();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.mnuAdd:

                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String date = edtDate.getText().toString().trim() + " " + edtTime.getText().toString().trim();
                    Date time = df.parse(date);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSelect.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    newpath = Utils.saveToInternalStorage(bitmap, System.currentTimeMillis() + "", EditNoteActivity.this);
                    Note noteedit = new Note(newpath, edtContent.getText().toString(), time);
                    noteedit.setId(note.getId());
                    controller.editNote(noteedit);
                    Intent intent = new Intent();
                    setResult(101, intent);
                    finish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgSelect:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
                builder.setMessage("What kind of style do you want to select ?");
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getimgCamera();


                    }
                });
                builder.setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getimgGallery();


                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.imageDelete:
                imgSelect.setImageResource(R.drawable.image_default);
                break;
            case R.id.imgcamera:
                getimgCamera();

                break;
            case R.id.imggallery:
                getimgGallery();
                break;

        }
    }

    public void datePicker() {
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        edtDate.setText("" + i + "-" + (i1 + 1) + "-" + i2);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    public void timePicker() {
        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int mi = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        edtTime.setText(i + ":" + i1);

                    }
                }, hour, mi, true);
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
