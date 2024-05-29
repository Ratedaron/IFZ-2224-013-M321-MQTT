package wiss.mqtt.demo;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class DemoApplication {

	public static void main(String[] args) {

		String broker = "tcp://192.168.56.107:1883"; // MQTT Broker-Adresse
		String clientId = "demo_client"; // Client-ID
		String topic = "sensoren/sensor1"; // MQTT-Topic
		int pubQos = 1;

		try {
			// Start sender 1 after a delay

			Thread senderThread1 = new Thread(new SecondMqttSender());
			senderThread1.start();
			System.out.println("Sender 1 gestartet.");

			// Wait for a few seconds
			Thread.sleep(5000);

			// Start sender 2 after another delay
			Thread senderThread2 = new Thread(new SenserNrThree());
			senderThread2.start();
			System.out.println("Sender 2 gestartet.");

			// Wait for a few seconds
			Thread.sleep(10000);

			// Start sender 3 after another delay
			Thread senderThread3 = new Thread(new SenserNrThree());
			senderThread3.start();
			System.out.println("Sender 3 gestartet.");

			// Wait for a few seconds
			Thread.sleep(10000);

			// Start sender 3 after another delay
			Thread VierterSensor = new Thread(new VierterSensor());
			VierterSensor.start();
			System.out.println("Sender 4 gestartet.");

			Thread.sleep(5000);
			// Connect to MQTT broker
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

				// Wait for a short time before publishing the next message
				Thread.sleep(1000);
			}

		} catch (MqttException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
