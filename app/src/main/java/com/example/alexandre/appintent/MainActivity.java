package com.example.alexandre.appintent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAbrirSite;
    private Button btnDiscar;
    private Button btnDiscarChamar;
    private Button btnAbrirMapa;
    private Button btnAbrirAgenda;
    private Button btnEditarPrimeiroContato;
    private Button btnAbrirCamera;

    private final int RESPOSTA_FOTO = 1;
    private final int RESPOSTA_LEITURA_C0NTATO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnAbrirSite = (Button) findViewById(R.id.btnAbrirSite);
        this.btnDiscar = (Button) findViewById(R.id.btnDiscar);
        this.btnDiscarChamar = (Button) findViewById(R.id.btnDiscarChamar);
        this.btnAbrirMapa = (Button) findViewById(R.id.btnAbrirMapa);
        this.btnAbrirAgenda = (Button) findViewById(R.id.btnAbrirAgenda);
        this.btnEditarPrimeiroContato = (Button) findViewById(R.id.btnEditarPrimeiroContato);
        this.btnAbrirCamera = (Button) findViewById(R.id.btnAbrirCamera);

        this.btnAbrirSite.setOnClickListener(this);
        this.btnDiscar.setOnClickListener(this);
        this.btnDiscarChamar.setOnClickListener(this);
        this.btnAbrirMapa.setOnClickListener(this);
        this.btnAbrirAgenda.setOnClickListener(this);
        this.btnEditarPrimeiroContato.setOnClickListener(this);
        this.btnAbrirCamera.setOnClickListener(this);
    }

    private void abrirSite() {
        String url = "https://www.unit.br";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void discarNumero() {
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        String numero = manager.getLine1Number();

        if (numero.length() == 0) {
            numero = "79999999999";
        }

        String uri = "tel:" + numero.trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    private void discarChamarNumero() {
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        String numero = manager.getLine1Number();

        if (numero.length() == 0) {
            numero = "79999999999";
        }

        String uri = "tel:" + numero.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    private void abrirMapa() {
        Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988");
        Intent i = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        i.setPackage("com.google.android.apps.maps");
        startActivity(i);
    }

    private void abrirAgenda() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, RESPOSTA_LEITURA_C0NTATO);
    }

    private void editarPrimeiroContato() {
        String id = null;

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                null, null);
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            id = cursor.getString(index);
        }
        cursor.close();

        if (id != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(id));
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void abrirCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, RESPOSTA_FOTO);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (RESPOSTA_LEITURA_C0NTATO):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAbrirSite:
                abrirSite();
                break;
            case R.id.btnDiscar:
                discarNumero();
                break;
            case R.id.btnDiscarChamar:
                discarChamarNumero();
                break;
            case R.id.btnAbrirMapa:
                abrirMapa();
                break;
            case R.id.btnAbrirAgenda:
                abrirAgenda();
                break;
            case R.id.btnEditarPrimeiroContato:
                editarPrimeiroContato();
                break;
            case R.id.btnAbrirCamera:
                abrirCamera();
                break;
            default:
                break;
        }
    }
}
