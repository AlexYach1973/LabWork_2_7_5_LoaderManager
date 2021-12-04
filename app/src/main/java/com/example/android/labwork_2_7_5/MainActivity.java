package com.example.android.labwork_2_7_5;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static boolean READ_CONTACTS_GRANTED = false;

    private ListView listContact;
    private  LoaderManager loaderManager;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        Button btnRead = findViewById(R.id.button_read);
        listContact = findViewById(R.id.contactList);

        // Разрешения
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        // если устройство до API 23, устанавливаем разрешение
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            READ_CONTACTS_GRANTED = true;
        } else {
            // вызываем диалоговое окно для установки разрешений для API > 23
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }

        // если разрешение установлено, загружаем контакты через кнопку btnReadContacts
        btnRead.setOnClickListener(v -> {
            if (READ_CONTACTS_GRANTED) {

                // Получаем loadManager
                loaderManager = getLoaderManager();
                // Инициализируем
                loaderManager.initLoader(1111, null, this);
                // Дальше автоматически вызываются методы Loader'a

            } else {
                Toast.makeText(MainActivity.this, "Требуется установить разрешения",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        return new CursorLoader(this, uri, null,
                null,null, null);
    }

    @SuppressLint("Range")
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<List<String>> list = new ArrayList<>();
        while (data.moveToNext()) {
//            String name;
            List<String> list_data = new ArrayList<>();
            // NAME
            String name = data.getString(
                    data.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // _ID
            int id = data.getInt(data.getColumnIndex(ContactsContract.Contacts._ID));

            // Telephone
            @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" +
                            id, null, null);
            while (cursor.moveToNext()) {
                list_data.add(name);
                list_data.add(cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }
            list.add(list_data);
        }

        MyAdapter adapter = new MyAdapter(context, list);
        listContact.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}