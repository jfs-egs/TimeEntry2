package com.jfs_egs.android.timeentry;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by JFS on 4/25/2016.
 */
public class JFStv extends TextView {               //3 STANDARD VIEWS TO DEFINE A COMPONENT
    /**
     * Created by JFS on 4/8/2016.
     */
        public JFStv(Context context) {
            super(context);
            checkversion(context);
        }
        public JFStv (Context context, AttributeSet attrs) {
            super(context, attrs);
            checkversion(context);
        }

    public JFStv(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            checkversion(context);
        }

        public void checkversion(Context context) {
            if (Build.VERSION.SDK_INT < 23) {       //THIS FUNCTION IS DESIGEND TO ACCOUNT FOR DIFFERENCES IN ANDROID VERSIONS
                setTextAppearance(context, R.style.JFSrowtvstyle);   //THIS IS DEFINED IN styles.xml
            } else {
                super.setTextAppearance(R.style.JFSrowtvstyle);
            }
        }
}

