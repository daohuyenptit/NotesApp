package com.example.dell.noteapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.IOException;
import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {
    ArrayList<Note> notes = new ArrayList<>();
    NoteController controller;
    SwipeMenuListView lvNote;
    NoteAdapter adapter;
    Button btnDelete, btnCancel;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView txtname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        addControl();
        ActionBar();
        controller = new NoteController(this);
//        try {
//            controller.createDataBase();
//            controller.openDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //swipe
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(R.drawable.swipe_item);
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.drawable.edit_icon);
                menu.addMenuItem(deleteItem);
            }
        };
        lvNote.setMenuCreator(creator);
        lvNote.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(NoteListActivity.this, EditNoteActivity.class);
                        intent.putExtra("note", notes.get(position));
                        startActivityForResult(intent, 100);
                        break;

                }
                return false;
            }
        });

        adapter = new NoteAdapter(notes, getLayoutInflater(), this);
        lvNote.setAdapter(adapter);
        load();
        lvNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int vt = i;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NoteListActivity.this);

                alertDialog.setTitle("Delete Note");
                alertDialog.setMessage("Do you want to delete this note");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        controller.deleteNote(notes.get(vt).getId());
                        load();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();

                return false;
            }
        });
        lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NoteListActivity.this, EditNoteActivity.class);
                intent.putExtra("note", notes.get(i));
                startActivityForResult(intent, 100);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_setting:
                        Log.i("nav", "success");
                        startActivity(new Intent(NoteListActivity.this, SettingActivity.class));
                        break;
                    case R.id.nav_lock:
                        String pincode = Utils.getPinCode(NoteListActivity.this);
                        Utils.setPinCode(NoteListActivity.this, pincode);
                        break;

                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void addControl() {
        lvNote = findViewById(R.id.lvNote);
        toolbar = findViewById(R.id.toolbar1);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);

    }

    public void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSetting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);

                return true;
            case R.id.mnuThem:
                Intent i = new Intent(NoteListActivity.this, AddActivity.class);
                startActivityForResult(i, 10);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 100) {
            load();
        } else if (requestCode == 100 && resultCode == 101) {
            load();
        }

    }

    public void load() {
        notes.clear();

        notes.addAll(controller.getAll());

        adapter.notifyDataSetChanged();

    }

    public void insert(Note note) {

        controller.insertNote(note);
        load();

    }
}
