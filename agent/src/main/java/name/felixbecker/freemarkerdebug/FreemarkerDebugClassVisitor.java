package name.felixbecker.freemarkerdebug;

import name.felixbecker.freemarkerdebug.org.objectweb.asm.ClassVisitor;
import name.felixbecker.freemarkerdebug.org.objectweb.asm.MethodVisitor;

public class FreemarkerDebugClassVisitor extends ClassVisitor {


	public FreemarkerDebugClassVisitor(int api, ClassVisitor cv) {
		super(api, cv);

	}
	private String className = null;
	
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		
		if(Logger.isDebugEnabled()){
			Logger.debug("visiting byte code of class " + name + " version is " + version);
		}
		
		className = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}

	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		
		final MethodVisitor nextVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
		
		if(nextVisitor != null){
			return new EnableDisableFreemarkerTraceMethodAdapter(className, nextVisitor, access, name, desc);
		}
		
		return nextVisitor;
		
	}
	
	
}
