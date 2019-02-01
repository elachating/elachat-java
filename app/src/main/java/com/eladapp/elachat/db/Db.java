package com.eladapp.elachat.db;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.elastos.carrier.Carrier;
import org.elastos.carrier.FriendInfo;
import org.elastos.carrier.exceptions.CarrierException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ela.Carrier.Chatcarrier;

public class Db {
    private static String dbpath ="/data/data/com.eladapp.elachat/chat.db";
    List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    Chatcarrier chatcarrier = new Chatcarrier();
    Boolean curn = false;
    public void initdb(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        String sqla="create table if not exists messagelist ("
                + "id integer primary key autoincrement,"
                + "sender text, "
                + "content text, "
                + "yn integer,"
                + "time text,"
                + "voicepath text,"
                + "voiceurl text,"
                + "imagepath text,"
                + "imageurl text,"
                + "mtype integer not null,"
                + "reciver text)";
        String sqlb="create table if not exists firendlist("
                + "id integer primary key autoincrement, "
                + "userid text, "
                + "remark text,"
                + "nickname text)";
        String sqlc ="create table if not exists newfirendlist("
                + "id integer primary key autoincrement,"
                + "userid text,"
                + "yn integer,"
                + "hello text,"
                + "nickname text)";
        String sqld ="create table if not exists newmessagelast("
                + "id integer primary key autoincrement,"
                + "userid text,"
                + "content text,"
                + "yn integer,"
                + "time text,"
                + "mtype integer not null,"
                + "num integer)";
        String sqle ="create table if not exists didinfo("
                + "id integer primary key autoincrement,"
                + "did text,"
                + "pubkey text,"
                + "prvkey text,"
                + "useradr text,"
                + "nickname text,"
                + "walletadr text)";
        db.execSQL(sqla);
        db.execSQL(sqlb);
        db.execSQL(sqlc);
        db.execSQL(sqld);
        db.execSQL(sqle);
        db.close();
    }
    /**
    *
    * 新增DID信息到DIDinfo表
    * */
    public boolean adddid(String did,String prvkey,String pubkey,String useradr,String nickname,String walletadr){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("did", did);
        values.put("pubkey", pubkey);
        values.put("prvkey", prvkey);
        values.put("useradr",useradr);
        values.put("nickname", nickname);
        values.put("walletadr", walletadr);
        long rs = dba.insert("didinfo", null, values);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    /**
    *
    * 读取did信息列表
    * */
    public JSONArray getdidinfo(){
            SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
            Cursor result = dba.query ("didinfo",new String[]{"id,did,pubkey,prvkey,useradr,nickname,walletadr"},"id=? ",new String[]{"1"},null,null,null);
            JSONArray json = new JSONArray();
            if(result.moveToFirst()){
                while(!result.isAfterLast()){
                    String did = result.getString(result.getColumnIndex("did"));
                    String pubkey = result.getString(result.getColumnIndex("pubkey"));
                    String prvkey = result.getString(result.getColumnIndex("prvkey"));
                    String useradr = result.getString(result.getColumnIndex("useradr"));
                    String nickname = result.getString(result.getColumnIndex("nickname"));
                    String walletadr = result.getString(result.getColumnIndex("walletadr"));
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("did", did);
                        obj.put("useradr",useradr);
                        obj.put("prvkey",prvkey);
                        obj.put("pubkey",pubkey);
                        obj.put("nickname",nickname);
                        obj.put("walletadr",walletadr);
                        json.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    result.moveToNext();
                }
            }
            dba.close();
            return json;

    }
    /**
     *  新朋友操作
     */
    //新增好友到新好友列表
    public boolean addnewfriend(String uid,String remark){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("userid", uid);
        values.put("yn", 0);
        values.put("hello", remark);
        long rs = dba.insert("newfirendlist", null, values);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    //更新新增好友的列表状态
    public boolean updatenewfriend(String uid){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("yn", 1);
        long rs = dba.update("newfirendlist", values, "userid=?", new String[] { uid});
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    //获取新增的好友列表
    public JSONArray newfriendlist(){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("newfirendlist",new String[]{"userid,yn,hello"},null,null,null,null,null);
        JSONArray json = new JSONArray();
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String fuserid = result.getString(result.getColumnIndex("userid"));
                String hello = result.getString(result.getColumnIndex("hello"));
                Integer yn = result.getInt(result.getColumnIndex("yn"));
                JSONObject obj = new JSONObject();
                try {
                    obj.put("userid", fuserid);
                    obj.put("yn",yn.toString());
                    obj.put("message",hello);
                    json.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result.moveToNext();
            }
        }
        dba.close();
        return json;
    }
    //获取好友列表
    public List<Map<String, String>>  getfriendlist(){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Chatcarrier chatcarrier = new Chatcarrier();
        Cursor result = dba.query ("newfirendlist",new String[]{"userid,yn,hello"},null,null,null,null,null);
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String fuserid = result.getString(result.getColumnIndex("userid"));
                String hello = result.getString(result.getColumnIndex("hello"));
                Integer yn = result.getInt(result.getColumnIndex("yn"));
                Map<String, String> maps = new HashMap<String, String>();
                FriendInfo friendinfo = chatcarrier.friendinfo(fuserid);
                if(yn.equals(1)){
                    maps.put("userid", fuserid);
                    maps.put("remark",friendinfo.getName().toString());
                    list.add(maps);
                }
                result.moveToNext();
            }
        }
        dba.close();
        return list;
    }
    //获取好友列表
    public List<Map<String, String>> getfriendlistlist(){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("newfirendlist",new String[]{"userid,yn,hello"},null,null,null,null,null);
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String fuserid = result.getString(result.getColumnIndex("userid"));
                String hello = result.getString(result.getColumnIndex("hello"));
                Integer yn = result.getInt(result.getColumnIndex("yn"));
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("userid", fuserid);
                maps.put("yn",yn.toString());
                maps.put("message",hello);
                list.add(maps);
                result.moveToNext();
            }
        }
        dba.close();
        return list;
    }
    //新增消息表
    public boolean addfriendmessage(String fuid,String content,String voicepath,String imgpath,int cate,String reciver){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        long time = new Date().getTime();
        ContentValues values = new ContentValues();
        values.put("sender", fuid);
        values.put("content", content);
        values.put("yn", 0);
        values.put("time", String.valueOf(time));
        values.put("voicepath", voicepath);
        values.put("imagepath", imgpath);
        values.put("mtype", cate);
        values.put("reciver", reciver);
        long rs = dba.insert("messagelist", null, values);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    //获取消息列表
    public List<Map<String, String>> getfriendmessagelist(String uid){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("messagelist",new String[]{"sender,content,yn,time,voicepath,imagepath,mtype,reciver"},"sender=? or reciver=? ",new String[]{uid,uid},null,null,null);
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String sender = result.getString(result.getColumnIndex("sender"));
                String content = result.getString(result.getColumnIndex("content"));
                Integer yn = result.getInt(result.getColumnIndex("yn"));
                String time = result.getString(result.getColumnIndex("time"));
                String voceipath = result.getString(result.getColumnIndex("voicepath"));
                String imagepath = result.getString(result.getColumnIndex("imagepath"));
                Integer mtype = result.getInt(result.getColumnIndex("mtype"));
                String reciver = result.getString(result.getColumnIndex("reciver"));
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("sender", sender);
                maps.put("content",content);
                maps.put("yn",yn.toString());
                maps.put("time",time);
                maps.put("voicepath",voceipath);
                maps.put("imagepath",imagepath);
                maps.put("mtype",mtype.toString());
                maps.put("reciver",reciver);
                list.add(maps);
                result.moveToNext();
            }
        }
        dba.close();
        return list;
    }
    //检测是否存在账户，不存在则新增
    public List<Map<String, String>> isexistfriend(){
        Carrier mycarrier = chatcarrier.carrierinstance();
        try {
            List<FriendInfo> friendlist = mycarrier.getFriends();
            for(int j=0;j<friendlist.size();j++){
               // boolean checks = checkfriend(friendlist.get(j).getUserId().toString());
                //if(checks){ }else{
               //     addnewfriends(friendlist.get(j).getUserId().toString(),"hello");
               // }
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("userid", friendlist.get(j).getUserId().toString());
                maps.put("remark",friendlist.get(j).getName().toString());
                list.add(maps);
            }
        } catch (CarrierException e) {
            e.printStackTrace();
        }
        return list;
    }
    //检测是否存在好友
    public boolean checkfriend(String uid){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("newfirendlist",new String[]{"userid"},"userid=? ",new String[]{uid},null,null,null);
        int i = 0;
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                i = 1;
            }
        }
        if(i==1){
            return true;
        }else{
            return false;
        }
    }
    //新增好友到新好友
    public boolean addnewfriends(String uid,String remark){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("userid", uid);
        values.put("yn", 1);
        values.put("hello", remark);
        long rs = dba.insert("newfirendlist", null, values);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }
    //新增或更新到最后一条记录
    public boolean addorupdatenewmessagelast(String uid,String content,Integer mtype,Integer num){
            SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
            ContentValues values = new ContentValues();
            long time = new Date().getTime();
            System.out.println("加入最后一条记录时间："+time);
            if(checkmessagelast(uid)){
                values.put("content", content);
                values.put("mtype", mtype);
                values.put("time", String.valueOf(time));
                values.put("num", num);
                long rs = dba.update("newmessagelast", values, "userid=?", new String[] {uid});
                System.out.println("更新新消息："+rs);
                dba.close();
                if (rs!=-1) {
                    return false;
                }else{
                    return true;
                }
            }else{
                values.put("userid", uid);
                values.put("content", content);
                values.put("mtype", mtype);
                values.put("yn", 0);
                values.put("time", String.valueOf(time));
                values.put("num", num);
                long rs = dba.insert("newmessagelast", null, values);
                System.out.println("加入新消息："+rs);
                dba.close();
                if (rs!=-1) {
                    return false;
                }else{
                    return true;
                }
            }
    }
    //查询newmessagelast是否存在记录
    public boolean checkmessagelast(String uid){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("newmessagelast",new String[]{"userid"},null,null,null,null,null);
        if(result.moveToFirst()){
            curn = true;
        }
        System.out.println("判断是否存在："+curn.toString());
        dba.close();
        return curn;
    }
    //更新指定消息记录为已读状态
    public boolean updatemessagelast(String uid){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        ContentValues values = new ContentValues();
        values.put("yn", 1);
        long rs = dba.update("newmessagelast", values, "userid=?", new String[] {uid});
        System.out.println("更新新消息："+rs);
        dba.close();
        if (rs!=-1) {
            return false;
        }else{
            return true;
        }
    }

    //获取消息列表
    List<Map<String, Object>> listo=new ArrayList<Map<String,Object>>();
    public List<Map<String, Object>> getfriendmessagelistnoread(){
        SQLiteDatabase dba = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor result = dba.query ("newmessagelast",new String[]{"userid,content,mtype,num,yn,time"},null,null,null,null,null);
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                String sender = result.getString(result.getColumnIndex("userid"));
                String content = result.getString(result.getColumnIndex("content"));
                Integer mtype = result.getInt(result.getColumnIndex("mtype"));
                Integer num = result.getInt(result.getColumnIndex("num"));
                Integer yn = result.getInt(result.getColumnIndex("yn"));
                long time = Long.parseLong(result.getString(result.getColumnIndex("time")));
                Map<String, Object> maps = new HashMap<String, Object>();
                maps.put("sender", sender);
                maps.put("content",content);
                maps.put("yn",yn.toString());
                maps.put("time",time);
                maps.put("mtype",mtype.toString());
                maps.put("num",num.toString());
                listo.add(maps);
                result.moveToNext();
            }
        }
        dba.close();
        return listo;
    }
}