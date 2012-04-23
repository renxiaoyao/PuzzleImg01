package com.puzzle.main;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author renxiaoyao
 * */
public class PuzzleImgActivity extends Activity {
    /** Called when the activity is first created. */
	
	private static int SELECT_PICTURE;
    private File tempFile;
    Button button01;
    Button button02;
    Button button03;
    Button button04;
    Button button05;
    Button button06;
    Button button07;
    Button curBtn;
    private Bitmap bitmap = null;  
    private ImageView image;
    String curBmp = "00";
    List<Bitmap> bmpList = null;
    File sdcardDir;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);// remove title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        createDir();//1.sdcardDir.getPath()+"/pintuimgs";
        
        //2.Get displayMetrics
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        String strMetrics = "Display metrics:" + dm.widthPixels + "*" + dm.heightPixels;
        Toast.makeText(PuzzleImgActivity.this, strMetrics, Toast.LENGTH_SHORT).show();
        
        this.tempFile=new File("/sdcard/pintuimgs/temp/temp.jpg");
        button01 = (Button) findViewById(R.id.btn01);
        button02 = (Button) findViewById(R.id.btn02);
        button03 = (Button) findViewById(R.id.btn03);
        button04 = (Button) findViewById(R.id.btn04);
        button05 = (Button) findViewById(R.id.btn05);
        button06 = (Button) findViewById(R.id.btn06);
        button07 = (Button) findViewById(R.id.button07);
        
        OnClickListener imgViewBtnLisn = new Button.OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ad.start();
				int btnId = v.getId();
				switch(btnId){
				case R.id.btn01:
					curBtn = button01;
					Toast.makeText(PuzzleImgActivity.this, "R.id.btn01:", Toast.LENGTH_SHORT).show();
					break;
				case R.id.btn02:
					curBtn = button02;
					break;
				case R.id.btn03:
					curBtn = button03;
					break;
				case R.id.btn04:
					curBtn = button04;
			    	break;
				case R.id.btn05:
					curBtn = button05;
					break;
				case R.id.btn06:
					curBtn = button06;
					break;
				}
				
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                
//                intent.putExtra("aspectX", 1);
//                intent.putExtra("aspectY", 1);//X:Y=4:3
                
                tempFile=new File("/sdcard/pintuimgs/temp/temp" + btnId + ".jpg");
                intent.putExtra("output", Uri.fromFile(tempFile));
                intent.putExtra("outputFormat", "JPEG");

                startActivityForResult(Intent.createChooser(intent, "Select Img"),
                        SELECT_PICTURE);
			}
		};
        
		OnClickListener saveImgBtnLisn = new Button.OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tempBtn = R.id.btn01 - 1;
				
				bmpList = new ArrayList();
				for(int i=0; i<6; i++){
					tempBtn++;
					bitmap = BitmapFactory.decodeFile("/sdcard/pintuimgs/temp/temp" + tempBtn + ".jpg");
					bitmap = ThumbnailUtils.extractThumbnail(bitmap, 240, 180);    
//					bmpList.add(bitmap); 
					saveBitmapToSDCard(bitmap,i+"");
				}
				Log.e(">>>>>bmpList:", bmpList.size()+"");
				// Draw img.
				SaveBitmap();
			}
		};
		
		button01.setOnClickListener(imgViewBtnLisn);
		button02.setOnClickListener(imgViewBtnLisn);
		button03.setOnClickListener(imgViewBtnLisn);
		button04.setOnClickListener(imgViewBtnLisn);
		button05.setOnClickListener(imgViewBtnLisn);
		button06.setOnClickListener(imgViewBtnLisn);
		button07.setOnClickListener(saveImgBtnLisn);
//        setContentView(button);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
            	curBtn.setBackgroundDrawable(Drawable.createFromPath(tempFile
                        .getAbsolutePath()));
            }
        }
    }
    
	/**
	 * @author renxiaoyao
	 * */
	public void createDir(){
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			// Create a directory object.
			sdcardDir =Environment.getExternalStorageDirectory();
			//Get the SDCard path.
			String path=sdcardDir.getPath()+"/pintuimgs";
			String pathTemp=sdcardDir.getPath()+"/pintuimgs/temp";
			String pathBmp=sdcardDir.getPath()+"/pintuimgs/bmp";
			File path2 = new File(pathTemp);
			File path3 = new File(pathBmp);
			
			if (!path2.exists()) {
				//If not exist , create it.
				path2.mkdirs();
				Toast.makeText(PuzzleImgActivity.this, "Save img in:" + path, Toast.LENGTH_SHORT).show();
//				setTitle("paht ok,path:"+path);
			}
			
			if (!path3.exists()) {
				path3.mkdirs();
			}
		}else{
			setTitle("Read SD Card error!");
			return;
		}
	}
	
	
	public static void saveBitmapToSDCard(Bitmap bmp, String strPath){
        if (null != bmp && null != strPath && !strPath.equalsIgnoreCase("")){
        	FileOutputStream fos = null;  
            try {  
                fos = new FileOutputStream("/sdcard/pintuimgs/bmp/" + strPath + ".jpg");  
                if (fos != null) {  
                	bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);  
                    fos.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            } 
        }
    }
	
	public void SaveBitmap(){
		Bitmap bitmap = Bitmap.createBitmap(480, 540, Config.ARGB_8888);  
		Canvas canvas = new Canvas(bitmap);
		//Get background;
		Bitmap bmps = BitmapFactory.decodeResource(getResources(), R.drawable.grid02);
		canvas.drawBitmap(bmps, 0, 0, null);
		
		Bitmap tempBmp ;
		for(int i=0; i<6; i++){
			tempBmp = BitmapFactory.decodeFile("/sdcard/pintuimgs/bmp/" + i + ".jpg");
			//Get the bmps
			int left = (i%2)*240;
			int top = ( i/2 )*180;
			canvas.drawBitmap(tempBmp, left, top, null);
			Log.e(">>>>>left+top",""+left+","+top);
		}
		
		//Save all layers.
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		//Save path.
		Date date=new Date(); 
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddhhmmss"); 
		curBmp = df.format(date)+ "" + Math.round(Math.random()*10); 
		Log.e(">>>>>>>>curbmp:",curBmp);
		File file = new File("/sdcard/pintuimgs/");
		if(!file.exists())
			file.mkdirs();
		try {
			Log.e(">>>>>>>>savePath:",file.getPath() + "/" + curBmp + ".jpg");
			FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + "/"+ curBmp + ".jpg");
			Toast.makeText(PuzzleImgActivity.this, "Save img in: sdcard/pintuimgs/" + curBmp + ".jpg", Toast.LENGTH_SHORT).show();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}