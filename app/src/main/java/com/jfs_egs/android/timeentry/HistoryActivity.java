package com.jfs_egs.android.timeentry;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import android.support.v4.app.FragmentManager;

public class HistoryActivity extends AppCompatActivity implements JFSDeleteEntriesFrag.OnDateChosenListener,
        JFSDeleteEntriesFrag.OnDeleteConfirmListener,JFSDeleteEntriesFrag.SelectedRowDeletionListener{
String dateholder="";
ArrayList<Integer> rowsselected = new ArrayList<>();

    //THESE ARE LISTENERS FOR ACTIONS DEFINED IN JFSDeleteEntriesFrag -- THEY LISTEN FOR THE EVENT THAT OCCURS THERE AND USE THE DATA PASSED FROM THE FRAGMENT
        //***   *** GET THE DATE SELECTED ON THE DATE SPINNER IN THE FRAGMENT   *** ***
    public void okdate (String chosendatestring){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        JFSDeleteEntriesFrag df = JFSDeleteEntriesFrag.newInstance(chosendatestring, "DATECONFIRM");
        try {
            df.show(fm, "NewTag");
            dateholder=chosendatestring;
           // Toast.makeText(getApplicationContext(),"IN CALLING ACTIVITY; DATE = " + chosendatestring,Toast.LENGTH_LONG).show();
        } catch (Exception es) {
            Toast.makeText(getApplicationContext(), es.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
        //GET THE CONFIRMING BUTTON CLICK FROM THE FRAGMENT, AFFRIMING THAT USER WANTS TO DELETE ENTRIES
    public void DeletionConfirmed (String delexstring){
        if(delexstring=="YES") {
//            Toast.makeText(getApplicationContext(),"IN CALLING ACTIVITY; DATE REMAINS = " + dateholder,Toast.LENGTH_LONG).show();
            DBHelper deletehelper = new DBHelper(getApplicationContext());
            String messx = deletehelper.DeletePrior(dateholder);
            //Toast.makeText(getApplicationContext(),messx,Toast.LENGTH_LONG).show();
        }
    }
    public void seldelconfirmed (String seldelconfstring){
        if(seldelconfstring=="YES") {
            DBHelper deletehelper = new DBHelper(getApplicationContext());
            String delx = deletehelper.DeleteRows(rowsselected);
          //  Toast.makeText(getApplicationContext(), "IN HISTORY; CONFIRMED DELETE: " + delx, Toast.LENGTH_LONG).show();
        }
    }

    //***   ***     ***     ***     ***     ***     ***
    //END LISTENERS FOR ACTIONS DEFINED IN JFSDeleteEntriesFrag

    //  *** *** STANDARD PAGE CREATION METHODS  *** ***
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        LinearLayout toolbarx = (LinearLayout) findViewById(R.id.tbll);
        ImageView emailx = new ImageView(getApplicationContext());
        emailx.setImageDrawable(getResources().getDrawable(R.mipmap.bisqueemail));
        toolbarx.addView(emailx);

        DBHelper dbhelper = new DBHelper(getApplicationContext());      //MUST BE ADDED IN onCreate, NOT BEFORE

        emailx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getselectedrows();
                if (rowsselected.size()==0){
                    Toast.makeText(getApplicationContext(), "You must select one or more rows to e-mail.",Toast.LENGTH_LONG).show();
                }
                if (rowsselected.size() > 0){
                    Toast.makeText(getApplicationContext(), "SENDING AN EMAIL.",Toast.LENGTH_LONG).show();
                    selectemails();
                }
            }
        });

        ImageView rtx = (ImageView)findViewById(R.id.leftarrow);
        rtx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent backintent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(backintent);
            }                                               //END onClick
        });                                      //END OnCLickListener

        ImageView delentriesbtn = (ImageView)findViewById(R.id.octoiv);
        delentriesbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getselectedrows();
//                Toast.makeText(getApplicationContext(),String.valueOf(rowsselected.toString()),Toast.LENGTH_LONG).show();
                if (rowsselected.size() == 0) {                                     //IF NO ROW SELECTED, USE DATEPICKER TO DELETE ROWS BEFORE A CERTAIN DATE
//** CODE TO PASS VARIABLES, USING newInstance METHOD FROM FRAGMENT java FILE                       //***   ***     ***    ***
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    JFSDeleteEntriesFrag df = JFSDeleteEntriesFrag.newInstance("TEST1", "CALSHOW");
                    try {
                        df.show(fm, "NewTag");
                    } catch (Exception es) {
                        Toast.makeText(getApplicationContext(), es.getMessage(), Toast.LENGTH_LONG).show();
                    }
//END CODE TO PASS VARIABLES
                    // Toast.makeText(getApplicationContext(),"CLICKED",Toast.LENGTH_LONG).show();
                 }// END IF selectedtablerows > 0

                else if(rowsselected.size()>0){                         //IF SOME ROW SELECTED, DELETE THE ROWS USER SELECTED
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    JFSDeleteEntriesFrag df = JFSDeleteEntriesFrag.newInstance("TEST1", "SELDEL");
                    try {
                        df.show(fm, "NewTag");
                    } catch (Exception es) {
                        Toast.makeText(getApplicationContext(), es.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }       //END else if rowsselected > 0 (USER SELECTED SOME ROWS TO DELETE)

                }   //END onCLick
            });  //END DELETE BUTTON onClickListener

    //CREATE AND POPULATE A TABLELAYOUT
        final TableLayout tbx= (TableLayout) findViewById(R.id.HistoryTable);
        //***
        View.OnLongClickListener rclistener = new View.OnLongClickListener() {      //THIS LISTENER IS SET ON EACH ROW ADDED TO TABLE, SO THAT ROWS CAN BE SELECTED FOR DELETION
            public boolean onLongClick(View v) {
                TableRow clickedRow = (TableRow) v;
                if(clickedRow.isSelected()) {               //IF ROW IS SELECTED, DE-SELECT AND CHANGE ROW COLOR BACK TO NORMAL
                    clickedRow.setSelected(false);
                    if(tbx.indexOfChild(v) % 2==0){clickedRow.setBackgroundColor(getResources().getColor(R.color.JFS_light_cyan));     }
                    else{clickedRow.setBackgroundColor(getResources().getColor(R.color.jfstableblue)); }
                }
                else {                                      //IF ROW IS NOT SELECTED, SELECT IT AND CHANGE ROW COLOR TO INDICATE SELCETION
                    clickedRow.setSelected(true);
                    clickedRow.setBackgroundColor(getResources().getColor(R.color.jfsbisque));
                    TextView uidtv = (TextView) clickedRow.getChildAt(4);
                    String uid = uidtv.getText().toString();
                   // String rowlc = String.valueOf(clickedRow.isSelected()) + " :: " + String.valueOf(tbx.indexOfChild(v)) + " :: " + uid;
                  //  Toast.makeText(getApplicationContext(), rowlc, Toast.LENGTH_LONG).show();
                }
                return true;
            }
        };
        //***
        Cursor curse = dbhelper.getAllEntries();
        DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //NEED TO CONVERT STRING TO DATE BEFORE REFORMATTING AND RE-CONVERTING TO STRING
        SimpleDateFormat JFSfmt = new SimpleDateFormat("M-dd-yyyy HH:mm");
        while (curse.moveToNext()) {
            String xname = curse.getString(0);   //THESE GET THE VALUE (OF TYPE STRING, LONG, ETC.) FROM THE STATED COLUMN
            String xtime = curse.getString(1);
            String xdesc=curse.getString(2);
            String rawdatestring= curse.getString(3);
            String xdate="";
            try {
                Date rawdate = inputFormatter.parse(rawdatestring);
                xdate = JFSfmt.format(rawdate);
            }catch(ParseException px){Toast.makeText(this, "PARSE EXCEPTION: " + px.getMessage(), Toast.LENGTH_LONG).show();}
            String xuid = String.format("%.0f", curse.getFloat(4));
            TableRow.LayoutParams paramx1 = new TableRow.LayoutParams(0, 60, .15f);  //1st var=height(overrriden by var 3); 2d var=height in
            TableRow.LayoutParams paramx2 = new TableRow.LayoutParams(0, 60, .05f);  //pixels; 3rd var= %width out of 1
            TableRow.LayoutParams paramx3 = new TableRow.LayoutParams(0, 60, .43f);
            TableRow.LayoutParams paramx4 = new TableRow.LayoutParams(0, 60, .35f);
            TableRow.LayoutParams paramx5 = new TableRow.LayoutParams(0, 60, .02f);
            TableRow trow = new TableRow(this);
            trow.setClickable(true);
            JFStv nametv = new JFStv(this);nametv.setText(xname);nametv.setLayoutParams(paramx1);   //JFStv DEFINED IN SEP. JAVA FILE; IT SETS textColor AND ACCOUNTS FOR VERSION DIFFERENCES
            JFStv timetv = new JFStv(this);timetv.setText(xtime);timetv.setLayoutParams(paramx2);
            JFStv desctv = new JFStv(this);desctv.setText(xdesc);desctv.setLayoutParams(paramx3);
            JFStv datetv = new JFStv(this);datetv.setText(xdate);datetv.setLayoutParams(paramx4);
            JFStv idtv = new JFStv(this);idtv.setText(xuid);idtv.setLayoutParams(paramx2);
            trow.addView(nametv);trow.addView(timetv);trow.addView(desctv);trow.addView(datetv);trow.addView(idtv);
            trow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            try {
                if (tbx.getChildCount() % 2 == 0) {
                    trow.setBackgroundColor(getResources().getColor(R.color.JFS_light_cyan));
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                     }
            trow.setOnLongClickListener(rclistener);  //SET A LISTENER FOR A LONG CLICK ON EACH ROW
            tbx.addView(trow);
        }  //END WHILE
    }   //END ON CREATE


//***   ***     FUNCTIONS   ***     ***
//***   ***         ***     ***     ***
    public void getselectedrows() {
        rowsselected.clear();
        TableLayout tblselected = (TableLayout) findViewById(R.id.HistoryTable);
        for (int i = 0; i < tblselected.getChildCount(); i++) {      //SEE WHETHER ANY TABLE ROWS HAVE BEEN SELECTED
            if (tblselected.getChildAt(i).isSelected()) {
                TableRow tr = (TableRow) tblselected.getChildAt(i);         //GET THE SELECTED ROW
                TextView seltv = (TextView) tr.getChildAt(4);                   //GET THE UID TEXTVIEW IN SELECTED ROW
                String rowuid = seltv.getText().toString();                           //GET THE STRING VALUE OF THEE UID TEXTVIEW
                int Irowuid = Integer.valueOf(rowuid);
                rowsselected.add(Irowuid);                           //rowsselected DEFINED AS ACTIVITY-WIDE VARIABLE ABOVE TO HOLD VALUE WHEN FRAGMENT CALLED FOR CONFIRMATION DIALOG
            } //END IF
        }   //END FOR
    }

    public void selectemails(){
        DBHelper mailhelper = new DBHelper(getApplicationContext());
        Cursor cxmail = mailhelper.mailrows(rowsselected);
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        StringBuilder erow = new StringBuilder();
        while (cxmail.moveToNext()) {
            for(int z=0;z<4;z++) {
                erow.append(cxmail.getString(z) + "\t");
            }
            erow.append("\n");
        }
        Uri data = Uri.parse("mailto:?subject=" + "TIME ENTRgit remote add origin https://github.com/jfs-egs/TimeEntryNEW.git\n" +
                "git push -u origin masterY" + "&body=" + erow.toString() + "&to=" + "joseph.shea@fisherbroyles.com");
        testIntent.setData(data);
        startActivity(testIntent);
    }

}       //END CLASS




//SUPERSEDED CODE
//  *** *** *** *** ***

       /* Toolbar tb = (Toolbar)findViewById(R.id.JFSToolBar);    //THIS MUST BE USED IN CONJUNCTION WITH onCreateOptionsMenu METHOD BELOW
        try{setSupportActionBar(tb);}
        catch(Exception e1){Toast.makeText(getApplicationContext(),e1.getMessage(),Toast.LENGTH_LONG).show();}
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);  */
//***

/*    //METHODS RE TOOLBAR MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.jfsmenuitems1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuitem1) {
            */
//WORKING CODE -- THIS PASSES NO PARAMETERS BECAUSE IT DOES NOT CALL newInstance METHOD IN FRAGMENT
 /*  try {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                JFSDeleteEntriesFrag jf = new JFSDeleteEntriesFrag();
                jf.show(fm, "fragment_name");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "START ERROR: " + e.getMessage(), Toast.LENGTH_LONG);
            }    //   END WORKING CODE   */
//***
/*
//** CODE TO PASS VARIABLES, USING newInstance METHOD FROM FRAGMENT java FILE
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            JFSDeleteEntriesFrag df = JFSDeleteEntriesFrag.newInstance("TEST1","CALSHOW");
            try {
             df.show(fm, "NewTag");
            } catch (Exception es) {
                Toast.makeText(getApplicationContext(), es.getMessage(), Toast.LENGTH_LONG).show();
            }
//END CODE TO PASS VARIABLES

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }       */