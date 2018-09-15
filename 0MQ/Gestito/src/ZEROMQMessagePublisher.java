import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONStringer;
import org.zeromq.ZMQ;


public class ZEROMQMessagePublisher implements Runnable {

	private static int count = 0; 
	String PcName;
	int port;
	OSInspector inspector;
	NetworkInspector nInspector;
	int updateDelay;
	String serverIp;
	public ZEROMQMessagePublisher(OSInspector inspector, int i, NetworkInspector nInspector, String serverIp) {
		this.PcName="PC"+ count++;
		this.inspector=inspector;
		this.updateDelay = i;
		this.nInspector=nInspector;
		this.serverIp=serverIp;
	}
	
	
	
	public void run() {
		 ZMQ.Context context = ZMQ.context(1);
		 ZMQ.Socket publisher = context.socket(ZMQ.PUB);
	     

		 publisher.connect("tcp://"+serverIp+":"+ 5556);
		while(true) {
	 Process p;
	 double result;
	 result = inspector.CpuUsage;
	 publishMessage(result,PcName+"/CpuUsage",publisher);
	 result = inspector.FreeRam;
	 publishMessage(result,PcName+"/FreeRam",publisher);
	 result = inspector.FreeDisk;
	 publishMessage(result,PcName+"/FreeDisk",publisher);
	 result = inspector.NumberOfProcesses;
	 publishMessage(result,PcName+"/NumberOfProcesses",publisher);
	 result = nInspector.TCPin;
	 publishMessage(result,PcName+"/TCPin",publisher);
	 result = nInspector.UDPin;
	 publishMessage(result,PcName+"/UDPin",publisher);
	 result = nInspector.OTHERin;
	 publishMessage(result,PcName+"/OTHERin",publisher);
	 result = nInspector.TCPout;
	 publishMessage(result,PcName+"/TCPout",publisher);
	 result = nInspector.UDPout;
	 publishMessage(result,PcName+"/UDPout",publisher);
	 result = nInspector.OTHERout;
	 publishMessage(result,PcName+"/OTHERout",publisher);
	 
	 
	 try {
			Thread.sleep(updateDelay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
}
	 
}



private  void publishMessage(double result, String topicName, ZMQ.Socket publisher) {
	
	 String myString = new JSONStringer()
		     .object()
		     	.key("value")
		         .value(result)
		         .key("time")
		         .value(System.currentTimeMillis())
		     .endObject()
		     .toString();
	publisher.send((topicName +"\n" + myString ));
	
	
	
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
