package com.example.dell.noteapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Dell on 4/8/2018.
 */

public class Utils {


    public static String saveToInternalStorage(Bitmap bitmapImage, String profile, Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, profile + ".jpeg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    public static void savePinCode(Context context, String name, String code) {
        SharedPreferences sharedPreference = context.getSharedPreferences("MyShare", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(name, code);
        editor.commit();
    }

    public static String getPinCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyShare", context.MODE_PRIVATE);
        return sharedPreferences.getString("savePin", "");
    }
    public static void setPinCode(final Context context1, final String pincode) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context1);
        builder.setTitle("Enter PIN code");
        final EditText input = new EditText(context1);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (input.getText().toString().equals(pincode)) {
                    if (context1 instanceof MainActivity) {
                        dismissKeyboard(context1, input);
                        context1.startActivity(new Intent(context1, NoteListActivity.class));

                        dialogInterface.dismiss();
                    } else {
                        dialogInterface.dismiss();
                    }

                } else {
                    setPinCode(context1, pincode);
                }

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setPinCode(context1, pincode);
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void dismissKeyboard(Context context, EditText myEditText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);

    }
}
