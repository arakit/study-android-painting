package com.example.paintingapp;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class TouchPaintView extends View {

	private Paint paint;

	private Bitmap bitmap;
	private Canvas canvas;

	public TouchPaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.LTGRAY);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(15);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
	}

	// ビューのサイズの変更された時に呼ばれる
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);


	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(bitmap, 0, 0, paint);

	}

	private float oldX = 0;
	private float oldY = 0;

	//タッチされた時のイベント　
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch(event.getAction()){
		//指で押した時
		case MotionEvent.ACTION_DOWN:
			oldX = event.getX();
			oldY = event.getY();
			break;

		//指が動いた時
		case MotionEvent.ACTION_MOVE:

			canvas.drawLine(oldX, oldY, event.getX(), event.getY(), paint);
			oldX = event.getX();
			oldY = event.getY();
			invalidate();

			break;
		}

		return true;
	}



	public void saveToFile(){
		//SDカードがマウントされているか確認
		if(!sdcardWriteReady()){
			Toast.makeText(getContext(),
					"SDカードがありません。",
					Toast.LENGTH_SHORT).show();
			return ;
		}

		//SDカードあります！

		//SDカードのディレクトリ取得
		File file = new File(
				Environment
				.getExternalStorageDirectory()
				.getPath() );

		//SDディレクトリ＋ ファイル名
		//24354333.jpg
		File filename = new File(
				file.getAbsolutePath(),
				System.currentTimeMillis()+".jpg" );


		try{
			//データ書き込み
			FileOutputStream out = new FileOutputStream(filename);
			bitmap.compress(CompressFormat.JPEG,
					100, out);
			out.close();

			scanFile(filename);

			//成功
			Toast.makeText(getContext(),
					"保存成功したよ！ヽ(｀▽´)/",
					Toast.LENGTH_SHORT).show();

		}catch(Exception ex){
			//失敗
			Toast.makeText(getContext(),
					"保存失敗",
					Toast.LENGTH_SHORT).show();

		}



	}

	//SDカードがマウントされているか確認する
	private boolean sdcardWriteReady(){
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	//知らせる
	public void scanFile(File file){
		MediaScannerConnection.scanFile(getContext(),
				new String[]{file.getAbsolutePath()},
				new String[]{"image/jpeg"}, null);
	}






}
