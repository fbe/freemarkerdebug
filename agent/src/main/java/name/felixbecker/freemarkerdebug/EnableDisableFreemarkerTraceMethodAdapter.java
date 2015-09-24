package name.felixbecker.freemarkerdebug;

import name.felixbecker.freemarkerdebug.org.objectweb.asm.Handle;
import name.felixbecker.freemarkerdebug.org.objectweb.asm.Label;
import name.felixbecker.freemarkerdebug.org.objectweb.asm.MethodVisitor;
import name.felixbecker.freemarkerdebug.org.objectweb.asm.commons.AdviceAdapter;

public class EnableDisableFreemarkerTraceMethodAdapter extends AdviceAdapter {

	private final Label l0 = new Label();
	private final Label l1 = new Label();
	private final String className;
	private final String methodName;

	private boolean finallyInsertionDone=false;
	
	public EnableDisableFreemarkerTraceMethodAdapter(String className, MethodVisitor mv, int access, String name, String desc) {
		super(ASM5, mv, access, name, desc);
		this.className = className;
		this.methodName = name;
		
		if(Logger.isDebugEnabled()){
			Logger.debug("now instrumenting class: " + className + ", method: " + methodName);
		}
		
	}
	
	@Override
	public void onMethodEnter() {
	
		// Dispatcher Servlet: Enable tracing call
		if(className.equals("org/springframework/web/servlet/DispatcherServlet") && "render".equals(methodName)){
			mv.visitMethodInsn(INVOKESTATIC, "name/felixbecker/freemarkerdebug/FreemarkerInstructionsThreadLocal", "initialize", "()V", false);
		}
		
		// Freemarker Environment: enable instruction stack modification callbacks
		
		if(className.equals("freemarker/core/Environment")){
			
			if(methodName.equals("popElement")){
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, "freemarker/core/Environment", "instructionStack", "Ljava/util/ArrayList;");
				mv.visitMethodInsn(INVOKESTATIC, "name/felixbecker/freemarkerdebug/FreemarkerInstructionsThreadLocal", "popElement", "(Ljava/util/List;)V", false);
			} else if(methodName.equals("pushElement")) {
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKESTATIC, "name/felixbecker/freemarkerdebug/FreemarkerInstructionsThreadLocal", "pushElement", "(Ljava/lang/Object;)V", false);
			} else if(methodName.equals("replaceElementStackTop")){
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, "freemarker/core/Environment", "instructionStack", "Ljava/util/ArrayList;");
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKESTATIC, "name/felixbecker/freemarkerdebug/FreemarkerInstructionsThreadLocal", "replaceElementStackTop", "(Ljava/util/List;Ljava/lang/Object;)V", false);
			}
			
		}
		
		mv.visitLabel(l0);
		
	}

	public void insertTryFinallyOnTopLevel() {
		if (finallyInsertionDone) return;
		finallyInsertionDone=true;
		mv.visitTryCatchBlock(l0, l1, l1, null);
	}

	@Override
	public void visitTypeInsn(final int opcode, final String type) {
		insertTryFinallyOnTopLevel();
		super.visitTypeInsn(opcode, type);

	}

	@Override
	public void visitInsn(final int opcode) {
		insertTryFinallyOnTopLevel();
		super.visitInsn(opcode);
	}

	@Override
	public void visitVarInsn(final int opcode, final int var) {
		insertTryFinallyOnTopLevel();
		super.visitVarInsn(opcode, var);
	}

	@Override
	public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
		insertTryFinallyOnTopLevel();
		super.visitFieldInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitIntInsn(final int opcode, final int operand) {
		insertTryFinallyOnTopLevel();
		super.visitIntInsn(opcode, operand);
	}

	@Override
	public void visitLdcInsn(final Object cst) {
		insertTryFinallyOnTopLevel();
		super.visitLdcInsn(cst);
	}

	@Override
	public void visitMultiANewArrayInsn(final String desc, final int dims) {
		insertTryFinallyOnTopLevel();
		super.visitMultiANewArrayInsn(desc, dims);
	}

	@Override
	public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
		insertTryFinallyOnTopLevel();
		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}

	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
		insertTryFinallyOnTopLevel();
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
	}

	@Override
	public void visitJumpInsn(final int opcode, final Label label) {
		insertTryFinallyOnTopLevel();
		super.visitJumpInsn(opcode, label);
	}

	@Override
	public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
		insertTryFinallyOnTopLevel();
		super.visitLookupSwitchInsn(dflt, keys, labels);
	}

	@Override
	public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
		insertTryFinallyOnTopLevel();
		super.visitTableSwitchInsn(min, max, dflt, labels);
	}

	@Override
	public void onMethodExit(int opcode) {
		if (opcode==ATHROW) return; // don't instrument an ATHROW, it may still be catched in the application. It WILL be catched by the try/finally block inserted on the top level

		// Disable tracing call
		if(className.equals("org/springframework/web/servlet/DispatcherServlet") && "render".equals(methodName)){
			mv.visitMethodInsn(INVOKESTATIC, "name/felixbecker/freemarkerdebug/FreemarkerInstructionsThreadLocal", "printAndClear", "()V", false);
		}
	}

	@Override 
	public void visitMaxs(int maxStack, int maxLocals) {
		
		mv.visitLabel(l1);
		
		// Disable tracing call
		if(className.equals("org/springframework/web/servlet/DispatcherServlet") && "render".equals(methodName)){
			mv.visitMethodInsn(INVOKESTATIC, "name/felixbecker/freemarkerdebug/FreemarkerInstructionsThreadLocal", "printAndClear", "()V", false);
		}
		
		mv.visitInsn(ATHROW);
		super.visitMaxs(maxStack+4, maxLocals);
	}

}
