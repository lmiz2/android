package com.example.songhyeonseok.drawing;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ToggleButton;

import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.Surface;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PaintingRoom extends AppCompatActivity{
    private String idByANDROID_ID;

    final protected int CALL_THE_FRIENDFRAME = 10;
    final protected int CALL_THE_RECORDFRAME = 11;
    final protected int GET_PICTUER_AT_G_DRIVE = 32322;
    final int SET_PEN_MODE = 345;
    final int SET_ERASER_MODE = 456;
    final int REQ_CODE_SELECT_IMAGE=100;
    final int REQ_CODE_CAPTURE = 110;
    final protected int BORDER_SIZE_BIG = 21;
    final protected int BORDER_SIZE_MID = 22;
    final protected int BORDER_SIZE_SMALL = 23;
    private static final String TAG = "PaintingRoom";
    private static final int REQUEST_CODE = 1000;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private static final int DISPLAY_WIDTH = 720;
    private static final int DISPLAY_HEIGHT = 1280;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private MediaRecorder mMediaRecorder;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_PERMISSIONS = 10;
    File file;
    String cameraTempFilePath;
    Uri imageFileUri;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child("Rooms");
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private StorageReference mStorageRef;
    private GoogleApiClient dgoogleApiClient;
    float width_coeffi,height_coeffi;
    boolean noBody,isPlay_On;
    String MyUid,MyName;
    FrameLayout parent;
    LinearLayout tools,coverArea;
    Button tools_btn_cleanall,tools_btn_borderSize,tools_btn_chooseColor;
    ToggleButton tools_btn_eraser;
    View chattView;
    ListView chattDatas;
    Button chattSend;
    EditText chattInput;
    ArrayAdapter adapter;
    ImageView l1;
    MyView mv;
    Bitmap bitmap;
    String back_key="no1";

    Animation appear_to_top,disappear_to_left,disappear_to_right,from_left,from_right,disappear_to_bottom;

    private FloatingActionMenu fam;
    private FloatingActionButton fab_chatt,fabdrive, fabpicture, fabTools,fabrec_play,fabcapture;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paintingroom);
        //--------------------------------------------------------------------------------------------------
        idByANDROID_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mMediaRecorder = new MediaRecorder();
        mProjectionManager = (MediaProjectionManager) getSystemService
                (Context.MEDIA_PROJECTION_SERVICE);
        this.getFilesDir().delete();
        file = new File(this.getFilesDir(),"recorded_canvas");

        mStorageRef= FirebaseStorage.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        MyName = mFirebaseUser.getDisplayName();
        MyUid = mFirebaseUser.getUid();


        fabpicture= (FloatingActionButton) findViewById(R.id.fab_get_picture);
        fabdrive = (FloatingActionButton) findViewById(R.id.drive);
        fabTools = (FloatingActionButton) findViewById(R.id.fab_tools);
        fabrec_play = (FloatingActionButton) findViewById(R.id.rec_play);
        fabcapture = (FloatingActionButton) findViewById(R.id.capture);

        fam = (FloatingActionMenu) findViewById(R.id.fab_menu);
        fab_chatt = (FloatingActionButton) findViewById(R.id.fab_chatt);

        tools = (LinearLayout)findViewById(R.id.drawer_tools);
        tools_btn_borderSize = (Button)findViewById(R.id.border_size);
        tools_btn_chooseColor = (Button)findViewById(R.id.choose_color);
        tools_btn_cleanall = (Button)findViewById(R.id.clean_all);
        tools_btn_eraser = (ToggleButton)findViewById(R.id.eraserModeBtn);

        appear_to_top = AnimationUtils.loadAnimation(this,R.anim.totop);
        from_left = AnimationUtils.loadAnimation(this,R.anim.from_left);
        from_right = AnimationUtils.loadAnimation(this,R.anim.from_right);
        disappear_to_left = AnimationUtils.loadAnimation(this,R.anim.gone_to_left);
        disappear_to_right = AnimationUtils.loadAnimation(this,R.anim.gone_to_right);
        disappear_to_bottom = AnimationUtils.loadAnimation(this,R.anim.gone_to_bot);


        //handling menu status (open or close)
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {

                } else {

                }
            }
        });

        //handling each floating action button clicked
        fabpicture.setOnClickListener(onButtonClick());
        fabdrive.setOnClickListener(onButtonClick());
        fabTools.setOnClickListener(onButtonClick());
        fabcapture.setOnClickListener(onButtonClick());
        fabrec_play.setOnClickListener(onButtonClick());
        tools_btn_cleanall.setOnClickListener(onButtonClick());
        tools_btn_borderSize.setOnClickListener(onButtonClick());
        tools_btn_eraser.setOnClickListener(onButtonClick());
        tools_btn_chooseColor.setOnClickListener(onButtonClick());

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });
        fam.setClosedOnTouchOutside(true);

        fab_chatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chattView.getVisibility() == View.VISIBLE) {
                    chattWinDisAppear();
                }else{
                    chattWinAppear();
                }
            }
        });


        cameraTempFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp_DrawRoomBacks.jpg";
        File imageFile = new File(cameraTempFilePath);
        imageFileUri = Uri.fromFile(imageFile);


        WindowManager winM = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display display = winM.getDefaultDisplay();
        Point point = new Point();

        display.getSize(point);

        width_coeffi = (float) (point.x/720.0); //화면 계수(기본 가로화면크기 720, 이 기기의 화면크기를 기본화면크기로 나눈것)생성
        height_coeffi = (float) (point.y/1280.0);// 기본 세로화면크기 1280
        //--------------------------------------------------------------------------------------------------
        noBody = true;//테스트용 라인
        if(noBody){//칠판 참여자가 아무도 없을경우
            Intent intent = new Intent(getApplicationContext(),FriendFrame.class);
            startActivityForResult(intent,CALL_THE_FRIENDFRAME);//친구창 띄우기
        }
        //startActivityForResult(new Intent(getApplicationContext(),RecordQ.class),CALL_THE_RECORDFRAME);//녹화여부 묻기


        parent  = (FrameLayout)findViewById(R.id.back_canv);
        coverArea = (LinearLayout)findViewById(R.id.coverArea_paintroom);
        l1 = (ImageView) findViewById(R.id.canvas1);

        //BackgroundDrawingView robot1 = new BackgroundDrawingView(this,width_coeffi,height_coeffi); ----------------------스레드 임시 멈춤///////////////////////////
        mv = new MyView(this,width_coeffi,height_coeffi);
        parent.addView(mv);
        //parent.addView(robot1); ----------------------스레드 임시 멈춤///////////////////////////////////////////
        createChattWin();
        runListener();
        startPermissionReq();
        }//onCreate의 끝

    public void chattWinAppear(){
        chattDatas.smoothScrollByOffset(adapter.getCount());
        chattView.setVisibility(View.VISIBLE);
        chattView.startAnimation(appear_to_top);
        fabDisappear();
    }

    public void chattWinDisAppear(){
        chattView.startAnimation(disappear_to_bottom);
        chattView.setVisibility(View.GONE);
        fabAppear();
    }

    public void fabDisappear(){
        fab_chatt.startAnimation(disappear_to_left);
        fab_chatt.setVisibility(View.GONE);
        fam.startAnimation(disappear_to_right);
        fam.setVisibility(View.GONE);
    }

    public void fabAppear(){
        fab_chatt.startAnimation(from_left);
        fab_chatt.setVisibility(View.VISIBLE);
        fam.startAnimation(from_right);
        fam.setVisibility(View.VISIBLE);
    }

    public void toolsAppear(){
        tools.startAnimation(appear_to_top);
        tools.setVisibility(View.VISIBLE);
    }

    public void toolsDisappear(){
        tools.startAnimation(disappear_to_bottom);
        tools.setVisibility(View.GONE);
        fabAppear();
    }

    @Override
    public void onBackPressed() {
        if(chattView.getVisibility() == View.VISIBLE){
            chattWinDisAppear();
        }
        else if(tools.getVisibility() == View.VISIBLE){
            toolsDisappear();
        }
        else {
            super.onBackPressed();
        }
    }

    public void createChattWin(){
        chattView = getLayoutInflater().inflate(R.layout.chattwindow,null);
        chattDatas = (ListView)chattView.findViewById(R.id.chatt_datas);
        chattSend = (Button)chattView.findViewById(R.id.chatt_send);
        chattInput = (EditText)chattView.findViewById(R.id.chatt_input);
        parent.addView(chattView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chattDatas.setAdapter(adapter);


        chattSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chattInput.getText().toString().equals("COM_CLEAN")){
                    databaseReference.child("DrawRoom").child("Chatting").removeValue();
                    adapter.clear();
                    chattDatas.setAdapter(adapter);
                    chattInput.setText("");
                    return;
                }
                ChatData cd = new ChatData(MyName,chattInput.getText().toString());
                databaseReference.child("DrawRoom").child("Chatting").push().setValue(cd);
                chattInput.setText("");
            }
        });

        databaseReference.child("DrawRoom").child("Chatting").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData tmp = dataSnapshot.getValue(ChatData.class);
                adapter.add(tmp.getUserName()+ " : "+tmp.getMessage());
                chattDatas.smoothScrollByOffset(adapter.getCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });


        chattDatas.smoothScrollByOffset(adapter.getCount());
        chattView.setVisibility(View.GONE);

    }


    public void startPermissionReq(){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //Manifest.permission.READ_CALENDAR이 접근 승낙 상태 일때
        } else{
            //Manifest.permission.READ_CALENDAR이 접근 거절 상태 일때
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                //사용자가 다시 보지 않기에 체크를 하지 않고, 권한 설정을 거절한 이력이 있는 경우
            } else{
                //사용자가 다시 보지 않기에 체크하고, 권한 설정을 거절한 이력이 있는 경우
            }

            //사용자에게 접근권한 설정을 요구하는 다이얼로그를 띄운다.
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

        }else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)){
                //사용자가 다시 보지 않기에 체크를 하지 않고, 권한 설정을 거절한 이력이 있는 경우
            } else{
                //사용자가 다시 보지 않기에 체크하고, 권한 설정을 거절한 이력이 있는 경우
            }
            Toast.makeText(getApplicationContext(),"권한 거절됨",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_CODE);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        //위 예시에서 requestPermission 메서드를 썼을시 , 마지막 매개변수에 0을 넣어 줬으므로, 매칭

        switch (requestCode) {

            case REQUEST_PERMISSIONS: {
                if ((grantResult.length > 0) && (grantResult[0] +
                        grantResult[1]) == PackageManager.PERMISSION_GRANTED) {
                    onToggleScreenShare();
                } else {
                    playbtn_set_false();
                    Snackbar.make(findViewById(android.R.id.content), R.string.label_permissions,
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(intent);
                                }
                            }).show();
                }
                return;
            }


            case 0:{
                // requestPermission의 두번째 매개변수는 배열이므로 아이템이 여러개 있을 수 있기 때문에 결과를 배열로 받는다.
                // 해당 예시는 요청 퍼미션이 한개 이므로 i=0 만 호출한다.
                if(grantResult[0] == 0){
                    //해당 권한이 승낙된 경우.
                }else{
                    Toast.makeText(getApplicationContext(),"권한 거절로 인해 앱이 다운 될 수 있습니다.",Toast.LENGTH_SHORT).show();
                    //해당 권한이 거절된 경우.
                }
            }
            break;
        }
    }

    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            if (isPlay_On) {
                playbtn_set_false();
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                Log.v(TAG, "Recording Stopped");
            }
            mMediaProjection = null;
            stopScreenSharing();
        }
    }


    void playbtn_set_false(){
        isPlay_On = false;
        fabrec_play.setImageResource(R.drawable.play);
    }
    void playbtn_set_true(){
        isPlay_On = true;
        fabrec_play.setImageResource(R.drawable.pause);
    }

    void go_rec_play(View view){
        playbtn_set_true();
        Log.d("aa","asdjf@@@@@@@@@@@@@@@2");
        if (ContextCompat.checkSelfPermission(PaintingRoom.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(PaintingRoom.this,
                        Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (PaintingRoom.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (PaintingRoom.this, Manifest.permission.RECORD_AUDIO)) {
                playbtn_set_false();
                Snackbar.make(findViewById(android.R.id.content), R.string.label_permissions,
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(PaintingRoom.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                                        REQUEST_PERMISSIONS);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(PaintingRoom.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        REQUEST_PERMISSIONS);
            }
        } else {
            Log.d("aa","asdjf@@@@@@@@@@@@@@@3");
            onToggleScreenShare();
        }
    }


    void go_rec_pause(View view){
        if(null!=mMediaRecorder) {
            try {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                stopScreenSharing();
                playbtn_set_false();
            }catch (RuntimeException e){
                Toast.makeText(getApplicationContext(),"잠시후 다시 시도하십시오.",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void onToggleScreenShare() {
        if (isPlay_On) {
            initRecorder();
            shareScreen();
        } else {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            Log.v(TAG, "Stopping Recording");
            stopScreenSharing();
        }
    }

    private void shareScreen() {
        if (mMediaProjection == null) {
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
            return;
        }
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    private VirtualDisplay createVirtualDisplay() {
        return mMediaProjection.createVirtualDisplay("PaintingRoom",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null
                /*Handler*/);
    }

    private void initRecorder() {

        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setOutputFile(file.getPath());
            Log.d("pathis",file.getPath());
            mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
            mMediaRecorder.setVideoFrameRate(30);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATIONS.get(rotation + 90);
            mMediaRecorder.setOrientationHint(orientation);
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ex","ception");
        }
    }


    private void stopScreenSharing() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
        // be reused again
        destroyMediaProjection();
    }

    private void destroyMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;
            Log.i(TAG, "MediaProjection Stopped");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setMessage("녹화된 동영상을 보시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("보기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    Intent video_intent = new Intent(PaintingRoom.this, VideoViewACT.class);
                                    video_intent.putExtra("path", file.getPath());
                                    startActivity(video_intent);

                                }
                            })
                    .setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            uploadVideo(file);
        }



    }

    public void uploadVideo(File file){
        Log.d("video"," uploaded");
        final String vid_key = firebaseDatabase.getReference().child("pracArea").child("users").child("video").push().getKey();
        firebaseDatabase.getReference().child("pracArea").child("users").child("video").child(vid_key).setValue("");

        Uri filepath = Uri.fromFile(file);
        StorageReference riversRef = mStorageRef.child("videos/"+vid_key);
        UploadTask uploadTask = riversRef.putFile(filepath);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("비디오 업로드 : ", "실패");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                firebaseDatabase.getReference().child("pracArea").child("users").child("video").child(vid_key).setValue(downloadUrl.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyMediaProjection();
        bitmap = null;
        mv.finalize();
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
    }


    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fabTools) {
                    fam.close(true);
                    toolsAppear();
                    fabDisappear();
                } else if (view == fabdrive) {
                    Intent intent = new Intent(getApplicationContext(),RetreiveContent.class);
                    startActivityForResult(intent,GET_PICTUER_AT_G_DRIVE);
                } else if(view == fabpicture) {
                    Intent intent = new Intent(Intent.ACTION_PICK);

                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                } else if(view == fabcapture){
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                    startActivityForResult(intent, REQ_CODE_CAPTURE);
                } else if(view == fabrec_play){
                    //@##################################################################################################################
                    if(!isPlay_On) {
                        go_rec_play(view);
                    }else{
                        go_rec_pause(view);
                    }

                } else if(view == tools_btn_eraser){
                    if(tools_btn_eraser.isChecked()){
                        mv.eraseMode = true;
                        mv.setPenOrEraser(SET_ERASER_MODE);
                    }else {
                        mv.eraseMode = false;
                        mv.setPenOrEraser(SET_PEN_MODE);
                    }
                } else if(view == tools_btn_borderSize){
                    PopupMenu p2 = new PopupMenu(getApplicationContext(),view);
                    getMenuInflater().inflate(R.menu.menu_border,p2.getMenu());
                    p2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getTitle().toString()){
                                case "굵게":
                                    mv.setBorder(BORDER_SIZE_BIG);
                                    break;
                                case "중간":
                                    mv.setBorder(BORDER_SIZE_MID);
                                    break;
                                case "가늘게":
                                    mv.setBorder(BORDER_SIZE_SMALL);
                                    break;
                            }
                            return false;
                        }
                    });
                    p2.show();
                } else if(view == tools_btn_chooseColor){
                    PopupMenu p = new PopupMenu(getApplicationContext(),view);
                    getMenuInflater().inflate(R.menu.menu_color,p.getMenu());
                    p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getTitle().toString()){
                                case "검은색":
                                    mv.setColor("BLACK");
                                    break;
                                case "흰색":
                                    mv.setColor("WHITE");
                                    break;
                                case "빨강색":
                                    mv.setColor("RED");
                                    break;
                                case "파랑색":
                                    mv.setColor("BLUE");
                                    break;
                                case "초록색":
                                    mv.setColor("GREEN");
                                    break;
                            }
                            return false;
                        }
                    });
                    p.show();
                } else if(view == tools_btn_cleanall){
                    mv.setScreenClear();
                    l1.setImageResource(0);
                    databaseReference.child("DrawRoom").child("cleanSignal").push().setValue(MyUid);
                    databaseReference.child("DrawRoom").child("Borders").removeValue();
                    databaseReference.child("DrawRoomBacks").removeValue();
                    databaseReference.child("DrawRoom").child("cleanSignal").removeValue();
                }
                if(fam.isOpened()) {
                    fam.close(true);
                }
            }
        };
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }



    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        bitmap = null;



        if(requestCode == REQ_CODE_SELECT_IMAGE)

        {

            if(resultCode== Activity.RESULT_OK)

            {
                Uri uri = data.getData();
                String imagePath = getRealPathFromURI(uri);
                ExifInterface exif = null;

                try {
                    BitmapFactory.Options bo = new BitmapFactory.Options();
                    bo.inSampleSize = 4;
                    exif = new ExifInterface(imagePath);
                    bitmap = BitmapFactory.decodeFile(imagePath,bo);



                } catch (FileNotFoundException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();

                } catch (IOException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();

                } catch (Exception e)

                {

                    e.printStackTrace();

                }

                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);
                Matrix matrix = new Matrix();
                matrix.postRotate(exifDegree);

                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                setAndSendImage(resizedBitmap);

              //  bitmap.recycle(); 리사이클문제 해결하기@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            }

        }else if(requestCode == GET_PICTUER_AT_G_DRIVE){
            if(resultCode == Activity.RESULT_OK){
                byte[] bytes =  data.getByteArrayExtra("picture");
                Bitmap tmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                setAndSendImage(tmp);
            }
        }else if (requestCode == REQUEST_CODE) {

            if (resultCode != RESULT_OK) {
                Toast.makeText(this,
                        "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
                return;
            }


            mMediaProjectionCallback = new MediaProjectionCallback();
            mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
            mMediaProjection.registerCallback(mMediaProjectionCallback, null);
            mVirtualDisplay = createVirtualDisplay();
            mMediaRecorder.start();

        }else if(requestCode == REQ_CODE_CAPTURE && resultCode == RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap mImageBitmap = BitmapFactory.decodeFile(cameraTempFilePath, options);

            try {
                ExifInterface exif = new ExifInterface(cameraTempFilePath);
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);
                Matrix matrix = new Matrix();
                matrix.postRotate(exifDegree);

                Bitmap resizedBitmap = Bitmap.createBitmap(mImageBitmap, 0, 0, mImageBitmap.getWidth(), mImageBitmap.getHeight(), matrix, true);

                setAndSendImage(resizedBitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void setAndSendImage(Bitmap bitmap){

        l1.setImageBitmap(bitmap);
        sendImage(bitmap);
    }

    void runListener(){
        databaseReference.child("DrawRoomBacks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getValue(String.class);
                setImage(key);
                Log.d("key : ",key);
//                databaseReference.child("DrawRoomBacks").removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //l1.setImageResource(0);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("DrawRoom").child("cleanSignal").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String tmp = dataSnapshot.getValue().toString();
                Log.d("@@@@@@@@@@@@@@@@@@22",tmp);
                if(!tmp.equals(MyUid)) {
                    mv.setScreenClear();
                    Log.d("@@@@@@@@@@@@@@@@@@12",MyUid);
                }
                if(tmp.equals(MyUid)) {
                    mv.setScreenClear();
                    Log.d("@@@@@@@@@@@@@@@@@@12",MyUid);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void setImage(String key){

        mStorageRef.child("DrawRoomBacks").child("no1.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0, bytes.length) ;
                l1.setImageBitmap(bitmap);
                bitmap = null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("실패됨. : ","BytesDownload");
            }
        });


        Log.d("셋 이미지 key : ",key);
    }

    void sendImage(Bitmap bitmap){
        StorageReference riversRef = mStorageRef.child("DrawRoomBacks").child(back_key+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("업로드실패","!");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d("URL : ", String.valueOf(downloadUrl));
                databaseReference.child("DrawRoomBacks").push().setValue(back_key);
            }
        });
    }








    class MyView extends View {
        private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        private DatabaseReference databaseReference = firebaseDatabase.getReference().child("Rooms");
        private PaintData pd = new PaintData();
        private float wc, hc;
        private boolean eraseMode,other_eraseMode;

        float border = 10f;
        int paintColor = Color.BLACK;
        Paint paint = new Paint();
        Paint other_paint = new Paint();//자동으로 그려지는 선
        Paint canvasPaint;
        Canvas mcanvas;
        Bitmap bitmap;
        boolean init_flag;

        PorterDuffXfermode clear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        Path path = new Path();    // 자취를 저장할 객체, 내가 그리는 선
        Path other_path = new Path();//자동으로 그려지는 선




        public MyView(Context context, float w_coef, float h_coef) {
            super(context);

            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
            paint.setStrokeWidth(border);// 선의 굵기 지정
            paint.setColor(paintColor);
            paint.setStrokeJoin(Paint.Join.ROUND);

            other_paint.setAntiAlias(true);
            other_paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
            other_paint.setStrokeWidth(border);// 선의 굵기 지정
            other_paint.setColor(paintColor);
            other_paint.setStrokeJoin(Paint.Join.ROUND);

            eraseMode = false;
            other_eraseMode = false;

            wc = w_coef;
            hc = h_coef;

        }


        public void finalize(){
            bitmap = null;
        }



        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
            mcanvas = new Canvas(bitmap);
            canvasPaint = new Paint(Paint.DITHER_FLAG);
            init_flag = false;

            databaseReference.child("DrawRoom").child("Borders").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    PaintData pd2 = dataSnapshot.getValue(PaintData.class);
                    float x1 = pd2.getX() * wc;
                    float y1 = pd2.getY() * hc;

                    if(!pd2.getAuthorUid().equals(MyUid)) {

                        other_paint.setStrokeWidth(pd2.getBorder());
                        other_paint.setColor(pd2.getColor());
                        if(pd2.isEraseMode()) {
                            setPenOrEraser_other(SET_ERASER_MODE);
                        }else{
                            setPenOrEraser_other(SET_PEN_MODE);
                        }

                        switch (pd2.getState()){
                            case "start":
                                other_path.moveTo(x1, y1); // 자취에 그리지 말고 위치만 이동해라
                                break;
                            case "moving":
                                if(other_eraseMode){
                                    mcanvas.drawPath(other_path,other_paint);
                                }
                                other_path.lineTo(x1, y1);
                                break;
                            case "end":
                                mcanvas.drawPath(other_path,other_paint);//mcanvas가 null인경우가 간헐적으로 생김. onSizeChanged 호출 문제인것으로 추정됨.
                                other_path.reset();
                                break;



                        }
                        invalidate();

                    }else if(!pd2.getDeviceID().equals(idByANDROID_ID)){
                        paint.setStrokeWidth(pd2.getBorder());
                        paint.setColor(pd2.getColor());
                        if (pd2.isEraseMode()) {
                            setPenOrEraser(SET_ERASER_MODE);
                        } else {
                            setPenOrEraser(SET_PEN_MODE);
                        }

                        switch (pd2.getState()) {
                            case "start":
                                path.moveTo(x1, y1); // 자취에 그리지 말고 위치만 이동해라
                                break;
                            case "moving":
                                if (eraseMode) {
                                    mcanvas.drawPath(path, paint);
                                }
                                path.lineTo(x1, y1);
                                break;
                            case "end":
                                mcanvas.drawPath(path, paint);//mcanvas가 null인경우가 간헐적으로 생김. onSizeChanged 호출 문제인것으로 추정됨.
                                path.reset();
                                break;


                        }
                        invalidate();


                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            databaseReference.child("DrawRoom").child("Borders").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        PaintData pd2 = dataSnapshot1.getValue(PaintData.class);
                        float x1 = pd2.getX() * wc;
                        float y1 = pd2.getY() * hc;

                        if(!pd2.getAuthorUid().equals(MyUid)) {

                            other_paint.setStrokeWidth(pd2.getBorder());
                            other_paint.setColor(pd2.getColor());
                            if(pd2.isEraseMode()) {
                                setPenOrEraser_other(SET_ERASER_MODE);
                            }else{
                                setPenOrEraser_other(SET_PEN_MODE);
                            }

                            switch (pd2.getState()){
                                case "start":
                                    other_path.moveTo(x1, y1); // 자취에 그리지 말고 위치만 이동해라
                                    break;
                                case "moving":
                                    if(other_eraseMode){
                                        mcanvas.drawPath(other_path,other_paint);
                                    }
                                    other_path.lineTo(x1, y1);
                                    break;
                                case "end":
                                    mcanvas.drawPath(other_path,other_paint);//mcanvas가 null인경우가 간헐적으로 생김. onSizeChanged 호출 문제인것으로 추정됨.
                                    other_path.reset();
                                    break;



                            }
                            invalidate();

                        }else{
                            paint.setStrokeWidth(pd2.getBorder());
                            paint.setColor(pd2.getColor());
                            if (pd2.isEraseMode()) {
                                setPenOrEraser(SET_ERASER_MODE);
                            } else {
                                setPenOrEraser(SET_PEN_MODE);
                            }

                            switch (pd2.getState()) {
                                case "start":
                                    path.moveTo(x1, y1); // 자취에 그리지 말고 위치만 이동해라
                                    break;
                                case "moving":
                                    if (eraseMode) {
                                        mcanvas.drawPath(path, paint);
                                    }
                                    path.lineTo(x1, y1);
                                    break;
                                case "end":
                                    mcanvas.drawPath(path, paint);//mcanvas가 null인경우가 간헐적으로 생김. onSizeChanged 호출 문제인것으로 추정됨.
                                    path.reset();
                                    break;


                            }
                            invalidate();


                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            databaseReference.child("DrawRoom").child("Borders").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    init_flag = true;//Loading Done.
                    coverArea.setVisibility(GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        public void setPenOrEraser(int id){
            if(id == SET_PEN_MODE){
                eraseMode = false;
                paint.setXfermode(null);
                invalidate();
            }else if(id == SET_ERASER_MODE){
                eraseMode = true;
                paint.setXfermode(clear);
                Log.d("지우개 기능","작동");
                invalidate();
            }
        }

        public void setPenOrEraser_other(int id){
            if(id == SET_PEN_MODE){
                other_eraseMode = false;
                other_paint.setXfermode(null);
                invalidate();
            }else if(id == SET_ERASER_MODE){
                other_eraseMode = true;
                other_paint.setXfermode(clear);
                Log.d("지우개 기능","작동");
                invalidate();
            }
        }

        public void setScreenClear(){
            mcanvas.drawColor(0,PorterDuff.Mode.CLEAR);
            invalidate();
        }

        public void setBorder(int size){
            switch (size) {
                case BORDER_SIZE_SMALL:
                    border = 10f;
                    paint.setStrokeWidth(border);
                    break;
                case BORDER_SIZE_MID:
                    border = 30f;
                    paint.setStrokeWidth(border);
                    break;
                case BORDER_SIZE_BIG:
                    border = 50f;
                    paint.setStrokeWidth(border);
                    break;
            }
        }

        public void setColor(String newColor){
            invalidate();
            paintColor = Color.parseColor(newColor);
            paint.setColor(paintColor);
        }



        @Override
        protected void onDraw(Canvas canvas) { // 화면을 그려주는 메서드
            canvas.drawBitmap(bitmap,0,0,paint);
            canvas.drawBitmap(bitmap,0,0,other_paint);
            if(!eraseMode) {
                canvas.drawPath(path, paint);
            }
            if(!other_eraseMode){
                canvas.drawPath(other_path, other_paint);
            }
            // 저장된 path 를 그려라
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(x, y); // 자취에 그리지 말고 위치만 이동해라
                    pd.setState("start");
                    pd.setX(x / wc);
                    pd.setY(y / hc);
                    pd.setBorder(border);
                    pd.setEraseMode(eraseMode);
                    pd.setColor(paintColor);
                    pd.setAuthorUid(MyUid);
                    pd.setDeviceID(idByANDROID_ID);
                    setPaint();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(eraseMode){
                        mcanvas.drawPath(path,paint);
                    }
                    path.lineTo(x, y); // 자취에 선을 그려라
                    pd.setX(x / wc);
                    pd.setY(y / hc);
                    pd.setBorder(border);
                    pd.setState("moving");
                    pd.setEraseMode(eraseMode);
                    pd.setColor(paintColor);
                    pd.setAuthorUid(MyUid);
                    pd.setDeviceID(idByANDROID_ID);
                    setPaint();
                    break;
                case MotionEvent.ACTION_UP:
                    mcanvas.drawPath(path,paint);
                    path.reset();
                    pd.setX(x / wc);
                    pd.setY(y / hc);
                    pd.setBorder(border);
                    pd.setState("end");
                    pd.setEraseMode(eraseMode);
                    pd.setColor(paintColor);
                    pd.setAuthorUid(MyUid);
                    pd.setDeviceID(idByANDROID_ID);
                    setPaint();
                    //databaseReference.child("DrawRoom").child("Borders").removeValue();
                    break;
            }

            invalidate(); // 화면을 다시그려라

            return true;
        }


        void setPaint() {
            databaseReference.child("DrawRoom").child("Borders").push().setValue(pd);
        }


    }



}//PaintingRoom클래스의 끝

