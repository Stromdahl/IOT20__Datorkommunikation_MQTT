package com.company2;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Controller implements MqttCallback {

    String broker;
    String clientId;
    int qos;
    private String topic = "MMB/temperaturesensor";

    MemoryPersistence persistence = new MemoryPersistence();

    MqttClient client;

    public Controller(String broker, String clientId, String topic, int qos) throws MqttException {
        this.broker = broker;
        this.clientId = clientId;
        this.qos = qos;

        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);

        this.client = new MqttClient(broker, clientId, new MemoryPersistence());
        this.client.setCallback(this);
        this.client.connect(conOpt);

        this.client.subscribe(this.topic, qos);
    }

    public void publishMessage(String content, String topic) throws MqttException {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        client.publish(topic, message);
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        System.out.println("Got temperature: " + new String( mqttMessage.getPayload()));
        int temperature = Integer.parseInt(message);
        if(temperature > 22){
            publishMessage("+", "MMB/ctrl");
            System.out.println("Publishing: +");
        } else {
            publishMessage("-", "MMB/ctrl");
            System.out.println("Publishing: -");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
