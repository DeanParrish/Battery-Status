package com.example.parrish.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;


public class CreateEntry extends Activity {

    SeekBar seekBar_start;
    TextView textView_start;
    SeekBar seekBar_end;
    TextView textView_end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);
        initializeVariables();

        // Initialize the textview with '0'.
        textView_start.setText(seekBar_start.getProgress() + "%" );
        textView_end.setText(seekBar_start.getProgress() + "%" );

        //Seekbar start
        seekBar_start.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView_start.setText(seekBar.getProgress() + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView_start.setText(seekBar.getProgress() + "%");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_start.setText(seekBar.getProgress() + "%");
            }
        });

        //seekbar end
        seekBar_end.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView_end.setText(seekBar.getProgress() + "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView_end.setText(seekBar.getProgress() + "%");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_end.setText(seekBar.getProgress() + "%");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_entry, menu);
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


           // A private method to help us initialize our variables.
    private void initializeVariables() {
        seekBar_start = (SeekBar) findViewById(R.id.seekBar);
        textView_start = (TextView) findViewById(R.id.txtStart);
        seekBar_end = (SeekBar) findViewById(R.id.seekBar2);
        textView_end = (TextView) findViewById(R.id.txtEnd);
    }

}
