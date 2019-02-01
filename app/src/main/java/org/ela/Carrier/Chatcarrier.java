package org.ela.Carrier;

import org.elastos.carrier.Carrier;
import org.elastos.carrier.FriendInfo;
import org.elastos.carrier.UserInfo;
import org.elastos.carrier.exceptions.CarrierException;
import org.elastos.carrier.session.CloseReason;
import org.elastos.carrier.session.Manager;
import org.elastos.carrier.session.ManagerHandler;
import org.elastos.carrier.session.Session;
import org.elastos.carrier.session.SessionRequestCompleteHandler;
import org.elastos.carrier.session.Stream;
import org.elastos.carrier.session.StreamHandler;
import org.elastos.carrier.session.StreamState;
import org.elastos.carrier.session.StreamType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.fail;

public class Chatcarrier {
    private Carrier mycarrier =  Carrier.getInstance();
    private UserInfo myinfo;
    private FriendInfo friendinfo;
    //加好友功能
    public String applyfriend(String address,String checkMessage) throws CarrierException{
        String userId = Carrier.getIdFromAddress(address);
        if( !Carrier.isValidAddress(address)){
            return "valid";
        }else if(!mycarrier.isFriend(userId)){
            mycarrier.addFriend(address,checkMessage);
           return "succfully";
        }else{
            return "repeate";
        }
    }
    //接受好友请求
    public void acceptfriend(String uid){
        try {
            mycarrier.acceptFriend(uid);
        } catch (CarrierException e) {
            e.printStackTrace();
        }
    }
    //更新我的信息
    public void updatemyinfo(String nickname,String headimg){
        myinfo.setName(nickname);
        try {
            mycarrier.setSelfInfo(myinfo);
        } catch (CarrierException e) {
            e.printStackTrace();
        }
    }
    //获取我的信息
    public UserInfo getmyinfo(){
        try {
            myinfo =  mycarrier.getSelfInfo();
        } catch (CarrierException e) {
            e.printStackTrace();
        }
        return myinfo;
    }
    //获取指定好友的信息
    public FriendInfo friendinfo(String fuid){
        try {
           friendinfo =  mycarrier.getFriend(fuid);
        } catch (CarrierException e) {
            e.printStackTrace();
        }
        return friendinfo;
    }
    //返回实例
    public Carrier carrierinstance(){
        return mycarrier;
    }
    //发送消息
    public void sendmessage(String fuid,String message){
        try {
            System.out.println("好友："+fuid+"消息："+message);
            mycarrier.sendFriendMessage(fuid,message);
        } catch (CarrierException e) {
            e.printStackTrace();
        }
    }
    private Manager sessionMgr;
    private Session activsession;
    private String fromuid;
    private Stream activstream;
    private String sessionRequestSdp;

    /*
     * 新的文件传输
     *
     * */

    private static final String TAG = "StreamTest";
    private static Synchronizer friendConnSyncher = new Synchronizer();
    private static Synchronizer commonSyncher = new Synchronizer();
    private static Carrier carrier;
    private static Manager sessionManager;
    private static final SessionManagerHandler sessionHandler = new SessionManagerHandler();
    private static final TestStreamHandler streamHandler = new TestStreamHandler();
    private static Session session;
    private static Stream stream;
    static boolean mRequestReceived = false;
    static String mSdp = "";
    static StreamState mState;
    static int mCompleteStatus;
    static class SessionManagerHandler implements ManagerHandler {
        @Override
        public void onSessionRequest(Carrier carrier, String from, String sdp) {
            mRequestReceived = true;
            mSdp = sdp;
            synchronized (sessionHandler) {
                sessionHandler.notify();
            }
        }
    }
    static class TestStreamHandler implements StreamHandler {
        @Override
        public void onStateChanged(Stream stream, StreamState state) {
            mState = state;
            synchronized (this) {
                this.notify();
            }
        }
        @Override
        public void onStreamData(Stream stream, byte[] data) {
            synchronized (this) {
                this.notify();
            }
        }
        @Override
        public boolean onChannelOpen(Stream stream, int channel, String cookie) {
            synchronized (this) {
                this.notify();
            }
            return true;
        }
        @Override
        public void onChannelOpened(Stream stream, int channel) {
            synchronized (this) {
                this.notify();
            }
        }

        @Override
        public void onChannelClose(Stream stream, int channel, CloseReason reason) {
            synchronized (this) {
                this.notify();
            }
        }

        @Override
        public boolean onChannelData(Stream stream, int channel, byte[] data) {
            synchronized (this) {
                this.notify();
            }
            return true;
        }

        @Override
        public void onChannelPending(Stream stream, int channel) {
            synchronized (this) {
                this.notify();
            }
        }

        @Override
        public void onChannelResume(Stream stream, int channel) {
            synchronized (this) {
                this.notify();
            }
        }
    }

    static class TestSessionRequestCompleteHandler implements SessionRequestCompleteHandler {
        @Override
        public void onCompletion(Session session, int status, String reason, String sdp) {
            mCompleteStatus = status;
            if (status == 0) {
                try {
                    session.start(sdp);
                }
                catch (CarrierException e) {
                    e.printStackTrace();
                    fail();
                }
            }

            synchronized (this) {
                notify();
            }
        }
    }
    /**
     * 获得指定文件的byte数组
     */

    public byte[] getfilebytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 1024*10];
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
    private byte[] InputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }
    private byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 1024];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
    public static byte[] getBytes(char[] chars) {
        String tmp = new String(chars);
        return tmp.getBytes();
    }
    private static int sReturnValue = 0;
    private  void doBulkWrite(final String paths,String frienduserid)
    {

        //发送消息到对方，通知对方传输文件的类型
        String[] arr = paths.split("\\.");
        System.out.println("文件扩展名："+arr[1]);
        Carrier mycarrierb = Carrier.getInstance();
        try {
            mycarrierb.sendFriendMessage(frienduserid,"extendfile|"+arr[1]);
        } catch (CarrierException e) {
            e.printStackTrace();
        }

        final byte[] dataa = getfilebytes(paths);
        System.out.println("数据："+new String(dataa));
        Thread threada = new Thread(new Runnable() {
            @Override
            public void run() {
                int flength = (int) dataa.length;
                System.out.println("字节数组长度："+flength);
                int rc = 0;
                int sent = 0;
                do {
                    try {
                        if(flength - sent > 2048){
                            rc = stream.writeData(dataa, sent, 2048);
                        }else{
                            rc = stream.writeData(dataa, sent, flength-sent);
                        }
                    } catch (CarrierException e) {
                        int errorCode = e.getErrorCode();
                        if (errorCode == 0x81000010) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                            continue;
                        } else {
                            e.printStackTrace();
                            System.out.println(String.format("Write data failed: 0x%s.", Integer.toHexString(errorCode)));
                            return;
                        }
                    }
                    sent += rc;
                    System.out.println("结束返回长度："+sent);
                } while (sent < flength);
            }
        });
        threada.start();
        try {
            threada.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testStreamWrite(String fuid,int stream_options,String path)
    {
        setUp();
        testStreamScheme(StreamType.Text,fuid,stream_options,path);
    }

    private void testStreamScheme(StreamType stream_type,String fuid, int stream_options,String path)
    {
        friendConnSyncher.reset();
        commonSyncher.reset();
        try {
            session = sessionManager.newSession(fuid);
            stream = session.addStream(stream_type, stream_options, streamHandler);
            synchronized (streamHandler) {
                streamHandler.wait();
            }
            TestSessionRequestCompleteHandler completeHandler = new TestSessionRequestCompleteHandler();
            session.request(completeHandler);
            synchronized (streamHandler) {
                streamHandler.wait(1000);
            }
            synchronized (completeHandler) {
                completeHandler.wait();
            }
            synchronized (streamHandler) {
                streamHandler.wait(1000);
            }
            if ((!mState.equals(StreamState.Connecting))
                    && (!mState.equals(StreamState.Connected))) {
            }
            synchronized (streamHandler) {
                streamHandler.wait(1000);
            }
            doBulkWrite(path,fuid);
            session.removeStream(stream);
            session.close();
        }
        catch (CarrierException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public  void setUp() {
        sessionManager = Manager.getInstance();
    }
}