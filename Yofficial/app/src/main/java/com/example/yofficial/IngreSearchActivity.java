package com.example.yofficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class IngreSearchActivity extends AppCompatActivity {

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private Ingre_SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> selectedlist;
    private ArrayList<String> everylist;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Ingredients ingreDB;
    private Context c = this;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingre_search);



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("ingredients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ingreDB = dataSnapshot.getValue(Ingredients.class);

                editSearch = (EditText) findViewById(R.id.editSearch);
                listView = (ListView) findViewById(R.id.listView);


                // 리스트를 생성한다.
                list = new ArrayList<String>();

                // 검색에 사용할 데이터을 미리 저장한다.
                for (int i = 0; i < ingreDB.meats.size(); i++) {    //육류 추가
                    list.add(ingreDB.meats.get(i));
                }

                for (int i = 0; i < ingreDB.begetables.size();i++) {    //채소류 추가
                    list.add(ingreDB.begetables.get(i));
                }

                for (int i = 0; i < ingreDB.grains.size();i++) {    //곡물류 추가
                    list.add(ingreDB.grains.get(i));
                }

                for (int i = 0; i < ingreDB.fruits.size();i++) {    //과일류 추가
                    list.add(ingreDB.fruits.get(i));
                }

                for (int i = 0; i < ingreDB.seafoods.size();i++) {  //해산물류 추가
                    list.add(ingreDB.seafoods.get(i));
                }

                for (int i = 0; i < ingreDB.eggs.size();i++) {  //달걀류 추가
                    list.add(ingreDB.eggs.get(i));
                }

                for (int i = 0; i < ingreDB.nuts.size();i++) {  //견과류 추가
                    list.add(ingreDB.nuts.get(i));
                }

                for (int i = 0; i < ingreDB.noodles.size();i++) {   //면류 추가
                    list.add(ingreDB.noodles.get(i));
                }

                for (int i = 0; i < ingreDB.oils.size();i++) {  //유지류 추가
                    list.add(ingreDB.oils.get(i));
                }

                for (int i = 0; i < ingreDB.sauces.size();i++) {    //앙념류 추가
                    list.add(ingreDB.sauces.get(i));
                }



                // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
                everylist = new ArrayList<String>();
                everylist.addAll(list);

                selectedlist = new ArrayList<>();
                selectedlist.addAll(list);

                // 리스트에 연동될 아답터를 생성한다.
                adapter = new Ingre_SearchAdapter(list, c);

                // 리스트뷰에 아답터를 연결한다.
                listView.setAdapter(adapter);

                Spinner classfier_spinner = findViewById(R.id.classifier_spinner);
                ArrayAdapter classifierAdapter = ArrayAdapter.createFromResource(c, R.array.data_ingredient_class, android.R.layout.simple_spinner_item);
                classifierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                classfier_spinner.setAdapter(classifierAdapter);

                classfier_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            list.clear();

                            for (int i = 0; i < everylist.size(); i++) {
                                list.add(everylist.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(everylist);
                            adapter.notifyDataSetChanged();
                        }

                        if (position == 1) {
                            list.clear();
                            for (int i = 0; i < ingreDB.meats.size(); i++) {
                                list.add(ingreDB.meats.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        if (position == 2) {
                            list.clear();
                            for (int i = 0; i < ingreDB.begetables.size(); i++) {
                                list.add(ingreDB.begetables.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        if (position == 3) {
                            list.clear();
                            for (int i = 0; i < ingreDB.grains.size(); i++) {
                                list.add(ingreDB.grains.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(list);
                            adapter.notifyDataSetChanged();
                        }

                        if (position == 4) {
                            list.clear();
                            for (int i = 0; i < ingreDB.fruits.size(); i++) {
                                list.add(ingreDB.fruits.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(list);
                            adapter.notifyDataSetChanged();
                        }

                        if (position == 5) {
                            list.clear();
                            for (int i = 0; i < ingreDB.seafoods.size(); i++) {
                                list.add(ingreDB.seafoods.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        if (position == 6) {
                            list.clear();
                            for (int i = 0; i < ingreDB.eggs.size(); i++) {
                                list.add(ingreDB.eggs.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        if (position == 7) {
                            list.clear();
                            for (int i = 0; i < ingreDB.nuts.size(); i++) {
                                list.add(ingreDB.nuts.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        if (position == 8) {
                            list.clear();
                            for (int i = 0; i < ingreDB.noodles.size(); i++) {
                                list.add(ingreDB.noodles.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        if (position == 9) {
                            list.clear();
                            for (int i = 0; i < ingreDB.oils.size(); i++) {
                                list.add(ingreDB.oils.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        if (position == 10) {
                            list.clear();
                            for (int i = 0; i < ingreDB.sauces.size(); i++) {
                                list.add(ingreDB.sauces.get(i));
                            }
                            selectedlist.clear();
                            selectedlist.addAll(list);
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

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

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        editSearch.setText((String)list.get(position));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(selectedlist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < selectedlist.size(); i++)
            {

                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.

                if (selectedlist.get(i).toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(selectedlist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }


    public void mOnPopupClick(View v){

        boolean flag = false;

        for (int i = 0; i < selectedlist.size(); i++) {
            if (selectedlist.get(i).toLowerCase().equals(editSearch.getText().toString()))
            {
                flag = true;
            }
        }
        if (flag) {
            Intent intent = new Intent();
            intent.putExtra("result", editSearch.getText().toString());
            System.out.println(editSearch.getText().toString());
            setResult(1234, intent);

            finish();
        } else {
            Toast.makeText(getApplicationContext(), "다른 형식의 값을 입력하셨습니다. ", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                editSearch.setText(result);
            }
        }
    }


}