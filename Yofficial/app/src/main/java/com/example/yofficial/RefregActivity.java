package com.example.yofficial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RefregActivity extends AppCompatActivity {

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ArrayList<String> select_list = new ArrayList<String>();   // 재료선택 데이터 리스트
    private ListView listview;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private Refreg_SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Ingredients ingreDB;
    private Context c = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreg);
        getSupportActionBar().setElevation(0);
        editSearch = (EditText) findViewById(R.id.refreg_editSearch);
        listview = (ListView) findViewById(R.id.refreg_listView);

        // 리스트를 생성한다.

        //select_list = new ArrayList<String>();
        list = new ArrayList<String>();
        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

    }


    // 검색을 수행하는 메소드
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
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }


    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList(){

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("ingredients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ingreDB = dataSnapshot.getValue(Ingredients.class);



                // 검색에 사용할 데이터을 미리 저장한다.
                for (int i = 0; i < ingreDB.meats.size(); i++) {    //육류 추가
                    list.add(ingreDB.meats.get(i));
                }

                for (int i = 0; i < ingreDB.begetables.size();i++) {    //채소류 추가
                    list.add(ingreDB.begetables.get(i));
                }

                for (int i = 0; i < ingreDB.eggs.size();i++) {  //난류 추가
                    list.add(ingreDB.eggs.get(i));
                }

                for (int i = 0; i < ingreDB.fruits.size();i++) {    //과일류 추가
                    list.add(ingreDB.fruits.get(i));
                }

                for (int i = 0; i < ingreDB.grains.size();i++) {    //곡물류 추가
                    list.add(ingreDB.grains.get(i));
                }

                for (int i = 0; i < ingreDB.noodles.size();i++) {   //면류 추가
                    list.add(ingreDB.noodles.get(i));
                }

                for (int i = 0; i < ingreDB.nuts.size();i++) {  //견과류 추가
                    list.add(ingreDB.nuts.get(i));
                }

                for (int i = 0; i < ingreDB.oils.size();i++) {  //유지류 추가
                    list.add(ingreDB.oils.get(i));
                }

                for (int i = 0; i < ingreDB.sauces.size();i++) {    //앙념류 추가
                    list.add(ingreDB.sauces.get(i));
                }

                for (int i = 0; i < ingreDB.seafoods.size();i++) {  //해산물류 추가
                    list.add(ingreDB.seafoods.get(i));
                }



                // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
                arraylist = new ArrayList<String>();
                arraylist.addAll(list);

                // 리스트에 연동될 아답터를 생성한다.
                adapter = new Refreg_SearchAdapter(list, c);

                // 리스트뷰에 아답터를 연결한다.
                listview.setAdapter(adapter);


                // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
                editSearch.addTextChangedListener(new TextWatcher() {
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
                        String text = editSearch.getText().toString();
                        search(text);
                    }
                });

                // 리스트 목록 눌렀을때 해당 재료 이름 반영
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트 아이템 버튼 작동
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView input= (TextView)findViewById(R.id.refreg_ingredient_input);

                        if(select_list.contains(list.get(position))){

                            select_list.remove(list.get(position));

                            input.setText("선택된 재료(동일재료를 누르면 삭제됩니다!)\n" );
                            for(Object object : select_list) {
                                String element = (String) object;
                                input.append("[" + element + "] ");
                            }
                        }
                        else{
                            input.setText("선택된 재료(동일재료를 누르면 삭제됩니다!)\n" );

                            select_list.add(list.get(position));
                            for(Object object : select_list) {
                                String element = (String) object;
                                input.append("[" + element + "] ");
                            }
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    // 팝업창을 띄워준다 (추천된 요리를 보여줄 팝업창)
    public void mOnPopupClick(View v){
        Intent intent = new Intent(this, Refreg_PopupActivity.class);
        intent.putStringArrayListExtra("ArrayList", select_list);
        Log.d("123", select_list.toString());
        startActivityForResult(intent, 1);
    }
}