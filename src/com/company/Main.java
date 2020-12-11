package com.company;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        String broker       = "tcp://broker.hivemq.com:1883";
        String clientId     = "StroidJavaTest";
        String topic        = "MMB/temperaturesensor";
        int qos             = 2;

        Random random = new Random();

        try {
            System.out.println("Connecting to broker: "+broker);
            MqttPublishSample mqttPublishSample = new MqttPublishSample(broker, clientId, qos);
            System.out.println("Connected");

            while(true){
                int temp = random.nextInt(10);
                String content = String.valueOf(temp + 15);
                System.out.println("Publishing message: "+content);
                mqttPublishSample.publishMessage(content, topic);
                System.out.println("Message published");
                TimeUnit.SECONDS.sleep(3);
            }
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
