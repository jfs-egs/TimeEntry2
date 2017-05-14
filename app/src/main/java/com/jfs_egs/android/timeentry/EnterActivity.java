package com.jfs_egs.android.timeentry;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;

public class EnterActivity extends AppCompatActivity implements JFSDeleteEntriesFrag.DeleteClientListener,JFSDeleteEntriesFrag.ConfirmCDListener,
                                                                    JFSDeleteEntriesFrag.NewCMListener{
    SimpleDateFormat JFSfmt = new SimpleDateFormat("M-dd-yyyy HH:mm");
    String clientholder="";

    //DEFINE LISTENERS FOR INFORMATION RETURNED FROM FRAGMENTS
    public void getnametodelete (String delexstring){
        clientholder=delexstring;
        android.support.v4.app.FragmentManager fmx = getSupportFragmentManager();
        JFSDeleteEntriesFrag dfx = JFSDeleteEntriesFrag.newInstance(delexstring, "CONFIRMCD");
        dfx.show(fmx,"ConfirmClientDeleteTag");
        //Toast.makeText(getApplicationContext(),"IN ENTER ACTIVITY: Deleted " + delexstring,Toast.LENGTH_LONG).show();
    }
    public void cdconfirmed (String confirmationstring) {
        if(confirmationstring=="YES"){
            DBHelper cmdelhelper = new DBHelper(getApplicationContext());
            String pop=cmdelhelper.DeleteCM(clientholder);
            FillSpinner();
         }
    }
    public void getnewcmname (String newcmstring){
        DBHelper newhelper = new DBHelper(getApplicationContext());
        newhelper.AddCM(newcmstring);
        FillSpinner();
    }
    //***    END LISTENERS FOR INFORMATION RETURNED FROM FRAGMENTS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        ImageView rtx = (ImageView)findViewById(R.id.leftarrow);    //FIND RETURN BUTTON ON TOOLBAR
        rtx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent backintent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(backintent);
                            }                                               //END onClick
                               });                                      //END OnCLickListener

        ImageView newcmbtn = (ImageView)findViewById(R.id.bisqueadd);
        newcmbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //** CODE TO PASS VARIABLES, USING newInstance METHOD FROM FRAGMENT java FILE
                android.support.v4.app.FragmentManager fmx = getSupportFragmentManager();
                JFSDeleteEntriesFrag cmfrag = JFSDeleteEntriesFrag.newInstance("NEWCM", "NEWCM");
                cmfrag.show(fmx, "NewCMTag");
                //END CODE TO PASS VARIABLES
            }                                               //END onClick
        });                                      //END OnCLickListener

        FillSpinner();

        Button canbtn = (Button) findViewById(R.id.cancelentrybtn);      //IF CLICK CANCEL, RETURN TO MAIN SCREEN
        canbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
            Intent mainintent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainintent);
                }   //END onClick
            });     //END onClickListener

        //TOOL BAR IMAGES
        ImageView delcmbtn = (ImageView) findViewById(R.id.octoiv);
        delcmbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
//** CODE TO PASS VARIABLES, USING newInstance METHOD FROM FRAGMENT java FILE
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                JFSDeleteEntriesFrag df = JFSDeleteEntriesFrag.newInstance("TEST1", "DELETECLIENTS");
                df.show(fm, "ShowClientsTag");
//END CODE TO PASS VARIABLES
                                                    }
                                                });


                }  //END onCreate

    public void FillSpinner() {
        final JFSSpinner spx = (JFSSpinner)findViewById(R.id.spinx);      //CREATED CUSTOM SPINNER CLASS QUIA DEFAULT SPINNER DOES NOT ALLOW SELECTION OF DEFAULT ITEM
        DBHelper cmhelper = new DBHelper(getApplicationContext());
        Cursor curse= cmhelper.getAllCMs();                         //GET CMS FROM TABLE AND READ THEM INTO ARRAY
        String[] strark = new String[curse.getCount()];
        int x=0;
        while(curse.moveToNext()) {strark[x] = curse.getString(0);x=x+1;}  //READ DATA FROM SQLITE CM TABLE INTO A STRING ARRAY
        final ArrayAdapter<String> SpinAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinneritemlayout,strark); //SET LAYOUT TO CHANGE ITEMS SUCH AS CENTERING TEXT
        SpinAdapter.setDropDownViewResource(R.layout.spinnerddlayout);
        spx.setAdapter(SpinAdapter);
        Button newentrybtn = (Button) findViewById(R.id.entercompletedbtn);
        newentrybtn.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                EditText timebox = (EditText) findViewById(R.id.timetext);
                EditText entrybox = (EditText) findViewById(R.id.descriptext);
                if ((timebox.getText().toString().equals("")) || (entrybox.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), "You must enter data in each field.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        DBHelper dbhelpx = new DBHelper(getApplicationContext());
                        dbhelpx.addTimeEntry(String.valueOf(spx.getSelectedItem()), String.valueOf(timebox.getText()), String.valueOf(entrybox.getText()));
                        timebox.setText("");
                        entrybox.setText("");
                        //    Toast.makeText(getApplicationContext(),"ITEM CHOSEN=" + String.valueOf(spx.getSelectedItem()), Toast.LENGTH_LONG).show();
                        spx.setSelection(0);
                    } catch (Exception ee) {
                        Toast.makeText(getApplicationContext(), "ERROR ADDING: " + ee.getMessage(), Toast.LENGTH_LONG).show();
                    }  //END CATCH
                }  //END else
            }   //END onClick
        });     //END onClickListener

    }   //END FillSpinner



    }       //END CLASS


//***       ONITEMCLICK AND ONITEMLONGCLICK NOT AVAILABLE IN SPINNER
/*        spx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> av, View v, int position, long arg) {
                Toast.makeText(getApplicationContext(), "TEST: " + String.valueOf(position) + String.valueOf(av.getTag()), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> nav){}
        });     */
//***