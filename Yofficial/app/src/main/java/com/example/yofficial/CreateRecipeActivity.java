package com.example.yofficial;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class CreateRecipeActivity extends AppCompatActivity {

    private static final String TAG = "Create!";
    private static Toast sToast;

    private static final int REQUEST_CODE = 0;
    private ImageView getImage;
    private int timeIdCount = 0; // 재료 추가 테이블 count
    private int ssnIdCount = 0;  // 양념 추가 테이블 count
    private int stageIdCount = 0; // 태깅 단계 추가 테이블 count
    private Bitmap img;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Activity ac = this;
    private FirebaseAuth mAuth;
    private int ingSendNumber = 1112;
    private int ssnSendNumber = 2112;
    private int curChefExp;
    private int curChefLevel;
    boolean tp, sp;
    Activity activity = this;

    //재료 테이블 받아오기
    TableRow tr[] = new TableRow[100];
    EditText ingName[] = new EditText[100];
    EditText ingFigures[] = new EditText[100];

    //양념 테이블 받아오기
    TableRow tr1[] = new TableRow[100];
    EditText ssnName[] = new EditText[100];
    EditText ssnFigures[] = new EditText[100];

    //시간 태깅 단계 받아오기
    TableRow tr2[][] = new TableRow[100][2];
    EditText stageDescr[] = new EditText[100];
    TextView startTime[] = new TextView[100];
    TextView endTime[] = new TextView[100];
    EditText stageEdit[][] = new EditText[100][6];
    TextView separator[][] = new TextView[100][4];

    //재료 테이블 입력 ArrayList 이름과 양
    ArrayList<String> ingredientName = new ArrayList<String>();
    ArrayList<String> ingredientAmount = new ArrayList<String>();

    //양념 테이블 입력 ArrayList 이름과 양
    ArrayList<String> seasoningName = new ArrayList<String>();
    ArrayList<String> seasoningAmount = new ArrayList<String>();

    // 영상 url 및 시간 태깅 a받는 arrayList
    ArrayList<String> stepDescrib = new ArrayList<String>();
    ArrayList<String> startTimeList = new ArrayList<String>();
    ArrayList<String> endTimeList = new ArrayList<String>();

    EditText ing1;
    EditText ssn1;
    EditText vol1;
    EditText volume1;

    ImageView user_img;
    String imagePath;
    Uri imgUri;
    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }



        final EditText titleEdit = findViewById(R.id.titleEdit);
        final EditText subTitleEdit = findViewById(R.id.subTitleEdit);
        final EditText introEdit = findViewById(R.id.introEdit);
        ing1 = findViewById(R.id.ing1);
        volume1 = findViewById(R.id.volume1);
        ssn1 = findViewById(R.id.ssn1);
        vol1 = findViewById(R.id.vol1);
        final EditText youtubeUrl = findViewById(R.id.yUrl);
        final EditText stepDescrib1 = findViewById(R.id.stepDescrib1);
        final EditText s_hour1 = findViewById(R.id.s_hour1);
        final EditText s_minute1 = findViewById(R.id.s_minute1);
        final EditText s_second1 = findViewById(R.id.s_second1);
        final EditText e_hour1 = findViewById(R.id.e_hour1);
        final EditText e_minute1 = findViewById(R.id.e_minute1);
        final EditText e_second1 = findViewById(R.id.e_second1);



        final TableLayout ingTable = findViewById(R.id.ingTable);
        final TableLayout ssnTable = findViewById(R.id.ssnTable);
        final TableLayout stageTable = findViewById(R.id.stageTable);
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams stage_params = new TableRow.LayoutParams(0, 100);
        TableRow.LayoutParams stage_last_params = new TableRow.LayoutParams(0, 100);
        params.weight = 1f;
        stage_params.weight= 2f;
        stage_last_params.weight = 2f;
        stage_last_params.rightMargin = 40;


        //재료 테이블 배열 초기화
        for (int i = 0; i < ingName.length; i++) {
            ingName[i] = new EditText(this);
            ingName[i].setLayoutParams(params);
            ingName[i].setBackground(null);
            ingName[i].setHint("터치로 재료입력");
            ingName[i].setHintTextColor(Color.parseColor("#4075757E"));
            ingName[i].setTextSize(15);
            ingName[i].setFocusable(false);

        }

        for (int i = 0; i < ingFigures.length; i++) {
            ingFigures[i] = new EditText(this);
            ingFigures[i].setLayoutParams(params);
            ingFigures[i].setBackground(null);
            ingFigures[i].setHint("예시) 200g");
            ingFigures[i].setHintTextColor(Color.parseColor("#4075757E"));
            ingFigures[i].setTextSize(15);
        }

        for (int i = 0; i < tr.length; i++) {
            tr[i] = new TableRow(this);
        }

        //양념 테이블 배열 초기화
        for (int i = 0; i < ssnName.length; i++) {
            ssnName[i] = new EditText(this);
            ssnName[i].setLayoutParams(params);
            ssnName[i].setBackground(null);
            ssnName[i].setHint("터치로 양념입력");
            ssnName[i].setHintTextColor(Color.parseColor("#4075757E"));
            ssnName[i].setTextSize(15);
            ssnName[i].setFocusable(false);
        }

        for (int i = 0; i < ssnFigures.length; i++) {
            ssnFigures[i] = new EditText(this);
            ssnFigures[i].setLayoutParams(params);
            ssnFigures[i].setBackground(null);
            ssnFigures[i].setHint("예시) 2T");
            ssnFigures[i].setHintTextColor(Color.parseColor("#4075757E"));
            ssnFigures[i].setTextSize(15);
        }


        for (int i = 0; i < tr1.length; i++) {
            tr1[i] = new TableRow(this);
        }

        // 시간 태깅 단계 설명 배열 초기화
        for (int i = 0; i < stageDescr.length; i++) {
            stageDescr[i] = new EditText(this);
            stageDescr[i].setLayoutParams(params);
            stageDescr[i].setPadding(0,50,0,0);
            stageDescr[i].setBackground(null);
            stageDescr[i].setHintTextColor(Color.parseColor("#4075757E"));
            stageDescr[i].setTextSize(15);

            int int_temp = i + 2;
            String string_temp = Integer.toString(int_temp);
            string_temp += "단계 영상설명 ex) 물을 끊입니다.";
            stageDescr[i].setHint(string_temp);
        }


        for (int i = 0; i < startTime.length; i++) {
            startTime[i] = new TextView(this);
            TableRow.LayoutParams startParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            startParams.weight = 1;
            startParams.leftMargin = 20;
            startTime[i].setLayoutParams(startParams);
            startTime[i].setText("시작 시간");
            startTime[i].setTextColor(Color.parseColor("#000000"));
        }

        for (int i = 0; i < endTime.length; i++) {
            endTime[i] = new TextView(this);
            TableRow.LayoutParams endParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            endParams.leftMargin = 20;
            endParams.weight = 1;
            endTime[i].setLayoutParams(endParams);
            //endTime[i].setPadding(20,0,0,0);
            endTime[i].setText("종료 시간");
            endTime[i].setTextColor(Color.parseColor("#000000"));
        }


        for (int i = 0; i < stageEdit.length; i++) {
            for (int j = 0; j < stageEdit[i].length; j++) {
                stageEdit[i][j] = new EditText(this);
                stageEdit[i][j].setLayoutParams(stage_params);
                if (j == 5 || j == 2) {
                    stageEdit[i][j].setLayoutParams(stage_last_params);
                }
                stageEdit[i][j].setBackgroundResource(R.drawable.edittext_teduri);
                stageEdit[i][j].setPadding(20,0,0,0);
                stageEdit[i][j].setTextSize(15);
                stageEdit[i][j].setGravity(Gravity.RIGHT);
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(2);
                stageEdit[i][j].setFilters(FilterArray);
                stageEdit[i][j].setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }

        for (int i = 0; i < separator.length; i++) {
            for (int j = 0; j < separator[i].length; j++) {
                separator[i][j] = new TextView(this);
                TableRow.LayoutParams separatorParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                separatorParams.weight = 1;
                separator[i][j].setLayoutParams(separatorParams);
                separator[i][j].setGravity(Gravity.CENTER);
                separator[i][j].setText(":");
                separator[i][j].setTextSize(15);
            }
        }



        for (int i = 0; i < tr2.length; i++) {
            for (int j = 0; j < tr2[i].length; j++) {
                tr2[i][j] = new TableRow(this);
                tr2[i][j].setLayoutParams(params);
                tr2[i][j].setPadding(0, 40, 0, 0);
            }
        }

        //재료 테이블 추가 삭제
        Button insertIngBtn = findViewById(R.id.insertIng);
        insertIngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag;
                tr[timeIdCount].addView(ingName[timeIdCount]);
                tr[timeIdCount].addView(ingFigures[timeIdCount]);
                ingTable.addView(tr[timeIdCount]);
                timeIdCount++;
            }
        });
        Button removeIngBtn = findViewById(R.id.removeIng);
        removeIngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeIdCount > 0) {
                    ingTable.removeView(tr[timeIdCount - 1]);
                    tr[timeIdCount - 1].removeView(ingName[timeIdCount - 1]);
                    tr[timeIdCount - 1].removeView(ingFigures[timeIdCount - 1]);
                    timeIdCount--;
                }
            }
        });

        //양념 테이블 추가 삭제
        Button insertSsnBtn = findViewById(R.id.insertSsn);
        insertSsnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tr1[ssnIdCount].addView(ssnName[ssnIdCount]);
                tr1[ssnIdCount].addView(ssnFigures[ssnIdCount]);
                ssnTable.addView(tr1[ssnIdCount]);
                ssnIdCount++;
            }
        });
        Button removeSsnBtn = findViewById(R.id.removeSsn);
        removeSsnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ssnIdCount > 0) {
                    ssnTable.removeView(tr1[ssnIdCount - 1]);
                    tr1[ssnIdCount - 1].removeView(ssnName[ssnIdCount - 1]);
                    tr1[ssnIdCount - 1].removeView(ssnFigures[ssnIdCount - 1]);
                    ssnIdCount--;
                }
            }
        });

        //시간 태깅 입력 추가

        Button insertStage = (Button)findViewById(R.id.insertStage);
        insertStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stageTable.addView(stageDescr[stageIdCount]);
                tr2[stageIdCount][0].addView(startTime[stageIdCount]);
                tr2[stageIdCount][0].addView(stageEdit[stageIdCount][0]);

                tr2[stageIdCount][0].addView(separator[stageIdCount][0]); // 구분자

                tr2[stageIdCount][0].addView(stageEdit[stageIdCount][1]);

                tr2[stageIdCount][0].addView(separator[stageIdCount][1]); // 구분자

                tr2[stageIdCount][0].addView(stageEdit[stageIdCount][2]);

                tr2[stageIdCount][1].addView(endTime[stageIdCount]);
                tr2[stageIdCount][1].addView(stageEdit[stageIdCount][3]);

                tr2[stageIdCount][1].addView(separator[stageIdCount][2]); // 구분자

                tr2[stageIdCount][1].addView(stageEdit[stageIdCount][4]);

                tr2[stageIdCount][1].addView(separator[stageIdCount][3]); // 구분자

                tr2[stageIdCount][1].addView(stageEdit[stageIdCount][5]);
                stageTable.addView(tr2[stageIdCount][0]);
                stageTable.addView(tr2[stageIdCount][1]);
                stageIdCount++;
            }
        });

        Button removeStage = (Button)findViewById(R.id.removeStage);
        removeStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stageIdCount > 0) {
                    stageTable.removeView(tr2[stageIdCount - 1][1]);
                    stageTable.removeView(tr2[stageIdCount - 1][0]);
                    tr2[stageIdCount - 1][1].removeView(stageEdit[stageIdCount - 1][5]);
                    tr2[stageIdCount - 1][1].removeView(separator[stageIdCount - 1][3]); // 구분자
                    tr2[stageIdCount - 1][1].removeView(stageEdit[stageIdCount - 1][4]);
                    tr2[stageIdCount - 1][1].removeView(separator[stageIdCount - 1][2]); // 구분자
                    tr2[stageIdCount - 1][1].removeView(stageEdit[stageIdCount - 1][3]);
                    tr2[stageIdCount - 1][1].removeView(endTime[stageIdCount - 1]);
                    tr2[stageIdCount - 1][0].removeView(stageEdit[stageIdCount - 1][2]);
                    tr2[stageIdCount - 1][0].removeView(separator[stageIdCount - 1][1]); // 구분자
                    tr2[stageIdCount - 1][0].removeView(stageEdit[stageIdCount - 1][1]);
                    tr2[stageIdCount - 1][0].removeView(separator[stageIdCount - 1][0]); // 구분자
                    tr2[stageIdCount - 1][0].removeView(stageEdit[stageIdCount - 1][0]);
                    tr2[stageIdCount - 1][0].removeView(startTime[stageIdCount - 1]);
                    stageTable.removeView(stageDescr[stageIdCount - 1]);
                    stageIdCount--;
                }
            }
        });


        //사진 받아오기
        getImage = (ImageView)findViewById(R.id.user_image);

        getImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                Log.d(TAG, "Enter onclick");
                startActivityForResult(intent, 1);
            }
        });


        // 카테고리 스피너 선언부
        final Spinner mainIngSpinner = (Spinner)findViewById(R.id.main_ing);
        ArrayAdapter mainIngAdapter = ArrayAdapter.createFromResource(this, R.array.data_mainIng, android.R.layout.simple_spinner_item);
        mainIngAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainIngSpinner.setAdapter(mainIngAdapter);

        final Spinner typeSpinner = (Spinner)findViewById(R.id.type);
        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this, R.array.data_type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        final Spinner featureSpinner = (Spinner)findViewById(R.id.feature);
        ArrayAdapter featureAdapter = ArrayAdapter.createFromResource(this, R.array.data_feature, android.R.layout.simple_spinner_item);
        featureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        featureSpinner.setAdapter(featureAdapter);



        // 인분, 난이도, 소요시간 다중 선택 부
        final TextView servingSelB = (TextView) findViewById(R.id.servingSel);
        final TextView diffiSelB = (TextView) findViewById(R.id.diffiSel);
        final TextView duraSelB = (TextView) findViewById(R.id.duraSel);

        servingSelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] serNum = new String[]{"1인분","2인분","3인분","4인분","5인분","6인분이상"};
                final int[] selectedIndex = {0};

                AlertDialog.Builder dialog = new AlertDialog.Builder(CreateRecipeActivity.this);
                dialog.setTitle("몇 인분인지 선택해주세요.");
                dialog.setSingleChoiceItems(serNum, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex[0] = which;
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        servingSelB.setText(serNum[selectedIndex[0]]);
                    }
                }).create().show();
            }
        });

        diffiSelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] level = new String[]{"브론즈","실버","골드","다이아","마스터"};
                final int[] selectedIndex = {0};

                AlertDialog.Builder dialog = new AlertDialog.Builder(CreateRecipeActivity.this);
                dialog.setTitle("난이도를 선택해주세요.");
                dialog.setSingleChoiceItems(level, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex[0] = which;
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        diffiSelB.setText(level[selectedIndex[0]]);
                    }
                }).create().show();
            }
        });

        duraSelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] duraTime = new String[]{"5분","10분","15분","30분","1시간","2시간 이상"};
                final int[] selectedIndex = {0};

                AlertDialog.Builder dialog = new AlertDialog.Builder(CreateRecipeActivity.this);
                dialog.setTitle("요리에 걸리는 시간을 선택해주세요.");
                dialog.setSingleChoiceItems(duraTime, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex[0] = which;
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        duraSelB.setText(duraTime[selectedIndex[0]]);
                    }
                }).create().show();
            }
        });

        final RecipeInfo recipeInfo = new RecipeInfo();


        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() { // 레시피 정보 액티비티에 입력받은 값 전달
            @Override
            public void onClick(View v) {



                boolean titleFlag = false, subTitleFlag = false, introFlag = false;
                boolean imgFlag = false;
                boolean mainIngFlag = false, typeFlag = false, featureFlag = false;
                boolean servingFlag = false, difficultyFlag = false, durationFlag = false;
                boolean ingName1Flag = false, ingFigure1Flag = false;
                boolean ingNameFlag = false, ingFigureFlag = false;
                boolean ssnName1Flag = false, ssnFigure1Flag = false;
                boolean ssnNameFlag =false, ssnFigureFlag = false;
                boolean urlFlag = false;
                boolean stageDescr1Flag = false;
                boolean stageStartHour1Flag = false, stageStartMinute1Flag = false, stageStartSecond1Flag = false;
                boolean stageEndHour1Flag = false, stageEndMinute1Flag = false, stageEndSecond1Flag = false;
                boolean stageDescrFlag = true;
                boolean stageStartHourFlag = true, stageStartMinuteFlag = true, stageStartSecondFlag = true;
                boolean stageEndHourFlag = true, stageEndMinuteFlag = true, stageEndSecondFlag = true;
                boolean startOverEndFlag = true;

                String temp;

                temp = titleEdit.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "제목을 입력하세요");
                } else {
                    titleFlag = true;
                }

                temp = subTitleEdit.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "부제목을 입력하세요");
                } else {
                    subTitleFlag = true;
                }

                temp = introEdit.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "요리 소개를 입력하세요");
                } else {
                    introFlag = true;
                }

                temp = mainIngSpinner.getSelectedItem().toString();
                if (temp.equals("주재료")) {
                    showToast(getApplicationContext(), "주재료 항목을 입력하세요");
                } else {
                    mainIngFlag = true;
                }

                temp = typeSpinner.getSelectedItem().toString();
                if (temp.equals("타입")) {
                    showToast(getApplicationContext(), "타입 항목을 입력하세요");
                } else {
                    typeFlag = true;
                }

                temp = featureSpinner.getSelectedItem().toString();
                if (temp.equals("특징")) {
                    showToast(getApplicationContext(), "특징 항목을 입력하세요");
                } else {
                    featureFlag = true;
                }

                temp = servingSelB.getText().toString();
                if (temp.equals("선택")) {
                    showToast(getApplicationContext(), "인원 수를 선택하세요");
                } else {
                    servingFlag = true;
                }

                temp = diffiSelB.getText().toString();
                if (temp.equals("선택")) {
                    showToast(getApplicationContext(), "난이도를 선택하세요");
                } else {
                    difficultyFlag = true;
                }

                temp = duraSelB.getText().toString();
                if (temp.equals("선택")) {
                    showToast(getApplicationContext(), "소요 시간을 선택하세요");
                } else {
                    durationFlag = true;
                }

                temp = ing1.getText().toString();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "재료를 입력하세요");
                } else {
                    ingName1Flag = true;
                }

                for (int i = 0; i < timeIdCount; i++) {
                    temp = ingName[i].getText().toString();
                    boolean flag = true;
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), "재료를 입력하세요");
                        ingNameFlag = false;
                        flag = false;
                    }

                    if (i == timeIdCount - 1 && flag == true) {
                        ingNameFlag = true;
                    }
                }



                temp = volume1.getText().toString();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "재료 양을 입력하세요");
                } else {
                    ingFigure1Flag = true;
                }

                for (int i = 0; i < timeIdCount; i++) {
                    temp = ingFigures[i].getText().toString();
                    boolean flag = true;
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), "재료 양을 입력하세요");
                        ingFigureFlag = false;
                        flag = false;
                    }

                    if (i == timeIdCount - 1 && flag == true) {
                        ingFigureFlag = true;
                    }
                }

                temp = ssn1.getText().toString();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "앙념을 입력하세요");
                } else {
                    ssnName1Flag = true;
                }

                for (int i = 0; i < ssnIdCount; i++) {
                    temp = ssnName[i].getText().toString();
                    boolean flag = true;
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), "양념을 입력하세요");
                        ssnNameFlag = false;
                        flag = false;
                    }

                    if (i == timeIdCount - 1 && flag == true) {
                        ssnNameFlag = true;
                    }
                }

                temp = vol1.getText().toString();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "앙념 양을 입력하세요");
                } else {
                    ssnFigure1Flag = true;
                }

                for (int i = 0; i < ssnIdCount; i++) {
                    temp = ssnFigures[i].getText().toString();
                    boolean flag = true;
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), "양념 양을 입력하세요");
                        ssnFigureFlag = false;
                        flag = false;
                    }

                    if (i == timeIdCount - 1 && flag == true) {
                        ssnFigureFlag = true;
                    }
                }

                temp = youtubeUrl.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "URL을 입력하세요");
                } else {
                    urlFlag = true;
                }

                temp = stepDescrib1.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "1단계 영상 설명을 입력하세요");
                } else {
                    stageDescr1Flag = true;
                }

                temp = s_hour1.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "1단계 시작 시간을 입력하세요");
                } else if (Integer.parseInt(temp) >= 60) {
                    showToast(getApplicationContext(), "1단계 : 시간 값은 59를 넘을 수 없습니다");
                } else {
                    stageStartHour1Flag = true;
                }

                temp = s_minute1.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "1단계 시작 시간을 입력하세요");
                } else if (Integer.parseInt(temp) >= 60) {
                    showToast(getApplicationContext(), "1단계 : 시간 값은 59를 넘을 수 없습니다");
                }
                else {
                    stageStartMinute1Flag = true;
                }

                temp = s_second1.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "1단계 시작 시간을 입력하세요");
                } else if (Integer.parseInt(temp) >= 60) {
                    showToast(getApplicationContext(), "1단계 : 시간 값은 59를 넘을 수 없습니다");
                }  else {
                    stageStartSecond1Flag = true;
                }

                temp = e_hour1.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "1단계 종료 시간을 입력하세요");
                } else if (Integer.parseInt(temp) >= 60) {
                    showToast(getApplicationContext(), "1단계 : 시간 값은 59를 넘을 수 없습니다");
                }
                else {
                    stageEndHour1Flag = true;
                }

                temp = e_minute1.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "1단계 종료 시간을 입력하세요");
                } else if (Integer.parseInt(temp) >= 60) {
                    showToast(getApplicationContext(), "1단계 : 시간 값은 59를 넘을 수 없습니다");
                }
                else {
                    stageEndMinute1Flag = true;
                }

                temp = e_second1.getText().toString();
                temp = temp.trim();
                if (temp.getBytes().length <= 0) {
                    showToast(getApplicationContext(), "1단계 종료 시간을 입력하세요");
                } else if (Integer.parseInt(temp) >= 60) {
                    showToast(getApplicationContext(), "1단계 : 시간 값은 59를 넘을 수 없습니다");
                }
                else {
                    stageEndSecond1Flag = true;
                }


                tp = stageStartHour1Flag && stageStartMinute1Flag && stageStartSecond1Flag && stageEndHour1Flag && stageEndMinute1Flag && stageEndSecond1Flag;
                if (tp) {
                    int integer1, integer2;

                    integer1 = (Integer.parseInt(s_hour1.getText().toString()) * 3600) + (Integer.parseInt(s_minute1.getText().toString()) * 60)
                            + Integer.parseInt(s_second1.getText().toString());

                    integer2 = (Integer.parseInt(e_hour1.getText().toString()) * 3600) + (Integer.parseInt(e_minute1.getText().toString()) * 60)
                            + Integer.parseInt(e_second1.getText().toString());

                    if (integer2 <= integer1) {
                        showToast(getApplicationContext(), "1단계 종료 시간이 1단계 시작시간 보다 크거나 같습니다.");
                        stageStartHour1Flag = false;
                    }
                }

                for (int i = 0; i < stageIdCount; i++) {

                    boolean descrFlag = true;
                    boolean sHourFlag = true;
                    boolean sMinuteFlag = true;
                    boolean sSecondFlag = true;
                    boolean eHourFlag = true;
                    boolean eMinuteFlag = true;
                    boolean eSecondFlag = true;

                    temp = stageDescr[i].getText().toString();
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), (i+2) + "단계 영상 설명을 입력하세요");
                        descrFlag = false;
                    }

                    if (descrFlag && stageDescrFlag) {
                        stageDescrFlag = true;
                    } else {
                        stageDescrFlag = false;
                    }

                    temp = stageEdit[i][0].getText().toString();
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), (i+2) + "단계 시작 시간을 입력하세요");
                        sHourFlag= false;
                    } else if (Integer.parseInt(temp) >= 60) {
                        showToast(getApplicationContext(), (i + 2) + "단계 : 시간 값은 59를 넘을 수 없습니다");
                        sHourFlag= false;
                    }

                    if (stageStartHourFlag && sHourFlag) {
                        stageStartHourFlag = true;
                    } else {
                        stageStartHourFlag = false;
                    }

                    temp = stageEdit[i][1].getText().toString();
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), (i+2) + "단계 시작 시간을 입력하세요");
                        sMinuteFlag= false;
                    } else if (Integer.parseInt(temp) >= 60) {
                        showToast(getApplicationContext(), (i + 2) + "단계 : 시간 값은 59를 넘을 수 없습니다");
                        sMinuteFlag= false;
                    }

                    if (stageStartMinuteFlag && sMinuteFlag) {
                        stageStartMinuteFlag = true;
                    } else {
                        stageStartMinuteFlag = false;
                    }

                    temp = stageEdit[i][2].getText().toString();
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), (i+2) + "단계 시작 시간을 입력하세요");
                        sSecondFlag= false;
                    } else if (Integer.parseInt(temp) >= 60) {
                        showToast(getApplicationContext(), (i + 2) + "단계 : 시간 값은 59를 넘을 수 없습니다");
                        sSecondFlag= false;
                    }

                    if (stageStartSecondFlag && sSecondFlag) {
                        stageStartSecondFlag = true;
                    } else {
                        stageStartSecondFlag = false;
                    }

                    temp = stageEdit[i][3].getText().toString();
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), (i+2) + "단계 종료 시간을 입력하세요");
                        eHourFlag= false;
                    } else if (Integer.parseInt(temp) >= 60) {
                        showToast(getApplicationContext(), (i + 2) + "단계 : 시간 값은 59를 넘을 수 없습니다");
                        eHourFlag= false;
                    }

                    if (stageEndHourFlag && eHourFlag == true) {
                        stageEndHourFlag = true;
                    } else {
                        stageEndHourFlag = false;
                    }

                    temp = stageEdit[i][4].getText().toString();
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), (i+2) + "단계 종료 시간을 입력하세요");
                        eMinuteFlag= false;
                    } else if (Integer.parseInt(temp) >= 60) {
                        showToast(getApplicationContext(), (i + 2) + "단계 : 시간 값은 59를 넘을 수 없습니다");
                        eMinuteFlag= false;
                    }

                    if (stageEndMinuteFlag && eMinuteFlag == true) {
                        stageEndMinuteFlag = true;
                    } else {
                        stageEndMinuteFlag = false;
                    }

                    temp = stageEdit[i][5].getText().toString();
                    if (temp.getBytes().length <= 0) {
                        showToast(getApplicationContext(), (i+2) + "단계 종료 시간을 입력하세요");
                        eSecondFlag= false;
                    } else if (Integer.parseInt(temp) >= 60) {
                        showToast(getApplicationContext(), (i + 2) + "단계 : 시간 값은 59를 넘을 수 없습니다");
                        eSecondFlag= false;
                    }

                    if (stageEndSecondFlag && eSecondFlag == true) {
                        stageEndSecondFlag = true;
                    } else {
                        stageEndSecondFlag = false;
                    }

                    tp = sHourFlag && sMinuteFlag && sSecondFlag && eHourFlag && eMinuteFlag && eSecondFlag;
                    if (tp) {
                        int integer1, integer2;

                        integer1 = (Integer.parseInt(stageEdit[i][0].getText().toString()) * 3600) + (Integer.parseInt(stageEdit[i][1].getText().toString()) * 60)
                                + Integer.parseInt(stageEdit[i][2].getText().toString());

                        integer2 = (Integer.parseInt(stageEdit[i][3].getText().toString()) * 3600) + (Integer.parseInt(stageEdit[i][4].getText().toString()) * 60)
                                + Integer.parseInt(stageEdit[i][5].getText().toString());

                        if (integer2 <= integer1) {
                            showToast(getApplicationContext(), (i + 2) + "단계 종료 시간이 " + (i + 2) + "단계 시작시간 보다 크거나 같습니다.");
                            startOverEndFlag = false;
                        }
                    }
                }


                tp = titleFlag && subTitleFlag && introFlag && mainIngFlag && typeFlag && featureFlag && servingFlag && difficultyFlag && durationFlag
                        && ingName1Flag && ingFigure1Flag && ssnName1Flag && ssnFigure1Flag
                        && urlFlag && stageDescr1Flag && stageStartHour1Flag && stageStartMinute1Flag && stageStartSecond1Flag && stageEndHour1Flag
                        && stageEndMinute1Flag && stageEndSecond1Flag;

                  if (timeIdCount > 0) {
                      tp = tp && ingNameFlag && ingFigureFlag;
                  }

                  if (ssnIdCount > 0) {
                      tp = tp && ssnNameFlag && ssnFigureFlag;
                  }

                  if (stageIdCount > 0) {
                      tp = tp  && stageDescrFlag && stageStartHourFlag && stageStartMinuteFlag && stageStartSecondFlag
                              && stageEndHourFlag && stageEndMinuteFlag && stageEndSecondFlag && startOverEndFlag;
                  }

                System.out.println("titleFlag : " + titleFlag);
                System.out.println("subTitleFlag : " + subTitleFlag);
                System.out.println("intro : " + introFlag);
                System.out.println("mainIng : " + mainIngFlag);
                System.out.println("type : " + typeFlag);
                System.out.println("feature : " + featureFlag);
                System.out.println("serving : " + servingFlag);
                System.out.println("difficulty : " + difficultyFlag);
                System.out.println("duration : " + durationFlag);
                System.out.println("ingName1 : " + ingName1Flag);
                System.out.println("ingName : " + ingNameFlag);
                System.out.println("ingFigure1 : " + ingFigure1Flag);
                System.out.println("ingFigure : " + ingFigureFlag);
                System.out.println("ssnName1 : " + ssnName1Flag);
                System.out.println("ssnName : " + ssnNameFlag);
                System.out.println("ssnFigure1 : " + ssnFigure1Flag);
                System.out.println("ssnFigure : " + ssnFigureFlag);
                System.out.println("url : " + urlFlag);
                System.out.println("stDescr1 : " + stageDescr1Flag);
                System.out.println("stStH1 : " + stageStartHour1Flag);
                System.out.println("stStM1 : " + stageStartMinute1Flag);
                System.out.println("stStS1 : " + stageStartSecond1Flag);
                System.out.println("stEdH1 : " + stageEndHour1Flag);
                System.out.println("stEdM1 : " + stageEndMinute1Flag);
                System.out.println("stEdS1 : " + stageEndSecond1Flag);
                System.out.println("stDescr : " + stageDescrFlag);
                System.out.println("stStH : " + stageStartHourFlag);
                System.out.println("stStM :" + stageStartMinuteFlag);
                System.out.println("stStS : " + stageStartSecondFlag);
                System.out.println("stEdH : " + stageEndHourFlag);
                System.out.println("stEdM : " + stageEndMinuteFlag);
                System.out.println("stEdS : " + stageEndSecondFlag);


                android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(activity);
                // 다이얼로그 메세지
                alertdialog.setMessage("\n  레시피를 등록하시겠습니까?");


                // 확인버튼
                alertdialog.setNegativeButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp = true;

                        if (tp && sp) {


                            recipeInfo.setRecipeTitle(titleEdit.getText().toString());
                            recipeInfo.setRecipeSubTitle(subTitleEdit.getText().toString()); // 부제목 전달
                            recipeInfo.setIntroRecipe(introEdit.getText().toString()); //요리 설명 전달
                            recipeInfo.setMainIngredient(mainIngSpinner.getSelectedItem().toString()); // 카테고리 중 주재료 부분 전달
                            recipeInfo.setType(typeSpinner.getSelectedItem().toString()); // 카테고리 중 타입 부분 전달
                            recipeInfo.setFeature(featureSpinner.getSelectedItem().toString()); // 카테고리 중 특징 부분 전달
                            recipeInfo.setServings(servingSelB.getText().toString()); // 인분 수 전달
                            recipeInfo.setDifficulty(diffiSelB.getText().toString()); // 난이도 전달
                            recipeInfo.setDuraTime(duraSelB.getText().toString()); // 소요시간 전달


                            // 재료 입력 전달 부분
                            ingredientName.add(ing1.getText().toString());  // xml의 view를 통해 입력 받은 값 arraylist에 입력, 재료 이름 부분
                            ingredientAmount.add(volume1.getText().toString()); // xml의 view를 통해 입력 받은 값 arraylist에 입력, 재료의 양 부분


                            // 추가 버튼으로 만들어진 동적 테이블 행에 들어간 값 arraylist에 입력
                            for (int i = 0; i < timeIdCount; i++) {
                                EditText tempName = (EditText)tr[i].getChildAt(0);
                                ingredientName.add(tempName.getText().toString());

                                EditText tempAmount = (EditText)tr[i].getChildAt(1);
                                ingredientAmount.add(tempAmount.getText().toString());
                            }

                            recipeInfo.setIngredientName(ingredientName); // 들어가는 재료 이름 전달
                            recipeInfo.setIngredientAmount(ingredientAmount); // 들어가는 재료의 양 전달

                            // 양념 입력 전달 부분
                            seasoningName.add(ssn1.getText().toString());  // xml의 view를 통해 입력 받은 값 arraylist에 입력, 양념 이름 부분
                            seasoningAmount.add(vol1.getText().toString()); // xml의 view를 통해 입력 받은 값 arraylist에 입력, 양념의 양 부분

                            // 추가 버튼으로 만들어진 동적 테이블 행에 들어간 값 arraylist에 입력
                            for (int i = 0; i < ssnIdCount; i++) {
                                EditText tempName = (EditText)tr1[i].getChildAt(0);
                                seasoningName.add(tempName.getText().toString());

                                EditText tempAmount = (EditText)tr1[i].getChildAt(1);
                                seasoningAmount.add(tempAmount.getText().toString());
                            }


                            recipeInfo.setSeasoningName(seasoningName); // 들어가는 양념 이름 전달
                            recipeInfo.setSeasoningAmount(seasoningAmount); // 들어가는 양념의 양 전달

                            recipeInfo.setYoutubeUrl(youtubeUrl.getText().toString()); // 실행할 영상 url 전달

                            // 단계별 설명 및 시간 태깅 전달 부분
                            stepDescrib.add(stepDescrib1.getText().toString());


                            int stTime;
                            int edTime;

                            int h, m, s;

                            if(s_hour1.getText().toString().compareTo("") == 0){
                                h = 0;
                            }
                            else{
                                h = Integer.parseInt(s_hour1.getText().toString());
                            }

                            if(s_minute1.getText().toString().compareTo("") == 0){
                                m = 0;
                            }
                            else{
                                m = Integer.parseInt(s_minute1.getText().toString());
                            }

                            if(s_second1.getText().toString().compareTo("") == 0){
                                s = 0;
                            }
                            else{
                                s = Integer.parseInt(s_second1.getText().toString());
                            }

                            stTime = 3600 * h + 60 * m + s;

                            if(e_hour1.getText().toString().compareTo("") == 0){
                                h = 0;
                            }
                            else{
                                h = Integer.parseInt(e_hour1.getText().toString());
                            }

                            if(e_minute1.getText().toString().compareTo("") == 0){
                                m = 0;
                            }
                            else{
                                m = Integer.parseInt(e_minute1.getText().toString());
                            }

                            if(e_second1.getText().toString().compareTo("") == 0){
                                s = 0;
                            }
                            else{
                                s = Integer.parseInt(e_second1.getText().toString());
                            }

                            edTime = 3600 * h + 60 * m + s;

                            Log.d(TAG, ""+stTime+" " + edTime);

                            startTimeList.add(Integer.toString(stTime));
                            endTimeList.add(Integer.toString(edTime));



                            for (int i = 0; i < stageIdCount; i++) {
                                EditText tempStepDescrib = (EditText) stageTable.getChildAt(3 * (i + 1));
                                stepDescrib.add(tempStepDescrib.getText().toString());



                                EditText tempStartHour = (EditText) tr2[i][0].getChildAt(1);
                                EditText tempStartMinute = (EditText) tr2[i][0].getChildAt(3);
                                EditText tempStartSecond = (EditText) tr2[i][0].getChildAt(5);


                                if(tempStartHour.getText().toString().compareTo("") == 0){
                                    h = 0;
                                }
                                else{
                                    h = Integer.parseInt(tempStartHour.getText().toString());
                                }

                                if(tempStartMinute.getText().toString().compareTo("") == 0){
                                    m = 0;
                                }
                                else{
                                    m = Integer.parseInt(tempStartMinute.getText().toString());
                                }

                                if(tempStartSecond.getText().toString().compareTo("") == 0){
                                    s = 0;
                                }
                                else{
                                    s = Integer.parseInt(tempStartSecond.getText().toString());
                                }

                                stTime = 3600 * h + 60 * m + s;

                                startTimeList.add(Integer.toString(stTime));


                                EditText tempEndHour = (EditText) tr2[i][1].getChildAt(1);
                                EditText tempEndMinute = (EditText) tr2[i][1].getChildAt(3);
                                EditText tempEndSecond = (EditText) tr2[i][1].getChildAt(5);

                                if(tempEndHour.getText().toString().compareTo("") == 0){
                                    h = 0;
                                }
                                else{
                                    h = Integer.parseInt(tempEndHour.getText().toString());
                                }

                                if(tempEndMinute.getText().toString().compareTo("") == 0){
                                    m = 0;
                                }
                                else{
                                    m = Integer.parseInt(tempEndMinute.getText().toString());
                                }

                                if(tempEndSecond.getText().toString().compareTo("") == 0){
                                    s = 0;
                                }
                                else{
                                    s = Integer.parseInt(tempEndSecond.getText().toString());
                                }

                                edTime = 3600 * h + 60 * m + s;

                                endTimeList.add(Integer.toString(edTime));

                            }

                            for (int i = 0; i < stepDescrib.size(); i++) {
                                System.out.println(stepDescrib.get(i));
                                System.out.println(startTimeList.get(i));
                                System.out.println(endTimeList.get(i));
                            }

                            recipeInfo.setStepDescrib(stepDescrib); //단계별 설명 전달
                            recipeInfo.setStartTime(startTimeList); // 단계별 시작 시간 전달
                            recipeInfo.setEndTime(endTimeList); // 단계별 죵료 시간 전달



                            Log.d(TAG, recipeInfo.getYoutubeUrl());

                            if(recipeInfo.getYoutubeUrl().contains("v=")){
                                String[] url = recipeInfo.getYoutubeUrl().split("v=");
                                if(url.length >= 2){
                                    recipeInfo.setYoutubeUrl(url[1]);
                                }

                            }




                            database = FirebaseDatabase.getInstance();
                            myRef = database.getReference();

                            String recipeId = myRef.child("recipes").push().getKey();

                            Log.d(TAG, recipeId);
                            recipeInfo.setRecipeId(recipeId);
                            myRef.child("recipes").child(recipeId).setValue(recipeInfo);
                            Log.d(TAG, "Recipe Create Succeeded");


                            PostInfo postInfo = new PostInfo();
                            postInfo.setRecipeId(recipeId);
                            Log.d(TAG, "set recipe id");
                            postInfo.setTitle(recipeInfo.getRecipeTitle());
                            Log.d(TAG, "set recipe title");
                            Log.d(TAG, postInfo.getTitle());

                            Log.d(TAG, "Before get auth");
                            mAuth = FirebaseAuth.getInstance();
                            Log.d(TAG, "after get auth");
                            String userId = mAuth.getCurrentUser().getEmail().split("@")[0];
                            Log.d(TAG, userId);

                            postInfo.setUserId(userId);
                            postInfo.setViews(0);


                            myRef.child("posts").child(recipeId).setValue(postInfo).addOnSuccessListener(ac, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ac.getApplicationContext(), "Succeeded", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "post uploaded");
                                    //get user info from db and update it
                                    myRef.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            UserInfo u = dataSnapshot.getValue(UserInfo.class);
                                            curChefLevel = u.getChefLevel();
                                            curChefExp = u.getChefExp();

                                            curChefExp += 20;
                                            if(curChefExp >= 100){
                                                curChefLevel++;
                                                curChefExp -= 100;
                                            }

                                            u.setChefLevel(curChefLevel);
                                            u.setChefExp(curChefExp);

                                            Log.d(TAG, u.getId());
                                            Log.d(TAG, u.toString());
                                            myRef.child("users").child(u.getId()).setValue(u);
                                            Log.d(TAG, "updated");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }
                            }).addOnFailureListener(ac, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ac.getApplicationContext(), "post Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                            storage = FirebaseStorage.getInstance();
                            storageRef = storage.getReference().child("images/"+ recipeId +".jpg");
                            Log.d(TAG, "get storage");
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            img = resize(c, imgUri, 500);
                            Bitmap rotatedBitmap = null;
                            try {
                                ExifInterface exif = new ExifInterface(imagePath);

                                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                                switch(exifOrientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap = rotateImage(img, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap = rotateImage(img, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap = rotateImage(img, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap = img;
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "Enter catch");
                                Log.d(TAG, e.getMessage());
                                Log.d(TAG, e.getStackTrace().toString());
                            }

                            rotatedBitmap = Bitmap.createScaledBitmap(rotatedBitmap, 200, 200, true);
                            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);


                            byte[] data = baos.toByteArray();

                            Log.d(TAG, data.toString());

                            UploadTask uploadTask = storageRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed");
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d(TAG, "Succeeded");
                                    ac.finish();
                                }
                            });
                        }
                    }
                });

                // 취소버튼
                alertdialog.setPositiveButton("취소", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp = false;
                    }
                });


                if (tp) {
                    // 메인 다이얼로그 생성
                    android.app.AlertDialog alert = alertdialog.create();
                    // 아이콘 설정
                    alert.setIcon(R.drawable.yofficial);
                    // 타이틀
                    alert.setTitle("알림!");
                    // 다이얼로그 보기
                    alert.show();

                }



            }
        });


        ing1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateRecipeActivity.this, IngreSearchActivity.class);
                startActivityForResult(intent, 1111); //1111로 코드 부여
            }
        });

        for (int i = 0; i < ingName.length; i++) {
            int temp = i;
            ingName[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CreateRecipeActivity.this, IngreSearchActivity.class);
                    startActivityForResult(intent, ingSendNumber+ temp); //1111로 코드 부여
                }
            });
        }

        ssn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateRecipeActivity.this, IngreSearchActivity.class);
                startActivityForResult(intent, 2111); //1111로 코드 부여
            }
        });

        for (int i = 0; i < ssnName.length; i++) {
            int temp = i;
            ssnName[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CreateRecipeActivity.this, IngreSearchActivity.class);
                    startActivityForResult(intent, ssnSendNumber+ temp); //1111로 코드 부여
                }
            });
        }


    }


    //사진 받아오기 관련
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // 재료 입력 리스트에서 받아오기
        for (int i = 0; i < 100; i++) {
            if (requestCode == 1112 + i) {
                if (resultCode == 1234) {
                    String temp = data.getStringExtra("result");

                    if (requestCode == ingSendNumber + i) {
                        ingName[i].setText(temp);
                    }
                }
            }
        }

        if (requestCode == 1111) {
            if (resultCode == 1234) {
                String temp = data.getStringExtra("result");

                if (requestCode == ingSendNumber - 1) {
                    ing1.setText(temp);
                    ing1.requestFocus();
                }
            }
        }

        // 양념 입력 리스트에서 받아오기
        for (int i = 0; i < 100; i++) {
            if (requestCode == 2112 + i) {
                if (resultCode == 1234) {
                    String temp = data.getStringExtra("result");

                    if (requestCode == ssnSendNumber + i) {
                        ssnName[i].setText(temp);
                    }
                }
            }
        }

        if (requestCode == 2111) {
            if (resultCode == 1234) {
                String temp = data.getStringExtra("result");

                if (requestCode == ssnSendNumber - 1) {
                    ssn1.setText(temp);
                }
            }
        }


        if(requestCode == 1) {
            Log.d(TAG, "Enter onActivity Result");
            if(resultCode == RESULT_OK)
            {
                Log.d(TAG, "RESULT OK");

                try{
                    imgUri = data.getData();
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(imgUri, filePath, null, null, null);
                    cursor.moveToFirst();
                    imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inSampleSize = 4;
                    img = BitmapFactory.decodeFile(imagePath,options);

                    ExifInterface exif = new ExifInterface(imagePath);
                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    Bitmap rotatedBitmap = null;
                    switch(exifOrientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(img, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(img, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(img, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = img;
                    }

                    getImage.setImageBitmap(rotatedBitmap);
                    cursor.close();

                    user_img = findViewById(R.id.user_image);
                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) user_img.getLayoutParams();
                    params.width = user_img.getWidth();
                    params.height = user_img.getHeight();
                    getImage.setLayoutParams(params);
                    getImage.setPadding(30,0,0,0);


                }catch(Exception e)
                {
                    Log.d(TAG, "Enter catch");
                    Log.d(TAG, e.getMessage());
                    Log.d(TAG, e.getStackTrace().toString());
                }
            }
            else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }

    }

    public static void showToast(Context context, String message) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    private Bitmap resize(Context context,Uri uri,int resize){
        Bitmap resizeBitmap=null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options); // 1번

            int width = options.outWidth;
            int height = options.outHeight;
            int samplesize = 1;

            while (true) {//2번
                if (width / 2 < resize || height / 2 < resize)
                    break;
                width /= 2;
                height /= 2;
                samplesize *= 2;
            }

            options.inSampleSize = samplesize;
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options); //3번
            resizeBitmap=bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resizeBitmap;
    }

}


