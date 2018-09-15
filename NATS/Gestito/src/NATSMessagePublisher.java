import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.JSONStringer;

import io.nats.client.Connection;
import io.nats.client.Nats;

public class NATSMessagePublisher implements Runnable {

	private static int count = 0; 
	String PcName;
	OSInspector inspector;
	NetworkInspector nInspector;
	int updateDelay;
	String serverIp;
	public NATSMessagePublisher(OSInspector inspector, int updateDelay, NetworkInspector nInspector, String serverIp) {
		this.PcName="PC"+ count++;
		this.inspector=inspector;
		this.updateDelay = updateDelay;
		this.nInspector=nInspector;
		this.serverIp=serverIp;
		
	}
	
	
	
	public void run() {
		while(true) {
	 boolean isWindows = System.getProperty("os.name")
			  .toLowerCase().startsWith("windows");
	 Process p;
	 try {
		 Connection nc = Nats.connect("nats://"+serverIp + ":4222");

		 double result;
		 result = inspector.CpuUsage;
		 publishMessage(result,PcName+"/CpuUsage",nc);
		 result = inspector.FreeRam;
		 publishMessage(result,PcName+"/FreeRam",nc);
		 result = inspector.FreeDisk;
		 publishMessage(result,PcName+"/FreeDisk",nc);
		 result = inspector.NumberOfProcesses;
		 publishMessage(result,PcName+"/NumberOfProcesses",nc);
		 result = nInspector.TCPin;
		 publishMessage(result,PcName+"/TCPin",nc);
		 result = nInspector.UDPin;
		 publishMessage(result,PcName+"/UDPin",nc);
		 result = nInspector.OTHERin;
		 publishMessage(result,PcName+"/OTHERin",nc);
		 result = nInspector.TCPout;
		 publishMessage(result,PcName+"/TCPout",nc);
		 result = nInspector.UDPout;
		 publishMessage(result,PcName+"/UDPout",nc);
		 result = nInspector.OTHERout;
		 publishMessage(result,PcName+"/OTHERout",nc);
		
		 
		 try {
				Thread.sleep(updateDelay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
		
		
		
	 } catch (IOException e) {e.printStackTrace();} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} {
	};
}
		
	 
}
	
	private  void publishMessage(double result, String topicName, Connection nc) {
		
		
		 String myString = new JSONStringer()
			     .object()
			     	.key("value")
			         .value(result)
			         .key("time")
			         .value(System.currentTimeMillis())
			     .endObject()
			     .toString();
	
			nc.publish(topicName, myString.getBytes(StandardCharsets.UTF_8));
		
		
		
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
}
