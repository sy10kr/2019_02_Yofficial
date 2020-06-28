package com.example.yofficial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {
    ArrayList<BoardItem> list;
    ListView listview;
    BoardAdapter adapter;
    MenuItem mSearch;
    Context c = this;
    String text;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<BoardItem> arraylist;
    private EditText board_search;
    private final static String TAG = "community!";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_list);

        board_search = (EditText)findViewById(R.id.board_search);


        listview = (ListView) findViewById(R.id.listview1);
        list = new ArrayList<BoardItem>();

        arraylist = new ArrayList<BoardItem>();

        adapter = new BoardAdapter(c, list);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        Log.d(TAG,  "get ref");
        myRef.child("boards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, ""+dataSnapshot.getChildrenCount());

                Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                Log.d(TAG,  "get iterator");
                while(itr.hasNext()){
                    BoardItem b = itr.next().getValue(BoardItem.class);
                    Log.d(TAG, b.board_title);
                    list.add(b);

                }
                arraylist.clear();
                arraylist.addAll(list);
                adapter.setBoardList(list);
                listview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트 버튼 작동
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                String board_id= list.get(position).getBoard_id();
                intent.putExtra("id", board_id);
                startActivity(intent);
            }
        });

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        board_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                text =  board_search.getText().toString();
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        Log.d(TAG,  "get ref");
        myRef.child("boards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, ""+dataSnapshot.getChildrenCount());

                list.clear();
                Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                Log.d(TAG,  "get iterator");
                while(itr.hasNext()){
                    BoardItem b = itr.next().getValue(BoardItem.class);
                    Log.d(TAG, b.board_title);
                    list.add(b);

                }
                arraylist.clear();
                arraylist.addAll(list);
                Collections.reverse(list);
                adapter.setBoardList(list);
                listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).board_title.toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    public void make_board(View v){
        Intent intent = new Intent(getApplicationContext(), BoardMakeActivity.class);
        startActivity(intent);
    }

    public void search_board(View v){
        search(text);
    }

}
