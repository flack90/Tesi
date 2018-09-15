import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Scanner s=new Scanner(System.in);
		System.out.println("messaggi da inviare");
		int Messaggi=s.nextInt();
		JFrame mainFrame;
		 JPanel controlPanel;
		 JTextField textField = new JTextField(20);
		 mainFrame = new JFrame("Test MQTT");
	      mainFrame.setSize(400,400);
	      controlPanel = new JPanel();
	      mainFrame.add(controlPanel);
	      
	      JLabel pubLabel = new JLabel("",0);
	      
	      JLabel subLabel = new JLabel("",2);
	      controlPanel.add(pubLabel);
	      controlPanel.add(new JSeparator());
	      controlPanel.add(subLabel);
	      mainFrame.setVisible(true);  
	      
	      new Thread(new Subscriber(Messaggi,subLabel)).start();
	      Thread.sleep(1000);
	      new Thread(new Publisher(Messaggi,pubLabel)).start();

	}

}
