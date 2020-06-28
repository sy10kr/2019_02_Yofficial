package com.example.yofficial;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class VideoAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<VideoItem> videoList = null;
    private ArrayList<VideoItem> listview;
    private final static String TAG = "cond!";
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public VideoAdapter(Context context, List<VideoItem> videoList) {
        this.context = context;
        this.videoList = videoList;
        inflater = LayoutInflater.from(context);
        this.listview = new ArrayList<VideoItem>();
        this.listview.addAll(videoList);
    }

    @Override
    public int getCount(){
        return videoList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final ViewHolder holder;
        final VideoItem potion = videoList.get(position);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.v_item, null);

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.imageView1) ;
            holder.tv1 = (TextView) convertView.findViewById(R.id.txt_v_t) ;
            holder.tv2 = (TextView) convertView.findViewById(R.id.txt_v_u) ;
            holder.tv3= (TextView) convertView.findViewById(R.id.txt_v_n) ;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv1.setText(potion.v_title);
        holder.tv1.setSingleLine(true);
        holder.tv2.setText(potion.v_uploader);
        holder.tv3.setText(potion.view_num);
        holder.iv_icon.setImageDrawable(potion.getIcon());

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
    public VideoItem getItem(int position) {
        return videoList.get(position) ;
    }




    int[] checkIsActive(String servings, String mainIng, String type, String feature){
        int[] ans = new int[4];

        if(servings.compareTo("인원수") != 0){
            ans[0] = 1;
        }

        if(mainIng.compareTo("주재료") != 0){
            ans[1] = 1;
        }

        if(type.compareTo("타입") != 0){
            ans[2] = 1;
        }

        if(feature.compareTo("특징") != 0){
            ans[3] = 1;
        }



        return ans;

    }

    public void filter(String charText, String servings, String mainIng, String type, String feature) {
        charText = charText.toLowerCase(Locale.getDefault());
        videoList.clear();
        notifyDataSetChanged();
        Log.d(TAG, "Video cleared");
        if (charText.length() == 0) {
            videoList.addAll(listview);
        } else {



            //Check if conditional search active
            int[] isActive;
            isActive = checkIsActive(servings, mainIng, type, feature);
            //return integer array

            Log.d(TAG, "Active = "+isActive[0]+" "+isActive[1]+" "+ isActive[2]+" " + isActive[3]);



            for (VideoItem potion : listview) {
                Log.d(TAG, "name search");
                if (potion.getV_title().toLowerCase(Locale.getDefault()).contains(charText)) {

                    //if none are active just add and finish
                    //else need to get recipe id and get recipe Info.
                    String recipeId = potion.getRecipe_id();
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference();
                    Log.d(TAG, "succeeded name search");
                    myRef.child("recipes").child(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            RecipeInfo r = dataSnapshot.getValue(RecipeInfo.class);
                            int[] isSatisfy = new int[4];

                            //set flag to 0000
                            //four conditional search and set flags if conditional search is not active then just set to 1

                            Log.d(TAG, "Active = "+isActive[0]+" "+isActive[1]+" "+ isActive[2]+" " + isActive[3]);
                            if(isActive[0] == 0){

                                isSatisfy[0] = 1;
                            }
                            else{
                                if(servings.compareTo(r.getServings()) == 0){
                                    isSatisfy[0] = 1;
                                }
                            }

                            if(isActive[1] == 0){
                                isSatisfy[1] = 1;
                            }
                            else{
                                if(mainIng.compareTo(r.getMainIngredient()) == 0){
                                    isSatisfy[1] = 1;
                                }
                            }


                            if(isActive[2] == 0){
                                isSatisfy[2] = 1;
                            }
                            else{
                                if(type.compareTo(r.getType()) == 0){
                                    isSatisfy[2] = 1;
                                }
                            }
                            if(isActive[3] == 0){
                                isSatisfy[3] = 1;
                            }
                            else{
                                if(feature.compareTo(r.getFeature()) == 0){
                                    isSatisfy[3] = 1;
                                }
                            }

                            Log.d(TAG, "Satisfy = "+isSatisfy[0]+" "+isSatisfy[1]+" "+ isSatisfy[2]+" " + isSatisfy[3]);

                            //if all 4 is 1 then add
                            if(isSatisfy[0] == 1 && isSatisfy[1] == 1 && isSatisfy[2] == 1 && isSatisfy[3] == 1){
                                videoList.add(potion);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        }

    }

}
