package ceti.cococat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

public class GatoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton ibBoton1;
    private ImageButton ibBoton2;
    private ImageButton ibBoton3;
    private ImageButton ibBoton4;
    private ImageButton ibBoton5;
    private ImageButton ibBoton6;
    private ImageButton ibBoton7;
    private ImageButton ibBoton8;
    private ImageButton ibBoton9;

    private GatoTablero tablero;
    private boolean turno;

    private boolean isServer;
    private Client client;
    private Server server;
    private boolean iniciar;

    private View coordinatorLayout;

    private String nombre;
    private String nombreOponente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isServer = getIntent().getBooleanExtra("isServer",false);
        nombre = getIntent().getStringExtra("nombre");
        if(isServer){
            server = new Server(this);
            server.setRunning(true);
            server.start();
        }
        else{
            client = new Client(this);
            client.setRunning(true);
            client.start();
        }
        iniciar=false;
        while(!iniciar);
        setContentView(app.com.gatococo.R.layout.activity_gato);



        ibBoton1 = (ImageButton) findViewById(app.com.gatococo.R.id.boton1);
        ibBoton2 = (ImageButton) findViewById(app.com.gatococo.R.id.boton2);
        ibBoton3 = (ImageButton) findViewById(app.com.gatococo.R.id.boton3);
        ibBoton4 = (ImageButton) findViewById(app.com.gatococo.R.id.boton4);
        ibBoton5 = (ImageButton) findViewById(app.com.gatococo.R.id.boton5);
        ibBoton6 = (ImageButton) findViewById(app.com.gatococo.R.id.boton6);
        ibBoton7 = (ImageButton) findViewById(app.com.gatococo.R.id.boton7);
        ibBoton8 = (ImageButton) findViewById(app.com.gatococo.R.id.boton8);
        ibBoton9 = (ImageButton) findViewById(app.com.gatococo.R.id.boton9);

        ibBoton1.setOnClickListener(this);
        ibBoton2.setOnClickListener(this);
        ibBoton3.setOnClickListener(this);
        ibBoton4.setOnClickListener(this);
        ibBoton5.setOnClickListener(this);
        ibBoton6.setOnClickListener(this);
        ibBoton7.setOnClickListener(this);
        ibBoton8.setOnClickListener(this);
        ibBoton9.setOnClickListener(this);

        tablero = new GatoTablero();

        sendNombre();

        turno = false;
        if(isServer) {
            setTurn();
            sendTurn(!turno);
        }

        coordinatorLayout = findViewById(app.com.gatococo.R.id.snackbarPosition);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isServer){
            server.setRunning(false);
            server.close();
        }
        else{
            client.setRunning(false);
            client.close();
        }
    }

    private void setTurn(){
        Random rnd = new Random();
        turno = rnd.nextBoolean();
    }

    @Override
    public void onClick(View v) {
        int status = -1;

        //Obtiene filas y columnas
        int[] xy = checkPressed(v);
        int x = xy[0];
        int y = xy[1];

        if(turno) { // o
            status = tablero.setO(x,y);
            checkStatus(status, x, y);
            sendCoordinates(x,y);
        }
//        else{ // x
//            status = tablero.setX(x,y);
//        }

    }

    private View getVista(int x, int y){
        if (x == 0 && y == 0) {
            return ibBoton1;
        } else if (x == 1 && y == 0) {
            return ibBoton2;
        } else if (x == 2 && y == 0) {
            return ibBoton3;
        } else if (x == 0 && y == 1) {
            return ibBoton4;
        } else if (x == 1 && y == 1) {
            return ibBoton5;
        } else if (x == 2 && y == 1) {
            return ibBoton6;
        } else if (x == 0) {
            return ibBoton7;
        } else if (x == 1) {
            return ibBoton8;
        } else if (x == 2) {
            return ibBoton9;
        }
        return null;
    }

    private int[] checkPressed(View v){
        int x=-1,y=-1;
        if (v == ibBoton1) {
            x=0;
            y=0;
        } else if (v == ibBoton2) {
            x=1;
            y=0;
        } else if (v == ibBoton3) {
            x=2;
            y=0;
        } else if (v == ibBoton4) {
            x=0;
            y=1;
        } else if (v == ibBoton5) {
            x=1;
            y=1;
        } else if (v == ibBoton6) {
            x=2;
            y=1;
        } else if (v == ibBoton7) {
            x=0;
            y=2;
        } else if (v == ibBoton8) {
            x=1;
            y=2;
        } else if (v == ibBoton9) {
            x=2;
            y=2;
        }
        int[] xy = new int[2];
        xy[0] = x;
        xy[1] = y;
        return xy;
    }

    public void addStatusPartida(int status){
        File file = new File(getFileStreamPath("partidas.xml").getPath());
        if(file.exists()){
            XMLParser xml = new XMLParser(readFromFile());
            xml.addPartida(status);
            writeToFile(xml.getDocumentString());
        }
        else{
            XMLParser xml = new XMLParser();
            xml.createPartidas(status);
            writeToFile(xml.getDocumentString());
        }
        //XMLParser xml = new
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("partidas.xml", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING));
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
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

    private void checkStatus(int status, int x, int y){
        if(status == 0){ //no paso nada
            pintar(x,y,false);
        }
        else if(status>=10 && status<=17){ //alguien ya gano
            pintarWinningLine(status);
            pintar(x,y,false);
            disableBotones();
            turno = !turno; //el pintar me cambia el estado
            addStatusPartida(((turno)?1:2));
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Gano: " + (turno ? nombre : nombreOponente) ,
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Reiniciar", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pintar(0,0,true);
                    tablero.restart();
                    snackbar.dismiss();
                    enableBotones();
                }
            });

            snackbar.show();
        }
        else if(status == 4){ //nadie gano entonces se reinicia
            disableBotones();
            addStatusPartida(0);
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Nadie gano, quieres reiniciar?" ,
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Si", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pintar(0,0,true);
                    tablero.restart();
                    snackbar.dismiss();
                    enableBotones();
                }
            });

            snackbar.show();

        }
    }

    private void jugadaOponente(int x, int y){
        View v = getVista(x,y);
        int status = tablero.setO(x,y);
        checkStatus(status, x, y);
    }

    public void dataReceived(JSONObject json){
        try {
            switch (json.getInt("code")) {
                case 1:     //turno
                    turno = json.getBoolean("turn");
                    break;
                case 2:
                    int x = json.getInt("x");
                    int y = json.getInt("y");
                    jugadaOponente(x,y);
                    break;
                case 3:
                    nombreOponente = json.getString("nombre");
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void sendCoordinates(int x, int y){
        JSONObject json = new JSONObject();
        try{
            json.put("code",2);
            json.put("x",x);
            json.put("y",y);
            if(isServer) {
                server.sendData(json.toString());
            }
            else{
                client.sendData(json.toString());
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void sendTurn(boolean turn){
        JSONObject json = new JSONObject();
        try{
            json.put("code",1);
            json.put("turn",turn);
            if(isServer) {
                server.sendData(json.toString());
            }
            else{
                client.sendData(json.toString());
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void sendNombre(){
        JSONObject json = new JSONObject();
        try{
            json.put("code",1);
            json.put("nombre",nombre);
            if(isServer) {
                server.sendData(json.toString());
            }
            else{
                client.sendData(json.toString());
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void disableBotones(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ibBoton1.setEnabled(false);
                ibBoton2.setEnabled(false);
                ibBoton3.setEnabled(false);
                ibBoton4.setEnabled(false);
                ibBoton5.setEnabled(false);
                ibBoton6.setEnabled(false);
                ibBoton7.setEnabled(false);
                ibBoton8.setEnabled(false);
                ibBoton9.setEnabled(false);
            }
        });
    }

    public void enableBotones(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ibBoton1.setEnabled(true);
                ibBoton2.setEnabled(true);
                ibBoton3.setEnabled(true);
                ibBoton4.setEnabled(true);
                ibBoton5.setEnabled(true);
                ibBoton6.setEnabled(true);
                ibBoton7.setEnabled(true);
                ibBoton8.setEnabled(true);
                ibBoton9.setEnabled(true);
            }
        });
    }

    public void pintar(final int x, final int y, final boolean restart){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int color;
                if(restart){
                    color = getResources().getColor(app.com.gatococo.R.color.nostatus);
                    ibBoton1.setImageResource(app.com.gatococo.R.drawable.nostatus);
                    ibBoton2.setImageResource(app.com.gatococo.R.drawable.nostatus);
                    ibBoton3.setImageResource(app.com.gatococo.R.drawable.nostatus);
                    ibBoton4.setImageResource(app.com.gatococo.R.drawable.nostatus);
                    ibBoton5.setImageResource(app.com.gatococo.R.drawable.nostatus);
                    ibBoton6.setImageResource(app.com.gatococo.R.drawable.nostatus);
                    ibBoton7.setImageResource(app.com.gatococo.R.drawable.nostatus);
                    ibBoton8.setImageResource(app.com.gatococo.R.drawable.nostatus);
                    ibBoton9.setImageResource(app.com.gatococo.R.drawable.nostatus);

                    ibBoton1.setColorFilter(color);
                    ibBoton2.setColorFilter(color);
                    ibBoton3.setColorFilter(color);
                    ibBoton4.setColorFilter(color);
                    ibBoton5.setColorFilter(color);
                    ibBoton6.setColorFilter(color);
                    ibBoton7.setColorFilter(color);
                    ibBoton8.setColorFilter(color);
                    ibBoton9.setColorFilter(color);

                    ibBoton1.setBackgroundResource(android.R.drawable.btn_default);
                    ibBoton2.setBackgroundResource(android.R.drawable.btn_default);
                    ibBoton3.setBackgroundResource(android.R.drawable.btn_default);
                    ibBoton4.setBackgroundResource(android.R.drawable.btn_default);
                    ibBoton5.setBackgroundResource(android.R.drawable.btn_default);
                    ibBoton6.setBackgroundResource(android.R.drawable.btn_default);
                    ibBoton7.setBackgroundResource(android.R.drawable.btn_default);
                    ibBoton8.setBackgroundResource(android.R.drawable.btn_default);
                    ibBoton9.setBackgroundResource(android.R.drawable.btn_default);

                }
                else {

                    if (turno) { //o
                        color = getResources().getColor(app.com.gatococo.R.color.azul);
                        if (x == 0 && y == 0) {
                            ibBoton1.setImageResource(app.com.gatococo.R.drawable.o);
                            ibBoton1.setColorFilter(color);
                        } else if (x == 1 && y == 0) {
                            ibBoton2.setImageResource(app.com.gatococo.R.drawable.o);
                            ibBoton2.setColorFilter(color);
                        } else if (x == 2 && y == 0) {
                            ibBoton3.setImageResource(app.com.gatococo.R.drawable.o);
                            ibBoton3.setColorFilter(color);
                        } else if (x == 0 && y == 1) {
                            ibBoton4.setImageResource(app.com.gatococo.R.drawable.o);
                            ibBoton4.setColorFilter(color);
                        } else if (x == 1 && y == 1) {
                            ibBoton5.setImageResource(app.com.gatococo.R.drawable.o);
                            ibBoton5.setColorFilter(color);
                        } else if (x == 2 && y == 1) {
                            ibBoton6.setImageResource(app.com.gatococo.R.drawable.o);
                            ibBoton6.setColorFilter(color);
                        } else if (x == 0) {
                            ibBoton7.setImageResource(app.com.gatococo.R.drawable.o);
                            ibBoton7.setColorFilter(color);
                        } else if (x == 1) {
                            ibBoton8.setImageResource(app.com.gatococo.R.drawable.o);
                            ibBoton8.setColorFilter(color);
                        } else if (x == 2) {
                            ibBoton9.setImageResource(app.com.gatococo.R.drawable.o);
                            ibBoton9.setColorFilter(color);
                        }
                    } else {
                        color = getResources().getColor(app.com.gatococo.R.color.rojo);
                        if (x == 0 && y == 0) {
                            ibBoton1.setImageResource(app.com.gatococo.R.drawable.x);
                            ibBoton1.setColorFilter(color);
                        } else if (x == 1 && y == 0) {
                            ibBoton2.setImageResource(app.com.gatococo.R.drawable.x);
                            ibBoton2.setColorFilter(color);
                        } else if (x == 2 && y == 0) {
                            ibBoton3.setImageResource(app.com.gatococo.R.drawable.x);
                            ibBoton3.setColorFilter(color);
                        } else if (x == 0 && y == 1) {
                            ibBoton4.setImageResource(app.com.gatococo.R.drawable.x);
                            ibBoton4.setColorFilter(color);
                        } else if (x == 1 && y == 1) {
                            ibBoton5.setImageResource(app.com.gatococo.R.drawable.x);
                            ibBoton5.setColorFilter(color);
                        } else if (x == 2 && y == 1) {
                            ibBoton6.setImageResource(app.com.gatococo.R.drawable.x);
                            ibBoton6.setColorFilter(color);
                        } else if (x == 0) {
                            ibBoton7.setImageResource(app.com.gatococo.R.drawable.x);
                            ibBoton7.setColorFilter(color);
                        } else if (x == 1) {
                            ibBoton8.setImageResource(app.com.gatococo.R.drawable.x);
                            ibBoton8.setColorFilter(color);
                        } else if (x == 2) {
                            ibBoton9.setImageResource(app.com.gatococo.R.drawable.x);
                            ibBoton9.setColorFilter(color);
                        }
                    }

                    turno = !turno; // solamente si se pudo hacer la jugada se cambia el turno
                }
            }
        });

    }

    public void pintarWinningLine(final int line){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int color = getResources().getColor(app.com.gatococo.R.color.winningcolor);
                if(line == 10){
                    ibBoton1.setBackgroundColor(color);
                    ibBoton2.setBackgroundColor(color);
                    ibBoton3.setBackgroundColor(color);
                }
                else if(line == 11){
                    ibBoton4.setBackgroundColor(color);
                    ibBoton5.setBackgroundColor(color);
                    ibBoton6.setBackgroundColor(color);
                }
                else if(line == 12){
                    ibBoton7.setBackgroundColor(color);
                    ibBoton8.setBackgroundColor(color);
                    ibBoton9.setBackgroundColor(color);
                }
                else if(line == 13){
                    ibBoton1.setBackgroundColor(color);
                    ibBoton4.setBackgroundColor(color);
                    ibBoton7.setBackgroundColor(color);
                }
                else if(line == 14){
                    ibBoton2.setBackgroundColor(color);
                    ibBoton5.setBackgroundColor(color);
                    ibBoton8.setBackgroundColor(color);
                }
                else if(line == 15){
                    ibBoton3.setBackgroundColor(color);
                    ibBoton6.setBackgroundColor(color);
                    ibBoton9.setBackgroundColor(color);
                }
                else if(line == 16){
                    ibBoton1.setBackgroundColor(color);
                    ibBoton5.setBackgroundColor(color);
                    ibBoton9.setBackgroundColor(color);
                }
                else if(line == 17){
                    ibBoton3.setBackgroundColor(color);
                    ibBoton5.setBackgroundColor(color);
                    ibBoton7.setBackgroundColor(color);
                }
            }
        });
    }

    public void setIniciar(boolean iniciar) {
        this.iniciar = iniciar;
    }
}
