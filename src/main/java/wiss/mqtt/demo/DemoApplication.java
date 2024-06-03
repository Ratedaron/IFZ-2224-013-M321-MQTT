package wiss.mqtt.demo;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class DemoApplication {

	public static void main(String[] args) {

		String broker = "tcp://192.168.56.107:1883"; // MQTT Broker-Adresse
		String clientId = "demo_client"; // Client-ID
		String topic = "sensoren/sensor1"; // MQTT-Topic
		int pubQos = 1; // Qualitätsstufe der veröffentlichten Nachrichten
		
		try {
			// Startet Sender 1 nach einer Verzögerung
			Thread senderThread1 = new Thread(new SecondMqttSender());
			senderThread1.start();
			System.out.println("Sender 1 gestartet.");

			// Wartet für ein paar Sekunden
			Thread.sleep(5000);

			// Startet Sender 2 nach einer weiteren Verzögerung
			Thread senderThread2 = new Thread(new SenserNrThree());
			senderThread2.start();
			System.out.println("Sender 2 gestartet.");

			// Wartet für ein paar Sekunden
			Thread.sleep(10000);

			// Startet Sender 3 nach einer weiteren Verzögerung
			Thread senderThread3 = new Thread(new SenserNrThree());
			senderThread3.start();
			System.out.println("Sender 3 gestartet.");

			// Wartet für ein paar Sekunden
			Thread.sleep(10000);

			// Startet Sender 4 nach einer weiteren Verzögerung
			Thread VierterSensor = new Thread(new VierterSensor());
			VierterSensor.start();
			System.out.println("Sender 4 gestartet.");

			// Wartet für ein paar Sekunden
			Thread.sleep(5000);
			
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
