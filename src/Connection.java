import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Connection implements Runnable{
    private DataOutputStream out;
    private DataInputStream in;

    private JogoBase game;

    private boolean running;

    public Connection(JogoBase game, Socket socket){
        this.game = game;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

        }catch (IOException e){
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    public void sendPacket(int coord){

        try {
            out.writeInt(coord);
            out.flush();

            System.out.println("Posição enviada!: " + (coord + 1));

        }
        catch (IOException ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void run() {
        running = true;
        while (running){
            try {

                int coord = in.readInt();
                System.out.println("Posição recebida!: " + (coord + 1));

                game.packetReceived(coord);

            }catch (EOFException | SocketException e){
                running = false;
            }

            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void close(){
        try {
            running = false;
            in.close();
            out.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
