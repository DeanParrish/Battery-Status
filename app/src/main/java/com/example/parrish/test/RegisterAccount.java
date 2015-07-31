package com.example.parrish.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Classes.SaveData;
import Classes.User;


public class RegisterAccount extends Activity {
    List<String> listQuestions;
    List<String> listUnusedQuestions;
    List<Integer> listSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        ActionBar actionBar = getActionBar();
        actionBar.setElevation(0);
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

/*        SaveData save = new SaveData(getApplicationContext());
        save.upgrade(2, 3);*/

        final Spinner ddlQuestion1 = (Spinner) findViewById(R.id.ddlQuestion1);
        final Spinner ddlQuestion2 = (Spinner) findViewById(R.id.ddlQuestion2);
        final Spinner ddlQuestion3 = (Spinner) findViewById(R.id.ddlQuestion3);

        listQuestions = Arrays.asList(getResources().getStringArray(R.array.securityQuestions));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listQuestions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlQuestion1.setAdapter(adapter);
        ddlQuestion2.setAdapter(adapter);
        ddlQuestion3.setAdapter(adapter);

        ddlQuestion1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if (position == ddlQuestion2.getSelectedItemPosition() || position == ddlQuestion3.getSelectedItemPosition()) {
                        TextView errorText = (TextView) ddlQuestion1.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("Question already used!");//changes the selected item text to this

                        EditText answer1 = (EditText) findViewById(R.id.txtAnswer1);
                        answer1.setText("Please select another question.");
                        answer1.setEnabled(false);
                    } else {
                        EditText answer1 = (EditText) findViewById(R.id.txtAnswer1);
                        answer1.setText("");
                        answer1.setEnabled(true);
                        answer1.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(answer1, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ddlQuestion2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if (position == ddlQuestion1.getSelectedItemPosition() || position == ddlQuestion3.getSelectedItemPosition()) {
                        TextView errorText = (TextView) ddlQuestion2.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("Question already used!");//changes the selected item text to this

                        EditText answer2 = (EditText) findViewById(R.id.txtAnswer2);
                        answer2.setText("Please select another question.");
                        answer2.setEnabled(false);
                    } else {
                        EditText answer2 = (EditText) findViewById(R.id.txtAnswer2);
                        answer2.setText("");
                        answer2.setEnabled(true);
                        answer2.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(answer2, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ddlQuestion3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if (position == ddlQuestion2.getSelectedItemPosition() || position == ddlQuestion1.getSelectedItemPosition()) {
                        TextView errorText = (TextView) ddlQuestion3.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("Question already used!");//changes the selected item text to this

                        EditText answer3 = (EditText) findViewById(R.id.txtAnswer3);
                        answer3.setText("Please select another question.");
                        answer3.setEnabled(false);
                    } else {
                        EditText answer3 = (EditText) findViewById(R.id.txtAnswer3);
                        answer3.setText("");
                        answer3.setEnabled(true);
                        answer3.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(answer3, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set icon
        alertDialogBuilder.setIcon(R.mipmap.ic_alert);
        // set title
        alertDialogBuilder.setTitle("Account not saved!");

        // set dialog message
        alertDialogBuilder
                .setMessage("You will loose any unsaved data.")
                .setCancelable(false)
                .setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home){
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
    public void onSubmitClick(MenuItem item){
        String errorQuestion = "Question already used!";
        String defaultQuestion = "Recovery Question";
        boolean boolSubmit = true;
        SaveData save = new SaveData(getApplicationContext());
        List<String> usedEmails = save.getAllUsersEmail();
        Iterator<String> iterator = usedEmails.iterator();

        EditText editTextEmail = (EditText) findViewById(R.id.txtEmail);
        EditText editTextPassword = (EditText) findViewById(R.id.txtPassword);
        EditText editTextConfirmPass = (EditText) findViewById(R.id.txtConfirmPassword);
        Spinner ddlQuestion1 = (Spinner) findViewById(R.id.ddlQuestion1);
        Spinner ddlQuestion2 = (Spinner) findViewById(R.id.ddlQuestion2);
        Spinner ddlQuestion3 = (Spinner) findViewById(R.id.ddlQuestion3);
        EditText editTextAnswer1 = (EditText) findViewById(R.id.txtAnswer1);
        EditText editTextAnswer2 = (EditText) findViewById(R.id.txtAnswer2);
        EditText editTextAnswer3 = (EditText) findViewById(R.id.txtAnswer3);

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!isEmailValid(email)){
            editTextEmail.setError("Please enter a valid email");
            boolSubmit = false;
        } else {
            String usedEmail;
            while (iterator.hasNext()){
                usedEmail = iterator.next();
                if (editTextEmail.getText().toString().trim().equals(usedEmail.trim())){
                    editTextEmail.setError("Email has already been used");
                    editTextEmail.requestFocus();
                    boolSubmit = false;
                }
            }
        }

        if (editTextPassword.getText().toString().trim().equals("")){
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
        }

        if (!password.equals(editTextConfirmPass.getText().toString().trim())){
            editTextConfirmPass.setError("Passwords do not match");
            editTextConfirmPass.requestFocus();
            boolSubmit = false;
        }

        if (ddlQuestion1.getSelectedItem().toString().equals(errorQuestion) ||
                ddlQuestion1.getSelectedItem().toString().equals(defaultQuestion)){
            createToast("Please choose a valid security question", Toast.LENGTH_LONG);
            boolSubmit = false;
        }

        if (editTextAnswer1.getText().toString().trim().equals("")){
            editTextAnswer1.setError("Please enter your answer");
            editTextAnswer1.requestFocus();
            boolSubmit = false;
        }

        if (ddlQuestion2.getSelectedItem().toString().equals(errorQuestion) ||
                ddlQuestion2.getSelectedItem().toString().equals(defaultQuestion)){
            createToast("Please choose a valid security question", Toast.LENGTH_LONG);
            boolSubmit = false;
        }

        if (editTextAnswer2.getText().toString().trim().equals("")){
            editTextAnswer2.setError("Please enter your answer");
            editTextAnswer2.requestFocus();
            boolSubmit = false;
        }

        if (ddlQuestion3.getSelectedItem().toString().equals(errorQuestion) ||
                ddlQuestion3.getSelectedItem().toString().equals(defaultQuestion)){
            createToast("Please choose a valid security question", Toast.LENGTH_LONG);
            boolSubmit = false;
        }

        if (editTextAnswer3.getText().toString().trim().equals("")){
            editTextAnswer3.setError("Please enter your answer");
            editTextAnswer3.requestFocus();
            boolSubmit = false;
        }

        if (boolSubmit == true){
            save.addUser(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim(), ddlQuestion1.getSelectedItem().toString().trim(),
                         editTextAnswer1.getText().toString().trim(), ddlQuestion2.getSelectedItem().toString().trim(), editTextAnswer2.getText().toString().trim(),
                         ddlQuestion3.getSelectedItem().toString().trim(), editTextAnswer3.getText().toString().trim());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("userEmail", editTextEmail.getText().toString().trim());
            createToast("Account created!", Toast.LENGTH_SHORT);
            startActivity(intent);
            finish();
        }


    }

    public boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS. matcher(email).matches();
    }

    public void createToast(CharSequence text, Integer duration) {
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

}
