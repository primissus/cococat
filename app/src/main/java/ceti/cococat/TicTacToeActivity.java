package ceti.cococat;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

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

import app.com.gatococo.R;

public class TicTacToeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons;
    private int[][] tablero;

    private boolean turn;

    private boolean isServer;
    private ClientThread clientThread;
    private ServerThread serverThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isServer = getIntent().getBooleanExtra("isServer",false);
        if(isServer){
            serverThread = new ServerThread(this);
            serverThread.setRunning(true);
            serverThread.start();
        }
        else{
            clientThread = new ClientThread(this);
            clientThread.setRunning(true);
            clientThread.start();
        }
        setContentView(app.com.gatococo.R.layout.activity_tic_tac_toe);

        tablero = new int[3][3];
        buttons = new Button[3][3];
        LinearLayout container = (LinearLayout)findViewById(R.id.container);
        for(int i=0;i<3;i++){
            LinearLayout subContainer = (LinearLayout) container.getChildAt(i);
            for(int j=0; j<3; j++){
                Button button = (Button) subContainer.getChildAt(j);
                buttons[i][j] = button;
                button.setOnClickListener(this);
            }
        }
        for(int i=0;i<3;i++)
            for(int j=0; j<3; j++)

        turn = false;
        if(isServer) {
            setTurn();
            sendTurn(!turn);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isServer){
            serverThread.setRunning(false);
            serverThread.close();
        }
        else{
            clientThread.setRunning(false);
            clientThread.close();
        }
    }

    private void setTurn(){
        Random rnd = new Random();
        turn = rnd.nextBoolean();
    }
//
//    @Override
//    public void onClick(View v) {
//        int status = -1;
//
//        //Obtiene filas y columnas
//        int[] xy = checkPressed(v);
//        int x = xy[0];
//        int y = xy[1];
//
//        if(turno) { // o
//            status = tablero.setO(x,y);
//            checkStatus(status, x, y);
//            sendCoordinates(x,y);
//        }
////        else{ // x
////            status = tablero.setX(x,y);
////        }
//
//    }
//
//    private View getVista(int x, int y){
//        if (x == 0 && y == 0) {
//            return ibBoton1;
//        } else if (x == 1 && y == 0) {
//            return ibBoton2;
//        } else if (x == 2 && y == 0) {
//            return ibBoton3;
//        } else if (x == 0 && y == 1) {
//            return ibBoton4;
//        } else if (x == 1 && y == 1) {
//            return ibBoton5;
//        } else if (x == 2 && y == 1) {
//            return ibBoton6;
//        } else if (x == 0) {
//            return ibBoton7;
//        } else if (x == 1) {
//            return ibBoton8;
//        } else if (x == 2) {
//            return ibBoton9;
//        }
//        return null;
//    }
//
//    private int[] checkPressed(View v){
//        int x=-1,y=-1;
//        if (v == ibBoton1) {
//            x=0;
//            y=0;
//        } else if (v == ibBoton2) {
//            x=1;
//            y=0;
//        } else if (v == ibBoton3) {
//            x=2;
//            y=0;
//        } else if (v == ibBoton4) {
//            x=0;
//            y=1;
//        } else if (v == ibBoton5) {
//            x=1;
//            y=1;
//        } else if (v == ibBoton6) {
//            x=2;
//            y=1;
//        } else if (v == ibBoton7) {
//            x=0;
//            y=2;
//        } else if (v == ibBoton8) {
//            x=1;
//            y=2;
//        } else if (v == ibBoton9) {
//            x=2;
//            y=2;
//        }
//        int[] xy = new int[2];
//        xy[0] = x;
//        xy[1] = y;
//        return xy;
//    }

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
//
//    private void checkStatus(int status, int x, int y){
//        if(status == 0){ //no paso nada
//            pintar(x,y,false);
//        }
//        else if(status>=10 && status<=17){ //alguien ya gano
//            pintarWinningLine(status);
//            pintar(x,y,false);
//            disableBotones();
//            turno = !turno; //el pintar me cambia el estado
//            addStatusPartida(((turno)?1:2));
//            final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Gano: " + (turno ? nombre : nombreOponente) ,
//                    Snackbar.LENGTH_INDEFINITE);
//            snackbar.setAction("Reiniciar", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    pintar(0,0,true);
//                    tablero.restart();
//                    snackbar.dismiss();
//                    enableBotones();
//                }
//            });
//
//            snackbar.show();
//        }
//        else if(status == 4){ //nadie gano entonces se reinicia
//            disableBotones();
//            addStatusPartida(0);
//            final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Nadie gano, quieres reiniciar?" ,
//                    Snackbar.LENGTH_INDEFINITE);
//            snackbar.setAction("Si", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    pintar(0,0,true);
//                    tablero.restart();
//                    snackbar.dismiss();
//                    enableBotones();
//                }
//            });
//
//            snackbar.show();
//
//        }
//    }
//
//    private void jugadaOponente(int x, int y){
//        View v = getVista(x,y);
//        int status = tablero.setO(x,y);
//        checkStatus(status, x, y);
//    }
//
    public void dataReceived(JSONObject json){
        try {
            switch (json.getInt("code")) {
                case 1:     //turno
                    turn = json.getBoolean("turn");
                    break;
                case 2:
                    int x = json.getInt("x");
                    int y = json.getInt("y");
                    //jugadaOponente(x,y);
                    break;
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
                serverThread.sendData(json.toString());
            }
            else{
                clientThread.sendData(json.toString());
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
                serverThread.sendData(json.toString());
            }
            else{
                clientThread.sendData(json.toString());
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void disableButtons(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<3 ; i++)
                    for(int j=0; j<3; j++)
                        buttons[i][j].setEnabled(false);
            }
        });
    }

    public void enableBotones(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<3 ; i++)
                    for(int j=0; j<3; j++)
                        buttons[i][j].setEnabled(true);
            }
        });
    }
//
//    public void pintar(final int x, final int y, final boolean restart){
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                int color;
//                if(restart){
//                    color = getResources().getColor(app.com.gatococo.R.color.nostatus);
//                    ibBoton1.setImageResource(app.com.gatococo.R.drawable.nostatus);
//                    ibBoton2.setImageResource(app.com.gatococo.R.drawable.nostatus);
//                    ibBoton3.setImageResource(app.com.gatococo.R.drawable.nostatus);
//                    ibBoton4.setImageResource(app.com.gatococo.R.drawable.nostatus);
//                    ibBoton5.setImageResource(app.com.gatococo.R.drawable.nostatus);
//                    ibBoton6.setImageResource(app.com.gatococo.R.drawable.nostatus);
//                    ibBoton7.setImageResource(app.com.gatococo.R.drawable.nostatus);
//                    ibBoton8.setImageResource(app.com.gatococo.R.drawable.nostatus);
//                    ibBoton9.setImageResource(app.com.gatococo.R.drawable.nostatus);
//
//                    ibBoton1.setColorFilter(color);
//                    ibBoton2.setColorFilter(color);
//                    ibBoton3.setColorFilter(color);
//                    ibBoton4.setColorFilter(color);
//                    ibBoton5.setColorFilter(color);
//                    ibBoton6.setColorFilter(color);
//                    ibBoton7.setColorFilter(color);
//                    ibBoton8.setColorFilter(color);
//                    ibBoton9.setColorFilter(color);
//
//                    ibBoton1.setBackgroundResource(android.R.drawable.btn_default);
//                    ibBoton2.setBackgroundResource(android.R.drawable.btn_default);
//                    ibBoton3.setBackgroundResource(android.R.drawable.btn_default);
//                    ibBoton4.setBackgroundResource(android.R.drawable.btn_default);
//                    ibBoton5.setBackgroundResource(android.R.drawable.btn_default);
//                    ibBoton6.setBackgroundResource(android.R.drawable.btn_default);
//                    ibBoton7.setBackgroundResource(android.R.drawable.btn_default);
//                    ibBoton8.setBackgroundResource(android.R.drawable.btn_default);
//                    ibBoton9.setBackgroundResource(android.R.drawable.btn_default);
//
//                }
//                else {
//
//                    if (turno) { //o
//                        color = getResources().getColor(app.com.gatococo.R.color.azul);
//                        if (x == 0 && y == 0) {
//                            ibBoton1.setImageResource(app.com.gatococo.R.drawable.o);
//                            ibBoton1.setColorFilter(color);
//                        } else if (x == 1 && y == 0) {
//                            ibBoton2.setImageResource(app.com.gatococo.R.drawable.o);
//                            ibBoton2.setColorFilter(color);
//                        } else if (x == 2 && y == 0) {
//                            ibBoton3.setImageResource(app.com.gatococo.R.drawable.o);
//                            ibBoton3.setColorFilter(color);
//                        } else if (x == 0 && y == 1) {
//                            ibBoton4.setImageResource(app.com.gatococo.R.drawable.o);
//                            ibBoton4.setColorFilter(color);
//                        } else if (x == 1 && y == 1) {
//                            ibBoton5.setImageResource(app.com.gatococo.R.drawable.o);
//                            ibBoton5.setColorFilter(color);
//                        } else if (x == 2 && y == 1) {
//                            ibBoton6.setImageResource(app.com.gatococo.R.drawable.o);
//                            ibBoton6.setColorFilter(color);
//                        } else if (x == 0) {
//                            ibBoton7.setImageResource(app.com.gatococo.R.drawable.o);
//                            ibBoton7.setColorFilter(color);
//                        } else if (x == 1) {
//                            ibBoton8.setImageResource(app.com.gatococo.R.drawable.o);
//                            ibBoton8.setColorFilter(color);
//                        } else if (x == 2) {
//                            ibBoton9.setImageResource(app.com.gatococo.R.drawable.o);
//                            ibBoton9.setColorFilter(color);
//                        }
//                    } else {
//                        color = getResources().getColor(app.com.gatococo.R.color.rojo);
//                        if (x == 0 && y == 0) {
//                            ibBoton1.setImageResource(app.com.gatococo.R.drawable.x);
//                            ibBoton1.setColorFilter(color);
//                        } else if (x == 1 && y == 0) {
//                            ibBoton2.setImageResource(app.com.gatococo.R.drawable.x);
//                            ibBoton2.setColorFilter(color);
//                        } else if (x == 2 && y == 0) {
//                            ibBoton3.setImageResource(app.com.gatococo.R.drawable.x);
//                            ibBoton3.setColorFilter(color);
//                        } else if (x == 0 && y == 1) {
//                            ibBoton4.setImageResource(app.com.gatococo.R.drawable.x);
//                            ibBoton4.setColorFilter(color);
//                        } else if (x == 1 && y == 1) {
//                            ibBoton5.setImageResource(app.com.gatococo.R.drawable.x);
//                            ibBoton5.setColorFilter(color);
//                        } else if (x == 2 && y == 1) {
//                            ibBoton6.setImageResource(app.com.gatococo.R.drawable.x);
//                            ibBoton6.setColorFilter(color);
//                        } else if (x == 0) {
//                            ibBoton7.setImageResource(app.com.gatococo.R.drawable.x);
//                            ibBoton7.setColorFilter(color);
//                        } else if (x == 1) {
//                            ibBoton8.setImageResource(app.com.gatococo.R.drawable.x);
//                            ibBoton8.setColorFilter(color);
//                        } else if (x == 2) {
//                            ibBoton9.setImageResource(app.com.gatococo.R.drawable.x);
//                            ibBoton9.setColorFilter(color);
//                        }
//                    }
//
//                    turno = !turno; // solamente si se pudo hacer la jugada se cambia el turno
//                }
//            }
//        });
//
//    }
//
//    public void pintarWinningLine(final int line){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                int color = getResources().getColor(app.com.gatococo.R.color.winningcolor);
//                if(line == 10){
//                    ibBoton1.setBackgroundColor(color);
//                    ibBoton2.setBackgroundColor(color);
//                    ibBoton3.setBackgroundColor(color);
//                }
//                else if(line == 11){
//                    ibBoton4.setBackgroundColor(color);
//                    ibBoton5.setBackgroundColor(color);
//                    ibBoton6.setBackgroundColor(color);
//                }
//                else if(line == 12){
//                    ibBoton7.setBackgroundColor(color);
//                    ibBoton8.setBackgroundColor(color);
//                    ibBoton9.setBackgroundColor(color);
//                }
//                else if(line == 13){
//                    ibBoton1.setBackgroundColor(color);
//                    ibBoton4.setBackgroundColor(color);
//                    ibBoton7.setBackgroundColor(color);
//                }
//                else if(line == 14){
//                    ibBoton2.setBackgroundColor(color);
//                    ibBoton5.setBackgroundColor(color);
//                    ibBoton8.setBackgroundColor(color);
//                }
//                else if(line == 15){
//                    ibBoton3.setBackgroundColor(color);
//                    ibBoton6.setBackgroundColor(color);
//                    ibBoton9.setBackgroundColor(color);
//                }
//                else if(line == 16){
//                    ibBoton1.setBackgroundColor(color);
//                    ibBoton5.setBackgroundColor(color);
//                    ibBoton9.setBackgroundColor(color);
//                }
//                else if(line == 17){
//                    ibBoton3.setBackgroundColor(color);
//                    ibBoton5.setBackgroundColor(color);
//                    ibBoton7.setBackgroundColor(color);
//                }
//            }
//        });
//    }

    private boolean checkTablero(int x, int y){
        if()
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
