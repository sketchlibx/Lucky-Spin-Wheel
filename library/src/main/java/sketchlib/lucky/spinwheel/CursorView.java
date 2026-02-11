package sketchlib.lucky.spinwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class CursorView extends View {

    private static final int DEFAULT_BODY = Color.WHITE;
    private static final int GOLD_PRIMARY = Color.parseColor("#FDF8A7");
    private static final int GOLD_SHADOW = Color.parseColor("#977335");
    private static final int SHADOW_COLOR = Color.parseColor("#70000000");

    private Paint bodyPaint, borderPaint, dotPaint, shinePaint;
    private Path path;

    private int mBodyColor = 0;
    private int mBorderColor = 0;
    private int mCenterColor = 0;

    public CursorView(Context context) {
        this(context, null);
    }

    public CursorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CursorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CursorView);
            mBodyColor = ta.getColor(R.styleable.CursorView_lsw_cursor_bodyColor, DEFAULT_BODY);
            mBorderColor = ta.getColor(R.styleable.CursorView_lsw_cursor_borderColor, 0);
            mCenterColor = ta.getColor(R.styleable.CursorView_lsw_cursor_centerColor, 0);
            ta.recycle();
        } else {
            mBodyColor = DEFAULT_BODY;
        }

        bodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bodyPaint.setStyle(Paint.Style.FILL);
        bodyPaint.setColor(mBodyColor);
        bodyPaint.setShadowLayer(12f, 0f, 8f, SHADOW_COLOR);
        setLayerType(LAYER_TYPE_SOFTWARE, bodyPaint);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(10f);
        borderPaint.setStrokeJoin(Paint.Join.ROUND);
        borderPaint.setStrokeCap(Paint.Cap.ROUND);

        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setShadowLayer(6f, 0f, 3f, SHADOW_COLOR);

        shinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shinePaint.setColor(Color.WHITE);
        shinePaint.setAlpha(140);

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float pad = 14f;
        path.reset();
        path.moveTo(w / 2f, pad);

        path.cubicTo(
                w + pad * 2f, h * 0.32f,
                w * 0.62f, h * 0.62f,
                w / 2f, h - pad
        );

        path.cubicTo(
                w * 0.38f, h * 0.62f,
                -pad * 2f, h * 0.32f,
                w / 2f, pad
        );
        path.close();

        if (mBorderColor != 0) {
            borderPaint.setShader(null);
            borderPaint.setColor(mBorderColor);
        } else {
            borderPaint.setShader(new LinearGradient(
                    0, 0, 0, h,
                    GOLD_PRIMARY,
                    GOLD_SHADOW,
                    Shader.TileMode.CLAMP
            ));
        }

        if (mCenterColor != 0) {
            dotPaint.setShader(null);
            dotPaint.setColor(mCenterColor);
        } else {
            dotPaint.setShader(new RadialGradient(
                    w / 2f, h * 0.30f, w * 0.13f,
                    GOLD_PRIMARY,
                    GOLD_SHADOW,
                    Shader.TileMode.CLAMP
            ));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, bodyPaint);
        canvas.drawPath(path, borderPaint);

        float cx = getWidth() / 2f;
        float cy = getHeight() * 0.30f;
        float r = getWidth() * 0.13f;

        canvas.drawCircle(cx, cy, r, dotPaint);

        if (mCenterColor == 0) {
            canvas.drawCircle(cx - r * 0.3f, cy - r * 0.3f, r * 0.35f, shinePaint);
        }
    }
}