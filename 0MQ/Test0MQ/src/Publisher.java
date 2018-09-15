
import javax.swing.JLabel;

import org.json.JSONStringer;
import org.zeromq.ZMQ;

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
		ZMQ.Context context = ZMQ.context(1);
		 ZMQ.Socket publisher = context.socket(ZMQ.PUSH);
		 publisher.connect("tcp://localhost:5556");
		 int i=0;
		 startTime=System.currentTimeMillis();
		 for( i=0;i<messaggi;i++) {
			 
			publisher.send(("Test \n" + System.currentTimeMillis()));
			 endTime=System.currentTimeMillis();
			 long totaltime = endTime-startTime;
			 String st = new String("<html>");
			 st+=("Sent: "+ (i+1) +"<br>");
	 		st+=("Total time: "+ totaltime + " ms"+"<br>");
	 		st+=("</html>");
	 		label.setText(st);
			}
		
		 publisher.close();
		 context.term();
		
	}

}
