package com.company.jk.pcoordinator.common.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by jungkukjo on 13/01/2019.
 */

public class Textview_nanumsquareeb extends AppCompatTextView {



    public Textview_nanumsquareeb(Context context) {
        super(context, (AttributeSet)null);
        applyTypeface(context);
    }

    public Textview_nanumsquareeb(Context context, AttributeSet attrs) {
        super(context, attrs, 16842884);
        applyTypeface(context);
    }

    public Textview_nanumsquareeb(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        applyTypeface(context);

    }

    private void applyTypeface(Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/nanumsquareeb.ttf");
        setTypeface(typeface);
    }
}
