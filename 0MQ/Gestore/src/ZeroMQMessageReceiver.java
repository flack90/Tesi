import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.json.JSONObject;
import org.zeromq.ZMQ;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

public class ZeroMQMessageReceiver implements Runnable {

	int port;
	static  long totaltime = 0;
	static int arrived = 0;
	public ZeroMQMessageReceiver(int i) {
		port=i;
	}

	@Override
	public void run() {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.bind("tcp://localhost:"+5556);
        subscriber.subscribe("");
		
        while(true) {
        	String response = subscriber.recvStr();
			long time = System.currentTimeMillis();
        	System.out.println(response);
        	synchronized(this.getClass()) {
        	
				arrived++;
			    Scanner s = new Scanner (response);
				String topicName = s.nextLine();
				JSONObject j = new JSONObject(s.nextLine());
				Double value = (j.getDouble("value"));
				long mtime = j.getLong("time");
				long elapsedtime = time - mtime;
				totaltime+=elapsedtime;
				long mediumtime = totaltime/arrived;
				try {
				InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");
				if(!influxDB.databaseExists("0MQ"))
					influxDB.createDatabase("0MQ");
				
				Point point2 = Point.measurement("MediumTime")
						.addField("value",mediumtime)
						.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).
						build();
				String[] stringhe = topicName.split("/");
				Point point = Point.measurement(stringhe[0])
						.addField(stringhe[1],value)
						.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).
						build();
				influxDB.setDatabase("0MQ");
				influxDB.write(point);
				influxDB.write(point2);
				}catch(Exception e) {}
        	}	
        }

	}

}
