import javax.swing.JLabel;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONStringer;

public class Publisher implements Runnable {

	private JLabel label;
	private int messaggi;
	private long startTime;
	private long endTime;

	public Publisher(int messaggi, JLabel pubLabel) {
		this.label=pubLabel;
		this.messaggi=messaggi;
	}

	@Override
	public void run() {
		MqttClient client;
		try {
			client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
			client.connect();
			int i=0;
			 startTime=System.currentTimeMillis();
			 for( i=0;i<messaggi;i++) {
				MqttMessage message = new MqttMessage();
				message.setPayload(Long.toString(System.currentTimeMillis()).getBytes());
				client.publish("Test", message);
				endTime=System.currentTimeMillis();
				 long totaltime = endTime-startTime;
				 String st = new String("<html>");
				 st+=("Sent: "+ (i+1) +"<br>");
		 		st+=("Total time: "+ totaltime + " ms"+"<br>");
		 		st+=("</html>");
		 		label.setText(st);
			}
			
			client.disconnect();
			System.out.println("END");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
