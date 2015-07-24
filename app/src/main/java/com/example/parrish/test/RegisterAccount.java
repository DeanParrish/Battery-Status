package com.example.parrish.test;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class RegisterAccount extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        Spinner ddlQuestion1 = (Spinner) findViewById(R.id.ddlQuestion1);
        Spinner ddlQuestion2 = (Spinner) findViewById(R.id.ddlQuestion2);
        Spinner ddlQuestion3 = (Spinner) findViewById(R.id.ddlQuestion3);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.securityQuestions, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlQuestion1.setAdapter(adapter);
        ddlQuestion2.setAdapter(adapter);
        ddlQuestion3.setAdapter(adapter);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onSubmitClick(MenuItem item){
        int i = 4;

        i++;
    }
}
