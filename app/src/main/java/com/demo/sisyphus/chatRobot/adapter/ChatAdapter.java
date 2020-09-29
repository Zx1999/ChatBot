package com.demo.sisyphus.chatRobot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.sisyphus.chatRobot.R;
import com.demo.sisyphus.chatRobot.bean.Msg;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter {

    private Context context;

    private static final int ME = 0;
    private static final int OTHRE = 1;

    private  List<Msg> list = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public ChatAdapter(Context context, ArrayList<Msg> list){
        this.context = context;
        this.list = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout me;
        LinearLayout other;

        public ViewHolder(View itemView) {
            super(itemView);
            me = (LinearLayout) itemView.findViewById(R.id.me);
            other = (LinearLayout) itemView.findViewById(R.id.other);
        }

        public LinearLayout getMe() {
            return me;
        }

        public LinearLayout getOther() {
            return other;
        }
    }

    @Override
    //根据不同的ViewType类型，来返回不同的ViewHolder
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewHolder viewHolder = null;
        switch (viewType){
            case ME:
                viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item2, parent, false));
                break;
            case OTHRE:
                viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    //根据不同的ViewType类型，进行不同的数据绑定操作
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }

        TextView tv = new TextView(context);

        Msg msg = list.get(position);

        tv.setText(msg.getMsg());
        tv.setAutoLinkMask(Linkify.ALL);
        tv.setMovementMethod(LinkMovementMethod.getInstance());

        switch (msg.getType()){
            case ME:
                /**
                 * 当recyclerview承载的数据过多的时候，去滑动recyclerview，
                 * 划出屏幕以外的item会重新绑定数据，如果这个时候绑定数据的方式是
                 * viewgroup的addView（）方法的话，会出现item添加很多重复的view
                 * 所以这之前应该执行清除里面view的操作，即removeAllViews（）
                 */
                viewHolder.getMe().removeAllViews();
                tv.setBackgroundResource(R.mipmap.chat_black);
                tv.setTextSize(16);
                viewHolder.getMe().addView(tv);
                break;
            case OTHRE:
                viewHolder.getOther().removeAllViews();
                tv.setBackgroundResource(R.mipmap.chat_blue);
                tv.setTextSize(16);
                viewHolder.getOther().addView(tv);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //给每个position上的item返回一个固定的ViewType类型
    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType() == 0 ? ME : OTHRE;
    }
    //实现OnItemClickListener接口
    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }

}
