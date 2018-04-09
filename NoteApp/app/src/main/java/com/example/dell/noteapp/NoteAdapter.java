package com.example.dell.noteapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dell on 3/30/2018.
 */

public class NoteAdapter extends BaseAdapter implements com.bumptech.glide.module.GlideModule {
    ArrayList<Note> notes = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public NoteAdapter(ArrayList<Note> notes, LayoutInflater inflater, Context context) {
        this.notes = notes;
        this.inflater = inflater;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.note_item, viewGroup, false);
            viewHolder.txtDate = view.findViewById(R.id.txtDate);
            viewHolder.txtTime = view.findViewById(R.id.txtTime);
            viewHolder.txtContent = view.findViewById(R.id.txtcontent);
            viewHolder.imageView = view.findViewById(R.id.image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final Note note = getItem(i);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = note.getTime();
        String time = df.format(date);
        DateFormat df1 = new SimpleDateFormat("E, MMMM d, yyyy");
        String t1 = df1.format(date);
        viewHolder.txtDate.setText(t1);
        viewHolder.txtTime.setText(time.substring(11));
        viewHolder.txtContent.setText(note.getContent());
        final String path = note.getImage();
        // use library  to load image quickly
        Glide
                .with(context)
                .load(path)
                .into(viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_image);
                dialog.setTitle("load image");
                ImageView imageView = dialog.findViewById(R.id.img_dialog);
                Glide.with(context).load(path).into(imageView);
                dialog.show();
            }
        });

        return view;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }

    class ViewHolder {
        TextView txtDate;
        TextView txtTime;
        TextView txtContent;
        ImageView imageView;
    }
}


