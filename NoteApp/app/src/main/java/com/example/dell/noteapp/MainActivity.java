package com.example.dell.noteapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    LoginButton btnlogin;
    String pincode;
    int SPLASH_TIME_OUT = 100;
    private ProfilePictureView profilePictureView;
    private TextView email;
    private TextView gender;
    private TextView facebookName;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "NoteApp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        pincode = Utils.getPinCode(MainActivity.this);
        if (pincode.equals("") && name.equals("")) {
            innitFaceBook();
            btnlogin.setVisibility(View.VISIBLE);
        } else if (pincode.equals("") && (!name.equals(""))) {
            Intent intent = new Intent(MainActivity.this, NoteListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();


        } else if (!pincode.equals("") && (!name.equals(""))) {
            Utils.setPinCode(MainActivity.this, pincode);

        } else if (!pincode.equals("") && name.equals("")) {
            Utils.setPinCode(MainActivity.this, pincode);
        }

    }

    private void setProfileToView(JSONObject jsonObject) {
        try {
            //optString: dung để lấy dữ liệu và sẽ trae về null nếu trường đó không tồn tại
            String email = jsonObject.optString("email");
            String name = jsonObject.optString("name");
            String gender = jsonObject.optString("gender");


            // TODO
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.putString("name", name);
            editor.putString("gender", gender);
            editor.commit();

            startActivity(new Intent(MainActivity.this, NoteListActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void innitFaceBook() {
        callbackManager = CallbackManager.Factory.create();
        btnlogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {

                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("Main", response.toString());
                                        setProfileToView(object);
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }


                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException e) {
                        e.printStackTrace();
                    }
                }
        );


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
