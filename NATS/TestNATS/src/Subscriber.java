import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONObject;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;

public class Subscriber  implements Runnable{
	public static long totaltime = 0;
	public static int arrived = 0;
	public static long mediumtime=0;
	private JLabel label;
	private int messaggi;
	private boolean first=true;
	private long startTime;
	private long mediumDelay=0;
	private long delay=0;
	public Subscriber(int messaggi, JLabel subLabel) {
		this.label=subLabel;
		this.messaggi=messaggi;
	}
	@Override
	public void run() {
		try {
			
			Connection nc = Nats.connect();
			Dispatcher d = nc.createDispatcher((msg) -> {
				long time = System.currentTimeMillis();
	       		if(first) {
	       			first=false;
	       			startTime=time;
	       			
	       		}
				arrived++;
				String response = new String(msg.getData(), StandardCharsets.UTF_8);
			   
			    Scanner s = new Scanner (response);
			    long sendtime = Long.parseLong(s.nextLine());
			    delay= delay+(time-sendtime);
				mediumDelay=delay/arrived;
				
				
				
				totaltime=time-startTime;
	    		
		    	
	    		
	    		
	    		String st = new String("<html>");
	    		st+= ("Received: "+ arrived +"<br>");
	    		st+=("Lost : "+ (messaggi-arrived)+"<br>");
	    		st+=("Total time: "+ totaltime + " ms"+"<br>");
	    		st+=("Delay: "+ mediumDelay + " ms"+"<br>");
	    		st+=("</html>");
	    		label.setText(st);
	    		label.setText(st);
			});
			d.subscribe("Test");
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
