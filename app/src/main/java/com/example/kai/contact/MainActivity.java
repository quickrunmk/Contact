package com.example.kai.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    EditText Name, Phone, Email, Address;
    TabHost tabHost;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;
    ImageView contactImageView;
    DatabaseHandler dbHandler;
    //Uri imageURI = Uri.parse("android.resource:contact/drawable/image.png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText) findViewById(R.id.Name);
        Phone = (EditText) findViewById(R.id.Phone);
        Email = (EditText) findViewById(R.id.Email);
        Address = (EditText) findViewById(R.id.Address);
        contactListView = (ListView) findViewById(R.id.listView);
        contactImageView = (ImageView) findViewById(R.id.imageView);
        final Button Add = (Button) findViewById(R.id.Add);
        dbHandler = new DatabaseHandler(getApplicationContext());

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Creator");
        tabSpec.setContent(R.id.tabContactCreator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("List");
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Add.setEnabled(String.valueOf(Name.getText()).trim().length() > 0
                        && String.valueOf(Phone.getText()).trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Add.setEnabled(String.valueOf(Name.getText()).trim().length() > 0
                        && String.valueOf(Phone.getText()).trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contactImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select contact image"), 1);
            }
        });

        if(dbHandler.getContactsCount() != 0) {
            Contacts.addAll(dbHandler.getAllContacts());
        }

        populateList();

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact contact = new Contact(dbHandler.getContactsCount(), String.valueOf(Name.getText()), String.valueOf(Phone.getText()), String.valueOf(Email.getText()),String.valueOf(Address.getText()));
                if (!contactExists(contact)) {
                    dbHandler.createContact(contact);
                    Contacts.add(contact);
                    Toast.makeText(getApplicationContext(), String.valueOf(Name.getText()) + " has been created", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(Name.getText()) + " already exists", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if(resCode == RESULT_OK) {
            if(reqCode == 1) {
                contactImageView.setImageURI(data.getData());
            }
        }
    }

    private boolean contactExists(Contact contact) {
        String name= contact.get_name();
        int contactCount = Contacts.size();

        for(int i = 0; i < contactCount; i++) {
            if(name.compareToIgnoreCase(Contacts.get(i).get_name()) == 0)
               return true;
        }
        return false;
    }
    private void populateList() {
        ArrayAdapter<Contact> adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    private class ContactListAdapter extends ArrayAdapter<Contact> {
        public ContactListAdapter() {
            super (MainActivity.this, R.layout.listview_item, Contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);
            Contact currentContact = Contacts.get(position);
            TextView name = (TextView) view.findViewById(R.id.ContactName);
            name.setText(currentContact.get_name());
            TextView phone = (TextView) view.findViewById(R.id.ContactPhone);
            phone.setText(currentContact.get_phone());
            TextView email = (TextView) view.findViewById(R.id.ContactEmail);
            email.setText(currentContact.get_email());
            TextView address = (TextView) view.findViewById(R.id.ContactAddr);
            address.setText(currentContact.get_address());

            return view;
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
}
