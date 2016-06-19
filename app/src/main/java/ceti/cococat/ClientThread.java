package ceti.cococat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by law on 12/06/16.
 */
public class ClientThread extends Thread {

    private Socket socket;
    private boolean running;
    private TicTacToeActivity ticTacToeActivity;

    public ClientThread(TicTacToeActivity ticTacToeActivity){
        this.ticTacToeActivity = ticTacToeActivity;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("192.168.43.1",8188);
            ticTacToeActivity.setIniciar(true);
            //ticTacToeActivity.dismissProgresDialog();
            while(running){
                String data = receiveData();
                if(data!=null) {
                    try {
                        JSONObject json = new JSONObject(data);
//                        ticTacToeActivity.dataReceived(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String data)
    {
        try {
            DataOutputStream dOut= new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String receiveData( )
    {
        DataInputStream dIn;
        try {
            dIn = new DataInputStream(socket.getInputStream());
            return dIn.readUTF().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
