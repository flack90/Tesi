import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class TestMqttCallBack implements MqttCallback {
	public long totaltime = 0;
	public int arrived = 0;
	public long mediumtime=0;
	private JTextField textField;
	private JLabel statusLabel;
	private boolean first=true;
	private long startTime;
	private long mediumDelay;
	private int messaggi;
	private long delay;
	
	public TestMqttCallBack(JLabel statusLabel, int messaggi) {
		this.statusLabel=statusLabel;
		this.messaggi=messaggi;
	}

	@Override
	
	public void connectionLost(Throwable cause) {


		

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		long time = System.currentTimeMillis();
   		if(first) {
   			first=false;
   			startTime=time;
   			
   		}
   		Scanner s = new Scanner (message.toString());
   		long sendtime = Long.parseLong(s.nextLine());
		arrived++;
		totaltime=time-startTime;
		
    	delay= delay+(time-sendtime);
		mediumDelay=delay/arrived;
		
		
		String st = new String("<html>");
		st+= ("Received: "+ arrived +"<br>");
		st+=("Lost : "+ (messaggi-arrived)+"<br>");
		st+=("Total time: "+ totaltime + " ms"+"<br>");
		st+=("Delay: "+ mediumDelay + " ms"+"<br>");
		st+=("</html>");
		statusLabel.setText(st);
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		
		
	}

}
