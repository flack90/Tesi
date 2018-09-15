import java.util.Scanner;

public class Gestito0MQ {

	static int PCSimulati;
	static int UpdateDelay;
	public static void main(String[] args) {
		org.apache.log4j.BasicConfigurator.configure();
		OSInspector inspector =new OSInspector();
		new Thread(inspector).start();
		
		Scanner s = new Scanner (System.in);
		System.out.println("Inserire l'ip del server");
		String serverIp=s.nextLine();
		
		System.out.println("Scegliere numero di pc simulati");
		while (!s.hasNextInt()) {
	        System.out.println("Invalid Input");
	    	System.out.println("Scegliere numero di pc simulati");
	        s.next(); // this is important!
	    }

		PCSimulati=s.nextInt();
		System.out.println("Scegliere ogni quanto ogni pc simulato invia notifiche al server in secondi.");
	
		while (!s.hasNextInt()) {
	        System.out.println("Invalid Input!");
	        System.out.println("Scegliere ogni quanto ogni pc simulato invia notifiche al server in secondi.");
	
	    }
		UpdateDelay=s.nextInt()*1000;
		
		s=new Scanner(System.in);
		System.out.println("inserire l'ip dell'interfaccia di rete della quale si vuol catturare il traffico");
		String ip = s.nextLine();
		NetworkInspector nInspector = new NetworkInspector(ip);
		
		for(int i=0;i<PCSimulati;i++) {
			
			 
			Thread t = new Thread(new ZEROMQMessagePublisher(inspector,UpdateDelay,nInspector,serverIp));
			
				t.start();
		
		}
		nInspector.startCapture();
		s.close();
	}

}
