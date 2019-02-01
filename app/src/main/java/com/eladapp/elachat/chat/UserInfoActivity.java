package com.eladapp.elachat.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import com.eladapp.elachat.R;
import android.os.Messenger;

public class UserInfoActivity extends AppCompatActivity {
    private TextView user_name;
    private TextView user_remark;
    private Button btn_msg;
    private Messenger messenger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        user_name = (TextView) findViewById(R.id.user_name);
        user_name.setText(this.getIntent().getStringExtra("friendId"));
        btn_msg=(Button)findViewById(R.id.btn_msg);
        user_remark = (TextView) findViewById(R.id.tv_rmarke);
        user_remark.setText(this.getIntent().getStringExtra("curremark"));
        btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserInfoActivity.this, ChatActivity.class).putExtra("friendId", user_name.getText()));
            }
        });
    }
    public void back(View view){
        finish();
    }

}
