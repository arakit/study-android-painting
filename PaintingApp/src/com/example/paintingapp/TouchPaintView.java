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

	// �r���[�̃T�C�Y�̕ύX���ꂽ���ɌĂ΂��
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

	//�^�b�`���ꂽ���̃C�x���g�@
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch(event.getAction()){
		//�w�ŉ�������
		case MotionEvent.ACTION_DOWN:
			oldX = event.getX();
			oldY = event.getY();
			break;

		//�w����������
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
		//SD�J�[�h���}�E���g����Ă��邩�m�F
		if(!sdcardWriteReady()){
			Toast.makeText(getContext(),
					"SD�J�[�h������܂���B",
					Toast.LENGTH_SHORT).show();
			return ;
		}

		//SD�J�[�h����܂��I

		//SD�J�[�h�̃f�B���N�g���擾
		File file = new File(
				Environment
				.getExternalStorageDirectory()
				.getPath() );

		//SD�f�B���N�g���{ �t�@�C����
		//24354333.jpg
		File filename = new File(
				file.getAbsolutePath(),
				System.currentTimeMillis()+".jpg" );


		try{
			//�f�[�^��������
			FileOutputStream out = new FileOutputStream(filename);
			bitmap.compress(CompressFormat.JPEG,
					100, out);
			out.close();

			scanFile(filename);

			//����
			Toast.makeText(getContext(),
					"�ۑ�����������I�R(�M���L)/",
					Toast.LENGTH_SHORT).show();

		}catch(Exception ex){
			//���s
			Toast.makeText(getContext(),
					"�ۑ����s",
					Toast.LENGTH_SHORT).show();

		}



	}

	//SD�J�[�h���}�E���g����Ă��邩�m�F����
	private boolean sdcardWriteReady(){
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	//�m�点��
	public void scanFile(File file){
		MediaScannerConnection.scanFile(getContext(),
				new String[]{file.getAbsolutePath()},
				new String[]{"image/jpeg"}, null);
	}






}
