package com.eladapp.elachat.application;

import android.app.Application;
import org.ela.Carrier.Synchronizer;
import org.ela.Carrier.TestOptions;
import android.content.Context;

import org.ela.Elaspv.Elaspvapi;
import org.elastos.carrier.AbstractCarrierHandler;
import org.elastos.carrier.Carrier;
import org.elastos.carrier.ConnectionStatus;
import org.elastos.carrier.UserInfo;
import org.elastos.carrier.exceptions.CarrierException;
import org.elastos.carrier.session.AbstractStreamHandler;
import org.elastos.carrier.session.Manager;
import org.elastos.carrier.session.ManagerHandler;
import org.elastos.carrier.session.Session;
import org.elastos.carrier.session.Stream;
import org.elastos.carrier.session.StreamState;
import org.elastos.carrier.session.StreamType;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.widget.ImageView;
import com.eladapp.elachat.db.Db;
import com.elastos.spvcore.DIDManagerSupervisor;
import com.elastos.spvcore.ElastosWalletUtils;
import com.elastos.spvcore.IDid;
import com.elastos.spvcore.IDidManager;
import com.elastos.spvcore.IMainchainSubWallet;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.ISubWallet;
import com.elastos.spvcore.MasterWalletManager;
import com.elastos.spvcore.WalletException;
import com.lqr.emoji.IImageLoader;
import com.lqr.emoji.LQREmotionKit;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CloudchatApp extends Application{
    Carrier carrierInst = null;
    String carrierAddr = null;
    String carrierUserID = null;
    String TAG = "Carrier Cloudchat Output：";
    static String sender = "";
    static String contents = "";
    static byte[] filebyte;
    static int filenum;
    static int filetranid;
    Manager sessionMgra;
    Session activsession;
    String sessionRequestSdp;
    String curfuid;
    Context mcontext;
    public static final String action = "jason.broadcast.action";
    public static final String friendstatusaction = "friendstatus";
    private static Map receivefiletype = new HashMap();
    private Db db = new Db();
    private IMasterWallet mCurrentMasterWallet;
    private IDidManager mDidManager = null;
    static MasterWalletManager mWalletManager;
    static ArrayList<IMasterWallet> mMasterWalletList = new ArrayList<IMasterWallet>();
    private Map<String, ISubWallet> mSubWalletMap = new HashMap<String, ISubWallet>();
    private IMasterWallet masterWallet;
    private ISubWallet subWallet;
    private DIDManagerSupervisor mDIDManagerSupervisor = null;
    private IDidManager DIDManager;
    private String rootPaths;
    private  IDid did;
    @Override
    public void onCreate() {
        super.onCreate();
        LQREmotionKit.init(this, new IImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            }
        });
        Db db = new Db();
        db.initdb();
        TestOptions options = new TestOptions(getAppPath());
        TestHandler handler = new TestHandler();
        //1.初始化实例，获得相关信息
        try {
            //1.1获得Carrier的实例
            Carrier.initializeInstance(options, handler);
            carrierInst = Carrier.getInstance();
            //1.2获得Carrier的地址
            carrierAddr = carrierInst.getAddress();
            //Log.i(TAG,"address: " + carrierAddr);
            //1.3获得Carrier的用户ID
            carrierUserID = carrierInst.getUserId();
            //Log.i(TAG,"userID: " + carrierUserID);
            //1.4启动网络
            carrierInst.start(1000);
            handler.synch.await();
           // Log.i(TAG,"carrier client is ready now");
            setUp(carrierInst);
            String rootPath = getApplicationContext().getFilesDir().getParent();
            ElastosWalletUtils.InitConfig(getApplicationContext(),rootPath);
            initwallet(rootPath);
        } catch (CarrierException e) {
            e.printStackTrace();
        }
    }
    public void initwallet(String rootPath){
        //mDIDManagerSupervisor = new DIDManagerSupervisor(rootPath);
        mWalletManager = new MasterWalletManager(rootPath);
        mMasterWalletList = mWalletManager.GetAllMasterWallets();
        if(mMasterWalletList!=null && !mMasterWalletList.toString().equals("[]")){
            mCurrentMasterWallet = mMasterWalletList.get(0);
            if (mCurrentMasterWallet != null) {
            }
        } else {
            mMasterWalletList = new ArrayList<IMasterWallet>();
        }
    }
    //返回mWalletManager
    public MasterWalletManager getwalletmanager(){
        return mWalletManager;
    }
    //返回钱包列表
    public ArrayList<IMasterWallet> getwalletlist(){
        return mMasterWalletList;
    }
    //返回第一个钱包列表
    public IMasterWallet getwallet(){
        return mCurrentMasterWallet;
    }
    public String getsender(){
        //System.out.println("获取到的发送者："+sender);
        return sender;
    }
    public  String getcontents(){
        return contents;
    }
    public void setsender(){
        //System.out.println("初始化发送者");
        sender="";
    }
    /* 文件传输相关 */
    //文件传输正文开始标志
    public void setfiletranid(int a){
        filetranid = a;
    }
    //获取文件传输正文标志
    public int getfiletranid(){
        return filetranid;
    }
    //清空文件传输正文标志
    public void clearfiletranid(){
        filetranid = 0;
    }
    //设置传输文件的字节大小
    public void setfilenum(int a){
        filenum = a;
    }
    //获取传输文件的字节大小
    public int getfilenum(){
        return filenum;
    }
    //清空传输文件的字节大小
    public void clearfilenum(){
         filenum=0;
    }
    //清空传输文件正文
    public void clearfilebyte(){
        filebyte = null;
    }
    //合并文件传输正文
    public void setfilebyte(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        filebyte = bt3;
    }
    //第一次设置
    public void setfilebyteone(byte[] bt){
            filebyte = bt;
    }

    //获取文件传输正文
    public byte[] getfilebyte(){
        return  filebyte;
    }
    private String getAppPath() {
        Context context=this;
        File file=context.getFilesDir();
        String path=file.getAbsolutePath();
        return path;
    }

     class TestHandler extends AbstractCarrierHandler {
        Synchronizer synch = new Synchronizer();
        String from;
        ConnectionStatus friendStatus;
        String CALLBACK="call back";
        public void onReady(Carrier carrier) {
            synch.wakeup();
        }
        public void onFriendConnection(Carrier carrier, String friendId, ConnectionStatus status) {
            from = friendId;
            friendStatus = status;
            System.out.println("From:"+friendId+";状态："+status);
            Intent intent0 = new Intent(friendstatusaction);
            intent0.putExtra("friendid", friendId);
            if (friendStatus == ConnectionStatus.Connected) {
                intent0.putExtra("status", "1");
                sendBroadcast(intent0);
                synch.wakeup();
            }else if(friendStatus == ConnectionStatus.Disconnected){
                intent0.putExtra("status", "0");
                sendBroadcast(intent0);
            }
        }
        //2.2 通过好友验证
        public void onFriendRequest(Carrier carrier, String userId, UserInfo info, String hello) {
                System.out.println("好友请求信息："+userId+",备注:"+hello);
                db.addnewfriend(userId,hello);
        }
         @Override
         public void onFriendMessage(Carrier carrier, String from, byte[] message) {
             try {
                 String reveiverid = carrier.getUserId().toString();
                 String receivemessage = new String(message);
                 String [] wltadr = receivemessage.split(":");
                 if(receivemessage.length()>11 && receivemessage.substring(0,11).equals("extendfile|")){
                     String[] narr = receivemessage.split("\\|");
                     receivefiletype.put(from,narr[1]);
                 }else if(receivemessage.equals("new&&&|friend|&&&")){
                     //加入到好友列表
                     //db.addnewfriend(from.toString(),new String(message));
                 }else if(receivemessage.equals("getassetmessage:")) {
                     System.out.println("获取钱包地址消息："+receivemessage);
                     if (getwalletlist() == null || getwalletlist().toString().equals("[]")) {
                         carrierInst.sendFriendMessage(from, "Myassetadr:ERRONOWALLETADR");
                     } else {
                         String adr = getassetadr();
                         carrierInst.sendFriendMessage(from, "Myassetadr:" + adr);
                     }
                 }else if(wltadr[0].equals("Myassetadr")){
                     System.out.println("获取钱包地址消息111："+receivemessage);
                     Intent intent = new Intent(action);
                     intent.putExtra("fromid", from.toString());
                     intent.putExtra("message", new String(message));
                     intent.putExtra("msgcate", 1);
                     sendBroadcast(intent);
                 }else{
                     db.addfriendmessage(from,receivemessage,"","",1,reveiverid);
                     db.addorupdatenewmessagelast(from,receivemessage,1,1);
                     startAlarm();
                     Intent intent = new Intent(action);
                     intent.putExtra("fromid", from.toString());
                     intent.putExtra("message", new String(message));
                     intent.putExtra("msgcate", 1);
                     sendBroadcast(intent);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
    }

    /* 以下是文件传输功能 */
    private  final SessionManagerHandler sessionHandler = new SessionManagerHandler();
     class SessionManagerHandler implements ManagerHandler {
        @Override
        public void onSessionRequest(Carrier carrier, String from, String sdp) {
            sessionRequestSdp = sdp;
            curfuid = from;
            createsessionjoinstream(from);
        }
    }
    //准备接受数据，建立session
    public void setUp(Carrier curcarrier) {
        System.out.println("准备建立Session!");
        try {
            Manager.initializeInstance(curcarrier, sessionHandler);
            sessionMgra = Manager.getInstance();
        } catch (CarrierException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * 以下功能是接受数据
     *
     * */
    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
    public void createsessionjoinstream(String s){
        try{
            activsession = sessionMgra.newSession(s);
            activsession.addStream(StreamType.Text,Stream.PROPERTY_RELIABLE,new AbstractStreamHandler() {
                byte[] receivedData1 = null;
                @Override
                public void onStateChanged(Stream stream, StreamState state) {
                    try {
                        switch (state.name()) {
                            case "Initialized":
                                System.out.println("初始化");
                                activsession.replyRequest(0, null);
                                break;
                            case "TransportReady":
                                System.out.println("准备传输");
                                activsession.start(sessionRequestSdp);
                                break;
                            case "Connected":
                                //datas = null;
                                System.out.println("建立连接");
                                break;
                            case "Closed":
                                System.out.println("断开连接");
                                final String reveiverid = carrierInst.getUserId().toString();
                               Thread threadb = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Date date = new Date();
                                        SimpleDateFormat curtime = new SimpleDateFormat("yyyyMMddHHmmss");
                                        curtime.format(date);
                                        String filepath = "/storage/emulated/0/Download/";
                                        String filename = curtime.format(date).toString()+'.'+String.valueOf(receivefiletype.get(curfuid));

                                        getFile(receivedData1,filepath,filename);
                                        //加入到数据库
                                        Intent intent = new Intent(action);
                                        intent.putExtra("fromid", curfuid.toString());
                                        intent.putExtra("message", filepath + "" + filename);
                                        String extensionname = String.valueOf(receivefiletype.get(curfuid));
                                        if(extensionname.equals("mp4")){
                                            db.addfriendmessage(curfuid, "", "", filepath + "" + filename, 3, reveiverid);
                                           // db.addorupdatenewmessagelast(curfuid,filepath + "" + filename,3,1);
                                            intent.putExtra("msgcate", 3);
                                        }else if(extensionname.equals("m4a")) {
                                            db.addfriendmessage(curfuid, "", filepath + "" + filename,"",  4, reveiverid);
                                            //db.addorupdatenewmessagelast(curfuid,filepath + "" + filename,4,1);
                                            intent.putExtra("msgcate", 4);
                                        }else{
                                            db.addfriendmessage(curfuid, "", "", filepath + "" + filename, 2, reveiverid);
                                            //db.addorupdatenewmessagelast(curfuid,filepath + "" + filename,2,1);
                                            intent.putExtra("msgcate", 2);
                                        }
                                        sendBroadcast(intent);
                                    }
                                });
                                threadb.start();
                                try {
                                    threadb.join();
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                    } catch (CarrierException e) {
                        e.printStackTrace();
                    }
                    //synch1.wakeup();
                }
                @Override
                public void onStreamData(Stream stream, byte[] data) {
                    Log.d(TAG, "onStreamData data="+(new String(data)));
                    if(receivedData1==null) {
                        receivedData1 =data;
                    }else{
                        receivedData1 =byteMerger(receivedData1,data);
                   }
                }
            });
        }catch (CarrierException e){
            e.getMessage();
        }
    }
    /**
     *
     *
     *
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
            file = new File(filePath+"//"+fileName);
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
    private void startAlarm() throws IOException {
        File file = new File("/data/data/com.eladapp.elachat/voice.txt");
        if (file.exists()) {

            FileInputStream fis = new FileInputStream(file);
            int length = fis.available();
            byte [] buffer = new byte[length];
            fis.read(buffer);
            String res = new String(buffer, "UTF-8");
            fis.close();
            if(res.equals("0")){

            }else{
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (notification == null) return;
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            }
        }else{
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (notification == null) return;
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }
    }
    //读取指定文件
    public String getmvsinfo () throws IOException {
        File file = new File("/data/data/com.eladapp.elachat/voice.txt");
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte [] buffer = new byte[length];
        fis.read(buffer);
        String res = new String(buffer, "UTF-8");
        fis.close();
        return res;
    }
    //获取地址
    public String getassetadr(){
        ArrayList<IMasterWallet> Fmastwallet = getwalletlist();
        String adrinfo = Fmastwallet.get(0).GetSubWallet("ELA").GetAllAddress(0,1).toString();
        JSONObject jsonobj = JSONObject.fromObject(adrinfo);
        JSONArray jsonobja = JSONArray.fromObject(jsonobj.get("Addresses"));
        return String.valueOf(jsonobja.get(0));
    }
}