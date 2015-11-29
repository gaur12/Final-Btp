package braindevs.finalbtp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends Activity {
    RegistrationAdapter adapter_ob;
    RegistrationOpenHelper helper_ob;
    SQLiteDatabase db_ob;
    ListView nameList;
    Button registerBtn;
    Cursor cursor, cursor1;
    Context pcontext;
    private static final int PICK_CONTACT = 0;
    RegistrationAdapter adapter;
    RegistrationOpenHelper helper;
    IntentFilter mIntentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TelephonyManager tmngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            pcontext = context;
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener(pcontext);
            tmngr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        nameList = (ListView) findViewById(R.id.lv_name);
        registerBtn = (Button) findViewById(R.id.btn_register);
        adapter_ob = new RegistrationAdapter(this);
        adapter = new RegistrationAdapter(this);
        //  cs = new Callstate();
        String[] from = { helper_ob.FNAME, helper_ob.LNAME };
        int[] to = { R.id.tv_fname, R.id.tv_lname };
        cursor = adapter_ob.queryName();

        android.support.v4.widget.SimpleCursorAdapter cursorAdapter = new android.support.v4.widget.SimpleCursorAdapter(this,
                R.layout.row, cursor, from, to);
        nameList.setAdapter(cursorAdapter);
        nameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Bundle passdata = new Bundle();
                Cursor listCursor = (Cursor) arg0.getItemAtPosition(arg2);
                int nameId = listCursor.getInt(listCursor
                        .getColumnIndex(helper_ob.KEY_ID));
                // Toast.makeText(getApplicationContext(),
                // Integer.toString(nameId), 500).show();
                passdata.putInt("keyid", nameId);
                Intent passIntent = new Intent(MainActivity.this,
                        EditActivity.class);
                passIntent.putExtras(passdata);
                startActivity(passIntent);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String fnameValue = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone =
                                c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + fnameValue, null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            // Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();
                            //num.setText(cNumber);
                            //number=num.getText().toString();
                            long val = adapter.insertDetails(name, cNumber);
                        }
                    }
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cursor.requery();
        registerReceiver(receiver, mIntentFilter);

    }

    private class MyPhoneStateListener extends PhoneStateListener {
        int prev_state=0;
        AudioManager am = (AudioManager) pcontext.getSystemService(Context.AUDIO_SERVICE);
        String mode = "";
        int f=0;
        public MyPhoneStateListener(Context pcontext) {
            // pcontext=context;
        }

        public void onCallStateChanged(int state, String incoming) {

            AudioManager am = (AudioManager) pcontext.getSystemService(Context.AUDIO_SERVICE);
            String mode = "";
           // Toast.makeText(pcontext,"gdhbsafh",duration).show();
            if (state == 1) {
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(pcontext,"gdhbsafh",duration).show();
                cursor1 = adapter_ob.queryName();
                if (cursor1.moveToFirst()) {
                    do {
                        if(incoming.equals(cursor1.getString(2))||incoming.equals("+91"+cursor1.getString(2))
                                ||incoming.equals("+91"+cursor1.getString(2))
                                ||incoming.equals("0"+cursor1.getString(2))
                                ||incoming.equals("+9111"+cursor1.getString(2))
                                ||incoming.equals("011"+cursor1.getString(2))
                                ||incoming.equals("012"+cursor1.getString(2))
                                )
                        {
                            f=1;
                            Toast.makeText(pcontext,"gdhbsafh",duration).show();
                            break;
                        }
                    } while (cursor1.moveToNext());
                }

            }
            if(f==1)
            {

                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                am.setStreamVolume(AudioManager.STREAM_RING,am.getStreamMaxVolume(AudioManager.STREAM_RING),AudioManager.FLAG_PLAY_SOUND);
                f = 0;
                //   cursor1.moveToFirst();
            }
            else
            {
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                //cursor1.moveToFirst();
            }


        }
    }
}