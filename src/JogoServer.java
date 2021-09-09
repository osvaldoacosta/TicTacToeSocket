
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class JogoServer extends JogoBase{
    private ServerSocket serverSocket;
    private Socket socket;
    private Connection connection;
    private String[] board = getBoard();;
    private Scanner scanner = new Scanner(System.in);

    public JogoServer() {
        super(true);
        try {

            serverSocket = new ServerSocket(55666);
            System.out.println("Servidor criado, aguardando conexão!");
            socket = serverSocket.accept();

            connection = new Connection(this, socket);
            System.out.println("Conexão estabelecida!");
            initializeBoard();
            printBoard();

            getUserInput(scanner);


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public void packetReceived(int coord) {

        board[coord] = "O";
        printBoard();
        if(checkEnd()){

            connection.close();
        }else {

            currentPlayer = true;

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
            board[coord] = "X";
            printBoard();
            if(checkEnd()) connection.close();;
            currentPlayer = false;
        }


    }

    @Override
    public boolean checkEnd() {
        if (checkRowWin() || checkColumnWin() || checkDiagonalWin()) {
            System.out.println("\n---------------------------------------------------------------\n");

            if (currentPlayer) {

                System.out.print("\nO server ganhou! ");

            }

            else if (!currentPlayer){
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
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        JogoServer server = new JogoServer();
    }


}


