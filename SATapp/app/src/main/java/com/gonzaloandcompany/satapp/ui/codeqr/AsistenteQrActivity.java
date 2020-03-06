package com.gonzaloandcompany.satapp.ui.codeqr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.ui.home.detail.InventariableDetailActivity;

public class AsistenteQrActivity extends AppCompatActivity {
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asiente_qr);

        Button btnEscanear = findViewById(R.id.btnEscanear);

        btnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AsistenteQrActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AsistenteQrActivity.this, "pedir permiso", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(AsistenteQrActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                    if(ContextCompat.checkSelfPermission(AsistenteQrActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        escanear();
                    }else{
                        Toast.makeText(AsistenteQrActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AsistenteQrActivity.this, "abrir camera", Toast.LENGTH_SHORT).show();
                    escanear();

                }


                /*if (!permisoCamaraConcedido) {
                    Toast.makeText(AsistenteQrActivity.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_SHORT).show();
                    permisoSolicitadoDesdeBoton = true;
                    verificarYPedirPermisosDeCamara();
                    return;
                }
                escanear();*/
            }
        });

    }

    
    private void escanear() {
        Intent i = new Intent(AsistenteQrActivity.this, ActivityEscanear.class);
        startActivityForResult(i, CODIGO_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    codigo = data.getStringExtra("codigo");
                    Intent intent = new Intent(AsistenteQrActivity.this, InventariableDetailActivity.class);
                    intent.putExtra(Constants.ID_INVENTARIABLE, codigo);
                    startActivity(intent);
                    Log.e("ID", codigo);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                        escanear();
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(AsistenteQrActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(AsistenteQrActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }


    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(AsistenteQrActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }

}
