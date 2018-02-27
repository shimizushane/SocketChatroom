package com.example.apple.mysocketchat;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient extends AppCompatActivity implements Button.OnClickListener {

    Button btn;
    Button btn2;
    EditText ed1;
    EditText ed2;
    TextView tv;

    String tmp;
    Socket clientSocket;


    public static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);

        btn = (Button)findViewById(R.id.button1);
        btn2 = (Button)findViewById(R.id.button2);
        ed1 = (EditText)findViewById(R.id.editText1);
        ed2 = (EditText)findViewById(R.id.editText2);
        tv = (TextView)findViewById(R.id.textView1);

        btn.setOnClickListener(this);
        btn2.setOnClickListener(this);

        Thread t = new Thread(readData);
        t.start();

        tv.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override public void onClick(View view) {

      switch (view.getId()) {
        case R.id.button1:
          if (clientSocket.isConnected()){

            BufferedWriter bw;

            try {
              bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
              bw.write(ed1.getText() + ":" + ed2.getText()+"\n");
              bw.flush();
            } catch (IOException e) {
              Log.d("Error :",e.toString());
            }

            ed2.setText("");
          }
          break;
        case R.id.button2:
          Thread k = new Thread(readData);
          k.start();
          Log.d("Button Thread = ","Running!!!");
          break;
        default:
      }
    }

    private Runnable updateText = new Runnable() {
        @Override public void run() {
          tv.append(tmp + "\n");
        }
    };

    public Runnable readData = new Runnable() {
      @Override public void run() {

        InetAddress serverIp;

        try {
          //serverIp = InetAddress.getByName("10.0.2.2");
          serverIp = InetAddress.getByName("192.168.2.10");
          int serverPort = 5050;
          clientSocket = new Socket(serverIp,serverPort);

          BufferedReader br;

          br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          Log.d("Test",clientSocket.isConnected()?"Ture":"False");
          while (clientSocket.isConnected()){

            tmp = br.readLine();

            if(tmp != null)
              handler.post(updateText);
          }

        } catch (Exception e) {
          Log.d("Error :",e.toString());
        }
      }
    };
}
