package com.company.jk.pcoordinator.common.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.company.jk.pcoordinator.R;


public class DayTextView extends View {
    Context context;
    String value;
    int COLOR[];

    public DayTextView(Context context){
        super(context);
        this.context=context;
        this.setWillNotDraw(false);
        init(null);
    }
    public DayTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context=context;
        this.setWillNotDraw(false);
        init(attrs);
    }
    public DayTextView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.context=context;
        this.setWillNotDraw(false);
        init(attrs);
    }

    private void init(AttributeSet attrs){

        COLOR = new int[]{
                context.getResources().getColor(R.color.maintabledetail1),
                context.getResources().getColor(R.color.maintabledetail2),
                context.getResources().getColor(R.color.maintabledetail3),
                context.getResources().getColor(R.color.maintabledetail4),
                context.getResources().getColor(R.color.maintabledetail5),
                context.getResources().getColor(R.color.maintabledetail6),
                context.getResources().getColor(R.color.maintabledetail7),
                context.getResources().getColor(R.color.maintabledetail8),
                context.getResources().getColor(R.color.maintabledetail9),
                context.getResources().getColor(R.color.maintabledetail10),
                context.getResources().getColor(R.color.maintabledetail11),
                context.getResources().getColor(R.color.maintabledetail12),
                context.getResources().getColor(R.color.maintabledetail13),
                context.getResources().getColor(R.color.maintabledetail14),
                context.getResources().getColor(R.color.maintabledetail15),
                context.getResources().getColor(R.color.maintabledetail16),
                context.getResources().getColor(R.color.maintabledetail17),
                context.getResources().getColor(R.color.maintabledetail18),
                context.getResources().getColor(R.color.maintabledetail19),
                context.getResources().getColor(R.color.maintabledetail20),
                context.getResources().getColor(R.color.maintabledetail21),
                context.getResources().getColor(R.color.maintabledetail22),
                context.getResources().getColor(R.color.maintabledetail23),
                context.getResources().getColor(R.color.maintabledetail25),
                context.getResources().getColor(R.color.maintabledetail26),
                context.getResources().getColor(R.color.maintabledetail24),
                context.getResources().getColor(R.color.maintabledetail27),
                context.getResources().getColor(R.color.maintabledetail28),
                context.getResources().getColor(R.color.maintabledetail29),
                context.getResources().getColor(R.color.maintabledetail30),
                context.getResources().getColor(R.color.maintabledetail31),
        };

        if(attrs != null){
//            TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.MyView);
//            textColor=a.getColor(R.styleable.MyView_customTextColor, Color.RED);
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
        paint.setColor(COLOR[Integer.parseInt(value)]);
        canvas.drawCircle(67, 50, 50, paint);

        paint.setTextSize(60);

        if (value.substring(0,1).equals("0")){
            value = value.substring(1,2);
        }
//        paint.setColor(COLOR[Integer.parseInt(value) % 3]);
        paint.setColor(Color.WHITE);

        if (value.length() > 1){
            canvas.drawText(value, 32, 70, paint);
        }else {
            canvas.drawText(value, 49, 70, paint);
        }

//        Log.i("onDraw 날짜는 ",  value);

    }

    public void setText(String s){
        invalidate();   // onDraw 를 항상 콜 하기 위해서 반드시 해 줘야 함.
        value = s;
//        Log.i("날짜는 ",  value);
    }
}
