package com.eladapp.elachat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.eladapp.elachat.chat.Fragmentapp;
import com.eladapp.elachat.chat.Fragmentmessage;
import com.eladapp.elachat.chat.Fragmentprofile;
import com.eladapp.elachat.chat.Fragmentwallet;

public class ElachatActivity extends AppCompatActivity {
    private FragmentTransaction mFragmentTransaction;//fragment事务
    private FragmentManager mFragmentManager;//fragment管理者
    private Fragmentmessage fragmentmessage;
    private Fragmentprofile fragmentprofile;
    private Fragmentwallet fragmentwallet;
    private Fragmentapp fragmentapp;
    private int id;
    private BottomNavigationBar  bottomNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elachat);
        //Intent intent = getIntent();
        id = getIntent().getIntExtra("id", 0);
        barinit(id);
        changeFragment(id);
        }
    private void barinit(int ids){
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .setActiveColor("#2e2e2e")//选中颜色 图标和文字
                .setInActiveColor("#8e8e8e")//默认未选择颜色
                .setBarBackgroundColor("#ECECEC");//默认背景色
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.bottom_message_checked,"消息").setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.bottom_message_unchecked)))
                .addItem(new BottomNavigationItem(R.drawable.bottom_app_checked,"应用").setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.bottom_app_unchecked)))
                .addItem(new BottomNavigationItem(R.drawable.bottom_asset_checked,"资产").setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.bottom_asset_unchecked)))
                .addItem(new BottomNavigationItem(R.drawable.bottom_me_checked,"我").setInactiveIcon(ContextCompat.getDrawable(this,R.drawable.bottom_me_unchecked)))
                .setFirstSelectedPosition(ids)
                .initialise();
        mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransactiona = mFragmentManager.beginTransaction();
        fragmentmessage = new Fragmentmessage();
        mFragmentTransactiona.add(R.id.splash_root, fragmentmessage);
        mFragmentTransactiona.commit();
        bottomNavigationBar
                .setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        changeFragment(position);
                    }
                    @Override
                    public void onTabUnselected(int position) {

                    }

                    @Override
                    public void onTabReselected(int position) {

                    }
                });
    }
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        //如果此fragment不为空的话就隐藏起来
        if (fragmentapp != null) {
            fragmentTransaction.hide(fragmentapp);
        }
        if(fragmentmessage != null) {
            fragmentTransaction.hide(fragmentmessage);
        }
        if (fragmentwallet!= null) {
            fragmentTransaction.hide(fragmentwallet);
        }
        if (fragmentprofile != null) {
            fragmentTransaction.hide(fragmentprofile);
        }
    }
    private void changeFragment(int position){
        mFragmentManager = getFragmentManager();
        //开启事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        //显示之前将所有的fragment都隐藏起来,在去显示我们想要显示的fragment
        hideFragment(mFragmentTransaction);
        bottomNavigationBar.setFirstSelectedPosition(position);
        switch (position){
            case 0:
                //Toast.makeText(ElawalletaActivity.this,"这是新消息",Toast.LENGTH_SHORT).show();
                if (fragmentmessage == null) {
                    fragmentmessage = new Fragmentmessage();
                    mFragmentTransaction.add(R.id.splash_root, fragmentmessage);

                } else {
                    mFragmentTransaction.show(fragmentmessage);
                    // mFragmentTransaction.replace(R.id.splash_root,fragmentmessage).commit();
                }
                break;
            case 1:
                // Toast.makeText(ElawalletaActivity.this,"这是联系人",Toast.LENGTH_SHORT).show();
                if (fragmentapp == null) {
                    fragmentapp = new Fragmentapp();
                    mFragmentTransaction.add(R.id.splash_root, fragmentapp);
                } else {
                    mFragmentTransaction.show(fragmentapp);
                    // mFragmentTransaction.replace(R.id.splash_root,fragmentcontact).commit();
                }
                break;
            case 2:
                //Toast.makeText(ElawalletaActivity.this,"行情研发中",Toast.LENGTH_SHORT).show();

                if (fragmentwallet == null) {
                    fragmentwallet = new Fragmentwallet();
                    mFragmentTransaction.add(R.id.splash_root, fragmentwallet);
                } else {
                    mFragmentTransaction.show(fragmentwallet);
                    //mFragmentTransaction.add(R.id.splash_root, fragmentwallet);
                    //mFragmentTransaction.replace(R.id.splash_root,fragmentwallet).commit();
                }

                break;
            case 3:
                //Toast.makeText(ElachatActivity.this,"应用研发中",Toast.LENGTH_SHORT).show();
                if (fragmentprofile == null) {
                    fragmentprofile = new Fragmentprofile();
                    mFragmentTransaction.add(R.id.splash_root, fragmentprofile);
                } else {
                    mFragmentTransaction.show(fragmentprofile);
                    //mFragmentTransaction.replace(R.id.splash_root,fragmentdapps).commit();
                }
                break;
        }
        mFragmentTransaction.commit();
    }
}
