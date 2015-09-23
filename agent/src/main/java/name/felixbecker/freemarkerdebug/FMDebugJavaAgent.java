package name.felixbecker.freemarkerdebug;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;


public class FMDebugJavaAgent {

	public FMDebugJavaAgent(){
		try {
	        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
	        ObjectName name = new ObjectName("name.felixbecker.freemarkerdebug=Agent"); 
	        //SnailHunterJMXBean mbean = new SnailHunterJMXBean(); 
	        //mbs.registerMBean(mbean, name); 
		} catch(Exception e){ // start agent even if jmx fails
			Logger.error("Couldn't start jmx mbean!", e);
		}
	}
	
	public static void premain(String agentArgs, Instrumentation inst) {
		
		System.out.println("========= RUNNING FREEMARKERDEBUG JAVA AGENT =========");

		if("enableDebug".equals(agentArgs)){
			Logger.setDebugEnabled(true);
		}
		
		Logger.info("Adding freemarker agent instrumentation transformer");
		inst.addTransformer(new FreemarkerDebugClassTransformer());
	}

}
