package ceti.cococat;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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

    private View coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        turno = false;

        coordinatorLayout = findViewById(app.com.gatococo.R.id.snackbarPosition);


    }

    @Override
    public void onClick(View v) {
        int x=-1,y=-1;
        int status;

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

        if(turno) { // o
            status = tablero.setO(x,y);
        }
        else{ // x
            status = tablero.setX(x,y);
        }

        if(status == 0){ //no paso nada
            pintar(x,y,false);
        }
        else if(status == 2){

        }
        else if(status>=10 && status<=17){ //alguien ya gano
            pintarWinningLine(status);
            pintar(x,y,false);
            disableBotones();
            turno = !turno; //el pintar me cambia el estado
            final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Gano: " + (turno ? "o" : "x") ,
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


    public void disableBotones(){
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

    public void enableBotones(){
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

    public void pintar(int x,int y,boolean restart){
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

    public void pintarWinningLine(int line){
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

}
