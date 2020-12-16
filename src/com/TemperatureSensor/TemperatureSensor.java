package com.TemperatureSensor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TemperatureSensor {

    String broker;
    String clientId;
    String topic;
    int qos;

    MqttClient client;

    Random random = new Random();

    int readDelay = 60;

    public TemperatureSensor(String broker, String clientId, String topic, int qos) throws MqttException {
        this.broker = broker;
        this.clientId = clientId;
        this.topic = topic;
        this.qos = qos;

        this.client = new MqttClient(broker, clientId, new MemoryPersistence());
        this.connectClientToBroker();
    }

    public void run() {
        while (client.isConnected()) {
            try {
                String temperature = getTemperature();
                publishMessage(temperature, this.topic);
                printPublishMessage(temperature);
                TimeUnit.SECONDS.sleep(readDelay);
            } catch (MqttException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    int randomTemperature(int min, int max) {
        return min + random.nextInt(max - min);
    }

    String getTemperature() {
        return String.valueOf(randomTemperature(15, 25));
    }

    public void connectClientToBroker() throws MqttException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        client.connect(connOpts);
    }

    public void publishMessage(String content, String topic) throws MqttException {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        client.publish(topic, message);
    }

    public void printPublishMessage(String msg) {
        System.out.printf("Publishing message: %s\n", msg);
    }

    public void disconnectClient() throws MqttException {
        client.disconnect();
    }

    public static void main(String[] args) {
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "MariaMattiasBesim-temperature";
        String topic = "MMB/temperaturesensor";
        int qos = 2;

        try {
            TemperatureSensor temperatureSensor = new TemperatureSensor(broker, clientId, topic, qos);
            temperatureSensor.run();
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
}
