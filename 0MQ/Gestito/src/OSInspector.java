import java.io.File;
import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;




public class OSInspector implements Runnable {

	public double FreeRam=0;
	public double CpuUsage=0;
	public double FreeDisk=0;
	public double NumberOfProcesses=0;
	
	@Override
	public void run(){
		try {
		OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		
		File f = new File("/");
		while (true) {
			CpuUsage=bean.getSystemCpuLoad()*100;
			FreeRam=bean.getFreePhysicalMemorySize();
			FreeDisk=f.getFreeSpace();
			NumberOfProcesses=ProcessHandle.allProcesses().count();
			
			
			Thread.sleep(1000);
		}
		}catch(InterruptedException e) {e.printStackTrace();}
	}

}
