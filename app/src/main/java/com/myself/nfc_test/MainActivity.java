package com.myself.nfc_test;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.TagTechnology;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView myUID;

    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(), NfcB.class.getName(), NfcF.class.getName(), NfcV.class.getName(),
                    NdefFormatable.class.getName(), TagTechnology.class.getName(), IsoDep.class.getName(),
                    MifareClassic.class.getName(), MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //특정한 이벤트가 발생했을 때 작동하는 인텐트.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        //특정한 NFC 이벤트가 발생했을 때 인텐트를 생성.
        IntentFilter filter = new IntentFilter();

        //태그가 스마트폰 근처로 접근했을 때 이벤트가 발생함.
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);

        //NFC 이벤트로부터 인텐트를 얻어 디스패치를 활성화.
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onNewIntent(Intent intent){
        if(intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)){
            myUID = (TextView) findViewById(R.id.myUID);
            myUID.setText(ByteArrayToHEXString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
        }
    }

    private String ByteArrayToHEXString(byte[] array){
        int i, j, in;

        String[] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out = "";

        for(j = 0 ; j < array.length ; ++j){
            in = (int)array[j]&0xff;
            i = (in >> 4)&0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

}