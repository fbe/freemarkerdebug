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
		INSTRUCTION_STACK.set(new ArrayList<Instruction>());
	}
	
	public static void clear() {
		for(Instruction i : INSTRUCTION_STACK.get()){
			System.out.println(i);
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
