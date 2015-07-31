package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.GeneralSecurityException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Classes.SaveData;
import Classes.User;


public class Login extends Activity {

    Context context = this;
    private final static String ALGORITM = "Blowfish";
    private final static String KEY = "g7~98I2D}>r?iWo(;]7IR1@v1<7'%2";
//    private final static String PLAIN_TEXT = "here is your text";
    private String PLAIN_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check for network connection
        if (hasNetworkConnection() == false){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("userID", "");
            startActivity(intent);
            finish();
        }
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_login);
        } else {
            setContentView(R.layout.activity_login_l);
        }
        addListenerOnButtonLogin();
        handleSignUpClick();

        // hides shadow from action bar
        ActionBar actionBar = getActionBar();
        actionBar.setElevation(0);
        actionBar.hide();

        TextView signUp = (TextView) findViewById(R.id.lbl_sign_up);
        signUp.setPaintFlags(signUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView forgot = (TextView) findViewById(R.id.lbl_forgot);
        forgot.setPaintFlags(forgot.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView lblSkip = (TextView) findViewById(R.id.lbl_skip);
        lblSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void addListenerOnButtonLogin() {

        Button btn_login = (Button) findViewById(R.id.btn_login);
        final TextView textViewEmail = (TextView) findViewById(R.id.txt_email);
        final TextView textViewPassword = (TextView) findViewById(R.id.txt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean boolLogin = true;
/*                try {

                    byte[] encrypted = encrypt(KEY, PLAIN_TEXT);
                    Log.i("FOO", "Encrypted: " + bytesToHex(encrypted));

                    String decrypted = decrypt(KEY, encrypted);
                    Log.i("FOO", "Decrypted: " + decrypted);

                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }*/

                String txtEmail = textViewEmail.getText().toString();
                String txtPassword = textViewPassword.getText().toString();

                if (txtEmail.isEmpty()){
                    textViewEmail.setError("Please enter an email!");
                    boolLogin = false;
                }

                if (txtPassword.isEmpty()){
                    textViewPassword.setError("Please enter your password!");
                    boolLogin = false;
                }

                if (!isEmailValid(txtEmail)){
                    textViewEmail.setError("Please enter a valid email!");
                    boolLogin = false;
                }

                if (boolLogin == true){
                    SaveData save = new SaveData(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    User user = save.getUserByEmail(textViewEmail.getText().toString().trim());
                    user.logIn(getApplicationContext());
                    List<User> userList = save.getAllUsers();
                    intent.putExtra("userEmail", textViewEmail.getText().toString().trim());
                    intent.putExtra("userID", user.getId());
                    startActivity(intent);
                }


            }
        });
    }

    public void handleSignUpClick(){
        TextView textViewSignUp = (TextView) findViewById(R.id.lbl_sign_up);

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterAccount.class);
                startActivity(intent);
                finish();
            }
        });
    }
//
//    public void run(View v) {
//        try {
//
//            byte[] encrypted = encrypt(KEY, PLAIN_TEXT);
//            Log.i("FOO", "Encrypted: " + bytesToHex(encrypted));
//
//            String decrypted = decrypt(KEY, encrypted);
//            Log.i("FOO", "Decrypted: " + decrypted);
//
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//    }
/*
    private byte[] encrypt(String key, String plainText) throws GeneralSecurityException {

        SecretKey secret_key = new SecretKeySpec(key.getBytes(), ALGORITM);

        Cipher cipher = Cipher.getInstance(ALGORITM);
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);

        return cipher.doFinal(plainText.getBytes());
    }

    private String decrypt(String key, byte[] encryptedText) throws GeneralSecurityException {

        SecretKey secret_key = new SecretKeySpec(key.getBytes(), ALGORITM);

        Cipher cipher = Cipher.getInstance(ALGORITM);
        cipher.init(Cipher.DECRYPT_MODE, secret_key);

        byte[] decrypted = cipher.doFinal(encryptedText);

        return new String(decrypted);
    }

    public static String bytesToHex(byte[] data) {
        if (data == null)
            return null;

        String str = "";

        for (int i = 0; i < data.length; i++) {
            if ((data[i] & 0xFF) < 16)
                str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
            else
                str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
        }
        return str;
    }*/

    public void createToast(CharSequence text, Integer duration) {
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    public boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS. matcher(email).matches();
    }

    private boolean hasNetworkConnection(){
        boolean hasConnectedWiFi = false;
        boolean hasConnectedMobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo ni : networkInfo){
            if (ni.getTypeName().equalsIgnoreCase("WIFI")){
                hasConnectedWiFi = true;
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")){
                hasConnectedMobile = true;
            }
        }
        //return hasConnectedWiFi || hasConnectedMobile;
        return false;
    }
}
