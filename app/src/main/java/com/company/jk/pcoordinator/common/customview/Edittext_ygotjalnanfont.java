package com.company.jk.pcoordinator.common.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by jungkukjo on 13/01/2019.
 */

public class Edittext_ygotjalnanfont extends AppCompatEditText {

    public Edittext_ygotjalnanfont(Context context) {

        super(context, (AttributeSet)null);
        applyTypeface(context);
    }

    public Edittext_ygotjalnanfont(Context context, AttributeSet attrs) {
        super(context, attrs, androidx.appcompat.R.attr.editTextStyle);
        applyTypeface(context);
    }

    public Edittext_ygotjalnanfont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyTypeface(context);

    }


    private void applyTypeface(Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/ygotjalnanfont.ttf");
        setTypeface(typeface);
    }

}
