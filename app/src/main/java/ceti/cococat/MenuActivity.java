package ceti.cococat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import app.com.gatococo.R;

public class MenuActivity extends AppCompatActivity {

    private ListView lstMenu;
    private String menu[] = new String[]{"Iniciar Partida", "Ver Partidas","Salir"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        lstMenu=(ListView)findViewById(R.id.lstMenu);
        ArrayAdapter adapter= new ArrayAdapter(MenuActivity.this, android.R.layout.simple_list_item_1, menu);
        lstMenu.setAdapter(adapter);

        lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    mensajeServer().show();
                }
                if (position == 1) {
                    //Intent i = new Intent(MenuActivity.this,ScoreActivity.class);
                    //startActivity(i);
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
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(MenuActivity.this,GatoActivity.class);
                        i.putExtra("isServer", false);
                        startActivity(i);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}


