package ceti.cococat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

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
        turn = false;
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
        clear();

        disableButtons();
    }

    public void init(){
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

    @Override
    public void onClick(View v) {
        //Obtiene filas y columnas
        int[] coords = getCoords(v);
        int row = coords[0];
        int col = coords[1];

        if(turn && tablero[row][col]==0) {
            tablero[row][col] = 1;
            buttons[row][col].setText("X");
            buttons[row][col].setTextColor(Color.RED);
            turn = false;
            sendCoordinates(row,col);
            if(checkTablero(row,col)) {
                Toast.makeText(TicTacToeActivity.this, "Ganaste", Toast.LENGTH_SHORT).show();
                disableButtons();
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        enableButtons();
                        clear();
                        turn = false;
                        if(isServer) {
                            setTurn();
                            sendTurn(!turn);
                        }
                        addStatusPartida(1);
                    }
                };
                timer.schedule(task, 4000);
            }
            if(checkTie()){
                Toast.makeText(TicTacToeActivity.this, "Empate", Toast.LENGTH_SHORT).show();
                disableButtons();
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        enableButtons();
                        clear();
                        turn = false;
                        if(isServer) {
                            setTurn();
                            sendTurn(!turn);
                        }
                        addStatusPartida(1);
                    }
                };
                timer.schedule(task, 4000);
            }
        }
    }

    private int[] getCoords(View v){
        for(int i=0;i<3;i++)
            for(int j=0; j<3; j++)
                if(v == buttons[i][j]){
                    int[] coords = new int[2];
                    coords[0] = i;
                    coords[1] = j;
                    return coords;
                }
        return null;
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

    private void oponent(final int row, final int col){
        tablero[row][col] = 2;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttons[row][col].setText("O");
                buttons[row][col].setTextColor(Color.BLUE);
            }
        });
        turn = true;
        if(checkTablero(row,col)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TicTacToeActivity.this, "Perdiste", Toast.LENGTH_SHORT).show();
                }
            });
            disableButtons();
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    enableButtons();
                    clear();
                    turn = false;
                    if(isServer) {
                        setTurn();
                        sendTurn(!turn);
                    }
                    addStatusPartida(1);
                }
            };
            timer.schedule(task, 4000);
        }
        if(checkTie()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TicTacToeActivity.this, "Empate", Toast.LENGTH_SHORT).show();
                }
            });
            disableButtons();
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    enableButtons();
                    clear();
                    turn = false;
                    if(isServer) {
                        setTurn();
                        sendTurn(!turn);
                    }
                    addStatusPartida(1);
                }
            };
            timer.schedule(task, 4000);
        }
    }

    public void dataReceived(JSONObject json){
        try {
            switch (json.getInt("code")) {
                case 1:     //turno
                    turn = json.getBoolean("turn");
                    break;
                case 2:
                    int row = json.getInt("row");
                    int col = json.getInt("col");
                    oponent(row,col);
                    break;
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void sendCoordinates(int row, int col){
        JSONObject json = new JSONObject();
        try{
            json.put("code",2);
            json.put("row",row);
            json.put("col",col);
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

    public void enableButtons(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<3 ; i++)
                    for(int j=0; j<3; j++)
                        buttons[i][j].setEnabled(true);
            }
        });
    }

    private boolean checkTablero(int row, int col){
        if(tablero[row][0] == tablero[row][1] && tablero[row][0] == tablero[row][2]){//Filas
            illuminateButton(buttons[row][0]);
            illuminateButton(buttons[row][1]);
            illuminateButton(buttons[row][2]);
            return true;
        }
        if(tablero[0][col] == tablero[1][col] && tablero[0][col] == tablero[2][col]){//Columnas
            illuminateButton(buttons[0][col]);
            illuminateButton(buttons[1][col]);
            illuminateButton(buttons[2][col]);
            return true;
        }
        if(tablero[0][0]!=0 && tablero[0][0] == tablero[1][1] && tablero[0][0] == tablero[2][2]){//Diagonal 1
            illuminateButton(buttons[0][0]);
            illuminateButton(buttons[1][1]);
            illuminateButton(buttons[2][2]);
            return true;
        }
        if(tablero[0][2]!=0 && tablero[0][2] == tablero[1][1] && tablero[0][2] == tablero[2][0]){//Diagonal 2
            illuminateButton(buttons[0][2]);
            illuminateButton(buttons[1][1]);
            illuminateButton(buttons[2][0]);
            return true;
        } 
        return false;
    }

    private boolean checkTie(){
        int count = 0;
        for(int i=0;i<3;i++){
            for(int j=0; j<3; j++){
                if(tablero[i][j]!=0)
                    count++;
            }
        }
        if(count==9)
            return true;
        return false;
    }

    private void illuminateButton(final Button b){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                b.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
            }
        });
    }

    private void clear(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<3;i++){
                    for(int j=0; j<3; j++){
                        buttons[i][j].setText(".");
                        buttons[i][j].setTextColor(Color.DKGRAY);
                        buttons[i][j].getBackground().setColorFilter(null);
                        tablero[i][j] = 0;
                    }
                }
            }
        });
    }
}
