import java.util.Properties;

import javax.swing.JLabel;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONStringer;

public class Publisher implements Runnable {

	public Publisher(int messaggi, JLabel pubLabel) {
		this.label=pubLabel;
		this.messaggi=messaggi;
	}

	private long endTime;
	private long startTime;
	private int messaggi;
	private JLabel label;

	@Override
	public void run() {
		org.apache.log4j.BasicConfigurator.configure();
		 Properties props = new Properties();
		 props.put("bootstrap.servers", "localhost:9092");
		 props.put("acks", "all");
		 props.put("retries", 0);
		 props.put("batch.size", 16384);
		 props.put("linger.ms", 1);
		 props.put("buffer.memory", 33554432);
		 props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		 KafkaProducer<String,String> producer = new KafkaProducer<>(props);

		 int i=0;
		 startTime=System.currentTimeMillis();
		 for( i=0;i<messaggi;i++) {
			 
			ProducerRecord<String,String> message = new ProducerRecord<>("Test",Long.toString(System.currentTimeMillis()));
			producer.send(message);
			//producer.flush();
			endTime=System.currentTimeMillis();
			 long totaltime = endTime-startTime;
			 String st = new String("<html>");
			 st+=("Sent: "+ (i+1) +"<br>");
	 		st+=("Total time: "+ totaltime + " ms"+"<br>");
	 		st+=("</html>");
	 		label.setText(st);
		 }
		 
		
	}

}
