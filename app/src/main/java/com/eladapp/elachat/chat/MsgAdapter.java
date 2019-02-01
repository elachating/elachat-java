package com.eladapp.elachat.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lqr.emoji.MoonUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.eladapp.elachat.R;
import static com.lqr.emoji.LQREmotionKit.getContext;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<MsgEntity> mMsg;//消息的实体类集合
    private Context mContext;
    private Timer timer=new Timer();
    int[] chatleftvoice = {R.drawable.chat_voice_left_one,R.drawable.chat_voice_left_tow,R.drawable.chat_voice_left_three};
    int[] chatrightvoice = {R.drawable.chat_voice_right_one,R.drawable.chat_voice_right_tow,R.drawable.chat_voice_right_three};
    int SIGN = 17;
    int num =0;
    int numr =0;
    MediaPlayer mediaPlayer = new MediaPlayer();
    public static interface OnItemClickListener {
        void onItemClick(View view);
        void onItemLongClick(View view);
    }
    public MsgAdapter(Context context,List<MsgEntity> msg){
        this.mMsg=msg;
        this.mContext=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item,parent,false);

        return new ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MsgEntity msg=mMsg.get(position);
        if(msg.getMsgcate()==1){
            if (msg.getType()==MsgEntity.RCV_MSG){
                //接受消息:让发送消息有关的控件隐藏
                holder.send_layout.setVisibility(View.GONE);
                holder.send_img_layout.setVisibility(View.GONE);
                holder.rev_img_layout.setVisibility(View.GONE);
                holder.send_video_layout.setVisibility(View.GONE);
                holder.rev_video_layout.setVisibility(View.GONE);
                holder.rev_layout.setVisibility(View.VISIBLE);
                holder.rev_tv.setText(msg.getContent());
                MoonUtils.identifyFaceExpression(getContext(),holder.rev_tv,msg.getContent(), ImageSpan.ALIGN_BOTTOM);
            }else if (msg.getType()==MsgEntity.SEND_MSG){
                //发送消息:让接收消息有关的控件隐藏
                holder.rev_layout.setVisibility(View.GONE);
                holder.send_img_layout.setVisibility(View.GONE);
                holder.rev_img_layout.setVisibility(View.GONE);
                holder.send_video_layout.setVisibility(View.GONE);
                holder.rev_video_layout.setVisibility(View.GONE);
                holder.send_layout.setVisibility(View.VISIBLE);
                holder.send_tv.setText(msg.getContent());
                MoonUtils.identifyFaceExpression(getContext(),holder.send_tv,msg.getContent(), ImageSpan.ALIGN_BOTTOM);
            }
        }else if(msg.getMsgcate()==2){
            if (msg.getType()==MsgEntity.RCV_MSG){
                //接受消息:让发送消息有关的控件隐藏
                holder.send_layout.setVisibility(View.GONE);
                holder.rev_layout.setVisibility(View.GONE);
                holder.send_video_layout.setVisibility(View.GONE);
                holder.rev_video_layout.setVisibility(View.GONE);
                holder.send_img_layout.setVisibility(View.GONE);
                holder.rev_img_layout.setVisibility(View.VISIBLE);
                if(!msg.getContent().equals("")){
                    holder.chat_img_left.setImageURI(Uri.fromFile(new File(msg.getContent())));
                }
                MoonUtils.identifyFaceExpression(getContext(),holder.rev_tv,msg.getContent(), ImageSpan.ALIGN_BOTTOM);
            }else if (msg.getType()==MsgEntity.SEND_MSG){
                //发送消息:让接收消息有关的控件隐藏
                holder.send_layout.setVisibility(View.GONE);
                holder.rev_layout.setVisibility(View.GONE);
                holder.send_video_layout.setVisibility(View.GONE);
                holder.rev_video_layout.setVisibility(View.GONE);
                holder.send_img_layout.setVisibility(View.VISIBLE);
                holder.rev_img_layout.setVisibility(View.GONE);
                if(!msg.getContent().equals("")){
                    holder.chat_img_right.setImageURI(Uri.fromFile(new File(msg.getContent())));
                }
                MoonUtils.identifyFaceExpression(getContext(),holder.send_tv,msg.getContent(), ImageSpan.ALIGN_BOTTOM);
            }
        }else if(msg.getMsgcate()==3){
            if (msg.getType()==MsgEntity.RCV_MSG){
                //接受消息:让发送消息有关的控件隐藏
                holder.send_layout.setVisibility(View.GONE);
                holder.rev_layout.setVisibility(View.GONE);
                holder.send_img_layout.setVisibility(View.GONE);
                holder.rev_img_layout.setVisibility(View.GONE);
                holder.send_video_layout.setVisibility(View.GONE);
                holder.rev_video_layout.setVisibility(View.VISIBLE);
                if(!msg.getContent().equals("")){
                    //holder.chat_video_left.setImageURI(Uri.fromFile(new File(msg.getContent())));
                    //holder.chat_video_left.setVideoURI(Uri.fromFile(new File(msg.getContent())));
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(msg.getContent());// videoPath 本地视频的路径
                    Bitmap bitmap  = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC );
                    String strDuration = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int duration = Integer.parseInt(strDuration) / 1000;
                    holder.chat_video_left_play_time.setText(String.format("%d:%02d", duration / 60, duration % 60)+"  ");


                    holder.chat_video_left.setImageBitmap(bitmap);//对应的ImageView
                   // holder.chat_video_left.setVideoURI(Uri.parse(msg.getContent()));
                  //  holder.chat_video_left.start();
                }
                MoonUtils.identifyFaceExpression(getContext(),holder.rev_tv,msg.getContent(), ImageSpan.ALIGN_BOTTOM);
            }else if (msg.getType()==MsgEntity.SEND_MSG){
                //发送消息:让接收消息有关的控件隐藏
                holder.send_layout.setVisibility(View.GONE);
                holder.rev_layout.setVisibility(View.GONE);
                holder.send_img_layout.setVisibility(View.GONE);
                holder.rev_img_layout.setVisibility(View.GONE);
                holder.rev_video_layout.setVisibility(View.GONE);
                holder.send_video_layout.setVisibility(View.VISIBLE);
                if(!msg.getContent().equals("")){
                   // holder.chat_video_right.setImageURI(Uri.fromFile(new File(msg.getContent())));
                    //holder.chat_video_right.setVideoURI(Uri.fromFile(new File(msg.getContent())));

                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(msg.getContent());// videoPath 本地视频的路径
                    Bitmap bitmap  = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC );
                    holder.chat_video_right.setImageBitmap(bitmap);

                    String strDuration = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int duration = Integer.parseInt(strDuration) / 1000;
                    holder.chat_video_right_play_time.setText(String.format("%d:%02d", duration / 60, duration % 60)+"  ");
                   // holder.chat_video_left.setImageBitmap(bitmap);//对应的ImageView
                    //holder.chat_video_right.setVideoURI(Uri.parse(msg.getContent()));
                    //holder.chat_video_right.start();
                }
                MoonUtils.identifyFaceExpression(getContext(),holder.send_tv,msg.getContent(), ImageSpan.ALIGN_BOTTOM);
            }
        }else if(msg.getMsgcate()==4){
            if (msg.getType()==MsgEntity.RCV_MSG){
                //接受消息:让发送消息有关的控件隐藏
                holder.send_layout.setVisibility(View.GONE);
                holder.rev_layout.setVisibility(View.GONE);
                holder.send_img_layout.setVisibility(View.GONE);
                holder.rev_img_layout.setVisibility(View.GONE);
                holder.send_video_layout.setVisibility(View.GONE);
                holder.rev_video_layout.setVisibility(View.GONE);
                holder.send_audio_layout.setVisibility(View.GONE);
                holder.rev_audio_layout.setVisibility(View.VISIBLE);
                if(!msg.getContent().equals("")){
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(msg.getContent());
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int duration =  mediaPlayer.getDuration() / 1000;
                    holder.chat_audio_left_time.setText(String.format("%d'%02d''", duration / 60, duration % 60)+"  ");
                    MoonUtils.identifyFaceExpression(getContext(),holder.send_tv,msg.getContent(), ImageSpan.ALIGN_BOTTOM);
                }
            }else if (msg.getType()==MsgEntity.SEND_MSG){
                //发送消息:让接收消息有关的控件隐藏
                holder.send_layout.setVisibility(View.GONE);
                holder.rev_layout.setVisibility(View.GONE);
                holder.send_img_layout.setVisibility(View.GONE);
                holder.rev_img_layout.setVisibility(View.GONE);
                holder.rev_video_layout.setVisibility(View.GONE);
                holder.send_video_layout.setVisibility(View.GONE);
                holder.rev_audio_layout.setVisibility(View.GONE);
                holder.send_audio_layout.setVisibility(View.VISIBLE);
                if(!msg.getContent().equals("")){
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(msg.getContent());
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int duration =  mediaPlayer.getDuration() / 1000;
                    holder.chat_audio_right_time.setText(String.format("%d'%02d''", duration / 60, duration % 60)+"  ");
                    MoonUtils.identifyFaceExpression(getContext(),holder.send_tv,msg.getContent(), ImageSpan.ALIGN_BOTTOM);
                }
            }
        }
        holder.chat_img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //startActivity(new Intent(mContext, ImgActivity.class).putExtra("imgsrc", msg.getContent().toString()));
                Intent intent = new Intent(mContext,ImgActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("imgsrc", msg.getContent().toString());
                mContext.startActivity(intent);
            }
        });
        holder.chat_img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //startActivity(new Intent(mContext, ImgActivity.class).putExtra("imgsrc", msg.getContent().toString()));
                Intent intent = new Intent(mContext,ImgActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("imgsrc", msg.getContent().toString());
                mContext.startActivity(intent);
            }
        });
        holder.chat_video_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //startActivity(new Intent(mContext, ImgActivity.class).putExtra("imgsrc", msg.getContent().toString()));
                Intent intent = new Intent(mContext,PlayvideoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videosrc", msg.getContent().toString());
                mContext.startActivity(intent);
            }
        });
        holder.chat_video_left_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext,PlayvideoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videosrc", msg.getContent().toString());
                mContext.startActivity(intent);
            }
        });
        holder.chat_video_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //startActivity(new Intent(mContext, ImgActivity.class).putExtra("imgsrc", msg.getContent().toString()));
                Intent intent = new Intent(mContext,PlayvideoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videosrc", msg.getContent().toString());
                System.out.println("点击右侧");
                mContext.startActivity(intent);
            }
        });
        holder.chat_video_right_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext,PlayvideoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videosrc", msg.getContent().toString());
                mContext.startActivity(intent);
            }
        });
        holder.chat_audio_left_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    if(mediaPlayer != null &&  mediaPlayer.isPlaying()) {
                        //timer.cancel();
                        mediaPlayer.stop();
                    }
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(msg.getContent());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run(){
                        holder.chat_audio_left_play.setBackgroundResource(chatleftvoice[num++]);
                        if (num >= chatleftvoice.length) {
                            num = 0;
                        }
                        if(!mediaPlayer.isPlaying()){
                            timer.cancel();
                            holder.chat_audio_left_play.setBackgroundResource(chatleftvoice[2]);
                        }
                    }
                },0,300);
                */
            }
        });
        holder.chat_audio_right_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    if(mediaPlayer != null &&  mediaPlayer.isPlaying()) {
                        //timer.cancel();
                        mediaPlayer.stop();
                    }
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(msg.getContent());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //holder.chat_audio_right_play.setBackgroundResource(chatrightvoice[1]);
                /*
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run(){
                        holder.chat_audio_right_play.setBackgroundResource(chatrightvoice[numr++]);
                        if (numr >= chatrightvoice.length) {
                           numr = 0;
                        }
                        if(!mediaPlayer.isPlaying()){
                            timer.cancel();
                            holder.chat_audio_right_play.setBackgroundResource(R.drawable.chat_voice_right_three);
                        }
                    }
                },0,300);
                */
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMsg.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout rev_layout;
        LinearLayout send_layout;
        LinearLayout rev_img_layout;
        LinearLayout send_img_layout;
        LinearLayout rev_video_layout;
        LinearLayout send_video_layout;
        LinearLayout rev_audio_layout;
        LinearLayout send_audio_layout;
        ImageView chat_img_left;
        ImageView chat_img_right;
        ImageView chat_video_left;
        ImageView chat_video_right;
        ImageButton chat_video_left_play;
        ImageButton chat_video_right_play;
        ImageButton chat_audio_left_play;
        ImageButton chat_audio_right_play;
        Button chat_video_left_play_time;
        TextView chat_video_right_play_time;
        TextView chat_audio_left_time;
        TextView chat_audio_right_time;
        TextView rev_tv;
        TextView send_tv;
        public ViewHolder(View itemView) {
            super(itemView);
            rev_layout=itemView.findViewById(R.id.rev_layout);
            send_layout=itemView.findViewById(R.id.send_layout);
            rev_img_layout=itemView.findViewById(R.id.rev_img_layout);
            send_img_layout=itemView.findViewById(R.id.send_img_layout);
            rev_video_layout=itemView.findViewById(R.id.rev_video_layout);
            send_video_layout=itemView.findViewById(R.id.send_video_layout);
            rev_audio_layout=itemView.findViewById(R.id.rev_audio_layout);
            send_audio_layout=itemView.findViewById(R.id.send_audio_layout);
            chat_img_left=itemView.findViewById(R.id.chat_img_left);
            chat_img_right=itemView.findViewById(R.id.chat_img_right);
            chat_video_left=itemView.findViewById(R.id.chat_video_left);
            chat_video_right=itemView.findViewById(R.id.chat_video_right);
            chat_video_left_play = itemView.findViewById(R.id.chat_video_left_play);
            chat_video_right_play = itemView.findViewById(R.id.chat_video_right_play);
            chat_video_left_play_time = itemView.findViewById(R.id.chat_video_left_play_time);
            chat_video_right_play_time = itemView.findViewById(R.id.chat_video_right_play_time);
            chat_audio_left_time=itemView.findViewById(R.id.chat_audio_left_time);
            chat_audio_right_time=itemView.findViewById(R.id.chat_audio_right_time);
            chat_audio_left_play = itemView.findViewById(R.id.chat_audio_left_play);
            chat_audio_right_play = itemView.findViewById(R.id.chat_audio_right_play);
            rev_tv=itemView.findViewById(R.id.rev_tv);
            send_tv=itemView.findViewById(R.id.send_tv);
        }
    }



}
