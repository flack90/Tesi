

public class NetworkInspector  {

	String ip;
	public double TCPin=0;
	public double UDPin=0;
	public double OTHERin=0;

	public double TCPout=0;
	public double UDPout=0;
	public double OTHERout=0;
	
	public NetworkInspector(String ip) {
		this.ip=ip;
	}

	
	public void startCapture() {
	
				
			      
			            new Thread(new TrafficIn(this,ip)).start();
			            new Thread(new TrafficOut(this,ip)).start();
			      
			
			
	}

}
