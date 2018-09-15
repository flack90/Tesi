import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.json.JSONObject;

public class SimpleMqttCallBack implements MqttCallback {

	long totaltime = 0;
	int arrived = 0;
	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		long time = System.currentTimeMillis();
		arrived++;
		Scanner s = new Scanner (arg1.toString());
		
		JSONObject j = new JSONObject(s.nextLine());
		Double value = (j.getDouble("value"));
		long mtime = j.getLong("time");
		
		Double d;
		
		long elapsedtime = time - mtime;
		
		totaltime+=elapsedtime;
		long mediumtime = totaltime/arrived;
		try {
		InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");
		if(!influxDB.databaseExists("MQTT"))
			influxDB.createDatabase("MQTT");
		
		Point point2 = Point.measurement("MediumTime")
				.addField("value",mediumtime)
				.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).
				build();
		String[] stringhe = arg0.split("/");
		Point point = Point.measurement(stringhe[0])
				.addField(stringhe[1],value)
				.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).
				build();
		influxDB.setDatabase("MQTT");
		influxDB.write(point);
		influxDB.write(point2);
		}
		catch(Exception e) {};
	}

}
