package name.felixbecker.freemarkerdebug;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class FreemarkerDebugClassTransformer implements ClassFileTransformer {
	
	private final FMDebugClassModifier fmDebugClassModifier = new FMDebugClassModifier();

	private int toInt(byte b){
		return b & 0xff;
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
		
		if(Logger.isDebugEnabled()){
			Logger.debug("Agent got " + className + " (loader: "+loader+") for instrumentation (Version " + (toInt(bytes[7]) + toInt(bytes[6])) + "." + (toInt(bytes[5]) + toInt(bytes[4]))+") - ConstPoolSize: "+(toInt(bytes[8])+toInt(bytes[9]))+" - Checking if instrumentation is required..");
		}
		
		if(instrumentClass(className, loader)){
			
			Logger.debug("Preconditions checked, instrumenting " + className + " now");
			
			try {
				return fmDebugClassModifier.patchByteCode(bytes, loader);
			} catch(Throwable t){
				Logger.error("Error instrumenting " + className + " - returning unpatched code! Exception:", t);
				return bytes;
			}
		} else {
			return bytes;
		}
		
	}
	
	private boolean instrumentClass(String className, ClassLoader loader){
		
		if("org/springframework/web/servlet/DispatcherServlet".equals(className) || "freemarker/core/Environment".equals(className)){
			Logger.info("Found Dispatcherservlet / Freemarker Environment class, instrumenting it");
			return true;	
		}
		
		return false;
	}
	
}
