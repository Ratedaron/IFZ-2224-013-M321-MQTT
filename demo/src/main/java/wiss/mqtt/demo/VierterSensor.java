package wiss.mqtt.demo;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class VierterSensor implements Runnable {

    @Override
    public void run() {
        startSender();
    }

    public void startSender() {
        String broker = "tcp://192.168.56.107:1883"; // MQTT Broker-Adresse
        String clientId = "forth_sender"; // Client-ID
        String topic = "sensoren/sensor4"; // MQTT-Topic
        int pubQos = 1;

        try {
            MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");

            double counter = 0.0;

            while (true) {
                double sinValue = Math.sin(counter); // Sinus-Wert berechnen
                System.out.println("Sinus-Wert: " + sinValue); // Sinus-Wert ausgeben

                // Nachricht erstellen
                MqttMessage message = new MqttMessage(Double.toString(sinValue).getBytes());
                message.setQos(pubQos);

                // Nachricht zum Topic senden
                client.publish(topic, message);
                System.out.println("Published message: " + message);

                // Zählervariable erhöhen
                counter += 0.1;

                // 1 Sekunde warten
                Thread.sleep(1000);
            }

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

