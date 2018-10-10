package com.example.songhyeonseok.taos_gps_test;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    static final int ACTION_ENABLE_BT = 101;
    final int INIT_OBD_TERM_MILI_SEC = 1000;// ATZ, ATSP0, ATE0, ATSH7E4 4개 신호 전송 주기, 밀리세컨드 단위
    final int TYPE_2101 = 2101;
    final int TYPE_2100 = 2100;
    TextView mTextMsg;
    EditText mEditData;
    BluetoothAdapter mBA;
    ListView mListDevice;
    ArrayList<String> mArDevice; // 원격 디바이스 목록
    static final String  BLUE_NAME = "BluetoothEx";  // 접속시 사용하는 이름
    // 접속시 사용하는 고유 ID
    static final UUID BLUE_UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");
    //0001101-0000-1000-8000-00805F9B34FB  fa87c0d0-afac-11de-8a39-0800200c9a66  8ce255c0-200a-11e0-ac64-0800200c9a66
    String base_addr = "http://bms.taos.kr/";
    String batt_add = base_addr+"evraw";
    ClientThread mCThread = null; // 클라이언트 소켓 접속 스레드
    ServerThread mSThread = null; // 서버 소켓 접속 스레드
    SocketThread mSocketThread = null; // 데이터 송수신 스레드
    ParsingThread mParsingThread = null;
    StringBuffer viewingWindow;


    LocationManager lm;
    LinearLayout tmpLinear;
    ToggleButton toggleButton;
    Button change_addr,change_freq;
    EditText input_addr,input_freq;
    double s_longitude ;
    double s_latitude ;
    Thread thread;
    boolean flag;
    String target_addr;
    int freq;
    int a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = (ToggleButton)findViewById(R.id.start_gps);//gps 가동 시작버튼
        change_addr = (Button)findViewById(R.id.change_addr);
        change_freq = (Button)findViewById(R.id.change_freq);
        input_addr = (EditText) findViewById(R.id.input_addr);
        input_freq = (EditText)findViewById(R.id.input_freq);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        View.OnClickListener btn  = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.change_addr && !input_addr.getText().toString().equals("")){
                    base_addr = input_addr.getText().toString();
                    Log.d("addr ","not null@"+input_addr.getText().toString()+"@");
                    input_addr.setText("");
                }else if(v.getId() == R.id.change_freq && !input_freq.getText().toString().equals("")){
                    try {
                        float tmp = Float.valueOf(input_freq.getText().toString()) * 1000;
                        freq = Math.round(tmp);
                    }catch(NumberFormatException e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"입력이 잘못되었습니다.",Toast.LENGTH_SHORT).show();
                        input_freq.setText("");
                    }
                    Log.d("freq 수정 : ",""+freq);
                    input_freq.setText("");
                }
            }
        };

        change_addr.setOnClickListener(btn);
        change_freq.setOnClickListener(btn);


        flag =true;
        freq = 5000;
        target_addr = base_addr+"gps/";


        //-------------------------------블루투스 모듈 초기화 ---------------------------------------start
        tmpLinear = (LinearLayout)findViewById(R.id.layoutInput);
        mTextMsg = (TextView)findViewById(R.id.textMessage);
        mEditData = (EditText)findViewById(R.id.editData);

        tmpLinear.setVisibility(View.GONE);
        // ListView 초기화
        initListView();


        boolean isBlue = canUseBluetooth();
        if( isBlue )
            // 페어링된 원격 디바이스 목록 구하기

            getParedDevice();
        //-------------------------------블루투스 모듈 초기화 ---------------------------------------end

        viewingWindow = new StringBuffer();

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                thread = new Thread(new Runnable() { // 5초마다 get방식으로  위도, 경도 쏘는 Thread.--------------------------------start

                    @Override
                    public void run() {
                        while(flag) {
                            try {
                                URL url = new URL(target_addr + s_latitude + "/" + s_longitude);
                                URLConnection conn = url.openConnection();
                                conn.getInputStream();
                                Log.i("msg", "go:"+url.toString()+", \n freq :"+freq);
                                Thread.sleep(freq);

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                Log.i("msg", "no");
                            }
                        }
                    }
                });


                try{
                    if(toggleButton.isChecked()){

                        toggleButton.setText("수신중..");
                        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        flag = true;

                    }else{
                        flag = false;
                        if(thread.isAlive()){
                            thread.interrupt();
                        }
                        toggleButton.setText("위치정보 미수신중");
                        lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                    }

                    thread.start();

                }catch(SecurityException ex){
                }

            }
        });


        //--------------------------------------------------------------------------------------------------------end




    }




    private final LocationListener mLocationListener = new LocationListener() {

        double longitude;
        double latitude;

        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            longitude = location.getLongitude(); //경도
            latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
            s_longitude = longitude;
            s_latitude = latitude;

        }
        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }



    };

    public boolean canUseBluetooth() {
        // 블루투스 어댑터를 구한다
        mBA = BluetoothAdapter.getDefaultAdapter();
        // 블루투스 어댑터가 null 이면 블루투스 장비가 존재하지 않는다.
        if( mBA == null ) {
            mTextMsg.setText("기기를 찾을 수 없습니다.");
            return false;
        }

        mTextMsg.setText("기기를 찾았습니다.");
        // 블루투스 활성화 상태라면 함수 탈출
        if( mBA.isEnabled() ) {
            mTextMsg.append("\n기기가 사용가능 상태입니다.");
            return true;
        }

        // 사용자에게 블루투스 활성화를 요청한다
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, ACTION_ENABLE_BT);
        return false;
    }

    // 블루투스 활성화 요청 결과 수신
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == ACTION_ENABLE_BT ) {
            // 사용자가 블루투스 활성화 승인했을때
            if( resultCode == RESULT_OK ) {
                mTextMsg.append("\n기기를 사용 할 수 있습니다.");
                // 페어링된 원격 디바이스 목록 구하기
                getParedDevice();
            }
            // 사용자가 블루투스 활성화 취소했을때
            else {
                mTextMsg.append("\n기기를 사용 할 수 없습니다.");
            }
        }
    }

    // 원격 디바이스 검색 시작
    public void startFindDevice() {
        // 원격 디바이스 검색 중지
        stopFindDevice();
        // 디바이스 검색 시작
        mBA.startDiscovery();
        // 원격 디바이스 검색 이벤트 리시버 등록
        registerReceiver(mBlueRecv, new IntentFilter( BluetoothDevice.ACTION_FOUND ));
    }

    // 디바이스 검색 중지
    public void stopFindDevice() {
        // 현재 디바이스 검색 중이라면 취소한다
        if( mBA.isDiscovering() ) {
            mBA.cancelDiscovery();
            // 브로드캐스트 리시버를 등록 해제한다
            unregisterReceiver(mBlueRecv);
        }
    }

    // 원격 디바이스 검색 이벤트 수신
    BroadcastReceiver mBlueRecv = new BroadcastReceiver() {
        public void  onReceive(Context context, Intent intent) {
            if( intent.getAction() == BluetoothDevice.ACTION_FOUND ) {
                // 인텐트에서 디바이스 정보 추출
                BluetoothDevice device = intent.getParcelableExtra( BluetoothDevice.EXTRA_DEVICE );
                // 페어링된 디바이스가 아니라면
                if( device.getBondState() != BluetoothDevice.BOND_BONDED )
                    // 디바이스를 목록에 추가
                    addDeviceToList(device.getName(), device.getAddress());
            }
        }
    };

    // 디바이스를 ListView 에 추가
    public void addDeviceToList(String name, String address) {
        // ListView 와 연결된 ArrayList 에 새로운 항목을 추가
        String deviceInfo = name + " - " + address;
        Log.d("tag1", "Device Find: " + deviceInfo);
        mArDevice.add(deviceInfo);
        // 화면을 갱신한다
        ArrayAdapter adapter = (ArrayAdapter)mListDevice.getAdapter();
        adapter.notifyDataSetChanged();
    }

    // ListView 초기화
    public void initListView() {
        // 어댑터 생성
        mArDevice = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mArDevice);
        // ListView 에 어댑터와 이벤트 리스너를 지정
        mListDevice = (ListView)findViewById(R.id.listDevice);
        mListDevice.setAdapter(adapter);
        mListDevice.setOnItemClickListener(this);
    }

    // 다른 디바이스에게 자신을 검색 허용
    public void setDiscoverable() {
        // 현재 검색 허용 상태라면 함수 탈출
        if( mBA.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE )
            return;
        // 다른 디바이스에게 자신을 검색 허용 지정
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(intent);
    }

    // 페어링된 원격 디바이스 목록 구하기
    public void getParedDevice() {
        if( mSThread != null ) return;
        // 서버 소켓 접속을 위한 스레드 생성 & 시작
        mSThread = new ServerThread();
        mSThread.start();

        // 블루투스 어댑터에서 페어링된 원격 디바이스 목록을 구한다
        Set<BluetoothDevice> devices = mBA.getBondedDevices();
        // 디바이스 목록에서 하나씩 추출
        for( BluetoothDevice device : devices ) {
            // 디바이스를 목록에 추가
            addDeviceToList(device.getName(), device.getAddress());
        }

        // 원격 디바이스 검색 시작
        startFindDevice();

        // 다른 디바이스에 자신을 노출
        setDiscoverable();
    }

    // ListView 항목 선택 이벤트 함수
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        // 사용자가 선택한 항목의 내용을 구한다
        String strItem = mArDevice.get(position);

        // 사용자가 선택한 디바이스의 주소를 구한다
        int pos = strItem.indexOf(" - ");
        if( pos <= 0 ) return;
        String address = strItem.substring(pos + 3);
        mTextMsg.setText("선택된 기기 : " + address);

        // 디바이스 검색 중지
        stopFindDevice();
        // 서버 소켓 스레드 중지
        if(mSThread !=null) {
            mSThread.cancel();
            mSThread = null;
        }

        mCThread = null;
     //   if( mCThread != null ) return;
        // 상대방 디바이스를 구한다
        BluetoothDevice device = mBA.getRemoteDevice(address);
        // 클라이언트 소켓 스레드 생성 & 시작
        mCThread = new ClientThread(device);
        mCThread.start();
    }

    // 클라이언트 소켓 생성을 위한 스레드
    private class ClientThread extends Thread {
        private BluetoothSocket mmCSocket;

        // 원격 디바이스와 접속을 위한 클라이언트 소켓 생성
        public ClientThread(BluetoothDevice  device) {
            try {
                mmCSocket = device.createInsecureRfcommSocketToServiceRecord(BLUE_UUID);
            } catch(IOException e) {
                showMessage("Create Client Socket error");
                return;
            }
        }

        public void run() {
            // 원격 디바이스와 접속 시도
            try {
                mmCSocket.connect();
            } catch(IOException e) {
                showMessage("연결 실패");
                e.printStackTrace();
                // 접속이 실패했으면 소켓을 닫는다
                try {
                    mmCSocket.close();
                } catch (IOException e2) {
                    showMessage("Client Socket close error");
                }
                return;
            }

            // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
            onConnected(mmCSocket);
        }

        // 클라이언트 소켓 중지
        public void cancel() {
            try {
                mmCSocket.close();
            } catch (IOException e) {
                showMessage("Client Socket close error");
            }
        }
    }

    // 서버 소켓을 생성해서 접속이 들어오면 클라이언트 소켓을 생성하는 스레드
    private class ServerThread extends Thread {
        private BluetoothServerSocket mmSSocket;

        // 서버 소켓 생성
        public ServerThread() {
            try {
                mmSSocket = mBA.listenUsingInsecureRfcommWithServiceRecord(BLUE_NAME, BLUE_UUID);
            } catch(IOException e) {
                showMessage("Get Server Socket Error");
            }
        }

        public void run() {
            BluetoothSocket cSocket = null;

            // 원격 디바이스에서 접속을 요청할 때까지 기다린다
            try {
                cSocket = mmSSocket.accept();
            } catch(IOException e) {
                showMessage("연결중...");
                return;
            }

            // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
            onConnected(cSocket);
        }

        // 서버 소켓 중지
        public void cancel() {
            try {
                mmSSocket.close();
            } catch (IOException e) {
                showMessage("Server Socket close error");
            }
        }
    }

    // 메시지를 화면에 표시
    public void showMessage(String strMsg) {
        // 메시지 텍스트를 핸들러에 전달
        Message msg = Message.obtain(mHandler, 0, strMsg);
        mHandler.sendMessage(msg);
        Log.d("tag1", strMsg);
    }

    // 메시지 화면 출력을 위한 핸들러
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String strMsg = (String)msg.obj;
                mTextMsg.setText(strMsg);
            }
        }
    };

    // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
    public void onConnected(BluetoothSocket socket) {
        showMessage("연결이 완료되었습니다.");

        // 데이터 송수신 스레드가 생성되어 있다면 삭제한다
        if( mSocketThread != null )
            mSocketThread = null;

        if(mParsingThread != null){
            mParsingThread = null;
        }
        // 데이터 송수신 스레드를 시작
        mSocketThread = new SocketThread(socket);
        mSocketThread.start();
        mParsingThread = new ParsingThread();
        mParsingThread.start();
    }

    void setCurrentType(int type){
        mParsingThread.setCurrent_type(type);
    }


    // 데이터 송수신 스레드
    private class SocketThread extends Thread {
        private final BluetoothSocket mmSocket; // 클라이언트 소켓
        private InputStream mmInStream; // 입력 스트림
        private OutputStream mmOutStream; // 출력 스트림
        Boolean socketFlag = true;

        public void setSocketFlag(Boolean socketFlag) {
            this.socketFlag = socketFlag;
        }

        public SocketThread(BluetoothSocket socket) {
            mmSocket = socket;

            // 입력 스트림과 출력 스트림을 구한다
            try {
                mmInStream = socket.getInputStream();
                mmOutStream = socket.getOutputStream();
            } catch (IOException e) {
                showMessage("Get Stream error");
            }
        }

        // 소켓에서 수신된 데이터를 화면에 표시한다
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            final String strSet1[] = {"ATSH7E4","2101"};
            final String strSet2[] = {"ATSH7E2","2100"};


            init();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (socketFlag){
                        try {
                            Thread.sleep(freq);
                            setCurrentType(TYPE_2101);
                            write(strSet1[0]);
                            Thread.sleep(500);
                            write(strSet1[1]);

                            Thread.sleep(1000);

                            setCurrentType(TYPE_2100);
                            write(strSet2[0]);
                            Thread.sleep(500);
                            write(strSet2[1]);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            while (socketFlag) {
                try {
                    // 입력 스트림에서 데이터를 읽는다
                    bytes = mmInStream.read(buffer);
                    String strBuf = new String(buffer, 0, bytes);
                    viewingWindow.append(strBuf);
                    showMessage("ByteStream 수신 감지됨 : "+strBuf);
                    Log.d("감지 시간 확인","");
                    Thread.sleep(250);
                } catch (Exception e) {
                    showMessage("연결이 종료되었습니다.");
                    this.setSocketFlag(false);
                    this.interrupt();
                    break;
                }
            }


        }

        public void init(){
            try {
                String[] initstr= {"ATZ\n\r","ATSP0\n\r","ATE0\n\r"};
                // 출력 스트림에 데이터를 저장한다
                for(String a:initstr){
                    Thread.sleep(INIT_OBD_TERM_MILI_SEC);
                    byte[] buffer = a.getBytes();
                    mmOutStream.write(buffer);
                    showMessage("초기화중.. : " + a);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Socket write error");
                viewingWindow.delete(0,viewingWindow.length()-1);
            }
        }

        // 데이터를 소켓으로 전송한다
        public void write(String strBuf) {//String strBuf
            try {
                String tmp = strBuf+"\n\r";
                // 출력 스트림에 데이터를 저장한다
                byte[] buffer = tmp.getBytes();
                mmOutStream.write(buffer);
                showMessage("보낸 메세지 : " + strBuf);
            } catch (IOException e) {
                showMessage("Socket write error");
                viewingWindow.delete(0,viewingWindow.length()-1);
            }
        }
    }

    private class ParsingThread extends Thread{
        private boolean flag = true;
        private ParsingBattData pbd = new ParsingBattData();
        int current_type = 0;

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public void setCurrent_type(int current_type) {
            this.current_type = current_type;
        }

        @Override
        public void run() {
            super.run();
            while (flag){
                try {
                    int loop_type = current_type;//정보 타입 동기화
                    if(viewingWindow.length() > 61) {
                        String tmp = pbd.getJSON(viewingWindow.toString(),loop_type);
                        showMessage(tmp);
                        if(!isinNull(tmp)) {
                            sendToHttp_batt_data(tmp,loop_type);
                            viewingWindow.delete(0,viewingWindow.length()-1);
                        }else {
                            Log.d("Null Detector"," null 발견됨.");
                            viewingWindow.delete(0,viewingWindow.length()-1);
                        }

                    }
                    Thread.sleep(500);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    // 버튼 클릭 이벤트 함수
    public void onClick(View v) {
        switch( v.getId() ) {
            case R.id.btnSend : {
                // 데이터 송수신 스레드가 생성되지 않았다면 함수 탈출
                if( mSocketThread == null ) return;
                // 사용자가 입력한 텍스트를 소켓으로 전송한다
                String strBuf = mEditData.getText().toString();
                if( strBuf.length() < 1 ) return;
                mEditData.setText("");
                mSocketThread.write(strBuf);
                break;
            }
        }
    }

    public boolean isinNull(String s){

        String regex = "(null||Null||NULL)";
        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(s);

        while (matcher.find()){
            if(matcher.group().equals("null")||matcher.group().equals("Null")||matcher.group().equals("NULL")){
                return true;
            }
        }

        return false;
    }


    // 앱이 종료될 때 디바이스 검색 중지
    public void onDestroy() {
        super.onDestroy();
        // 디바이스 검색 중지
        stopFindDevice();

        // 스레드를 종료
        if( mCThread != null ) {
            mCThread.cancel();
            mCThread = null;
        }
        if( mSThread != null ) {
            mSThread.cancel();
            mSThread = null;
        }

        if( mSocketThread != null ) {
            mSocketThread.setSocketFlag(false);
            mSocketThread.interrupt();
            mSocketThread = null;
        }

        flag = false;
        if(thread != null) {
            if (thread.isAlive()) {
                thread.interrupt();
            }
        }
        toggleButton.setText("위치정보 미수신중");
        lm.removeUpdates(mLocationListener);
        try {
                unregisterReceiver(mBlueRecv);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public void sendToHttp_batt_data(String s,int type){
        final String tmp = s;
        final String batt_add ;
        if(type == TYPE_2101){
            batt_add = this.batt_add+"1";
        }else {
            batt_add = this.batt_add+"2";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
               // tmphttp = new StringBuffer();
               // tmphttp.append(httpstring);
                Log.d("☆★☆★tag",tmp);
                try {
                    // 연결 url 설정
                    URL url = new URL(batt_add);
                    // 커넥션 객체 생성
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 연결되었으면.
                    if (conn != null) {
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setRequestProperty("Content-type", "application/json");
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(5000);
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        OutputStream os = conn.getOutputStream();
                        os.write(tmp.toString().getBytes("UTF-8"));
                        os.flush();
                        os.close();
                        Log.d("tkf","실행됨");
                        // 연결되었음 코드가 리턴되면.
                        a = conn.getResponseCode();
                        Log.d("",a+"");
                        conn.disconnect();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        try {
        }catch(Exception e){
            e.printStackTrace();
        }

    }


}

