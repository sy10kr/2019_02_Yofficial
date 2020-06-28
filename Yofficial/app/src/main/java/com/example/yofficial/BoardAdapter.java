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

public class BoardAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    private List<BoardItem> boardList = null;
    private ArrayList<BoardItem> listview;

    public BoardAdapter(Context context, List<BoardItem> boardList) {
        this.context = context;
        this.boardList = boardList;
        inflater = LayoutInflater.from(context);
        this.listview = new ArrayList<BoardItem>();
        this.listview.addAll(boardList);
    }


    public void setBoardList(List<BoardItem> boardList) {
        this.boardList = boardList;
    }

    @Override
    public int getCount(){
        return boardList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final BoardAdapter.ViewHolder holder;
        final BoardItem potion = boardList.get(position);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            holder = new BoardAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.b_item, null);

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            holder.tv1 = (TextView) convertView.findViewById(R.id.txt_c_date) ;
            holder.tv2 = (TextView) convertView.findViewById(R.id.txt_c_u) ;
            holder.tv3= (TextView) convertView.findViewById(R.id.txt_c_data) ;
            convertView.setTag(holder);
        }
        else{
            holder = (BoardAdapter.ViewHolder) convertView.getTag();
        }

        holder.tv1.setText(potion.board_date);
        holder.tv2.setText(potion.board_uploader);
        holder.tv3.setText(potion.board_title);

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
    public BoardItem getItem(int position) {
        return boardList.get(position) ;
    }
}
