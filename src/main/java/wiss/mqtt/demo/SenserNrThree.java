package wiss.mqtt.demo;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SenserNrThree implements Runnable {

    @Override
    public void run() {
        startSender(); // Startet den Sender
    }

    public void startSender() {
        String broker = "tcp://192.168.56.107:1883"; // MQTT Broker-Adresse
        String clientId = "third_sender"; // Client-ID
        String topic = "sensoren/sensor3"; // MQTT-Topic
        int pubQos = 1; // Qualitätsstufe der veröffentlichten Nachrichten

        try {
            // Verbindung zum MQTT-Broker herstellen
            MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Verbindung zum Broker wird hergestellt: " + broker);
            client.connect(connOpts);
            System.out.println("Verbunden");
            double counter = 0.0; // Zählervariable für die Sinus-Berechnung
            
            while (true) {

                // Berechnet den Sinus-Wert
                double sinValue = Math.sin(counter);
                System.out.println("Sinus-Wert: " + sinValue); // Gibt den Sinus-Wert aus

                // Erstellt die Nachricht
                MqttMessage message = new MqttMessage(Double.toString(sinValue).getBytes());
                message.setQos(pubQos);

                // Sendet die Nachricht an das Topic
                client.publish(topic, message);
                System.out.println("Nachricht veröffentlicht: " + message);

                // Erhöht die Zählervariable
                counter += 0.1;

                // Wartet eine kurze Zeit vor der nächsten Veröffentlichung
                Thread.sleep(1000);
            }

        } catch (MqttException | InterruptedException e) {
            // Fehlerbehandlung
            e.printStackTrace();
        }
    }
}
