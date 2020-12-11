package com.company;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String broker       = "tcp://broker.hivemq.com:1883";
        String clientId     = "StroidJavaTest";
        String topic        = "Stroid/Test";
        int qos             = 2;

        try {
            System.out.println("Connecting to broker: "+broker);
            MqttPublishSample mqttPublishSample = new MqttPublishSample(broker, clientId, qos);
            System.out.println("Connected");

            Scanner scanner = new Scanner(System.in);
            while(true){
                String content = scanner.nextLine();
                if(content.toLowerCase().equals("exit")){
                    break;
                }
                System.out.println("Publishing message: "+content);
                mqttPublishSample.publishMessage(content, topic);
                System.out.println("Message published");
            }
            mqttPublishSample.disconnectClient();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}
