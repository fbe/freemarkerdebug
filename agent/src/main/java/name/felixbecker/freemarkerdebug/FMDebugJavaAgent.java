package name.felixbecker.freemarkerdebug;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import name.felixbecker.freemarkerdebug.jmx.FMDebugJMXBeanImpl;


public class FMDebugJavaAgent {

	public static void premain(String agentArgs, Instrumentation inst) {
		
		System.out.println("========= RUNNING FREEMARKERDEBUG JAVA AGENT =========");

		try {
	        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
	        final ObjectName name = new ObjectName("name.felixbecker.freemarkerdebug:type=Agent"); 
	        final FMDebugJMXBeanImpl mbean = new FMDebugJMXBeanImpl();
	        mbs.registerMBean(mbean, name); 
		} catch(Exception e){ // start agent even if jmx fails
			Logger.error("Couldn't start jmx mbean!", e);
		}
		
		if("enableDebug".equals(agentArgs)){
			Logger.setDebugEnabled(true);
		}
		
		Logger.info("Adding freemarker agent instrumentation transformer");
		inst.addTransformer(new FreemarkerDebugClassTransformer());
	}

}
