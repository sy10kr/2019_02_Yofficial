package com.example.yofficial;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CommentAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<CommentItem> commentList = null;
    private ArrayList<CommentItem> listview;

    public CommentAdapter(Context context, List<CommentItem> commentList) {
        this.context = context;
        this.commentList = commentList;
        inflater = LayoutInflater.from(context);
        this.listview = new ArrayList<CommentItem>();
        this.listview.addAll(commentList);
    }

    public void setCommentList(List<CommentItem> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int getCount(){
        return commentList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final ViewHolder holder;
        final CommentItem potion = commentList.get(position);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.c_item, null);

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            holder.tv1 = (TextView) convertView.findViewById(R.id.txt_c_u) ;
            holder.tv2 = (TextView) convertView.findViewById(R.id.txt_c_date) ;
            holder.tv3= (TextView) convertView.findViewById(R.id.txt_c_data) ;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv1.setText(potion.user_id);
        holder.tv2.setText(potion.upload_date);
        holder.tv3.setText(potion.comment_data);

        return convertView;
    }

    public class ViewHolder {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        ImageView iv_icon;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public CommentItem getItem(int position) {
        return commentList.get(position) ;
    }




/*
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        commentList.clear();
        if (charText.length() == 0) {
            commentList.addAll(listview);
        } else {
            for (CommentItem potion : listview) {
                if (potion.getV_title().toLowerCase(Locale.getDefault()).contains(charText)) {
                    videoList.add(potion);
                }
            }
        }
        notifyDataSetChanged();
    }

 */

}
