import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.json.JSONObject;

public class GestoreKafka {

	static  long totaltime = 0;
	static int arrived = 0;
	public static void main(String[] args) {
		org.apache.log4j.BasicConfigurator.configure();
		InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");
 		if(!influxDB.databaseExists("KAFKA"))
 			influxDB.createDatabase("KAFKA");
		 Properties props = new Properties();
	     props.put("bootstrap.servers", "localhost:9092");
	     props.put("group.id", "test");
	     props.put("enable.auto.commit", "true");
	     props.put("auto.commit.interval.ms", "1000");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	     consumer.subscribe(Pattern.compile(".*"));
	     
	     while (true) {
	         ConsumerRecords<String, String> records = consumer.poll(100);

				long time = System.currentTimeMillis();
	        for (ConsumerRecord<String, String> record : records) {
	        	 System.out.println();
	        	 System.out.println("SERNERNN");
	        	 JSONObject j = new JSONObject(record.value());
					Double value = (j.getDouble("value"));
					long mtime = j.getLong("time");
					long elapsedtime = time - mtime;
					totaltime+=elapsedtime;
					long mediumtime = totaltime/arrived;
	             System.out.println(  record.value());
	             String[] stringhe = record.topic().split("/");
	            Point point = Point.measurement(stringhe[0])
	     				.addField(stringhe[1],value)
	     				.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).
	     				build();
	            Point point2 = Point.measurement("MediumTime")
						.addField("value",mediumtime)
						.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).
						build();
	     		influxDB.setDatabase("KAFKA");
	     		influxDB.write(point);
	     		influxDB.write(point2);
	         }
	     }
	 

	}

}
