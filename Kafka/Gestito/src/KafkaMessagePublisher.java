import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONStringer;

public class KafkaMessagePublisher implements Runnable {


		private static int count = 0; 
		String PcName;
		OSInspector inspector;
		NetworkInspector nInspector;
		int updateDelay;
		String serverIp;
		public KafkaMessagePublisher(OSInspector inspector, int updateDelay, NetworkInspector nInspector, String serverIp) {
			this.PcName="PC"+ count++;
			this.inspector=inspector;
			this.updateDelay = updateDelay;
			this.nInspector=nInspector;
			this.serverIp=serverIp;
			
		}
		
		
		
		public void run() {
			while(true) {
					
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

			 double result;
			 result = inspector.CpuUsage;
			 publishMessage(result,PcName+"/CpuUsage",producer);
			 result = inspector.FreeRam;
			 publishMessage(result,PcName+"/FreeRam",producer);
			 result = inspector.FreeDisk;
			 publishMessage(result,PcName+"/FreeDisk",producer);
			 result = inspector.NumberOfProcesses;
			 publishMessage(result,PcName+"/NumberOfProcesses",producer);
			 result = nInspector.TCPin;
			 publishMessage(result,PcName+"/TCPin",producer);
			 result = nInspector.UDPin;
			 publishMessage(result,PcName+"/UDPin",producer);
			 result = nInspector.OTHERin;
			 publishMessage(result,PcName+"/OTHERin",producer);
			 result = nInspector.TCPout;
			 publishMessage(result,PcName+"/TCPout",producer);
			 result = nInspector.UDPout;
			 publishMessage(result,PcName+"/UDPout",producer);
			 result = nInspector.OTHERout;
			 publishMessage(result,PcName+"/OTHERout",producer);
			 	try {
				 Thread.sleep(updateDelay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 
	
		 
			
			}
		
		
		}
		private  String getResult(Process p) {
			InputStream input =p.getInputStream();
			 String m ="";
			Scanner reader = new Scanner(input);
			reader.nextLine();
			reader.nextLine();
			m = m + reader.nextLine() + "\n";
			
			return m;
		}
		
		private  void publishMessage(double result, String topicName, KafkaProducer<String,String> producer) {
			
			
			
			 String myString = new JSONStringer()
				     .object()
				     	.key("value")
				         .value(result)
				         .key("time")
				         .value(System.currentTimeMillis())
				     .endObject()
				     .toString();
			ProducerRecord<String,String> message = new ProducerRecord<>(topicName,myString);
			producer.send(message);
			producer.flush();
			System.out.println();
			System.out.println("SENTNTNTNTNTNTNTNTNTN");
			
			
		}
		
}
