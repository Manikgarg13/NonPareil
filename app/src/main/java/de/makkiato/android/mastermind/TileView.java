package de.makkiato.android.mastermind;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.Vector;

public abstract class TileView extends View {
	List<Paint> colors = new Vector<Paint>();
	Paint emptyColor = new Paint();
	int[][] tileColors;
	boolean[][] isColorSet;
	int noLines = 0;
	int noPositions = 0;
	int xOffset = 0;
	int yOffset = 0;
	int tileSize = 0;
	final Paint paint = new Paint();
	Bitmap tileBmp;
	Bitmap tileBmpActive;
	private int activeLine = 0;

	public TileView(Context context, AttributeSet attrs) {
		super(context, attrs);//recieves parameter from patternview class and passes attributes and context of mastermind class and sends to super class
		initialize(context);
	}

	public TileView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int p = 0; p < noPositions; p++) {
			for (int l = 0; l < noLines; l++) {
				if (tileBmp != null) {
					canvas.drawBitmap(tileBmp, xOffset + p * tileSize, yOffset
							+ l * tileSize, paint);
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth = measureSize(widthMeasureSpec, noPositions
				* tileSize);
		int measuredHeight = measureSize(heightMeasureSpec, noLines * tileSize);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	private void initialize(Context context) {
		setupConstants(context);//calls  patterview class methond
		setupTileBitmaps(context);//calls function present in tileView
		setupColors(context);//set colous and calls funtion from tileview
	}

	protected void setupConstants(Context context) {
		Rect outRect = new Rect();
		getWindowVisibleDisplayFrame(outRect);
		try {
			noPositions = Integer.parseInt(context.getString(R.string.noPositions));//gets total no of positions from string.xml
			noLines = Integer.parseInt(context.getString(R.string.noLines));//same as above
			tileSize = (Math.min(outRect.height(), outRect.width()) - 15)
					/ (noPositions + 1);//decides the size for tiles in the game
		} catch (NumberFormatException ex) {
			tileSize = noPositions = noLines = 0;
		}
		xOffset = 0;
		yOffset = 0;
	}

	private void setupTileBitmaps(Context context) {// place 2 tiles on the layout
		Drawable drawable = context.getResources().getDrawable(R.drawable.tile);
		Bitmap bitmap = Bitmap.createBitmap(tileSize, tileSize,
				Bitmap.Config.ARGB_8888);//use bitmap , recourses classes to create a square like tile
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, tileSize, tileSize);
		drawable.draw(canvas);
		tileBmp = bitmap;

		drawable = context.getResources().getDrawable(R.drawable.tile_active);// for active line
		bitmap = Bitmap.createBitmap(tileSize, tileSize,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, tileSize, tileSize);
		drawable.draw(canvas);
		tileBmpActive = bitmap;
	}

	private void setupColors(Context context) {//takes colors values from colour.xml file
		tileColors = new int[noPositions][noLines];//color count
		isColorSet = new boolean[noPositions][noLines];//t or f
		for (int p = 0; p < noPositions; p++) {
			for (int l = 0; l < noLines; l++) {
				tileColors[p][l] = -1;
				isColorSet[p][l] = false;
			}
		}

		XmlResourceParser parser = context.getResources().getXml(R.xml.colors);
		int eventType;
		try {
			eventType = parser.getEventType();
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG) {
					if (parser.getName().equals("color")) {
						eventType = parser.next();
						if (eventType == XmlResourceParser.TEXT) {
							Paint p = new Paint();
							p.setColor(Color.parseColor(parser.getText()));
							p.setStyle(Style.FILL_AND_STROKE);
							colors.add(p);
						}
					} else if (parser.getName().equals("empty")) {
						eventType = parser.next();
						if (eventType == XmlResourceParser.TEXT) {
							emptyColor.setColor(Color.parseColor(parser
									.getText()));
							emptyColor.setStyle(Style.FILL);
						}
					}
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
		}
	}

	private int measureSize(int measureSpec, int defaultSize) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		// int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {
			result = defaultSize;
		} else {
			result = defaultSize;
		}
		return result;
	}

	public int[] getPosition(int x, int y) {//give the position in tiles
		return new int[] { (int) Math.floor(x / tileSize),
				(int) Math.floor(y / tileSize) };
	}

	public int getActiveLine() {//gives the location for each line
		return activeLine;
	}

	public int setInitialActiveLine() {//sets first line to start with
		int result;
		if (noLines > 0) {
			activeLine = noLines - 1;
			result = activeLine;
		} else {
			activeLine = 0;
			result = -1;
		}
		invalidate();//pre defined
		return result;
	}

	public int setNextLineActive() {//when one line is completed by user it set the next line
		int result;
		if (activeLine > 0) {
			activeLine--;
			result = activeLine;
		} else {
			activeLine = 0;
			result = -1;
		}
		invalidate();
		return result;
	}
	
	public abstract int reset();
}
