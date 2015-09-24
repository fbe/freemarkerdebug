package name.felixbecker.freemarkerdebug;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import name.felixbecker.freemarkerdebug.trace.End;
import name.felixbecker.freemarkerdebug.trace.Instruction;
import name.felixbecker.freemarkerdebug.trace.Start;

@SuppressWarnings("rawtypes") 
public class FreemarkerInstructionsThreadLocal {

	private static int ALERT_THRESHOLD_IN_MS = 500;
	
	private static final ThreadLocal<List<Instruction>> INSTRUCTION_STACK = new ThreadLocal<List<Instruction>>();
	
	public static void initialize(){
		INSTRUCTION_STACK.set(new ArrayList<Instruction>());
	}
	
	public static void printAndClear() {
		
		final List<Instruction> instructions = INSTRUCTION_STACK.get();

		if(instructions.size() > 0){

			
			long startMs = instructions.get(0).instructionTime;
			long endMs = instructions.get(instructions.size() -1).instructionTime;
			long delta = endMs - startMs;
			
			if(delta >= ALERT_THRESHOLD_IN_MS){
				
				Logger.info("Freemarker render phase took more than the configured " + ALERT_THRESHOLD_IN_MS + "ms. Printing call trace");
			
				int maxTimeLetters =(delta+ "").length();
				for(Instruction i : instructions){
					System.out.println(instructionToString(i, startMs, maxTimeLetters));
				}
				
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
	
	public static String instructionToString(Instruction i, long startMs, int maxTimeLetters){
		
		final String templateElementClassName = i.templateElement.getClass().getCanonicalName();
		final long callMs = i.instructionTime - startMs;
		final String callMsFormatted = String.format("%0"+maxTimeLetters+"d", callMs);
		
		final StringBuilder sb = new StringBuilder("["+callMsFormatted + "ms] - " + String.format("%1$5s", i.getClass().getSimpleName().toUpperCase()) + " - " + templateElementClassName + " ");

		
		
		switch(templateElementClassName){
		
			case "freemarker.core.TextBlock":
			case "freemarker.core.MixedContent":
				sb.append(toMax50CharsWith3DotSuffix(i.templateElement));
				break;
				
			case "freemarker.core.Macro":
				sb.append("Name: " + getFieldFromObject("name", i.templateElement));
				break;
		
			default:
				sb.append(": " + toMax50CharsWith3DotSuffix(i.templateElement)); 
				break;
			}
		
		return sb.toString();
	}
	
	
	private static final ConcurrentHashMap<String, Field> REFLECTION_CACHE_MAP = new ConcurrentHashMap<>();
	
	private static String toMax50CharsWith3DotSuffix(Object o){
		final String stringValue = o.toString();
		final String shortened = stringValue.length() > 50 ? stringValue.substring(0,47) + "..." : stringValue ;
		
		return shortened.replaceAll("\\n","\\\\n");
	}
	
	private static Object getFieldFromObject(String fieldName, Object o){
		
		final String cacheKey = o.getClass().getCanonicalName()+"."+fieldName;
		
		try {
			
			if(!REFLECTION_CACHE_MAP.containsKey(cacheKey)){
				final Field f = o.getClass().getDeclaredField(fieldName);
				f.setAccessible(true);
				REFLECTION_CACHE_MAP.put(cacheKey, f);
			}
	
			return REFLECTION_CACHE_MAP.get(cacheKey).get(o);
			
		} catch(Exception e){
			return "Error looking up fieldName of Object " + o.getClass().getCanonicalName() + " - " + e.getMessage();
		}
		
	}
	
	
}
