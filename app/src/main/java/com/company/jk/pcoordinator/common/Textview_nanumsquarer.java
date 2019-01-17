package com.company.jk.pcoordinator.common;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by jungkukjo on 13/01/2019.
 */

public class Textview_nanumsquarer extends AppCompatTextView{



    public Textview_nanumsquarer(Context context) {
        super(context, (AttributeSet)null);
        applyTypeface(context);
    }

    public Textview_nanumsquarer(Context context, AttributeSet attrs) {
        super(context, attrs, 16842884);
        applyTypeface(context);
    }

    public Textview_nanumsquarer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        applyTypeface(context);

    }

    private void applyTypeface(Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/nanumsquarer.ttf");
        setTypeface(typeface);
    }
}
