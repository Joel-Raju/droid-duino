package com.example.joel.speechtotext;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends Activity {

    ImageButton btn;
    ImageButton call;



    Button btn_BT;
    private TextView txtSpeechInput;
    BluetoothDevice mmDevice;
    BluetoothAdapter mBluetoothAdapter;
    String msg="";
    Button btn_credit;
    OutputStream mmOutputStream;
    InputStream mmInputStream;

    String data;

    final Handler handler = new Handler();


    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();


        try
        {
        Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        handler.post(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
            }
        });

        btn_credit=(Button)findViewById(R.id.btn_credit);
        btn_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Credits.class);
                startActivity(i);
            }
        });

        btn=(ImageButton)findViewById(R.id.btnSpeak);
        txtSpeechInput=(TextView)findViewById(R.id.txtSpeechInput);


        btn_BT =(Button)findViewById(R.id.btnBt);
        btn_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mBluetoothAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }

                //request bluetooth coonnection
                else if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, 0);

                } else {

                    try {

                        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                        if (pairedDevices.size() > 0) {
                            for (BluetoothDevice device : pairedDevices) {
                                if (device.getName().equals("HC-05")) {

                                    mmDevice = device;
                                    //break;
                                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
                                    BluetoothSocket mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                                    mmSocket.connect();
                                    mmOutputStream = mmSocket.getOutputStream();
                                    mmInputStream=mmSocket.getInputStream();


                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

            }


        });


        // hide the action bar
        //getActionBar().hide();

        call=(ImageButton)findViewById(R.id.btnCall);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent callIntent=new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:09567364774"));
                startActivity(callIntent);
            }
        });




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                promptSpeehInput();
            }
        });
    }


    private void promptSpeehInput()
    {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException a)
        {
            Toast.makeText(getApplicationContext(),getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    try{
                        msg="*"+result.get(0)+"#";
                        mmOutputStream.write(msg.getBytes());
                        mmOutputStream.flush();
                    }

                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "No device connected", Toast.LENGTH_SHORT).show();
                    }




                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

                    //Got text






}
break;
        }

        }
        }


@Override
public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        switch(keyCode)
        {

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Toast.makeText(MainActivity.this, "Volume Down", Toast.LENGTH_SHORT).show();

                Intent callIntent=new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:09567364774"));
                startActivity(callIntent);
                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                Toast.makeText(MainActivity.this, "Volume Down", Toast.LENGTH_SHORT).show();
                String phoneNo = "09567364774";
                String msg = "help me";
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                    Toast.makeText(getApplicationContext(), "Message Sent",
                            Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
        }

        return false;
    }
}
