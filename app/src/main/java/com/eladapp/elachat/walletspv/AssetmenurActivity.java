package com.eladapp.elachat.walletspv;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.eladapp.elachat.R;
/**
 * @author liu
 * @date 2018-10-3
 */
public class AssetmenurActivity extends Activity implements OnClickListener{

    private LinearLayout addasset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assetrightmenu);
        initView();
    }


    private void initView(){
        addasset = findViewById(R.id.add_asset_layout);
        addasset.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_asset_layout:
                Intent intent=new Intent();
                intent.setClass(this,WalletcreateonestepActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        this.finish();
    }
}