package com.olivier.button;


        import android.animation.ObjectAnimator;
        import android.content.Context;
        import android.content.res.TypedArray;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.util.AttributeSet;
        import android.util.TypedValue;
        import android.widget.ImageView;
        import com.olivier.R;

public class CircleButton extends ImageView {

    private static final int PRESSED_COLOR_LIGHTUP = 255 / 25;
    private static final int PRESSED_RING_ALPHA = 75;
    private static final int DEFAULT_PRESSED_RING_WIDTH_DIP = 4;
    private static final int ANIMATION_TIME_ID = android.R.integer.config_shortAnimTime;

    private int centerY;
    private int centerX;
    private int outerRadius;
    private int pressedRingRadius;
    private String text;
    private int textSize;

    private Paint circlePaint;
    private Paint focusPaint;
    private Paint textPaint;


    private float animationProgress;

    private int pressedRingWidth;
    private int defaultColor = Color.BLACK;
    private int pressedColor;
    private ObjectAnimator pressedAnimator;

    public CircleButton(Context context) {
        super(context);
        android.util.Log.d("Ugo","contrcutor 1");
        init(context, null);
    }

    public CircleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        android.util.Log.d("Ugo","contrcutor 2");
        init(context, attrs);
    }

    public CircleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        android.util.Log.d("Ugo","contrcutor 3");
        init(context, attrs);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);

        if (circlePaint != null) {
            circlePaint.setColor(pressed ? pressedColor : defaultColor);
        }

        if (pressed) {
            showPressedRing();
        } else {
            hidePressedRing();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        android.util.Log.d("Ugo","onDraw");
        canvas.drawCircle(centerX, centerY, pressedRingRadius + animationProgress, focusPaint);
        canvas.drawCircle(centerX, centerY, outerRadius - pressedRingWidth, circlePaint);

        /*
         * dessin du texte
         *
         */
        android.util.Log.d("UGO","onDraw textSize="+textSize);
        android.util.Log.d("UGO","onDraw color="+Color.BLACK);
        android.util.Log.d("UGO","onDraw width="+canvas.getWidth()+" height="+canvas.getHeight());

        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(textSize);

        canvas.drawText(text, 15, canvas.getHeight()/2, textPaint);

        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        outerRadius = Math.min(w, h) / 2;
        pressedRingRadius = outerRadius - pressedRingWidth - pressedRingWidth / 2;
    }

    public float getAnimationProgress() {
        return animationProgress;
    }

    public void setAnimationProgress(float animationProgress) {
        this.animationProgress = animationProgress;
        this.invalidate();
    }

    public void setColor(int color) {
        this.defaultColor = color;
        this.pressedColor = getHighlightColor(color, PRESSED_COLOR_LIGHTUP);

        circlePaint.setColor(defaultColor);
        focusPaint.setColor(defaultColor);

        focusPaint.setAlpha(PRESSED_RING_ALPHA);

        textPaint.setColor(Color.BLACK);

        this.invalidate();
    }

    private void hidePressedRing() {
        pressedAnimator.setFloatValues(pressedRingWidth, 0f);
        pressedAnimator.start();
    }

    private void showPressedRing() {
        pressedAnimator.setFloatValues(animationProgress, pressedRingWidth);
        pressedAnimator.start();
    }

    private void init(Context context, AttributeSet attrs) {
        android.util.Log.d("UGO","starting circle button init");
        this.setFocusable(true);
        this.setScaleType(ScaleType.CENTER_INSIDE);
        setClickable(true);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);

        focusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        focusPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.STROKE);

        pressedRingWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PRESSED_RING_WIDTH_DIP, getResources()
                .getDisplayMetrics());

        int color = Color.CYAN;
        if (attrs != null) {
            android.util.Log.d("UGO","found special attributes");
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleButton);
            color = a.getColor(R.styleable.CircleButton_cb_color, color);

            android.util.Log.d("UGO","color is "+color);
            pressedRingWidth = (int) a.getDimension(R.styleable.CircleButton_cb_pressed_ring_width, pressedRingWidth);
            text = (String)a.getString(R.styleable.CircleButton_cb_text);
            textSize = a.getInt(R.styleable.CircleButton_cb_text_size,10);
            android.util.Log.d("UGO","textSize is "+textSize);
            a.recycle();
        }

        android.util.Log.d("Ugo","setting color ::"+color);
        setColor(color);

        focusPaint.setStrokeWidth(pressedRingWidth);
        final int pressedAnimationTime = getResources().getInteger(ANIMATION_TIME_ID);
        pressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 0f);
        pressedAnimator.setDuration(pressedAnimationTime);
    }

    private int getHighlightColor(int color, int amount) {
        return Color.argb(Math.min(255, Color.alpha(color)), Math.min(255, Color.red(color) + amount),
                Math.min(255, Color.green(color) + amount), Math.min(255, Color.blue(color) + amount));
    }
}