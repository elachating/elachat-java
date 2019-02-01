package com.eladapp.elachat.chat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eladapp.elachat.R;
import java.io.File;

/**
 * @author liu
 * @date 2018-10-3
 */
public class ImgActivity extends Activity implements OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       String imgsrc = this.getIntent().getStringExtra("imgsrc");
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_img);
        ImageView img = (ImageView)findViewById(R.id.chat_img_big);
        img.setImageURI(Uri.fromFile(new File(imgsrc)));
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onClick(View v) {
        finish();
    }
}