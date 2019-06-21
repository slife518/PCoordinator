package com.company.jk.pcoordinator.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class DayTextView extends View {
    Context context;
    String value;
    int textColor;

    public DayTextView(Context context){
        super(context);
        this.context=context;
        init(null);
        value = "31";
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
//        plusBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.plus);
//        minusBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.minus);
//
//        plusRectDst=new Rect(10, 10, 210, 210);
//        minusRectDst=new Rect(400, 10, 600, 210);
//
//        if(attrs != null){
//            TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.MyView);
//            textColor=a.getColor(R.styleable.MyView_customTextColor, Color.RED);
//        }
//
//        listeners=new ArrayList<>();
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

        paint.setTextSize(80);
        paint.setColor(textColor);
        canvas.drawText(String.valueOf(value), 260, 150, paint);

    }

    public void setText(String s){
        value = s;
        Log.i("날짜는 ",  value);
    }
}
