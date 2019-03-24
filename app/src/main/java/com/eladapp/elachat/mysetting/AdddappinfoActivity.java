package com.eladapp.elachat.mysetting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;
import com.eladapp.elachat.utils.StreamTools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.util.Base64;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.Random;
import android.os.Handler;
import android.os.Message;

import org.apache.commons.lang.StringUtils;
import org.elastos.carrier.exceptions.SystemException;

public class AdddappinfoActivity extends AppCompatActivity{
    ImageView dappinfo_img;
    ImageView btn_dappinfo_save;
    EditText appname;
    EditText dappimg_base64;
    EditText appshortname;
    EditText remark;
    TextView cate;
    TextView catename;
    TextView dappinfo_id;
    RelativeLayout dappinfo_cate_layout;
    private Db db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddappinfo);
        initview();
        dappinfo_id.setText("DAPPID：elachat_"+new Date().getTime()+""+getRandomString(5));
        dappinfo_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");//选择图片
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
        btn_dappinfo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dappisnull = judgenull(appname.getText().toString(),appshortname.getText().toString(),remark.getText().toString(),cate.getText().toString(),dappimg_base64.getText().toString());
                if(dappisnull.equals("1")){
                    Toast.makeText(getApplicationContext(), "DAPP的名称不能空！", Toast.LENGTH_SHORT).show();
                }else if(dappisnull.equals("2")){
                    Toast.makeText(getApplicationContext(), "DAPP的简称不能空！", Toast.LENGTH_SHORT).show();
                }else if(dappisnull.equals("3")){
                    Toast.makeText(getApplicationContext(), "DAPP的简介不能为空！", Toast.LENGTH_SHORT).show();
                }else if(dappisnull.equals("4")){
                    Toast.makeText(getApplicationContext(), "所属类型不能为空！", Toast.LENGTH_SHORT).show();
                }else if(dappisnull.equals("5")){
                    Toast.makeText(getApplicationContext(), "DAPP图片不能为空！", Toast.LENGTH_SHORT).show();
                }else{
                    //直接更新数据库和更新服务器数据
                    addcateinfo(dappinfo_id.getText().toString(),appname.getText().toString(),cate.getText().toString(),appshortname.getText().toString(),remark.getText().toString(),dappimg_base64.getText().toString());
                    //POST提交到服务器端
                    String dappurl = "http://test.eladevp.com/index.php/Home/Dapplist/adddappinfo";
                    System.out.println("地址："+dappurl);
                    adddappinfo(dappurl,dappinfo_id.getText().toString(),appname.getText().toString(),appshortname.getText().toString(),remark.getText().toString(),dappimg_base64.getText().toString(),cate.getText().toString(),"");
                }
            }
        });
        dappinfo_cate_layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AdddappinfoActivity.this, DappcateinfoActivity.class), 2);
            }
        });
    }
    //更新数据库
    public boolean addcateinfo(String appid,String dappname,String cateid,String dappshortname,String remarks,String dappimgs){
        db = new Db();
        boolean rs = db.adddappinfo(appid,"",dappname,cateid,"",dappshortname,remarks,dappimgs,"");
        return rs;
    }
    //网络获取数据处理成适合LISTVIEW展示的数据
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            System.out.println("返回结果:"+msg.toString());
            if (msg.obj.equals("adddappinfo")) {
                if(msg.what==1){
                    Bundle b = msg.getData();
                    String res = b.getString("res");
                    if(res.equals("1")){
                        finish();
                    }
                }else{

                }
            }
        }
    };
    //构建POST方法获取指定的DAPP信息列表
    public void adddappinfo(String dappurl,String appid,String appname,String appshortname,String remark,String dappimg,String cate,String menujson){
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(dappurl);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    String data = "{\"dappid\":\""+ appid +"\",\"dappname\":\""+ appname +"\",\"did\":\"\",\"cate\":\""+ cate +"\",\"pubkey\":\"\",\"appshortname\":\""+ appshortname +"\",\"remark\":\""+ remark +"\",\"dappimg\":\""+ dappimg +"\",\"menujson\":\""+ menujson +"\"}";
                    conn.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(data.getBytes());
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream in = conn.getInputStream();
                        String content = StreamTools.readString(in);
                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        System.out.println("DAPP列表："+content.toString());
                        bundle.putString("res",content.toString());
                        msg.setData(bundle);
                        msg.what = 1;
                        msg.obj = "adddappinfo";
                        handler.sendMessage(msg);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = "adddappinfo";
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Message msg = Message.obtain();
                    System.out.println("错误："+e.getMessage());
                    msg.what = 0;
                    msg.obj = "adddappinfo";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    public static String GetImageStrFromPath(String imgPath) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String encode = Base64.encodeToString(data, Base64.DEFAULT);
        System.out.println("新的："+encode.toString());
        return encode;
    }
    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
       // ac.getContentResolver().qu
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return compressImage(bitmap);//再进行质量压缩
    }
    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    /**
     * 通过Base32将Bitmap转换成Base64字符串
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 100, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    //Base64转化为图片
    public Bitmap Base64ToBitmap(String str){
        byte[] bytes = Base64.decode(str,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
    //判断是否为空
    public String judgenull(String appname,String appshortname,String remark,String cate,String dappimg){
        String dappnull = "0";
        if(appname.equals("")){
            dappnull = "1";
        }else if(appshortname.equals("")){
            dappnull = "2";
        }else if(remark.equals("")){
            dappnull = "3";
        }else if(cate.equals("")){
            dappnull = "4";
        }else if(dappimg.equals("")){
            dappnull = "5";
        }
        return dappnull;
    }
    String path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if ("file".equalsIgnoreCase(uri.getScheme())) {
                    path = uri.getPath();
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    path = getPath(this, uri);
                } else {
                    path = getRealPathFromURI(uri);
                }
                try {
                    Bitmap dappinfoimg = getBitmapFormUri(this,uri);
                    dappinfo_img.setImageBitmap(dappinfoimg);
                    dappimg_base64.setText(Bitmap2StrByBase64(dappinfoimg));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == 2){
            String cateid = data.getExtras().getString("cateid");
            String catenames = data.getExtras().getString("catename");
            cate.setText(cateid);
            catename.setText(catenames);
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(null!=cursor&&cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }
    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public void initview(){
        dappinfo_img = (ImageView)findViewById(R.id.dappinfo_img);
        btn_dappinfo_save = (ImageView)findViewById(R.id.btn_dappinfo_save);
        appname = (EditText)findViewById(R.id.dappinfo_dappname);
        appshortname = (EditText)findViewById(R.id.dappinfo_dappshortname);
        remark = (EditText)findViewById(R.id.dappinfo_dappsinstr);
        dappinfo_id = (TextView)findViewById(R.id.dappinfo_id);
        cate = (TextView)findViewById(R.id.dappinfo_dappcate);
        catename = (TextView)findViewById(R.id.dappinfo_dappcatename);
        dappinfo_cate_layout = (RelativeLayout)findViewById(R.id.dappinfo_cate_layout);
        dappimg_base64 = (EditText)findViewById(R.id.dappimg_base64);
    }
    public void back(View view){
        finish();
    }

}