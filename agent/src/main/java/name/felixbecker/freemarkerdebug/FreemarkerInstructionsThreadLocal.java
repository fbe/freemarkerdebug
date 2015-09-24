package name.felixbecker.freemarkerdebug;

import java.util.ArrayList;
import java.util.List;

import name.felixbecker.freemarkerdebug.trace.End;
import name.felixbecker.freemarkerdebug.trace.Instruction;
import name.felixbecker.freemarkerdebug.trace.Start;

@SuppressWarnings("rawtypes") 
public class FreemarkerInstructionsThreadLocal {

	
	private static final ThreadLocal<List<Instruction>> INSTRUCTION_STACK = new ThreadLocal<List<Instruction>>();
	
	public static void initialize(){
		System.out.println("=============> Initializing Thread Local initialize!");
		INSTRUCTION_STACK.set(new ArrayList<Instruction>());
	}
	
	public static void printAndClear() {
		System.out.println("=============> Initializing Thread Local printAndClear!");

		final List<Instruction> instructions = INSTRUCTION_STACK.get();

		if(instructions.size() > 0){
			
			long startMs = instructions.get(0).instructionTime;
			
			for(Instruction i : instructions){
				System.out.println("["+(i.instructionTime - startMs)+"" + "ms] --- " + i);
			}
		}
		
		INSTRUCTION_STACK.remove();
	}
	
	public static void start(Object e){
		if(!isExcludedFromTrace(e)){
			INSTRUCTION_STACK.get().add(new Start(e));
		}
	}
	
	// TODO count excludes?
	private static boolean isExcludedFromTrace(Object e) {
		return "freemarker.core.TextBlock".equals(e.getClass().getCanonicalName()) && false; // currently dump everything
	}

	public static void end(Object e){
		if(!isExcludedFromTrace(e)){
			INSTRUCTION_STACK.get().add(new End(e));
		}
	}
	
	public static void popElement(List instructionStack){
    	Object element = instructionStack.get(instructionStack.size() - 1);
    	FreemarkerInstructionsThreadLocal.end(element);
	}

	public static void replaceElementStackTop(List instructionStack, Object instr) {
    	Object old = instructionStack.get(instructionStack.size() - 1);
    	FreemarkerInstructionsThreadLocal.end(old);
    	FreemarkerInstructionsThreadLocal.start(instr);		
	}

	public static void pushElement(Object element) {
    	FreemarkerInstructionsThreadLocal.start(element);
	}
	
	
	
	
}
