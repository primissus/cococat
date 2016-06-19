package ceti.cococat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import app.com.gatococo.R;

public class ScoreActivity extends AppCompatActivity {

    private TextView txtGanadas;
    private TextView txtPerdidas;
    private TextView txtEmpates;
    private TextView txtNoScores;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_score);

        txtGanadas = (TextView)findViewById(R.id.txtGanadas);
        txtPerdidas = (TextView)findViewById(R.id.txtPerdidas);
        txtEmpates = (TextView)findViewById(R.id.txtEmpates);
        txtNoScores = (TextView)findViewById(R.id.txtNoScores);
        button = (Button)findViewById(R.id.btnScoreExit);

        File file = new File(getFileStreamPath("partidas.xml").getPath());
        if(file.exists()){
            XMLParser xml = new XMLParser(readFromFile());
            int[] partidas = xml.getPartidas();
            int ganadas = 0;
            int perdidas = 0;
            int empates = 0;
            for(int i=0 ; i<partidas.length; i++){
                if(partidas[i]==0)
                    empates++;
                else if(partidas[i]==1)
                    ganadas++;
                else
                    perdidas++;
            }
            txtGanadas.setText(String.valueOf(ganadas));
            txtPerdidas.setText(String.valueOf(perdidas));
            txtEmpates.setText(String.valueOf(empates));
        }
        else{
            txtGanadas.setVisibility(View.GONE);
            txtPerdidas.setVisibility(View.GONE);
            txtEmpates.setVisibility(View.GONE);
            txtNoScores.setVisibility(View.VISIBLE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("partidas.xml");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
