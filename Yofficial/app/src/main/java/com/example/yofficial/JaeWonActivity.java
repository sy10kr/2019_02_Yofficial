package com.example.yofficial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class JaeWonActivity extends Activity{

    private static final String TAG = "JaeWonActivity!";

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Ingredients i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate : Starting.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jaewon);


        /*

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
*/
        /*
        Ingredients i = new Ingredients();
        ArrayList<String> meats = new ArrayList<String>();
        ArrayList<String> grains = new ArrayList<String>();
        ArrayList<String> begetables = new ArrayList<String>();
        ArrayList<String> fruits = new ArrayList<String>();
        ArrayList<String> seafoods = new ArrayList<String>();
        ArrayList<String> eggs = new ArrayList<String>();
        ArrayList<String> nuts = new ArrayList<String>();
        ArrayList<String> noodles = new ArrayList<String>();
        ArrayList<String> sauces = new ArrayList<String>();
        ArrayList<String> oils = new ArrayList<String>();

        meats.add("살치살");
        meats.add("업진살");
        meats.add("수구레");
        meats.add("치마살");
        meats.add("번데기");
        meats.add("부채살");
        meats.add("차돌박이");
        meats.add("갈매기살");
        meats.add("닭가슴살");
        meats.add("닭똥집");
        meats.add("푸아그라");
        meats.add("닭발");
        meats.add("토시살");
        meats.add("안창살");
        meats.add("프로슈토");
        meats.add("삼겹살");
        meats.add("홍두깨살");
        meats.add("돼지 앞다리살");
        meats.add("소 갈비살");
        meats.add("LA 갈비");
        meats.add("채끝살");
        meats.add("제비추리");
        meats.add("소 대창");
        meats.add("소 꽃등심");
        meats.add("항정살");
        meats.add("돼지 등심덧살");
        meats.add("삼각살");
        meats.add("꾸리살");
        meats.add("닭안심살");
        meats.add("소 간");
        meats.add("소 막창");
        meats.add("돼지 목살");
        meats.add("닭봉");
        meats.add("선지");
        meats.add("아롱사태");
        meats.add("돼지 곱창");
        meats.add("돼지 안심살");
        meats.add("소 앞다리살");
        meats.add("돼지 창자");
        meats.add("양지머리");
        meats.add("소 안심살");
        meats.add("소 곱창");
        meats.add("우설");
        meats.add("우둔살");
        meats.add("소 윗등심살");
        meats.add("돼지 간");
        meats.add("돼지 전지");
        meats.add("소 설깃살");
        meats.add("도가니뼈");
        meats.add("토시살");
        meats.add("돼지갈비");
        meats.add("처녑");
        meats.add("벌집양");
        meats.add("소 보섭살");
        meats.add("소 꼬리");
        meats.add("치마양지");
        meats.add("소 목심살");
        meats.add("사골");
        meats.add("소 꽃갈비");
        meats.add("소 본갈비");
        meats.add("소 등심");
        meats.add("돼지 갈비살");
        meats.add("소 염통");
        meats.add("돼지 토시살");
        meats.add("돼지 염통");
        meats.add("소 마구리");
        meats.add("소 앞사태");

        grains.add("오트밀");
        grains.add("쥐눈이콩");
        grains.add("검은콩");
        grains.add("보리");
        grains.add("콩");
        grains.add("들깨");
        grains.add("기장");
        grains.add("흑미");
        grains.add("차조");
        grains.add("호밀");
        grains.add("통밀");
        grains.add("대두");
        grains.add("완두콩");
        grains.add("밤콩");
        grains.add("동부콩");
        grains.add("사탕수수");
        grains.add("검은깨");
        grains.add("노란메주콩");
        grains.add("현미");
        grains.add("참깨");
        grains.add("녹두");
        grains.add("강낭콩");
        grains.add("흰콩");
        grains.add("귀리");
        grains.add("옥수수");
        grains.add("땅콩");
        grains.add("청보리");
        grains.add("찹쌀");
        grains.add("노란콩");
        grains.add("겉보리");
        grains.add("적두");
        grains.add("청태");
        grains.add("율무");
        grains.add("좁쌀");
        grains.add("깨");
        grains.add("오트");
        grains.add("밀");
        grains.add("거피팥");
        grains.add("수수");
        grains.add("완두콩");
        grains.add("발아현미");
        grains.add("잠두");
        grains.add("조");
        grains.add("메밀");
        grains.add("쌀겨");
        grains.add("맥아");
        grains.add("귀리");

        begetables.add("토마토");
        begetables.add("송로버섯");
        begetables.add("당귀");
        begetables.add("돼지감자");
        begetables.add("케일");
        begetables.add("브로콜리");
        begetables.add("능이버섯");
        begetables.add("레드비트");
        begetables.add("표고버섯");
        begetables.add("상황버섯");
        begetables.add("순무");
        begetables.add("영지버섯");
        begetables.add("고들빼기");
        begetables.add("목이버섯");
        begetables.add("마늘");
        begetables.add("어성초");
        begetables.add("우엉");
        begetables.add("파프리카");
        begetables.add("부추");
        begetables.add("느타리버섯");
        begetables.add("무청");
        begetables.add("배추");
        begetables.add("우거지");
        begetables.add("노루궁뎅이버섯");
        begetables.add("새송이버섯");
        begetables.add("늙은호박");
        begetables.add("갓");
        begetables.add("아스파라거스");
        begetables.add("팽이버섯");
        begetables.add("방울토마토");
        begetables.add("양상추");
        begetables.add("아욱");
        begetables.add("양송이버섯");
        begetables.add("오크라");
        begetables.add("루꼴라");
        begetables.add("머위");
        begetables.add("방아잎");
        begetables.add("상추");
        begetables.add("잎새버섯");
        begetables.add("고추");
        begetables.add("자색고구마");
        begetables.add("호박고구마");
        begetables.add("애호박");
        begetables.add("비단풀");
        begetables.add("청경채");
        begetables.add("콜리플라워");
        begetables.add("싸리버섯");
        begetables.add("페퍼민트");
        begetables.add("청양고추");
        begetables.add("쑥갓");
        begetables.add("참나물");
        begetables.add("쪽파");
        begetables.add("양배추");
        begetables.add("박하");
        begetables.add("유채");
        begetables.add("양파");
        begetables.add("돌산갓");
        begetables.add("쑥");
        begetables.add("무순");
        begetables.add("콩나물");
        begetables.add("동아");
        begetables.add("제피");
        begetables.add("달래");
        begetables.add("명아주");
        begetables.add("무");
        begetables.add("열무");
        begetables.add("딜");
        begetables.add("셀러리");
        begetables.add("토란대");
        begetables.add("초고버섯");
        begetables.add("숙주나물");
        begetables.add("가지");
        begetables.add("총각무");
        begetables.add("생강");
        begetables.add("펜넬");
        begetables.add("감자");
        begetables.add("취나물");
        begetables.add("생강");
        begetables.add("밤버섯");
        begetables.add("당근");
        begetables.add("얌");
        begetables.add("곰취");
        begetables.add("연근");
        begetables.add("오이");
        begetables.add("꽈리고추");
        begetables.add("도라지");
        begetables.add("풋고추");
        begetables.add("얼갈이배추");
        begetables.add("대파");
        begetables.add("시금치");
        begetables.add("양송이");



        fruits.add("단감");
        fruits.add("아보카도");
        fruits.add("바나나");
        fruits.add("딸기");
        fruits.add("홍시");
        fruits.add("무화과");
        fruits.add("두리안");
        fruits.add("망고스틴");
        fruits.add("키위");
        fruits.add("자몽");
        fruits.add("오미자");
        fruits.add("리치");
        fruits.add("곶감");
        fruits.add("구아바");
        fruits.add("파파야");
        fruits.add("올리브");
        fruits.add("키위");
        fruits.add("귤");
        fruits.add("오디");
        fruits.add("으름");
        fruits.add("크랜베리");
        fruits.add("보리수");
        fruits.add("탱자");
        fruits.add("청포도");
        fruits.add("체리");
        fruits.add("자두");
        fruits.add("라즈베리");
        fruits.add("라임");
        fruits.add("석류");
        fruits.add("사과");
        fruits.add("배");
        fruits.add("참외");
        fruits.add("앵두");
        fruits.add("오렌지");
        fruits.add("살구");
        fruits.add("바나나");
        fruits.add("산수유");
        fruits.add("코코넛");
        fruits.add("레몬");
        fruits.add("한라봉");
        fruits.add("매실");
        fruits.add("대추");
        fruits.add("포도");
        fruits.add("파인애플");
        fruits.add("수박");
        fruits.add("망고");
        fruits.add("유자");
        fruits.add("다래");
        fruits.add("복숭아");
        fruits.add("멜론");


        seafoods.add("청각");
        seafoods.add("도루묵");
        seafoods.add("연어");
        seafoods.add("다금바리");
        seafoods.add("양미리");
        seafoods.add("메로");
        seafoods.add("갑오징어");
        seafoods.add("전갱이");
        seafoods.add("미역");
        seafoods.add("킹크랩");
        seafoods.add("농어");
        seafoods.add("붕장어");
        seafoods.add("다시마");
        seafoods.add("삼치");
        seafoods.add("새우");
        seafoods.add("골뱅이");
        seafoods.add("아귀");
        seafoods.add("도미");
        seafoods.add("모자반");
        seafoods.add("참치");
        seafoods.add("우뭇가사리");
        seafoods.add("참돔");
        seafoods.add("까나리");
        seafoods.add("한치");
        seafoods.add("보리새우");
        seafoods.add("코다리");
        seafoods.add("새조개");
        seafoods.add("과메기");
        seafoods.add("돗돔");
        seafoods.add("황어");
        seafoods.add("정어리");
        seafoods.add("전복");
        seafoods.add("굴");
        seafoods.add("밴댕이");
        seafoods.add("망둥어");
        seafoods.add("세발낙지");
        seafoods.add("참게");
        seafoods.add("맛조개");
        seafoods.add("방어");
        seafoods.add("명란");
        seafoods.add("노래미");
        seafoods.add("참다랑어");
        seafoods.add("가다랑어");
        seafoods.add("꽁치");
        seafoods.add("날치알");
        seafoods.add("동태");
        seafoods.add("참소라");
        seafoods.add("성게알");
        seafoods.add("주꾸미");
        seafoods.add("연어");
        seafoods.add("광어");
        seafoods.add("키조개");
        seafoods.add("홍어");
        seafoods.add("비단잉어");
        seafoods.add("부세");
        seafoods.add("홍합");
        seafoods.add("대하");
        seafoods.add("피문어");
        seafoods.add("가리비");
        seafoods.add("고등어");
        seafoods.add("김");
        seafoods.add("캐비아");
        seafoods.add("복어");
        seafoods.add("전어");
        seafoods.add("낙지");
        seafoods.add("오징어");
        seafoods.add("장어");
        seafoods.add("대구");
        seafoods.add("갈치");
        seafoods.add("명태");
        seafoods.add("소라");
        seafoods.add("매생이");
        seafoods.add("개불");
        seafoods.add("해삼");
        seafoods.add("말미잘");
        seafoods.add("조기");
        seafoods.add("다슬기");
        seafoods.add("조개");
        seafoods.add("쥐포");
        seafoods.add("파래");
        seafoods.add("숭어");
        seafoods.add("도다리");



        eggs.add("계란");
        eggs.add("메추라기알");
        eggs.add("오리알");
        eggs.add("거위알");
        eggs.add("꿩알");


        nuts.add("아몬드");
        nuts.add("호두");
        nuts.add("잣");
        nuts.add("캐슈넛");
        nuts.add("피스타치오");
        nuts.add("해바리기씨");
        nuts.add("호박씨");
        nuts.add("도토리");
        nuts.add("헤이즐넛");
        nuts.add("견과류");
        nuts.add("개암");
        nuts.add("땅콩");
        nuts.add("밤");
        nuts.add("은행");
        nuts.add("월넛");
        nuts.add("해바리기씨");
        nuts.add("잣");
        nuts.add("양귀비씨");
        nuts.add("도토리");
        nuts.add("카카오");
        nuts.add("밀");


        noodles.add("쿠스쿠스");
        noodles.add("스파게티면");
        noodles.add("푸실리");
        noodles.add("당면");
        noodles.add("쫄면");
        noodles.add("펜네");
        noodles.add("마카로니");
        noodles.add("쌀국수면");
        noodles.add("링귀니");
        noodles.add("건면");
        noodles.add("소면");
        noodles.add("라면");
        noodles.add("리가토니");
        noodles.add("콘킬리에");
        noodles.add("라자냐");
        noodles.add("우동면");
        noodles.add("보콘치니");

        sauces.add("고추냉이");
        sauces.add("와사비");
        sauces.add("바질");
        sauces.add("식초");
        sauces.add("카레");
        sauces.add("파슬리");
        sauces.add("마늘");
        sauces.add("베이킹소다");
        sauces.add("메이플시럽");
        sauces.add("시나몬");
        sauces.add("솔잎");
        sauces.add("맛술");
        sauces.add("소금");
        sauces.add("민트");
        sauces.add("낫토");
        sauces.add("고추장");
        sauces.add("춘장");
        sauces.add("설탕");
        sauces.add("간장");
        sauces.add("된장");
        sauces.add("조청");
        sauces.add("발사믹");
        sauces.add("올리고당");
        sauces.add("간장");
        sauces.add("겨자");
        sauces.add("맛소금");
        sauces.add("엿기름");
        sauces.add("부침가루");
        sauces.add("참깨");
        sauces.add("고춧가루");
        sauces.add("생강");
        sauces.add("깨소금");
        sauces.add("고추");
        sauces.add("튀김가루");
        sauces.add("고구마전분");
        sauces.add("감자전분");
        sauces.add("밀가루");
        sauces.add("미숫가루");
        sauces.add("콩가루");
        sauces.add("쌈장");
        sauces.add("이스트");
        sauces.add("굴소스");
        sauces.add("코코넛밀크");
        sauces.add("장어소스");
        sauces.add("참깨드레싱");
        sauces.add("튀김소스");
        sauces.add("타르타르 소스");
        sauces.add("간장");
        sauces.add("불고기 소스");
        sauces.add("토마토 소스");
        sauces.add("데리야끼 소스");
        sauces.add("마요네즈");
        sauces.add("케첩");



        oils.add("트러플 오일");
        oils.add("올리브");
        oils.add("들기름");
        oils.add("참기름");
        oils.add("포도씨유");
        oils.add("식용유");
        oils.add("팜유");
        oils.add("돼지기름");
        oils.add("쇼트닝");
        oils.add("땅콩기름");
        oils.add("버터");
        oils.add("마가린");
        oils.add("고추기름");
        oils.add("카놀라유");
        oils.add("옥수수기름");


        i.setMeats(meats);
        i.setGrains(grains);
        i.setBegetables(begetables);
        i.setFruits(fruits);
        i.setSeafoods(seafoods);
        i.setEggs(eggs);
        i.setNuts(nuts);
        i.setNoodles(noodles);
        i.setSauces(sauces);
        i.setOils(oils);


        myRef.child("ingredients").setValue(i);

*/
/*
        myRef.child("ingredients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i = dataSnapshot.getValue(Ingredients.class);
                if(i.getBegetables().contains("배추")){
                    Log.d(TAG, "Yes");
                }
                else {
                    Log.d(TAG, "NO");
                }
                Log.d(TAG, ""+i.begetables.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

 */











        Button myPageBtn = findViewById(R.id.myPageBtn);
        Button youtubeTestBtn = (Button)findViewById(R.id.youtubeTestBtn);
        Button dbTestBtn = (Button)findViewById(R.id.dbTestBtn);

        myPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });

        youtubeTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateRecipeActivity.class);
                startActivity(intent);
            }
        });

        dbTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onBackButtonClicked(View v){
        finish();
    }

}


