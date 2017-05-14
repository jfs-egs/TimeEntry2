package com.jfs_egs.android.timeentry;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
            LinearLayout tbout = (LinearLayout)findViewById(R.id.tbll);     //GET TOOLBAR LAYOUT FROM tblayout.xml
            ImageView iv = (ImageView)tbout.findViewById(R.id.octoiv);      //GET THE IMAGEVIEW IN tblayout.xml
            tbout.removeView(iv);                                           //REMOVE THAT IMAGEVIEW
            ImageView addx = (ImageView) tbout.findViewById(R.id.bisqueadd);
            tbout.removeView(addx);
            ImageView bb = (ImageView)tbout.findViewById(R.id.leftarrow);
            tbout.removeView(bb);
     //   Toolbar tb = (Toolbar)findViewById(R.id.JFSab);    //CAN SET TOOLBAR AS ACTIONBAR HERE; THEN MUST USE onCreateOptionsMenu METHOD BELOW TO POPULATE MENU
    //    try{setSupportActionBar(tb);}                     //TITLE CAN BE SET IN tblayout WITH app:title ITEMS
     //   catch(Exception e1){Toast.makeText(getApplicationContext(),"ERROR: " + e1.getMessage(),Toast.LENGTH_LONG).show();}

    Button enter = (Button)findViewById(R.id.newbtn);
        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //  Toast.makeText(getApplicationContext(),"CLICKED ENTER",Toast.LENGTH_LONG).show();
                Intent newintent = new Intent(getApplicationContext(), EnterActivity.class);
                startActivity(newintent);
            }
        });

        Button histbtn = (Button)findViewById(R.id.historybtn);
        histbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //  Toast.makeText(getApplicationContext(),"CLICKED ENTER",Toast.LENGTH_LONG).show();
                Intent historyintent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(historyintent);
            }
        });




    }   //END onCreate


    //METHODS RE TOOLBAR MENU
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.jfsmenuitems1, menu);
        return true;
    }                       */


}           //END MainActivity
