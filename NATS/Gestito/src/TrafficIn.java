import java.net.InetAddress;
import java.net.UnknownHostException;

import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

public class TrafficIn implements Runnable {

	NetworkInspector networkInspector;
	String ip;
	public TrafficIn(NetworkInspector networkInspector, String ip) {
		this.networkInspector=networkInspector;
		this.ip=ip;
	}

	@Override
	public void run() {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(ip);
			PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);
			int snapLen = 65536;
			int readTimeout = 50; // in milliseconds
			final PcapHandle handleIN;
			handleIN = nif.openLive(snapLen, PromiscuousMode.PROMISCUOUS, readTimeout);
			handleIN.setFilter("dst net " + ip, BpfCompileMode.OPTIMIZE);
			PacketListener listenerIN = new PacketListener() {
	            
				@Override
				public void gotPacket(PcapPacket packet) {
					TcpPacket tcppkt = packet.get(TcpPacket.class);
					UdpPacket udppkt = packet.get(UdpPacket.class);
					if(tcppkt!=null) 
						networkInspector.TCPin++;
					else 
						if(udppkt!=null) 
							networkInspector.UDPin++;
						else
							networkInspector.OTHERin++;
						
					
					
				}
	        };
	        handleIN.loop(-1, listenerIN);
	        handleIN.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PcapNativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotOpenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
