package id.pptik.bawaslubatch.helpers;

import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class SendToRMQ {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    public  void  sendRMQFan(String getJSON) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException, InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://pemilu:pemilu123!@167.205.7.19");
        factory.setVirtualHost("/pemilu");
        //factory.setUri("amqp://guest:guest@localhost");
        factory.setConnectionTimeout(3000000);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        //send message TO RMQ

        String messageOn = getJSON ;
        channel.basicPublish("amq.topic","pemilu2019",null,messageOn.getBytes());
        //System.out.println("published mesasge"  + messageOn);


    }
    public  void  sendRMQreport(String getJSON) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException, InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://pemilu:pemilu123!@167.205.7.19");
        factory.setVirtualHost("/pemilu");
        //factory.setUri("amqp://guest:guest@localhost");
        factory.setConnectionTimeout(3000000);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        //send message TO RMQ

        String messageOn = getJSON ;
        channel.basicPublish("amq.topic","report",null,messageOn.getBytes());
        //System.out.println("published mesasge"  + messageOn);


    }

    public void SendSpeed() throws InterruptedException {
        Thread.sleep(500); //0.5 sec
    }
}