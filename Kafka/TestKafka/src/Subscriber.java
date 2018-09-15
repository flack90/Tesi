import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;

public class Subscriber implements Runnable {
	public static long totaltime = 0;
	public static int arrived = 0;
	public static long mediumtime=0;
	private JLabel label;
	private int messaggi;
	private boolean first=true;
	private long startTime;
	private long mediumDelay;
	private long delay;

	
	public Subscriber(int messaggi, JLabel subLabel) {
		this.label=subLabel;
		this.messaggi=messaggi;
	}


	@Override
	public void run() {
		org.apache.log4j.BasicConfigurator.configure();
		 Properties props = new Properties();
	     props.put("bootstrap.servers", "localhost:9092");
	     props.put("group.id", "test");
	     props.put("enable.auto.commit", "true");
	     props.put("auto.commit.interval.ms", "1000");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	     consumer.subscribe(Pattern.compile("Test"));
	      
	     while (true) {
	         ConsumerRecords<String, String> records = consumer.poll(100);
	        
	        for (ConsumerRecord<String, String> record : records) {
	        	long time = System.currentTimeMillis();
	       		if(first) {
	       			first=false;
	       			startTime=time;
	       			
	       		}
	       		long sendtime= Long.parseLong(record.value());
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
	    		label.setText(st);
	    		label.setText(st);
	        }
	        }

		
	}

}
