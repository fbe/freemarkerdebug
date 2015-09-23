package name.felixbecker.freemarkerdebug;

import java.util.ArrayList;
import java.util.List;

import name.felixbecker.freemarkerdebug.trace.End;
import name.felixbecker.freemarkerdebug.trace.Instruction;
import name.felixbecker.freemarkerdebug.trace.Start;
import freemarker.core.TemplateElement;
import freemarker.core.TextBlock;


@SuppressWarnings("deprecation")
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
	
	public static void start(TemplateElement e){
		if(!isExcludedFromTrace(e)){
			INSTRUCTION_STACK.get().add(new Start(e));
		}
	}
	
	// TODO count excludes?
	private static boolean isExcludedFromTrace(TemplateElement e) {
		return e instanceof TextBlock;
	}

	public static void end(TemplateElement e){
		if(!isExcludedFromTrace(e)){
			INSTRUCTION_STACK.get().add(new End(e));
		}
	}
	
	
	
	
}
