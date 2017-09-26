package com.podarbetweenus.Activities;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class SqreRoundedImage extends ImageView {
	private Paint paintBorder;
	private int borderWidth = 2;
	
	
	public SqreRoundedImage(Context context) {
	    super(context);
	    paintBorder = new Paint();
		setBorderColor(Color.WHITE);
		paintBorder.setAntiAlias(true);		

	}

	public SqreRoundedImage(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	public SqreRoundedImage(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
	    if (drawable == null) {
	        return;
	    }

	    if (getWidth() == 0 || getHeight() == 0) {
	        return; 
	    }
	    try {
	    	
	    	//int circleCenter = viewWidth / 2;
	    	
			Bitmap b =  ((BitmapDrawable) drawable).getBitmap() ;
			Bitmap bitmap = b.copy(Config.ARGB_8888, true);
			int w = getWidth(), h = getHeight();
			Bitmap roundBitmap =  getCroppedBitmap(bitmap, w);
			//canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter + borderWidth, paintBorder);
			canvas.drawBitmap(roundBitmap, 0,0, paintBorder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setBorderWidth(int borderWidth)
	{
		this.borderWidth = borderWidth;
		this.invalidate();
	}
	public void setBorderColor(int borderColor)
	{		
		if(paintBorder != null)
			paintBorder.setColor(borderColor);
		
		this.invalidate();
	}
	
	
	
        public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        	
		    Bitmap output = null;
			try {
				Bitmap sbmp;
				if(bmp.getWidth() != radius || bmp.getHeight() != radius)
				    sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
				else
				    sbmp = bmp;
				output = Bitmap.createBitmap(sbmp.getWidth(),
						sbmp.getHeight(), Config.ARGB_8888);
				Canvas canvas = new Canvas(output);

				final int color = 0xffa19774;
				final Paint paint = new Paint();
  /*  final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
				final RectF rectf = new RectF(0, 0, sbmp.getWidth(), sbmp.getHeight());*/

				final Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
				final RectF rectf = new RectF(rect);
				int roundPx = radius;
				
				paint.setAntiAlias(true);
				paint.setFilterBitmap(true);
				paint.setDither(true);
				canvas.drawARGB(0, 0, 0, 0);
				paint.setColor(Color.parseColor("#BAB399"));
				
				Log.e("RAdious...", sbmp.getWidth() / 2 + 0.7f + "");
				canvas.drawRoundRect(rectf, roundPx, roundPx, paint);
				//canvas.drawRoundRect(rectf, sbmp.getWidth() / 2+0.7f,20.5f, paint);
				
				/*canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
				        sbmp.getWidth() / 2+0.1f, paint);*/
				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
				canvas.drawBitmap(sbmp, rect, rectf, paint);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    //canvas.draB
        return output;
	}

	}