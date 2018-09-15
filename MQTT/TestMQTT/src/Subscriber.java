import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import javafx.scene.paint.Color;

public class Subscriber implements Runnable {


	private JLabel label;
	private int messaggi;

	public Subscriber(int messaggi, JLabel subLabel) {
		this.label=subLabel;
		this.messaggi=messaggi;
	}

	@Override
	public void run() {
		MqttClient client;
		 

		try {
			client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
			TestMqttCallBack callback = new TestMqttCallBack(label,messaggi);
			client.setCallback( callback );
			client.connect();
			client.subscribe("Test");
			
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
