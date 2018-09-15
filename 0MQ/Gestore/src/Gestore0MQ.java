import org.zeromq.ZMQ;

public class Gestore0MQ {

	static int port = 5556;
	public static void main(String[] args) {
		
			
			 
			Thread t = new Thread(new ZeroMQMessageReceiver(port++));
			t.start();
		
		
	}

}
