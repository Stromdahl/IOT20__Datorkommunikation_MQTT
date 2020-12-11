package com.company2;

import com.company.MqttPublishSample;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {
    public static void main(String[] args) {
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "aogijasoågjåasojfg";
        String topic = "MMB/temperaturesensor";
        int qos = 2;

        try {
            Controller controller = new Controller(broker, clientId, topic, qos);
            controller.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
