package com.company3;

import com.company2.Controller;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Logger;

public class Main extends Controller {
    public Main(String broker, String clientId, String topic, int qos) throws MqttException {
        super(broker, clientId, topic, qos);
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String message = new String(mqttMessage.getPayload());

        try(FileWriter fw = new FileWriter("loggfil.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(formatter.format(date) + " " + topic + " " + message);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        System.out.println(formatter.format(date) + " " + topic + " " + message);
    }

    public static void main(String[] args)  {
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "blablabalbala";
        String topic = "MMB/loggfiler";
        int qos = 2;

        try {
            Main main = new Main(broker, clientId, topic, qos);
            main.subscribe(new String[] {"MMB/temperaturesensor", "MMB/ctrl"}, new int[] {2,2});


        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}
