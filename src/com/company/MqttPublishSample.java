package com.company;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublishSample {

    String broker;
    String clientId;
    int qos;

    MemoryPersistence persistence = new MemoryPersistence();

    MqttClient client;

    public MqttPublishSample(String broker, String clientId, int qos) throws MqttException {
        this.broker = broker;
        this.clientId = clientId;
        this.qos = qos;

        this.client = new MqttClient(broker, clientId, persistence);
        this.connectClientToBroker();
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

    public void disconnectClient() throws MqttException {
        client.disconnect();
    }

}
