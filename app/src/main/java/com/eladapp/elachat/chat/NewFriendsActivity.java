package com.eladapp.elachat.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import android.os.Messenger;

public class NewFriendsActivity extends AppCompatActivity {
    private ListView listView;
    private NewFriendsListAdapter newFriendsListAdapter;
    private Messenger messenger;
    private Db db = new Db();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);
        listView = (ListView) findViewById(R.id.listview_list);
        List<Map<String, String>> list = new ArrayList<Map<String,String>>();
        list = db.getfriendlistlist();
        newFriendsListAdapter = new NewFriendsListAdapter(this, list);
        listView.setAdapter(newFriendsListAdapter);
    }
    public void back(View view) {
        finish();
    }
}
