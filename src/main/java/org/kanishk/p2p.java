package org.kanishk;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
public class p2p {
    int startPoint = 6000;
    int endPoint = 6100;
    static int port ;
    public void serverSide()throws IOException{

            try(ServerSocket ss = new ServerSocket(port)){
                while(true){
                    Socket s = ss.accept();//blocking call, waits for the client to connect

                    InputStream input = s.getInputStream();//reads message from client
                    // int charactersToRead = 10;
                    // byte[] b = new byte[charactersToRead];
                    // input.read(b);
                    // ByteBuffer
                    BufferedReader br = new BufferedReader(new InputStreamReader(input));
                    String Message = br.readLine();
                    System.out.println("message from client :"+Message);

                    OutputStream output = s.getOutputStream();//sends acknowledgement to the client
                    //out.write("This data is going to be sent".getBytes());
                    PrintWriter writer = new PrintWriter(output, true);
                    writer.println("Hello Client (from the server of "+s.getLocalPort()+")");



//                    s.close();
                }
            }
            catch(IOException e){
                System.out.println("error");
            }

    }


    public void clientSide()throws IOException{
    //   while(true){//so that the first port can connect to ports opened after its creation

           for(int port = startPoint; port <= endPoint; port++) {
               if(port == p2p.port){
                   continue;
               }
               try (Socket s = new Socket("localhost", port)) {
                   System.out.println("connected to "+port);//sends message to the server
                    OutputStream output = s.getOutputStream();
                    PrintWriter writer =new PrintWriter(output,true); //lazy function execution
                    writer.println("Hello server "+s.getPort());

                   InputStream input = s.getInputStream();
                   BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                   String Message = reader.readLine();
                   System.out.println("Message from server: "+Message);

               }
               catch(IOException e){
               }
           }
    //   }
    }

    public static void main(String[] args) {
        p2p p = new p2p();

        if(args.length>1)
            port = Integer.parseInt(args[1]);
        else{
            System.out.println("please provide a port number to connect to");
            System.exit(0);
        }

        if(port>=6000 && port<=6100){
            Runnable serverThread = new Runnable(){
                public void run(){
                    try {
                        p.serverSide();
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            Runnable clientThread = new Runnable(){
                public void run(){

                    try {
                        Thread.sleep(2000);
                        p.clientSide();
                    }
                    catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            System.out.println("running...");
            new Thread(serverThread).start();
            new Thread(clientThread).start();

        }
        else
            System.out.println("please choose a port number from 6000 to 6100");

    }
}
