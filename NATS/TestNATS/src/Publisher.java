import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.JLabel;

import org.json.JSONStringer;

import io.nats.client.Connection;
import io.nats.client.Nats;

public class Publisher implements Runnable {

	private long endTime;
	private long startTime;
	private JLabel label;
	private int messaggi;

	public Publisher(int messaggi, JLabel pubLabel) {
		this.label=pubLabel;
		this.messaggi=messaggi;
	}

	@Override
	public void run() {
		try {
			Connection nc = Nats.connect("nats://localhost:4222");
			int i=0;
			 startTime=System.currentTimeMillis();
			 for( i=0;i<messaggi;i++) {
				 
			
					nc.publish("Test", Long.toString(System.currentTimeMillis()).getBytes());
					endTime=System.currentTimeMillis();
					 long totaltime = endTime-startTime;
					 String st = new String("<html>");
					 st+=("Sent: "+ (i+1) +"<br>");
			 		st+=("Total time: "+ totaltime + " ms"+"<br>");
			 		st+=("</html>");
			 		label.setText(st);
			}
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
		
}
