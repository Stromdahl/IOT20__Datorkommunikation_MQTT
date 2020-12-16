package com.Logger;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger implements MqttCallback {

    String broker = "tcp://broker.hivemq.com:1883";
    String clientId = "MMB-Logger";
    String topic = "MMB/+";
    int qos = 2;

    MqttClient client;

    public Logger() throws MqttException {
        this.connectToClient();
        this.subscribe();
    }

    private void connectToClient() throws MqttException {
        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(false);

        this.client = new MqttClient(broker, clientId, new MemoryPersistence());
        this.client.setCallback(this);
        this.client.connect(conOpt);
    }

    private void subscribe() throws MqttException {
        this.client.subscribe(this.topic, this.qos);
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String message = new String(mqttMessage.getPayload());

        try (FileWriter fw = new FileWriter("loggfil.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(formatter.format(date) + " " + topic + " " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(formatter.format(date) + " " + topic + " " + message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    public static void main(String[] args) {
        try {
            new Logger();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
