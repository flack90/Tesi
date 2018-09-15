import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.json.JSONObject;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;

public class GestoreNATS {
	static long totaltime = 0;
	static int arrived = 0;
	public static void main(String[] args) {
		Connection nc;
		try {
			
			nc = Nats.connect();
			Dispatcher d = nc.createDispatcher((msg) -> {
				long time = System.currentTimeMillis();
				arrived++;
			    String response = new String(msg.getData(), StandardCharsets.UTF_8);
			    System.out.println(response);
			    Scanner s = new Scanner (response);
				
			    JSONObject j = new JSONObject(s.nextLine());
				Double value = (j.getDouble("value"));
				long mtime = j.getLong("time");
				long elapsedtime = time - mtime;
				totaltime+=elapsedtime;
				long mediumtime = totaltime/arrived;
				try {
				InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");
				if(!influxDB.databaseExists("NATS"))
					influxDB.createDatabase("NATS");
				Point point2 = Point.measurement("MediumTime")
						.addField("value",mediumtime)
						.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).
						build();
				String[] stringhe = msg.getSubject().split("/");
				Point point = Point.measurement(stringhe[0])
						.addField(stringhe[1],value)
						.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).
						build();
				influxDB.setDatabase("NATS");
				influxDB.write(point);
				influxDB.write(point2);
				System.out.println("ciao");
				}catch(Exception e) {System.out.println("Ex");};
			});

			d.subscribe(">");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
