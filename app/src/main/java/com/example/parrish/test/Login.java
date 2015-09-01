package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Classes.Battery;
import Classes.Entry;
import Classes.SaveData;
import Classes.User;


public class Login extends Activity {

    Context context = this;
//    private final static String PLAIN_TEXT = "here is your text";
    private String PLAIN_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SaveData save = new SaveData(getApplicationContext());
        //save.update();
        final User activeUser = save.getActiveUser();



        //check for network connection
        if (hasNetworkConnection() == false){

            final Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            if (activeUser.getId() == null){
                intent.putExtra("userID", "");
                startActivity(intent);
                finish();
            } else {

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                // set icon
                dialog.setIcon(R.mipmap.ic_alert);
                // set title
                dialog.setTitle("Still user " + activeUser.getEmail() + "?");

                // set dialog message
                dialog
                        .setMessage("This action will affect the saving process.")
                        .setCancelable(false)
                        .setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                intent.putExtra("userID", activeUser.getId());
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // the dialog box and update all users to not active
                                save.logIn(null);
                                intent.putExtra("userID", "");
                                startActivity(intent);
                                finish();
                            }
                        });
                dialog.show();
            }
        } else if(hasNetworkConnection() == true && activeUser.getId() == null) {
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                setContentView(R.layout.activity_login);
            } else {
                setContentView(R.layout.activity_login_l);
            }
            List<Battery> batteryList = save.getAllBatteriesUser(null);
            Iterator<Battery> batteryIterator = batteryList.iterator();
            List<Entry> entryList = save.getAllEntriesForUser(null);
            Iterator<Entry> entryIterator = entryList.iterator();
            addListenerOnButtonLogin(batteryList, entryList);
            handleSignUpClick();

            // hides shadow from action bar
            ActionBar actionBar = getActionBar();
            actionBar.setElevation(0);
            actionBar.hide();

            TextView signUp = (TextView) findViewById(R.id.lbl_sign_up);
            signUp.setPaintFlags(signUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            TextView lblForgot = (TextView) findViewById(R.id.lbl_forgot);
            lblForgot.setPaintFlags(lblForgot.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            lblForgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textViewEmail = (TextView) findViewById(R.id.txt_email);
                    if (textViewEmail.getText().toString().trim().equals("")) {
                        textViewEmail.setError("You must enter an email to recover");
                    } else {
                        User user = save.getUserByEmail(textViewEmail.getText().toString().trim());
                        if (user.getEmail() == null){
                            textViewEmail.setError("You must enter an email to recover");
                        } else {
                            Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                            intent.putExtra("userEmail", textViewEmail.getText().toString().trim());
                            startActivity(intent);
                            //finish();
                        }

                    }
                }
            });

            TextView lblSkip = (TextView) findViewById(R.id.lbl_skip);
            lblSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("userID", activeUser.getId());
                    startActivity(intent);
                    finish();
                }
            });

        }
        else if (hasNetworkConnection() == true && activeUser.getId() != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("userID", activeUser.getId());
            startActivity(intent);
            finish();
        } else {
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

            TextView lblForgot = (TextView) findViewById(R.id.lbl_forgot);
            lblForgot.setPaintFlags(lblForgot.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            lblForgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textViewEmail = (TextView) findViewById(R.id.txt_email);
                    if (textViewEmail.getText().toString().trim().equals("")) {
                        textViewEmail.setError("You must enter an email to recover");
                    } else {
                        User user = save.getUserByEmail(textViewEmail.getText().toString().trim());
                        if (user.getEmail() == null){
                            textViewEmail.setError("You must enter an email to recover");
                        } else {
                            Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                            intent.putExtra("userEmail", textViewEmail.getText().toString().trim());
                            startActivity(intent);
                            //finish();
                        }

                    }
                }
            });


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

                if (txtEmail.isEmpty()) {
                    textViewEmail.setError("Please enter an email!");
                    boolLogin = false;
                }

                if (txtPassword.isEmpty()) {
                    textViewPassword.setError("Please enter your password!");
                    boolLogin = false;
                }

                if (!isEmailValid(txtEmail)) {
                    textViewEmail.setError("Please enter a valid email!");
                    boolLogin = false;
                }

                if (boolLogin == true) {
                    SaveData save = new SaveData(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    User user = save.getUserByEmail(textViewEmail.getText().toString().trim());
                    save.logIn(user.getId());
                    List<User> userList = save.getAllUsers();
                    intent.putExtra("userEmail", textViewEmail.getText().toString().trim());
                    intent.putExtra("userID", user.getId());
                    startActivity(intent);
                }


            }
        });
    }
    public void addListenerOnButtonLogin(final List<Battery> nullBatteryList, List<Entry> nullEntryList) {
        Button btn_login = (Button) findViewById(R.id.btn_login);
        final TextView textViewEmail = (TextView) findViewById(R.id.txt_email);
        final EditText textViewPassword = (EditText) findViewById(R.id.txt_password);
        final Iterator<Battery> batteryIterator = nullBatteryList.iterator();
        Iterator<Entry> entryIterator = nullEntryList.iterator();
        final SaveData save = new SaveData(getApplicationContext());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean boolLogin = true;
        String txtEmail = textViewEmail.getText().toString().trim();
        String txtPassword = textViewPassword.getText().toString().trim();

        if (save.getUserByEmail(txtEmail).getId() == null){
            textViewEmail.setError("User does not exist!");
            boolLogin = false;
        }

        if (txtEmail.isEmpty()) {
            textViewEmail.setError("Please enter an email!");
            boolLogin = false;
        }

        if (txtPassword.isEmpty()) {
            textViewPassword.setError("Please enter your password!");
            boolLogin = false;
        }

        if (!isEmailValid(txtEmail)) {
            textViewEmail.setError("Please enter a valid email!");
            boolLogin = false;
        }

        if (boolLogin == true && save.validatePassword(save.getUserByEmail(textViewEmail.getText().toString().trim()).getId(),
                textViewPassword.getText().toString().trim()) == false){
            boolLogin = false;
            textViewPassword.setError("Password is incorrect!");
        }

        if (boolLogin == true) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            User user = save.getUserByEmail(textViewEmail.getText().toString().trim());
            save.logIn(user.getId());
            List<User> userList = save.getAllUsers();
            intent.putExtra("userEmail", textViewEmail.getText().toString().trim());
            intent.putExtra("userID", user.getId());


            if (nullBatteryList.size() >= 1){
                /*save.setUserIDOfNull(save.getUserByEmail(txtEmail).getId(), Login.this);*/
                intent.putExtra("nullBatteries", true);
            } else {
                intent.putExtra("nullBatteries", false);
            }
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
        return hasConnectedWiFi || hasConnectedMobile;
        //return false;
    }
}
