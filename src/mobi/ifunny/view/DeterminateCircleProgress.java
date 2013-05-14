package mobi.ifunny.view;

import mobi.ifunny.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class DeterminateCircleProgress extends View {

	private Drawable mDrawableBack;
	private Drawable mDrawableFront;
	private Paint maskPaint;
	private TextPaint tp;
	private Bitmap mask;
	private Canvas maskCanvas;
	private Bitmap result;
	private Canvas resultCanvas;

	private int m_width;
	private int m_height;
	private Rect m_rect;

	private static final Config defConfig = Config.ARGB_8888;

	private int percent = 0;

	public DeterminateCircleProgress(Context context) {
		super(context);
		init();
	}

	public DeterminateCircleProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DeterminateCircleProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		mDrawableBack = getContext().getResources().getDrawable(R.drawable.circle_progress_bars);
		mDrawableFront = getContext().getResources().getDrawable(
				R.drawable.circle_progress_bars_filled);

		m_width = mDrawableBack.getIntrinsicWidth();
		m_height = mDrawableBack.getIntrinsicHeight();
		m_rect = new Rect(0, 0, m_width, m_height);
		mDrawableBack.setBounds(m_rect);
		mDrawableFront.setBounds(m_rect);

		mask = Bitmap.createBitmap(m_width, m_height, defConfig);
		maskCanvas = new Canvas(mask);

		result = Bitmap.createBitmap(m_width, m_height, defConfig);
		resultCanvas = new Canvas(result);

		tp = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
		tp.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
				getContext().getString(R.string.condensedBold)));
		tp.setTextAlign(Align.LEFT);
		tp.setStrokeWidth(3);
		tp.setTextSize(getContext().getResources().getDimension(R.dimen.default_fsize));
		tp.setStyle(Style.FILL);
		tp.setColor(Color.WHITE);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(m_width, m_height);
	};

	@Override
	protected void onDraw(Canvas canvas) {
		maskCanvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		resultCanvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		//background layer
		mDrawableBack.draw(canvas);
		//layer with mask
		drawMaskForCurrentPercent();
		drawResultLayer(canvas);
		//text in center
		drawTextForCurrentPercent(canvas);
		//
	}

	private void drawMaskForCurrentPercent() {
		maskCanvas.drawArc(new RectF(m_rect), 0, 360 * percent / 100, true, new Paint());
	}

	private void drawResultLayer(Canvas canvas) {
		mDrawableFront.draw(resultCanvas);
		resultCanvas.drawBitmap(mask, 0, 0, maskPaint);
		canvas.drawBitmap(result, 0, 0, null);
	}

	private void drawTextForCurrentPercent(Canvas canvas) {
		StaticLayout layout = new StaticLayout(percent + "%", tp, m_width * 2 / 3,
				Alignment.ALIGN_CENTER, 1, 0, false);
		canvas.save();
		canvas.translate((m_width - layout.getWidth()) / 2, (m_height - layout.getHeight()) / 2);
		layout.draw(canvas);
		canvas.restore();
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
		invalidate();
	}

}
