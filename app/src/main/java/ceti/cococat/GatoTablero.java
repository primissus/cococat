package ceti.cococat;

/**
 * Created by Administrador on 16/06/2016.
 */
public class GatoTablero {

    private char[][] tablero;
    private int x,y;
    private int status;
    private int winstatus;
    private char turno;
    private int conteoRestart;
    private int statusWin;

    public GatoTablero(){
        conteoRestart=0;
        //lenamos el tablero con z's
        x=0;
        y=0;
        winstatus = 0;
        tablero = new char[3][3];
        while(x!=3){
            while(y!=3){
                tablero[x][y]='z';
                y++;
            }
        y=0;
        x++;
        }
    }

    public int setX(int x, int y){
        turno = 'x';
        if(tablero[x][y] == 'z'){
            tablero[x][y] = 'x';
            status=0;
            statusWin = checkWin(turno);
            if(statusWin!=2){
                status = statusWin;
            }
            else {
                status = 4;
            }
        }
        else{
            status=1;
        }
        return status;
    }


    public int setO(int x, int y){
        turno = 'o';
        if(tablero[x][y] == 'z'){
            tablero[x][y] = 'o';
            status=0;
            statusWin = checkWin(turno);
            if(statusWin!=2){
                status = statusWin;
            }
            else {
                status = 4;
            }
        }
        else{
            status=1;
        }
        return status;
    }

    public int checkWin(char turno){
        conteoRestart++;
        if(tablero[0][0] == turno && tablero[1][0] == turno && tablero[2][0] == turno){
            winstatus = 10;
        }
        else if(tablero[0][1] == turno && tablero[1][1] == turno && tablero[2][1] == turno){
            winstatus = 11;
        }
        else if(tablero[0][2] == turno && tablero[1][2] == turno && tablero[2][2] == turno){
            winstatus = 12;
        }

        else if(tablero[0][0] == turno && tablero[0][1] == turno && tablero[0][2] == turno){
            winstatus = 13;
        }
        else if(tablero[1][0] == turno && tablero[1][1] == turno && tablero[1][2] == turno){
            winstatus = 14;
        }
        else if(tablero[2][0] == turno && tablero[2][1] == turno && tablero[2][2] == turno){
            winstatus = 15;
        }

        else if(tablero[0][0] == turno && tablero[1][1] == turno && tablero[2][2] == turno){
            winstatus = 16;
        }
        else if(tablero[0][2] == turno && tablero[1][1] == turno && tablero[2][0] == turno){
            winstatus = 17;
        }
        else if(conteoRestart==9){
            winstatus = 2;
        }
        return winstatus;
    }

    public void restart(){
        //lenamos el tablero con z's
        conteoRestart=0;
        x=0;
        y=0;
        winstatus = 0;
        tablero = new char[3][3];
        while(x!=3){
            while(y!=3){
                tablero[x][y]='z';
                y++;
            }
            y=0;
            x++;
        }
    }
}
