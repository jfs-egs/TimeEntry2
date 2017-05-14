package com.jfs_egs.android.timeentry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JFSDeleteEntriesFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JFSDeleteEntriesFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JFSDeleteEntriesFrag extends android.support.v4.app.DialogFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean spinnerstartup=true;                //THIS IS USED BELOW QUIA Spinner WIDGET SETS "SELECTED" ON INITIALIZATION

//DEFINE CALLBACK VARIABLES (INSTANCES OF THE INTERFACES DEFINED BELOW; INTERFACES ARE USED TO PASS DATA BACK TO CALLING ACTIVITY
    Context dialogcontext;
    OnDateChosenListener mCallback;     //THIS IS SET BELOW TO THE CALLING ACTIVITY
    OnDeleteConfirmListener delCallback;
    DeleteClientListener delclientCallback;
    ConfirmCDListener cdCallback;
    SelectedRowDeletionListener seldelCallback;
    NewCMListener cmCallback;
    // TODO: Rename and change types of parameters  //THESE ARE ASSIGNED TO ARG_PARAMS ABOVE IN onCreate
    private String mParam1;
    private String mParam2;

//METHODS
    public JFSDeleteEntriesFrag() {
        // Required empty public constructor
        dialogcontext=getActivity();
    }

//METHODS TO PASS DATA TO ACTIVITY
// EACH CALLING ACTIVITY MAY IMPLEMENT NONE, SOME, OR ALL OF THESE INTERFACES;  WHICH INTERFACES EACH CALLING ACTIVITY MUST IMPLEMENT IS SET IN onAttach BELOW
    //***   *** INTERFACES FOR HistoryActivity  ***   ***
    public interface OnDateChosenListener {
        public void okdate(String chosendatestring);
    }
    public interface OnDeleteConfirmListener {
        public void DeletionConfirmed(String deleteconfirmstring);
    }
    public interface SelectedRowDeletionListener{
        public void seldelconfirmed(String seldelconfirmationstring);
    }
    //***   *** INTERFACES FOR EntryActivity  ***   ***
    public interface DeleteClientListener {
        public void  getnametodelete(String delclientname);
    }
    public interface ConfirmCDListener {
      public void cdconfirmed (String cdconfirmationstring);
      }
    public interface NewCMListener{
        public void getnewcmname (String newcmstring);
    }
    //***   *** END INTERFACES FOR EntryActivity  ***   ***

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        String callingActivity= context.getClass().getSimpleName();
       // Toast.makeText(getActivity(),String.valueOf(context.getClass().getSimpleName() ),Toast.LENGTH_LONG).show();
        switch(callingActivity){                //THESE DEFINE WHAT LISTENERS MUST BE SET UP IN EACH ACTIVITY AND THROW AN ERROR
            case "HistoryActivity":                         //IF CALLING ACTIVITY DOES NOT HAVE THE INTERFACES DEFINED AS NECESSARY FOR IT HERE
                mCallback = (OnDateChosenListener) context;
                delCallback = (OnDeleteConfirmListener) context;
                seldelCallback = (SelectedRowDeletionListener) context;
                break;
            case "EnterActivity":
                delclientCallback = (DeleteClientListener) context;
                cdCallback  =   (ConfirmCDListener) context;
                cmCallback = (NewCMListener) context;
                break;
            default:
                throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }

        /*
        if (context instanceof OnDateChosenListener) {
            mCallback = (OnDateChosenListener) context;     //THIS IS THE CALLING ACTIVITY
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof OnDeleteConfirmListener) {
            delCallback = (OnDeleteConfirmListener) context;     //THIS IS THE CALLING ACTIVITY
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        } */
    }
    //END METHODS TO PASS DATA TO ACTIVITY

    //METHOD TO PASS DATA FROM ACTIVITY TO FRAGMENT
    /*** Use this factory method to create a new instance of this fragment using the provided parameters.
          * @param param1 Parameter 1.  * @param param2 Parameter 2. * @return A new instance of fragment JFSDeleteEntriesFrag.
     */
    public static JFSDeleteEntriesFrag newInstance(String param1, String param2) {     //THIS CAN BE USED IN CALLING ACTIVITY TO PASS PARAMETERS FROM ACTIVITY.  ocCreate, BELOW, TESTS WHETHER
        JFSDeleteEntriesFrag fragment = new JFSDeleteEntriesFrag();         //ARGS WERE PASSED (newInstance vs. new Fragment METHOD IN CALLING ACTIVITY); IF SO, onCreate GETS THE ARGUMENTS
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);         //param1 and param2 ARE DEFAULT VALUES; IF JFSDeleteEntriesFrag jf = new JFSDeleteEntriesFrag(); USED INSTEAD OF newINSTANCE IN CALLING ACTIVITY,
        args.putString(ARG_PARAM2, param2);         //NO PARAMETERS PASSED OR USED
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {       //THIS IS CREATED BEFORE onCreateView
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //END METHODS FOR PASSING PARAMETERS TO FRAGMENT

    //METHODS TO CREATE FRAGMENT
    //Either we can override onCreateView() method and inflate layout as below
    //or can override onCreateDialog() method and supply a dialog instance, as shown in JFrag in CALLREADER.
    //***                       FIRST METHOD
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragmentdelete_jfs, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);  //USE THIS TO SET TITLE AREA IN fragment_j.xml
        //    getDialog().requestWindowFeature(Window.FEATURE_LEFT_ICON);     //ADD ICON SPACE HERE, BUT MUST SET ICON IN onStart METHOD BELOW
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        //       getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        TextView rtv = (TextView) rootView.findViewById(R.id.fragtext);   //GET TEXTVIEW DEFINED IN FRAGMENT xml; STYLE IT HERE QUIA CANNOT DO SO IN xml, SINCE FRAGMENT DOES NOT INHERIT VIEW
        rtv.setTextSize(22);rtv.setTextColor(getResources().getColor(R.color.jfsbisque));rtv.setBackgroundColor(getResources().getColor(R.color.jfspagebackground));
        rtv.setPadding(6, 6, 6, 6);

//THESE ARE CALLS FROM HistoryActivity -- FRAGMENT IS CALLED THERE AND PASSES mParam1 AND mParam2 as PARAMETERS TO THE FRAGMENT
// ***  *** *** *** *** *** *** *** *** ***
        if(mParam2.equals("DATECONFIRM")) {     //DETERMINE LAYOUT ELEMENTS BASED ON PARAMETERS PASSED WHEN FRAGMENT CALLED FROM ACTIVITY (HERE HistoryActivity)
            rtv.setText("Do you want to delete all entries prior to " + mParam1 + "?");
        }

        if(mParam2.equals("SELDEL")) {     //DETERMINE LAYOUT ELEMENTS BASED ON PARAMETERS PASSED WHEN FRAGMENT CALLED FROM ACTIVITY (HERE HistoryActivity)
                rtv.setText("Do you want to delete the selected rows?");
        }
//THESE ARE CALLS FROM EnterActivity -- REPLACE TEXTVIEW IN THIS FRAG (FROM xml) WITH A SPINNER OR A CALENDARVIEW
// ***  *** *** *** *** *** *** *** *** ***
        if(mParam2.equals("DELETECLIENTS")) {     //DETERMINE LAYOUT ELEMENTS BASED ON PARAMETERS PASSED WHEN FRAGMENT CALLED FROM ACTIVITY (HERE EnterActivity)
                final LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.rl);
                ll.removeViews(0, 1);
                final JFSSpinner delspinner = new JFSSpinner(getActivity());        //USE CUSTOM SPINNER CLASS QUIA DEFAULT SPINNER DOES NOT ALLOW SELECTION OF DEFAULT ITEM ON SPINNER
                DBHelper getcmshelper = new DBHelper(getActivity());
                Cursor curse= getcmshelper.getAllCMs();                         //GET CMS FROM TABLE AND READ THEM INTO ARRAY
                String[] cmarray = new String[curse.getCount()];
                int x=0;
                while(curse.moveToNext()) {cmarray[x] = curse.getString(0); x=x+1;}  //READ CMS FROM SQLITE TABLE INTO ARRAY

                ArrayAdapter<String> delAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinneritemlayout,cmarray);   //CREATE ADAPTER TO TAKE DATA FROM cursor AND PLACE IN SPINNER
                delAdapter.setDropDownViewResource(R.layout.spinnerdd_dellayout);
                delspinner.setAdapter(delAdapter);
                delspinner.setPrompt("Select Client to delete.");
                ll.addView(delspinner, 0);                                                                      //ADD THE SPINNER TO THE ROOT FRAGMENT LAYOUT
                delspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spinnerstartup == false) {                                              //NEED THIS BECAUSE ON START UP FIRST ITEM IS SELECTED
                            mParam1 = parent.getItemAtPosition(position).toString();
                            delclientCallback.getnametodelete(mParam1);
                            dismiss();
                        }
                        spinnerstartup = false;
                    } // END onItemSelected

                    public void onNothingSelected(AdapterView<?> parent) {
                    }      //METHOD NEEDED IN onItemSelectedListener--THROWS ERROR WITHOUT
                }); //END setOnItemSelectedListener
        }

        if(mParam2.equals("CALSHOW")){
            LinearLayout rootll = (LinearLayout) rootView.findViewById(R.id.rl);
            TextView fragtv = (TextView) rootView.findViewById(R.id.fragtext);
            rootll.removeView(fragtv);
            rootll.setBackgroundColor(getResources().getColor(R.color.jfspagebackground));
            DatePicker dp = new DatePicker(getActivity());
            Calendar cal=Calendar.getInstance();

            dp.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {  //INITIALIZE dp TO CURRENT DATE AND ADD LISTENER
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    String daystr, mostr = "";
                    daystr = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                    month = month + 1;
                    mostr = (month < 10) ? "0" + String.valueOf(month) : String.valueOf(month);
                    mParam1 = String.valueOf(year) + "-" + mostr + "-" + daystr + " 00:00:01";
                }   //END onDateChanged
            });//END OnDateChangedListener
            dp.setSpinnersShown(true);dp.setCalendarViewShown(false);dp.setEnabled(true);

            rootll.addView(dp);
            int mox = dp.getMonth() + 1;
            String startmonstr = (mox <10) ? "0" + String.valueOf(mox) : String.valueOf(mox);
            String startdaystr = (dp.getDayOfMonth() <10) ? "0" + String.valueOf(dp.getDayOfMonth()) : String.valueOf(dp.getDayOfMonth());
            mParam1 = String.valueOf(dp.getYear()) + "-" +   startmonstr + "-" + startdaystr + " 00:00:01";
        }

        if(mParam2.equals("CONFIRMCD")){
            rtv.setText("Hit OK to confirm deletion of " + mParam1 + " or Cancel to exit");
        }

        if(mParam2.equals("NEWCM")){
            TextView fragtv = (TextView) rootView.findViewById(R.id.fragtext);
            fragtv.setText("Enter a new client/matter.");
            EditText etcm = new EditText(getActivity());
            etcm.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    mParam1 = s.toString();
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               //     Toast.makeText(getActivity(), "BEFORE textchanged: s =  " + s.toString() , Toast.LENGTH_LONG).show();
                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
               //     Toast.makeText(getActivity(), "ON TEXT CHANGED: s =  " + s.toString(), Toast.LENGTH_LONG).show();
                }
            });
            etcm.setBackgroundColor(getResources().getColor(R.color.jfsaqua));
            final LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.rl);
            ll.addView(etcm, 1);
        }
//END CALLS FROM ACTIVITIES
// ***  *** *** *** *** *** *** *** *** ***

//SET BUTTONS ***      ***     ***     ***     ***     ***     ***     SET BUTTONS||
       Button endbtn = (Button) rootView.findViewById(R.id.cancelbtn);
      //  endbtn.setBackgroundResource(R.drawable.jfsgoldrectangle);  //MUST SET BACKGROUND RES. HERE QUIA SETTING Background IN fragment_j.xml
        endbtn.setOnClickListener(new View.OnClickListener() {      //COVERS UP STROKE OF SURROUNDING RECTANGLE; THIS DRAWS A GOLD RECT AROUND BUTTON
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final Button okbtn = (Button) rootView.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mParam2) {
                    case "CALSHOW":
                        mCallback.okdate(mParam1);          //SEND DATA (mParam1) TO CALLING ACTIVITY ONCE USER CHOOSES DATE BY HITTING OK BUTTON, CLOSE FRAGMENT
                        break;
                    case "DATECONFIRM":
                        delCallback.DeletionConfirmed("YES");
                        break;
                    case "CONFIRMCD":
                        cdCallback.cdconfirmed("YES");
                        break;
                    case "SELDEL":
                        seldelCallback.seldelconfirmed("YES");
                        break;
                    case "NEWCM":
                        cmCallback.getnewcmname(mParam1);
                }  //END SWITCH
                dismiss();
            }   //END onClick
        });
        return rootView;
    }   //END FIRST METHOD

    @Override
    public void onStart() {
        super.onStart();
        //Do something!

/*TEMPOUT        TableLayout tl;
        tl=(TableLayout) getView().findViewById((R.id.helptable));
        try {
            if(tl.getChildCount()==0) {
                for (int x = 0; x < getResources().getStringArray(R.array.helparray).length; x++) {
                    TableRow tr = new TableRow(getActivity());     // trx.organize(7,getResources().getStringArray(R.array.helparray)[x]);
                    //ImageView iv = new ImageView(getActivity());
                    ImageView iv = (ImageView) getActivity().getLayoutInflater().inflate(R.layout.jfsimgrowivtemplate, null);  //THIS SETS iv TO CUSTOM
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.blue_telephone)); //LAYOUT, WHICH CALLS CUSTOM STYLE.
                    tr.addView(iv);                             //ImageView DOES NOT HAVE style OPTION, AS TextView DOES, SO CANNOT SET THROUGH JAVA CLASS.
                    TextView tv = new JFSImgRowTV(getActivity());
                    tv.setText(getResources().getStringArray(R.array.helparray)[x]);
                    tr.addView(tv);
                    tr.setPadding(0, 0, 0, 60);
                    tl.addView(tr);
                }
            } //END IF -- THIS PREVENTS SAME INFORMATION REPEATEDLY BEING ADDED TO TABLE AS NEW ROWS
        }catch (Exception eel){
            Toast.makeText(getActivity(), "EEL ERROR: " + eel.getMessage(), Toast.LENGTH_LONG).show();}

        getDialog().getWindow().setGravity(Gravity.CENTER_VERTICAL);
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes(); //THESE 4 LINES SIZE THE JFrag DIALOGUE
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);        END TEMP OUT */
    }       //END onStart





    /* TEMP OUT
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }       END TEMP OUT */

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

        /* TEMP OUT
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }   END TEMP OUT */
}
