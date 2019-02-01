package com.eladapp.elachat.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eladapp.elachat.application.CloudchatApp;
import com.eladapp.elachat.db.Db;
import com.eladapp.elachat.utils.MediaUtils;
import com.eladapp.elachat.walletspv.AssetchatsendconfirmActivity;
import com.lqr.emoji.EmotionLayout;
import com.lqr.emoji.IEmotionExtClickListener;
import com.lqr.emoji.IEmotionSelectedListener;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.eladapp.elachat.R;
import org.ela.Carrier.Chatcarrier;
import org.elastos.carrier.UserInfo;
import org.elastos.carrier.session.Stream;

public class ChatActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private EditText edt_msg;
    private Button btn_send;
    private ImageView chataddmore;
    private ImageView chatmotion;
    private ImageView transimg;
    private ImageView transvideo;
    private ImageView chatvoice;
    private List<MsgEntity> list;//存放消息实体的集合
    private LinearLayout bottommenu;
    private LinearLayout motionmenu;
    private LinearLayout audio_layouts;
    private EmotionLayout mElEmotion;
    private static Context mcontext;
    private Button menupopdialog;
    private  MsgAdapter msgAdapter;
    private String frienduserid;
    private Chatcarrier chatcarrier = new Chatcarrier();
    private Db db = new Db();
    private static String[] PERMISSIONS = {
            "android.permission.RECORD_AUDIO",
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE"
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private int type;
    private TextView mic,info;
    private ImageView micIcon;
    private MediaUtils mediaUtils;
    private boolean isCancel;
    private Chronometer chronometer;
    private RelativeLayout audioLayout;
    private String duration;
    private TextView btn_send_voice;
    private EditText edt_send_msg;
    private ImageView chatkeyboard;
    private ImageView zhuanzhang;
    private EditText toaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        frienduserid = this.getIntent().getStringExtra("friendId");
        db.updatemessagelast(frienduserid);
        initView();//初始化控件
        mediaUtils = new MediaUtils(this);
        mediaUtils.setRecorderType(MediaUtils.MEDIA_AUDIO);
        mediaUtils.setTargetDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
        mediaUtils.setTargetName(UUID.randomUUID() + ".m4a");
        mic = (TextView) findViewById(R.id.tv_mic);
        info = (TextView) findViewById(R.id.tv_info);
        btn_send_voice.setOnTouchListener(touchListener);
        chronometer = (Chronometer) findViewById(R.id.time_display);
        chronometer.setOnChronometerTickListener(tickListener);
        micIcon = (ImageView) findViewById(R.id.mic_icon);
        //audioLayout = (LinearLayout)findViewById(R.id.audio_layouts);

        chatcarrier.sendmessage(frienduserid,"getassetmessage:");
        if(getActionBar()!=null){
            getActionBar().hide();
        }
        initMsg();//模拟消息
        IntentFilter filter = new IntentFilter(CloudchatApp.action);
        registerReceiver(broadcastReceiver, filter);
        msgAdapter=new MsgAdapter(this,list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(msgAdapter);
        msgAdapter.notifyItemRangeChanged(0,list.size()-1);
        recyclerView.scrollToPosition(list.size()-1);

        zhuanzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toadr = toaddress.getText().toString();

                if(toadr.equals("")){
                    Toast.makeText(ChatActivity.this,"对方不在线.", Toast.LENGTH_SHORT).show();
                }else if(toadr.equals("Myassetadr:ERRONOWALLETADR")){
                    Toast.makeText(ChatActivity.this,"对方暂无创建资产钱包,可先通知先创建.", Toast.LENGTH_SHORT).show();
                }else{
                    String[] toadrarr = toadr.split(":");
                    if(toadrarr[0].equals("Myassetadr")){
                        startActivity(new Intent(ChatActivity.this, AssetchatsendconfirmActivity.class).putExtra("touid",frienduserid).putExtra("toaddress",toadrarr[1]));
                    }
                }
             }
        });
        chatvoice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // startAudio();
                chatvoice.setVisibility(View.GONE);
                edt_send_msg.setVisibility(View.GONE);
                btn_send_voice.setVisibility(View.VISIBLE);
                chatkeyboard.setVisibility(View.VISIBLE);
            }
        });
        chatkeyboard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // startAudio();
                chatvoice.setVisibility(View.VISIBLE);
                edt_send_msg.setVisibility(View.VISIBLE);
                btn_send_voice.setVisibility(View.GONE);
                chatkeyboard.setVisibility(View.GONE);

            }
        });
        chatmotion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mElEmotion.getVisibility() == View.GONE){
                    mElEmotion.setVisibility(View.VISIBLE);
                    bottommenu.setVisibility(View.GONE);
                }else{
                    bottommenu.setVisibility(View.GONE);
                    mElEmotion.setVisibility(View.GONE);
                }
            }
        });
        chataddmore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (bottommenu.getVisibility() == View.GONE){
                    mElEmotion.setVisibility(View.GONE);
                    bottommenu.setVisibility(View.VISIBLE);
                }else{
                    mElEmotion.setVisibility(View.GONE);
                    bottommenu.setVisibility(View.GONE);
                }
            }
        });
        edt_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(edt_msg.getText())){
                    btn_send.setVisibility(View.GONE);
                    chataddmore.setVisibility(View.VISIBLE);
                    bottommenu.setVisibility(View.GONE);
                    //mElEmotion.setVisibility(View.GONE);
                }else{
                    chataddmore.setVisibility(View.GONE);
                    bottommenu.setVisibility(View.GONE);
                    //mElEmotion.setVisibility(View.GONE);
                    btn_send.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_send.setVisibility(View.GONE);
                mElEmotion.setVisibility(View.GONE);
                chataddmore.setVisibility(View.VISIBLE);
                String send_content=edt_msg.getText().toString().trim();
                chatcarrier.sendmessage(frienduserid,send_content);
                UserInfo myinfo = chatcarrier.getmyinfo();
                if (!TextUtils.isEmpty(send_content)){
                    MsgEntity send_msg=new MsgEntity(MsgEntity.SEND_MSG,send_content,myinfo.getUserId(),frienduserid,1);
                    list.add(send_msg);
                    msgAdapter.notifyItemInserted(list.size()-1);
                    edt_msg.setText("");
                    db.addfriendmessage(myinfo.getUserId(),send_content,"","",1,frienduserid);
                    recyclerView.scrollToPosition(list.size()-1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        transimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions(ChatActivity.this);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");//选择图片
                //intent.setType(“audio/*”); //选择音频
                //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                //intent.setType(“video/*;image/*”);//同时选择视频和图片
                //intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
        transvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                verifyCammerPermissions(ChatActivity.this);
                startVideo();
            }
        });
    }
    // 录音开始
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean ret = false;
            float downY = 0;
            int action = event.getAction();
            switch (v.getId()) {
                case R.id.btn_send_voice:
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            startAnim(true);
                            mediaUtils.record();
                            ret = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            stopAnim();
                            if (isCancel) {
                                isCancel = false;
                                mediaUtils.stopRecordUnSave();
                                Toast.makeText(ChatActivity.this, "取消保存", Toast.LENGTH_SHORT).show();
                            } else {
                                int duration = getDuration(chronometer.getText().toString());
                                switch (duration) {
                                    case -1:
                                        break;
                                    case -2:
                                        mediaUtils.stopRecordUnSave();
                                        Toast.makeText(ChatActivity.this, "时间太短", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        mediaUtils.stopRecordSave();
                                        final String voicepath = mediaUtils.getTargetFilePath();
                                        //Toast.makeText(ChatActivity.this, "文件以保存至：" + path, Toast.LENGTH_SHORT).show();
                                        audio_layouts.setVisibility(v.GONE);
                                        //发送语音信息
                                        UserInfo myinfo = chatcarrier.getmyinfo();
                                        MsgEntity send_msg = new MsgEntity(MsgEntity.SEND_MSG, voicepath, myinfo.getUserId(), frienduserid, 4);
                                        list.add(send_msg);
                                        msgAdapter.notifyItemInserted(list.size() - 1);
                                        recyclerView.scrollToPosition(list.size() - 1);
                                        db.addfriendmessage(myinfo.getUserId().toString(), "", voicepath, "", 4, frienduserid);
                                        Thread thread=new Thread(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                chatcarrier.testStreamWrite(frienduserid, Stream.PROPERTY_RELIABLE, voicepath);
                                            }
                                        });
                                        thread.start();
                                        break;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float currentY = event.getY();
                            if (downY - currentY > 10) {
                                moveAnim();
                                isCancel = true;
                            } else {
                                isCancel = false;
                                startAnim(false);
                            }
                            break;
                    }
                    break;
            }
            return ret;
        }
    };

    Chronometer.OnChronometerTickListener tickListener = new Chronometer.OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            if (SystemClock.elapsedRealtime() - chronometer.getBase() > 60 * 1000) {
                stopAnim();
                mediaUtils.stopRecordSave();
                Toast.makeText(ChatActivity.this, "录音超时", Toast.LENGTH_SHORT).show();
                String path = mediaUtils.getTargetFilePath();
               // Toast.makeText(ChatActivity.this, "文件以保存至：" + path, Toast.LENGTH_SHORT).show();

               // audio_layouts.setVisibility(8);
            }
        }
    };

    private int getDuration(String str) {
        String a = str.substring(0, 1);
        String b = str.substring(1, 2);
        String c = str.substring(3, 4);
        String d = str.substring(4);
        if (a.equals("0") && b.equals("0")) {
            if (c.equals("0") && Integer.valueOf(d) < 1) {
                return -2;
            } else if (c.equals("0") && Integer.valueOf(d) > 1) {
                duration = d;
                return Integer.valueOf(d);
            } else {
                duration = c + d;
                return Integer.valueOf(c + d);
            }
        } else {
            duration = "60";
            return -1;
        }

    }

    private void startAnim(boolean isStart){
        audio_layouts.setVisibility(View.VISIBLE);
        info.setText("上滑取消");
        btn_send_voice.setBackground(getResources().getDrawable(R.drawable.mic_pressed_bg));
        micIcon.setBackground(null);
        micIcon.setBackground(getResources().getDrawable(R.drawable.ic_mic_white_24dp));
        if (isStart){
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setFormat("%S");
            chronometer.start();
        }
    }

    private void stopAnim(){
        audio_layouts.setVisibility(View.GONE);
        btn_send_voice.setBackground(getResources().getDrawable(R.drawable.mic_bg));
        chronometer.stop();
    }

    private void moveAnim(){
        info.setText("松手取消");
        micIcon.setBackground(null);
        micIcon.setBackground(getResources().getDrawable(R.drawable.ic_undo_black_24dp));
    }

    //录音结束
    public static void verifyCammerPermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS,1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startAudio(){
        Intent intent = new Intent();
        intent.setClass(ChatActivity.this,AudioRecorderActivity.class);
        startActivity(intent);
    }

    private void startVideo(){
        Intent intent = new Intent();
        intent.setClass(ChatActivity.this,VideoRecorderActivity.class);
        startActivityForResult(intent, 2);
       // startActivity(intent);
        //startActivityForResult();
    }
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMsg() {
        list=new ArrayList<MsgEntity>();
        List<Map<String, String>> lista = new ArrayList<Map<String,String>>();
        lista = db.getfriendmessagelist(frienduserid);
        for(int i=0;i<lista.size();i++){
            if(lista.get(i).get("mtype").equals("1")) {
                if (lista.get(i).get("sender").equals(frienduserid)) {
                    MsgEntity msg1 = new MsgEntity(MsgEntity.RCV_MSG, lista.get(i).get("content").toString(), lista.get(i).get("sender"), lista.get(i).get("receiver"), 1);
                    list.add(msg1);
                } else {
                    MsgEntity msg1 = new MsgEntity(MsgEntity.SEND_MSG, lista.get(i).get("content").toString(), lista.get(i).get("receiver"), lista.get(i).get("sender"), 1);
                    list.add(msg1);
                }
            }else if(lista.get(i).get("mtype").equals("2")) {
                if (lista.get(i).get("sender").equals(frienduserid)) {
                    MsgEntity msg1 = new MsgEntity(MsgEntity.RCV_MSG, lista.get(i).get("imagepath").toString(), lista.get(i).get("sender"), lista.get(i).get("receiver"), 2);
                    list.add(msg1);
                } else {
                    MsgEntity msg1 = new MsgEntity(MsgEntity.SEND_MSG, lista.get(i).get("imagepath").toString(), lista.get(i).get("receiver"), lista.get(i).get("sender"), 2);
                    list.add(msg1);
                }
            }else if(lista.get(i).get("mtype").equals("3")) {
                if (lista.get(i).get("sender").equals(frienduserid)) {
                    MsgEntity msg1 = new MsgEntity(MsgEntity.RCV_MSG, lista.get(i).get("imagepath").toString(), lista.get(i).get("sender"), lista.get(i).get("receiver"), 3);
                    list.add(msg1);
                } else {
                    MsgEntity msg1 = new MsgEntity(MsgEntity.SEND_MSG, lista.get(i).get("imagepath").toString(), lista.get(i).get("receiver"), lista.get(i).get("sender"), 3);
                    list.add(msg1);
                }
            }else if(lista.get(i).get("mtype").equals("4")) {
                if (lista.get(i).get("sender").equals(frienduserid)) {
                    MsgEntity msg1 = new MsgEntity(MsgEntity.RCV_MSG, lista.get(i).get("voicepath").toString(), lista.get(i).get("sender"), lista.get(i).get("receiver"), 4);
                    list.add(msg1);
                } else {
                    MsgEntity msg1 = new MsgEntity(MsgEntity.SEND_MSG, lista.get(i).get("voicepath").toString(), lista.get(i).get("receiver"), lista.get(i).get("sender"), 4);
                    list.add(msg1);
                }
            }
        }
    }
    //初始化控件
    private void initView() {
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        chataddmore = (ImageView)findViewById(R.id.chataddmore);
        btn_send = (Button)findViewById(R.id.btn_send);
        edt_msg=(EditText)findViewById(R.id.edt_send_msg);
        transimg = (ImageView)findViewById(R.id.transimg);
        transvideo = (ImageView)findViewById(R.id.transvideo);
        bottommenu = (LinearLayout)findViewById(R.id.bottommenu);
        chatmotion = (ImageView)findViewById(R.id.chatemoticon);
        mElEmotion = (EmotionLayout)findViewById(R.id.elEmotion);
        chatvoice = (ImageView)findViewById(R.id.chatvoice);
        btn_send_voice = (TextView)findViewById(R.id.btn_send_voice);
        edt_send_msg = (EditText)findViewById(R.id.edt_send_msg);
        chatkeyboard = (ImageView)findViewById(R.id.chatkeyboard);
        audio_layouts = (LinearLayout)findViewById(R.id.audio_layouts);
        zhuanzhang = (ImageView)findViewById(R.id.zhuanzhang);
        toaddress = (EditText)findViewById(R.id.toaddress);
        mElEmotion.attachEditText(edt_msg);
        mElEmotion.setEmotionAddVisiable(false);
        mElEmotion.setEmotionSettingVisiable(false);
        mElEmotion.setEmotionExtClickListener(new IEmotionExtClickListener() {
            @Override
            public void onEmotionAddClick(View view) {
                //Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onEmotionSettingClick(View view) {
                //Toast.makeText(getApplicationContext(), "setting", Toast.LENGTH_SHORT).show();
            }
        });
        mElEmotion.setEmotionSelectedListener(new IEmotionSelectedListener() {
            @Override
            public void onEmojiSelected(String key) {
               // Toast.makeText(getApplicationContext(), key, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {
                String stickerPath = stickerBitmapPath;
               // Toast.makeText(getApplicationContext(), stickerPath, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public void back(View view){
        //unregisterReceiver(broadcastReceiver);
        finish();
    }
    /**
    *
     *  获取图片文件地址等等
     *
    * */
    String path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UserInfo myinfo = chatcarrier.getmyinfo();
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if ("file".equalsIgnoreCase(uri.getScheme())) {
                    path = uri.getPath();
                   // Toast.makeText(this, path + "11111", Toast.LENGTH_SHORT).show();
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    path = getPath(this, uri);
                    //Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                } else {
                    path = getRealPathFromURI(uri);
                }
                MsgEntity send_msg = new MsgEntity(MsgEntity.SEND_MSG, path, myinfo.getUserId(), frienduserid, 2);
                list.add(send_msg);
                msgAdapter.notifyItemInserted(list.size() - 1);
                recyclerView.scrollToPosition(list.size() - 1);
                db.addfriendmessage(myinfo.getUserId().toString(), "", "", path, 2, frienduserid);
                Thread thread=new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        chatcarrier.testStreamWrite(frienduserid, Stream.PROPERTY_RELIABLE, path);
                    }
                });
                thread.start();
                return;
            }
        }else if(requestCode == 2){
            Bundle b=data.getExtras();
            final String videopath=b.getString("videopath");
            MsgEntity send_msg = new MsgEntity(MsgEntity.SEND_MSG, videopath, myinfo.getUserId(), frienduserid, 3);
            list.add(send_msg);
            msgAdapter.notifyItemInserted(list.size() - 1);
            recyclerView.scrollToPosition(list.size() - 1);
            db.addfriendmessage(myinfo.getUserId().toString(), "", "", videopath, 3, frienduserid);
            Thread thread=new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    chatcarrier.testStreamWrite(frienduserid, Stream.PROPERTY_RELIABLE, videopath);
                }
            });
            thread.start();

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
    /**
     * 获得指定文件的byte数组
     */
    private byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(167772);
            byte[] b = new byte[167772];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"\\"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    /****
     *
     * 新文件传输结束
     *
     * **/
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getExtras().getString("message");
            String fromid = intent.getExtras().getString("fromid");
            Integer msgcate = intent.getExtras().getInt("msgcate");
            //Myassetadr:ERRONOWALLETADR
            System.out.println("消息："+message);
            if(fromid.equals(frienduserid)){
                String[] receivemsg = message.split(":");
                if(message.equals("Myassetadr:ERRONOWALLETADR")){
                    //对方没有创建资产
                    toaddress.setText(message);
                }else if(receivemsg[0].equals("Myassetadr")){
                    //返回资产地址
                    toaddress.setText(message);
                }else{
                    //模拟接受消息
                    MsgEntity rcv_msg=new MsgEntity(MsgEntity.RCV_MSG,message,fromid,frienduserid,msgcate);
                    list.add(rcv_msg);
                    msgAdapter.notifyItemInserted(list.size()-1);
                    recyclerView.scrollToPosition(list.size()-1);
                }
            }
        }
    };
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
