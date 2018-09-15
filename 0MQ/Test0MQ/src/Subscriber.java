import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONObject;
import org.zeromq.ZMQ;

public class Subscriber implements Runnable {
	public  long totaltime = 0;
	public  int arrived = 0;
	 boolean first=true;
	private  long startTime;
	private  long mediumDelay;
	private JLabel label;
	private int messaggi;
	private long delay;
	
	public Subscriber(int messaggi, JLabel subLabel) {
		this.label=subLabel;
		this.messaggi=messaggi;
	}

	@Override
	public void run() {
		
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket subscriber = context.socket(ZMQ.PULL);
       subscriber.bind("tcp://localhost:5556");
       //subscriber.subscribe("Test");
       while(true) {
       	
       	String response = subscriber.recvStr();
       	
       	if(response!=null) {
       		long time = System.currentTimeMillis();
       		if(first) {
       			first=false;
       			startTime=time;
       			
       		}
	    		arrived++;

	       		Scanner s = new Scanner (response);
	       		s.nextLine();
	       		long sendtime = Long.parseLong(s.nextLine());
	    		totaltime=time-startTime;
	    		

	        	delay= delay+(time-sendtime);
	    		mediumDelay=delay/arrived;
	    		
	    		
	    		
	    		String st = new String("<html>");
	    		st+= ("Received: "+ arrived +"<br>");
	    		st+=("Lost : "+ (messaggi-arrived)+"<br>");
	    		st+=("Total time: "+ totaltime + " ms"+"<br>");
	    		st+=("Delay: "+ mediumDelay + " ms"+"<br>");
	    		st+=("</html>");
	    		label.setText(st);
       	}
       }
	}

}
