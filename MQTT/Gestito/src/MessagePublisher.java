
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONStringer;
public class MessagePublisher implements Runnable {

	private static int count = 0; 
	String PcName;
	OSInspector inspector;
	NetworkInspector nInspector;
	int updateDelay;
	String serverIp;
	public MessagePublisher(OSInspector inspector, int updateDelay, NetworkInspector nInspector, String serverIp) {
		this.PcName="PC"+ count++;
		this.inspector=inspector;
		this.updateDelay = updateDelay;
		this.nInspector=nInspector;
		this.serverIp=serverIp;
	}
	
	
	
	public  void run() {
		try {
			MqttClient client = new MqttClient("tcp://"+serverIp+":1883", MqttClient.generateClientId());
			client.connect();
			
			while(true) {
				
				 double result;
				 result = inspector.CpuUsage;
				 publishMessage(result,PcName+"/CpuUsage",client);
				 result = inspector.FreeRam;
				 publishMessage(result,PcName+"/FreeRam",client);
				 result = inspector.FreeDisk;
				 publishMessage(result,PcName+"/FreeDisk",client);
				 result = inspector.NumberOfProcesses;
				 publishMessage(result,PcName+"/NumberOfProcesses",client);
				 result = nInspector.TCPin;
				 publishMessage(result,PcName+"/TCPin",client);
				 result = nInspector.UDPin;
				 publishMessage(result,PcName+"/UDPin",client);
				 result = nInspector.OTHERin;
				 publishMessage(result,PcName+"/OTHERin",client);
				 result = nInspector.TCPout;
				 publishMessage(result,PcName+"/TCPout",client);
				 result = nInspector.UDPout;
				 publishMessage(result,PcName+"/UDPout",client);
				 result = nInspector.OTHERout;
				 publishMessage(result,PcName+"/OTHERout",client);
				 
				 
			    /* p=Runtime.getRuntime().exec("cmd /C wmic os get numberofprocesses");
			     result = getResult(p);
			     publishMessage(result,PcName+"/NumberOfProcesses",client);*/
			     
			
		 try {
				Thread.sleep(updateDelay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
		}
		
		
		}catch   (MqttException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	};
				 
}





private  void publishMessage(double result, String topicName, MqttClient client) {
	
	MqttMessage message = new MqttMessage();
	 String myString = new JSONStringer()
		     .object()
		     	.key("value")
		         .value(result)
		         .key("time")
		         .value(System.currentTimeMillis())
		     .endObject()
		     .toString();
	message.setPayload(myString.getBytes());
	try {
		client.publish(topicName, message);
	} catch (MqttPersistenceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MqttException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}
}

