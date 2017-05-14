package com.jfs_egs.android.timeentry;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by JFS on 4/23/2016.
 */
public class JFSSpinner extends Spinner{
    long presstime,uptime, durx =0;
    boolean startup = false;

    public void confirmlong(){
        Toast.makeText(getContext(),"PRESSED LONG",Toast.LENGTH_LONG).show();
    }


    public JFSSpinner(Context context)
    { super(context); }

    public JFSSpinner(Context context, AttributeSet attrs)
    { super(context, attrs); }

    public JFSSpinner(Context context, AttributeSet attrs, int defStyle)
    { super(context, attrs, defStyle); }





        //DO NOT REALLY NEED THESE OVERRIDES OF getOnItemSelectedListener BECAUSE ENDED UP USING getSelected RATHER THAN LISTENER
    //SINCE THERE IS NO LISTENER, THESE ALWAYS RETURN Null (getOnItemSelectedListener IS NEVER CREATED IN THE CALLING CODE, IN EnterActivity).
    @Override public void setSelection(int position, boolean animate){
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override public void setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
   //     Toast.makeText(getContext(),"TIME " + String.valueOf(durx) + " :: " + String.valueOf(position),Toast.LENGTH_LONG).show();
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
         //   try {
         //       getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
         //   }catch(Exception enull){} //CALLING THIS ON FIRST ITEM CAUSES getOnItemSelectedListener TO BE NULL, SO MUST CATCH IT HERE
        }
    }
;

}


