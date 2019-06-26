package com.company.jk.pcoordinator.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.company.jk.pcoordinator.R;


public class DayTextView extends View {
    Context context;
    String value;
    int textColor;
    Bitmap minusBitmap;
    Rect minusRectDst;
    int COLOR[];

    public DayTextView(Context context){
        super(context);
        this.context=context;
        init(null);
    }
    public DayTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context=context;
        init(attrs);
    }
    public DayTextView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        COLOR = new int[]{ context.getResources().getColor(R.color.maintabledetail1),  context.getResources().getColor(R.color.maintabledetail2),  context.getResources().getColor(R.color.maintabledetail3)};

        if(attrs != null){
            TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.MyView);
            textColor=a.getColor(R.styleable.MyView_customTextColor, Color.RED);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode= MeasureSpec.getMode(widthMeasureSpec);
        int widthSize= MeasureSpec.getSize(widthMeasureSpec);

        int heightMode= MeasureSpec.getMode(heightMeasureSpec);
        int heightSize= MeasureSpec.getSize(heightMeasureSpec);

        int width=0;
        int height=0;

        if(widthMode== MeasureSpec.AT_MOST){
            width=700;
        }else if(widthMode== MeasureSpec.EXACTLY){
            width=widthSize;
        }

        if(heightMode== MeasureSpec.AT_MOST){
            height=250;
        }else if(heightMode== MeasureSpec.EXACTLY){
            height=heightSize;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawCircle(67, 50, 50, paint);

        paint.setTextSize(60);



        if (value.substring(0,1).equals("0")){
            value = value.substring(1,2);
        }

        paint.setColor(COLOR[Integer.parseInt(value) % 3]);

        if (value.length() > 1){
            canvas.drawText(value, 32, 70, paint);
        }else {
            canvas.drawText(value, 49, 70, paint);
        }

    }

    public void setText(String s){
        value = s;
        Log.i("날짜는 ",  value);
    }
}
