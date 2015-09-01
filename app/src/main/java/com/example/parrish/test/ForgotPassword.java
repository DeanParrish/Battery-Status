package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Classes.SaveData;
import Classes.User;

public class ForgotPassword extends Activity {
    User user;
    SaveData save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        String userEmail;
/*        Spinner ddlQuestion1 = (Spinner) findViewById(R.id.ddlQuestion1);
        Spinner ddlQuestion2 = (Spinner) findViewById(R.id.ddlQuestion2);
        Spinner ddlQuestion3 = (Spinner) findViewById(R.id.ddlQuestion3);*/
        TextView txtQuestion1 = (TextView) findViewById(R.id.txtQuestion1);
        TextView txtQuestion2 = (TextView) findViewById(R.id.txtQuestion2);
        TextView txtQuestion3 = (TextView) findViewById(R.id.txtQuestion3);

        try {
            //       hides shadow from action bar
            ActionBar actionBar = getActionBar();
            actionBar.setElevation(0);
            // Enabling Up / Back navigation
            actionBar.setDisplayHomeAsUpEnabled(true);
            //hide label in action bar
            actionBar.setDisplayShowTitleEnabled(false);
            setTitle("Menu");

        } catch (NullPointerException e) {
            Log.e("actionbar", e.toString());
        }

        userEmail = getIntent().getStringExtra("userEmail");

        save = new SaveData(getApplicationContext());

        if (!userEmail.equals("")){
            user = save.getUserByEmail(userEmail);
            txtQuestion1.setText(user.getQuestion1());
            txtQuestion2.setText(user.getQuestion2());
            txtQuestion3.setText(user.getQuestion3());

/*            ddlQuestion1.setSelection(getSpinnerIndex(user.getQuestion1()));
            ddlQuestion2.setSelection(getSpinnerIndex(user.getQuestion2()));
            ddlQuestion3.setSelection(getSpinnerIndex(user.getQuestion3()));*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        //menu
        inflater.inflate(R.menu.menu_forgot_password, menu);
        for (int i = 0; i < menu.size(); i++){
            final MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.action_done){
                View itemActionView = item.getActionView();
                if (itemActionView != null){
                    itemActionView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            validateSecurityQuestions();
                        }
                    });
                    itemActionView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                }
            }
        }
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
        } else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public int getSpinnerIndex(String message){
        String[] messages = getResources().getStringArray(R.array.securityQuestions);
        Integer index = -1;

        for (int i=0; i <= messages.length; i++){
            if (messages[i].equals(message)){
                index = i;
                break;
            }
        }

        return  index;
    }

    public void validateSecurityQuestions(){
        EditText txtAnswer1 = (EditText) findViewById(R.id.txtAnswer1);
        EditText txtAnswer2 = (EditText) findViewById(R.id.txtAnswer2);
        EditText txtAnswer3 = (EditText) findViewById(R.id.txtAnswer3);
        EditText txtNewPass = (EditText) findViewById(R.id.txtNewPass);
        EditText txtVerifyNewPass = (EditText) findViewById(R.id.txtVerifyNewPass);
        boolean passRecovered = true;

        if (!txtAnswer1.getText().toString().toUpperCase().trim().equals(user.getAnswer1().toUpperCase())){
            txtAnswer1.setError("You have entered an incorrect answer");
            passRecovered = false;
        }

        if (!txtAnswer2.getText().toString().toUpperCase().trim().equals(user.getAnswer2().toUpperCase())){
            txtAnswer2.setError("You have entered an incorrect answer");
            passRecovered = false;
        }

        if (!txtAnswer3.getText().toString().toUpperCase().trim().equals(user.getAnswer3().toUpperCase())){
            txtAnswer3.setError("You have entered an incorrect answer");
            passRecovered = false;
        }

        if (!txtNewPass.getText().toString().trim().equals(txtVerifyNewPass.getText().toString().trim())){
            txtVerifyNewPass.setError("The entered passwords do not match");
            passRecovered = false;
        }

        if (passRecovered == true){
            String encryptedPass;
            String salt = new String(save.generateSalt(), StandardCharsets.UTF_8);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String date = dateFormat.format(calendar.getTime());
            try {
                encryptedPass = AESCrypt.encrypt(txtNewPass.getText().toString().trim(), salt);
            } catch (GeneralSecurityException e){
                String message = e.getMessage();
                encryptedPass = txtNewPass.getText().toString().trim();

            }
            save.updateUser(user.getId(), user.getEmail(), encryptedPass, user.getQuestion1(), user.getAnswer1(),
                    user.getQuestion2(), user.getAnswer2(), user.getQuestion3(), user.getAnswer3(), Character.toString('X'),
                    Character.toString('X'), date, salt);
            save.logIn(user.getId());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("userID", user.getId());
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT);
        }

    }
}
