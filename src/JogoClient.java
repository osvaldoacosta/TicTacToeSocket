

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class JogoClient extends JogoBase{
    private Socket socket;
    private Connection connection;
    private int[][] position = new int[3][3];
    private String[] board = getBoard();
    private Scanner scanner = new Scanner(System.in);

    public JogoClient() {
        super(false);
        try {
            socket = new Socket("localhost", 55666);
            connection = new Connection(this, socket);
            System.out.println("Conexão estabelecida!");
            initializeBoard();
            printBoard();





        }
        catch (IOException e){
            e.printStackTrace();
        }

    }



    @Override
    public void packetReceived(int coord) {

        board[coord] = "X";
        printBoard();

        if(checkEnd()){

            connection.close();
        }else {
            currentPlayer = false;


            if (isMyTurn()) {
                System.out.println("-----------------");
                System.out.println("Sua vez de jogar!");
                System.out.println("-----------------");
                printBoard();
                getUserInput(scanner);
            }
        }

    }

    @Override
    public void inputReceived(int coord) {
        if (board[coord] == "X" || board[coord] == "O"){
            System.out.println("O campo já foi preenchido, tente novamente");
            getUserInput(scanner);
        }

        if(isMyTurn()){
            connection.sendPacket(coord);
            board[coord] = "O";
            printBoard();
            if(checkEnd()) connection.close();
            currentPlayer = true;
        }


    }

    @Override
    public boolean checkEnd() {
        if (checkRowWin() || checkColumnWin() || checkDiagonalWin()) {
            System.out.println("\n---------------------------------------------------------------\n");

            if (currentPlayer) {

                System.out.print("\nO server ganhou! ");
            }
            else if(!currentPlayer){
                System.out.print("\nO client ganhou!");
            }
            System.out.println("\n---------------------------------------------------------------\n");
            return true;
        }
        return false;
    }



    @Override
    public void close() {
        try {
            connection.close();
            socket.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JogoClient client = new JogoClient();
    }


}
