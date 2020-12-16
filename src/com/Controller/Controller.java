package com.Controller;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Controller implements MqttCallback {

    String broker;
    String clientId;
    int qos;

    String temperatureTopic = "MMB/temperaturesensor";
    String controllerTopic = "MMB/ctrl";

    MqttClient client;

    public Controller(String broker, String clientId, int qos) throws MqttException {
        this.broker = broker;
        this.clientId = clientId;
        this.qos = qos;

        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);

        this.client = new MqttClient(broker, clientId, new MemoryPersistence());
        this.client.setCallback(this);
        this.client.connect(conOpt);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        System.out.println("Got temperature: " + new String(mqttMessage.getPayload()));
        int temperature = Integer.parseInt(message);
        if(temperature > 22){
            publishMessage("+", controllerTopic);
            printPublishMessage("+");
        } else {
            publishMessage("-", controllerTopic);
            printPublishMessage("-");
        }
    }

    public void publishMessage(String content, String topic) throws MqttException {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        client.publish(topic, message);
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost");
    }

    public void printPublishMessage(String msg) {
        System.out.printf("Publishing message: %s\n", msg);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public void subscribe(String topic, int qos) throws MqttException {
        this.client.subscribe(topic, qos);
    }

    public void subscribe(String[] topic, int[] qos) throws MqttException {
        this.client.subscribe(topic, qos);
    }

    public static void main(String[] args) {
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "MariaMattiasBesim-Controller";
        String temperatureTopic = "MMB/temperaturesensor";
        int qos = 2;

        try {
            Controller controller = new Controller(broker, clientId, qos);
            controller.subscribe(temperatureTopic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}
