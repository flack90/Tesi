import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class GestoreMQTT {

	public static void main(String[] args) {
		MqttClient client;
		try {
			client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
			client.setCallback( new SimpleMqttCallBack() );
			client.connect();
			Scanner s = new Scanner(System.in);
			client.subscribe("#");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
