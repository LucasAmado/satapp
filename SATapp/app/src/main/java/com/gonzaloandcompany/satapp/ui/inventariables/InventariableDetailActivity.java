package com.gonzaloandcompany.satapp.ui.inventariables;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.gonzaloandcompany.satapp.MainActivity;
import com.gonzaloandcompany.satapp.common.Constants;
import com.gonzaloandcompany.satapp.mymodels.Inventariable;
import com.gonzaloandcompany.satapp.retrofit.ApiSAT;
import com.gonzaloandcompany.satapp.retrofit.ServicePeticiones;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gonzaloandcompany.satapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventariableDetailActivity extends AppCompatActivity {
    String idInventariable = "5e5abf5b2f8e3d001779e79d";
    ServicePeticiones service;
    @BindView(R.id.textViewName)
    TextView tvNombre;
    @BindView(R.id.textViewCode)
    TextView tvCodigo;
    @BindView(R.id.textViewType)
    TextView tvTipo;
    @BindView(R.id.textViewCreatedAt)
    TextView tvCreado;
    @BindView(R.id.textViewUpdatedAt)
    TextView tvModificado;
    @BindView(R.id.textViewDescription)
    TextView tvDescripcion;
    @BindView(R.id.imageViewHeader)
    ImageView ivFoto;
    @BindView(R.id.imageViewEdit)
    ImageView icon_edit;
    @BindView(R.id.imageViewDelete)
    ImageView icon_delete;
    Inventariable select;
    TabItem tab_info;
    TabItem tab_tickets;
    @BindView(android.R.id.content)
    View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_inventariable_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        tab_info = findViewById(R.id.tabItem_info);
        tab_tickets = findViewById(R.id.tabItem_tickets);


        service = ApiSAT.createServicePeticiones(ServicePeticiones.class, Constants.TOKEN_PROVISIONAL);

        Call<Inventariable> call = service.getInventariableById(idInventariable);
        call.enqueue(new Callback<Inventariable>() {
            @Override
            public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                if (response.isSuccessful()) {
                    select = response.body();

                    LocalDate fechaCreacion = LocalDate.parse(select.getCreatedAt().substring(0, 9));
                    LocalDate fechaCambio = LocalDate.parse(select.getUpdatedAt().substring(0, 9));

                    tvNombre.setText(select.getNombre());
                    tvCodigo.setText(select.getCodigo());
                    tvTipo.setText(select.getTipo());
                    tvCreado.setText(fechaCreacion.toString("dd/MM/yyyy"));
                    tvModificado.setText(fechaCambio.toString("dd/MM/yyyy"));
                    tvDescripcion.setText(select.getDescripcion());

                } else {
                    Toast.makeText(InventariableDetailActivity.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Inventariable> call, Throwable t) {
                Toast.makeText(InventariableDetailActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });

        //TODO cargar imagen dispositivo


        icon_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InventariableDetailActivity.this);
                builder.setIcon(R.drawable.ic_warning);
                builder.setTitle("¿Está seguro de querer borrar este dispositivo?");
                builder.setMessage("Una vez borrado no se podrá deshacer esta decisión");
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       Call<Inventariable> call = service.deleteInventariable(idInventariable);
                       call.enqueue(new Callback<Inventariable>() {
                           @Override
                           public void onResponse(Call<Inventariable> call, Response<Inventariable> response) {
                               if(response.isSuccessful()){
                                   Snackbar.make(parent_view, "Dispositivo eliminado", Snackbar.LENGTH_SHORT).show();
                               }else{
                                   Snackbar.make(parent_view, "Algo ha salido mal", Snackbar.LENGTH_SHORT).show();
                               }
                               Intent intent = new Intent(InventariableDetailActivity.this, MainActivity.class);
                               startActivity(intent);
                           }

                           @Override
                           public void onFailure(Call<Inventariable> call, Throwable t) {
                               Snackbar.make(parent_view, "Error de conexión", Snackbar.LENGTH_SHORT).show();
                           }
                       });
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });

        icon_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new InventariableDialogFragment(idInventariable);
                dialog.show(getSupportFragmentManager(), "InventariableDialogFragment");
            }
        });

        //TODO intentar arreglar
        /*tab_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNombre.setVisibility(View.INVISIBLE);
                ivFoto.setVisibility(View.INVISIBLE);
            }
        });

        tab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNombre.setVisibility(View.VISIBLE);
                ivFoto.setVisibility(View.VISIBLE);
            }
        });*/

    }

}
