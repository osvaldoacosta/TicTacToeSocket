import java.util.Scanner;

public abstract class JogoBase {

    private static String[] board = new String[9];

    private boolean end = false;

    // true = server, false = client
    protected boolean currentPlayer;
    protected boolean player;

    public static String[] getBoard() {
        return board;
    }


    protected static void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            board[i] = " ";
        }
    }

    protected boolean isMyTurn(){
        if(player == currentPlayer){
            return true;
        }
        else {
            return false;
        }
    }

    protected boolean checkRowWin() {
        for (int i = 0; i < board.length; i += 3) {
            if (!board[i].equals(" ") && board[i].equals(board[i + 1]) && board[i].equals(board[i + 2])) {
                return true;
            }
        }
        return false;
    }

    protected boolean checkColumnWin() {
        for (int i = 0; i < 3; i++) {
            if (!board[i].equals(" ") && board[i].equals(board[i + 3]) && board[i].equals(board[i + 6])) {
                return true;
            }
        }
        return false;
    }

    protected boolean checkDiagonalWin() {
        if (!board[0].equals(" ") && board[0].equals(board[4]) && board[0].equals(board[8])
                || !board[2].equals(" ") && board[2].equals(board[4]) && board[2].equals(board[6])) {
            return true;
        }
        return false;
    }



    protected static void printBoard() {


        for (int i = 0; i < board.length; i++) {
            if ((i + 1) % 3 == 0) {
                System.out.println(board[i]);
                if (i != board.length - 1) {
                    System.out.println("-----");
                }
            } else {
                System.out.print(board[i] + "|");
            }
        }
    }


    protected void getUserInput(Scanner scanner) {
        try {
            System.out.print("\nInsira o quadrado entre 1-9: ");
            int playerInput = scanner.nextInt() - 1;
            inputReceived(playerInput);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public JogoBase(boolean player) {
        this.player = player;
        currentPlayer = true;



    }




    public abstract void packetReceived (int coord);
    public abstract void inputReceived(int coord);
    public abstract boolean checkEnd();

    public abstract void close();
}
