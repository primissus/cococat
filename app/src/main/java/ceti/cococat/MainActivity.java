package ceti.cococat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import app.com.gatococo.R;

public class MainActivity extends AppCompatActivity {

    private Button btnCrear;
    private Button btnUnir;
    private Button btnScores;
    private Button btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnUnir = (Button) findViewById(R.id.btnUnir);
        btnScores = (Button) findViewById(R.id.btnScores);
        btnSalir = (Button) findViewById(R.id.btnSalir);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TicTacToeActivity.class);
                i.putExtra("isServer", true);
                startActivity(i);
                finish();
            }
        });

        btnUnir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TicTacToeActivity.class);
                i.putExtra("isServer", false);
                startActivity(i);
                finish();
            }
        });

        btnScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ScoreActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}


