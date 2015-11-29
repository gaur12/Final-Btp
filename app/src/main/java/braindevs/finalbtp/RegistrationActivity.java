package braindevs.finalbtp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by GAURAV on 5/24/2015.
 */
public class RegistrationActivity extends Activity {
    RegistrationAdapter adapter;
    RegistrationOpenHelper helper;
    EditText fnameEdit, lnameEdit;
    Button submitBtn, resetBtn;
    private static final int PICK_CONTACT = 0;
    Button button;
    // String number,name;
    int number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        fnameEdit = (EditText) findViewById(R.id.et_fname);
        lnameEdit = (EditText) findViewById(R.id.et_lname);
        submitBtn = (Button) findViewById(R.id.btn_submit);
        //resetBtn = (Button) findViewById(R.id.btn_reset);
        adapter = new RegistrationAdapter(this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                // String fnameValue = fnameEdit.getText().toString();
                //String lnameValue = lnameEdit.getText().toString();
                //long val = adapter.insertDetails(fnameValue, lnameValue);
                //finish();
            }

        });}

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
}

        /*resetBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                fnameEdit.setText("");
                lnameEdit.setText("");
            }
        });*/


