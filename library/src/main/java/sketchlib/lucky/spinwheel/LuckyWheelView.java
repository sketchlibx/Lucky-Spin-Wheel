package sketchlib.lucky.spinwheel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import sketchlib.lucky.spinwheel.LuckyItem;

public class LuckyWheelView extends View {
	
	// --- ORIGINAL PREMIUM COLORS ---
	private static final int COLOR_LIGHT = Color.parseColor("#0F1C36");
	private static final int COLOR_DARK = Color.parseColor("#051124");
	
	private static final int COLOR_TEXT_PRIMARY = Color.parseColor("#FFFFFF");
	private static final int COLOR_TEXT_GOLD = Color.parseColor("#FACC15");
	
	private static final int GOLD_REAL_1 = Color.parseColor("#FFF3B0");
	private static final int GOLD_REAL_2 = Color.parseColor("#FACC15");
	private static final int GOLD_REAL_3 = Color.parseColor("#B58A00");
	
	private static final int BORDER_1 = Color.parseColor("#200E35");
	private static final int BORDER_2 = Color.parseColor("#381B5D");
	private static final int BORDER_3 = Color.parseColor("#582C8E");
	
	private List<LuckyItem> mData = new ArrayList<>();
	private Paint mSlicePaint, mTextPaint, mInnerGoldPaint, mOuterBorderPaint;
	private RectF mWheelRect = new RectF();
	private float mRadius, mCenterX, mCenterY;
	
	private boolean isRunning = false;
	private SpinListener mSpinListener;
	private ObjectAnimator objectAnimator;
	private long mDuration = 5000;
	private int mRoundOfNumber = 8;
	
	public interface SpinListener {
		void onRotateStart();
		void onRotateEnd(int resultIndex);
	}
	
	public LuckyWheelView(Context context) { this(context, null); }
	public LuckyWheelView(Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }
	public LuckyWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs) {
		// Paints Init
		mSlicePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mSlicePaint.setStyle(Paint.Style.FILL);
		
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
		mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		mTextPaint.setShadowLayer(6f, 0f, 3f, Color.parseColor("#90000000"));
		
		mInnerGoldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mInnerGoldPaint.setStyle(Paint.Style.STROKE);
		mInnerGoldPaint.setStrokeWidth(dpToPx(4));
		
		mOuterBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mOuterBorderPaint.setStyle(Paint.Style.STROKE);
		mOuterBorderPaint.setStrokeWidth(dpToPx(16));
		
		setLayerType(LAYER_TYPE_SOFTWARE, mTextPaint);
	}
	
	@Override
	protected void onMeasure(int wSpec, int hSpec) {
		int w = MeasureSpec.getSize(wSpec);
		int h = MeasureSpec.getSize(hSpec);
		int size = Math.min(w, h);
		if (size == 0) size = (int) dpToPx(300);
		setMeasuredDimension(size, size);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		int size = Math.min(w, h);
		mCenterX = w / 2f;
		mCenterY = h / 2f;
		
		mRadius = size / 2f - dpToPx(20); // Padding adjustment
		
		mWheelRect.set(mCenterX - mRadius, mCenterY - mRadius,
		mCenterX + mRadius, mCenterY + mRadius);
		
		// --- RESTORED GRADIENTS ---
		Shader goldShader = new SweepGradient(
		mCenterX, mCenterY,
		new int[]{GOLD_REAL_1, GOLD_REAL_2, GOLD_REAL_3, GOLD_REAL_1},
		new float[]{0f, 0.25f, 0.55f, 1f}
		);
		mInnerGoldPaint.setShader(goldShader);
		
		Shader borderShader = new LinearGradient(
		mCenterX - mRadius, mCenterY,
		mCenterX + mRadius, mCenterY,
		new int[]{BORDER_1, BORDER_2, BORDER_3},
		new float[]{0f, 0.5f, 1f},
		Shader.TileMode.CLAMP
		);
		mOuterBorderPaint.setShader(borderShader);
		
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mData == null || mData.isEmpty()) return;
		
		int slices = mData.size();
		float sweep = 360f / slices;
		float start = -90f;
		
		mTextPaint.setTextSize(mRadius * 0.14f);
		
		for (int i = 0; i < slices; i++) {
			LuckyItem item = mData.get(i);
			boolean lightSlice = i % 2 == 0;
			
			// Use item color if provided, otherwise use DEFAULT THEME
			if (item.color != 0) {
				mSlicePaint.setColor(item.color);
			} else {
				mSlicePaint.setColor(lightSlice ? COLOR_LIGHT : COLOR_DARK);
			}
			
			canvas.drawArc(mWheelRect, start + sweep * i, sweep, true, mSlicePaint);
			
			// Text Color logic
			if (item.color != 0) {
				mTextPaint.setColor(Color.WHITE);
			} else {
				mTextPaint.setColor(lightSlice ? COLOR_TEXT_PRIMARY : COLOR_TEXT_GOLD);
			}
			
			drawTextAndIcon(canvas, start + sweep * i, sweep, item.topText, item.icon);
		}
		
		// Draw Premium Borders
		float innerStroke = mInnerGoldPaint.getStrokeWidth();
		float outerStroke = mOuterBorderPaint.getStrokeWidth();
		
		float innerRadius = mRadius + innerStroke / 2f;
		canvas.drawCircle(mCenterX, mCenterY, innerRadius, mInnerGoldPaint);
		
		float outerRadius = innerRadius + innerStroke / 2f + outerStroke / 2f - dpToPx(2);
		canvas.drawCircle(mCenterX, mCenterY, outerRadius, mOuterBorderPaint);
	}
	
	private void drawTextAndIcon(Canvas canvas, float startAngle, float sweepAngle, String text, int iconRes) {
		float angle = startAngle + sweepAngle / 2f;
		double rad = Math.toRadians(angle);
		
		float h = mRadius;
		
		// Text Position
		float textRadius = h * 0.75f;
		float x = (float) (mCenterX + textRadius * Math.cos(rad));
		float y = (float) (mCenterY + textRadius * Math.sin(rad));
		
		// Draw Text
		if (text != null) {
			float textHeight = mTextPaint.descent() - mTextPaint.ascent();
			canvas.drawText(text, x, y + textHeight / 4, mTextPaint);
		}
		
		// Draw Icon
		if (iconRes != 0) {
			Drawable icon = ContextCompat.getDrawable(getContext(), iconRes);
			if (icon != null) {
				float iconRadius = h * 0.45f;
				float xImg = (float) (mCenterX + iconRadius * Math.cos(rad));
				float yImg = (float) (mCenterY + iconRadius * Math.sin(rad));
				
				int iconSize = (int) (mRadius * 0.10f); 
				icon.setBounds((int)xImg - iconSize, (int)yImg - iconSize, 
				(int)xImg + iconSize, (int)yImg + iconSize);
				
				canvas.save();
				canvas.rotate(angle + 90, xImg, yImg); 
				icon.draw(canvas);
				canvas.restore();
			}
		}
	}
	
	public void startLuckyWheelWithTargetIndex(int index) {
		if (isRunning || mData.isEmpty()) return;
		
		int slices = mData.size();
		float sliceAngle = 360f / slices;
		
		float current = getRotation() % 360f;
		if (current < 0) current += 360f;
		
		float target = (360f * mRoundOfNumber)
		+ (360f - (index * sliceAngle) - (sliceAngle / 2f))
		- current;
		
		objectAnimator = ObjectAnimator.ofFloat(this, "rotation", getRotation(), getRotation() + target);
		objectAnimator.setDuration(mDuration);
		objectAnimator.setInterpolator(new DecelerateInterpolator());
		
		objectAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				isRunning = true;
				if (mSpinListener != null) mSpinListener.onRotateStart();
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				isRunning = false;
				if (mSpinListener != null) mSpinListener.onRotateEnd(index);
			}
		});
		
		objectAnimator.start();
	}
	
	public void setData(List<LuckyItem> data) { this.mData = data; invalidate(); }
	public void setRound(int rounds) { this.mRoundOfNumber = rounds; }
	public void setSpinListener(SpinListener listener) { this.mSpinListener = listener; }
	
	private float dpToPx(float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
}