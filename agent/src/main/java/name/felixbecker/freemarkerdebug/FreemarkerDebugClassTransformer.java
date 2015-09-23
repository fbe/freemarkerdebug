package name.felixbecker.freemarkerdebug;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class FreemarkerDebugClassTransformer implements ClassFileTransformer {
	
	private final FMDebugClassModifier fmDebugClassModifier = new FMDebugClassModifier();

	private static final List<String> blacklistedclasses = new ArrayList<>();
	
	static {
		// important, don't instrument yourself!
		blacklistedclasses.add("de/douglas/snailhunter");
	}
	
	
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
			
		} else if("freemarker/core/Environment".equals(className)){
			Logger.info("Replacing Environment.class with patched Environment.class");

			try {
				
				URL resource = getClass().getResource("/freemarker/core/Environment.class");
				Logger.info("URI of the Environment.class: " + resource.toURI());
				
				final InputStream classStream = getClass().getResourceAsStream("/freemarker/core/Environment.class");
				
				final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
				int nRead;
				byte[] data = new byte[1024];
	
				while ((nRead = classStream.read(data, 0, data.length)) != -1) {
				  buffer.write(data, 0, nRead);
				}
	
				buffer.flush();

				return buffer.toByteArray();
			
			} catch(Exception e){
				Logger.error("Couldn't read bytes from patched Environment.class, returning unpatched version.", e);
			}
			
			return bytes;
		} else {
			return bytes;
		}
		
	}
	
	private boolean instrumentClass(String className, ClassLoader loader){
		
		if("org/springframework/web/servlet/DispatcherServlet".equals(className)){
			Logger.info("Found Dispatcherservlet, instrumenting it");
			return true;	
		}
		
		return false;
	}
	
	private boolean isClassLoaderAllowed(ClassLoader classLoader){
		
		// Strange reflection stuff not visible to other classloaders
		if(classLoader.getClass().getCanonicalName().equals("sun.reflect.DelegatingClassLoader")){
			return false;
		}

		/*
		 * http://docs.oracle.com/javase/7/docs/api/java/lang/instrument/package-summary.html:
		 * The agent class will be loaded by the system class loader (see ClassLoader.getSystemClassLoader). 
		 * This is the class loader which typically loads the class containing the application main method. 
		 * The premain methods will be run under the same security and classloader rules as the application main method. 
		 * 
		 */
		
		/*
		 * The oracle JDK classloader order is:
		 * 
		 * [BootstrapClassLoader] (bootstrap)
		 *           |
		 *    [ExtClassLoader] (jre/lib/ext/)
		 *           |
		 *   [SystemClassLoader] (Classpath)
		 *    
		 *    
		 * We want to ensure that classes from jre/lib/ext / bootstrap classpath will never be instrumented because they'll 
		 * not be able to see the SnailHunter (because the first classloader snailhunter is visible to is the system class loader).
		 * So return false for all classloader > SystemClassLoader. 
		 */
		
		ClassLoader clsToCheck = classLoader;

		while(clsToCheck != null){
			
			if(clsToCheck == ClassLoader.getSystemClassLoader()){
				return true;
			}
			
			clsToCheck = clsToCheck.getParent();
			
		}

		return false;
	}

}
