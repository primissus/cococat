package ceti.cococat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import app.com.gatococo.R;

public class MenuActivity extends AppCompatActivity {

    private ListView lstMenu;
    private EditText txtNombre;
    private String menu[] = new String[]{"Iniciar Partida", "Ver Partidas","Salir"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        txtNombre = (EditText)findViewById(R.id.txtNombre);
        lstMenu=(ListView)findViewById(R.id.lstMenu);
        ArrayAdapter adapter= new ArrayAdapter(MenuActivity.this, android.R.layout.simple_list_item_1, menu);
        lstMenu.setAdapter(adapter);

        lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    if(txtNombre.getText().toString().isEmpty())
                        Toast.makeText(MenuActivity.this, "Ingrese su nombre", Toast.LENGTH_SHORT).show();
                    else
                        mensajeServer().show();
                }
                if (position == 1) {
                    Intent i = new Intent(MenuActivity.this,ScoreActivity.class);
                    startActivity(i);
                    finish();
                }
                if (position == 2) {
                    finish();
                }
            }
        });
        registerForContextMenu(lstMenu);

    }

    public Dialog mensajeServer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Eres el servidor?")
                .setPositiveButton("Claro", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(MenuActivity.this,GatoActivity.class);
                        i.putExtra("isServer", true);
                        i.putExtra("nombre",txtNombre.getText().toString());
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(MenuActivity.this,GatoActivity.class);
                        i.putExtra("isServer", false);
                        i.putExtra("nombre",txtNombre.getText().toString());
                        startActivity(i);
                        finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}


