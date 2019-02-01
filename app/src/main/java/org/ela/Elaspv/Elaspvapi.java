package org.ela.Elaspv;

import android.content.Context;

import com.elastos.spvcore.DIDManagerSupervisor;
import com.elastos.spvcore.ElastosWalletUtils;
import com.elastos.spvcore.IDid;
import com.elastos.spvcore.IDidManager;
import com.elastos.spvcore.IMasterWallet;
import com.elastos.spvcore.ISubWallet;
//import com.elastos.spvcore.IdManagerFactory;
import com.elastos.spvcore.MasterWalletManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Elaspvapi {
    private IMasterWallet mCurrentMasterWallet;
    private IDidManager mDidManager = null;
    private MasterWalletManager mWalletManager;
    private ArrayList<IMasterWallet> mMasterWalletList = new ArrayList<IMasterWallet>();
    private Map<String, ISubWallet> mSubWalletMap = new HashMap<String, ISubWallet>();
    private IMasterWallet masterWallet;
    private ISubWallet subWallet;
    public void initialize(Context context,String spvpaths) {
        //1. 初始化钱包所需的数据
        ElastosWalletUtils.InitConfig(context,spvpaths);
        mWalletManager = new MasterWalletManager(spvpaths);
        mMasterWalletList = mWalletManager.GetAllMasterWallets();
        if (mMasterWalletList != null) {
            mCurrentMasterWallet = mMasterWalletList.get(0);
            if (mCurrentMasterWallet != null) {
                //mDidManager = IdManagerFactory.CreateIdManager(mCurrentMasterWallet, spvpaths);
            }
        } else {
            mMasterWalletList = new ArrayList<IMasterWallet>();
        }
    }
    //创建助记词
    public String creatememwords(String language){
        String mnemonic = mWalletManager.GenerateMnemonic(language);
        return mnemonic;
    }
    //获取主钱包
    public ArrayList<IMasterWallet> getmastwallet(){
        return mMasterWalletList;
    }
    //创建钱包
    public ISubWallet createmasterwallet(String masterwalletid,String mnemonic,String phrasepwd,String paypwd,String chainid){
        long feePerKb = 10000;
        masterWallet = mWalletManager.CreateMasterWallet(masterwalletid, mnemonic,phrasepwd, paypwd, true);
        subWallet = masterWallet.CreateSubWallet(chainid,feePerKb);
        return subWallet;
    }
    //获取指定chainid的子钱包
    public JSONArray getsubwalletlist(){
        mSubWalletMap.clear();
        ArrayList<ISubWallet> list = mCurrentMasterWallet.GetAllSubWallets();
        System.out.println("资产："+mCurrentMasterWallet.GetAllSubWallets().toString());
        JSONArray json = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            ISubWallet subWallet = list.get(i);
            if (subWallet != null) {
                JSONObject jsonObject = new JSONObject();
                mSubWalletMap.put(subWallet.GetChainId(), subWallet);
                jsonObject.put("chainid", subWallet.GetChainId());
                jsonObject.put("balance", (double) subWallet.GetBalance() / 100000000);
                json.add(jsonObject);
            }
        }
        return json;
    }

}