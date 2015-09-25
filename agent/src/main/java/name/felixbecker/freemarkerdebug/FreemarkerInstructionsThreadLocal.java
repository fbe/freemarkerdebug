package name.felixbecker.freemarkerdebug;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import name.felixbecker.freemarkerdebug.trace.End;
import name.felixbecker.freemarkerdebug.trace.FMTraceContext;
import name.felixbecker.freemarkerdebug.trace.Instruction;
import name.felixbecker.freemarkerdebug.trace.Start;

@SuppressWarnings("rawtypes") 
public class FreemarkerInstructionsThreadLocal {

	private static int ALERT_THRESHOLD_IN_MS = 500;
	
	private static final ThreadLocal<FMTraceContext> INSTRUCTION_STACK = new ThreadLocal<FMTraceContext>();
	
	public static void initialize(String rootTemplateName){
		INSTRUCTION_STACK.set(new FMTraceContext(rootTemplateName));
	}
	
	public static void printAndClear() {
		
		final FMTraceContext context = INSTRUCTION_STACK.get();
		
		final List<Instruction> instructions = INSTRUCTION_STACK.get().instructionList;

		if(instructions.size() > 0){

			
			long startMs = instructions.get(0).instructionTime;
			long endMs = instructions.get(instructions.size() -1).instructionTime;
			long delta = endMs - startMs;
			
			if(delta >= ALERT_THRESHOLD_IN_MS){
				
				Logger.info("["+context.traceUniqueId+"] Freemarker render phase for template "+context.rootTemplateName+" took more than the configured " + ALERT_THRESHOLD_IN_MS + "ms. Printing call trace");
			
				int maxTimeLetters =(delta+ "").length();
				for(Instruction i : instructions){
					System.out.println(instructionToString(i, startMs, maxTimeLetters, context));
				}
				
			}
			
		}
		
		INSTRUCTION_STACK.remove();
	}
	
	public static void start(Object e){
		if(!isExcludedFromTrace(e)){
			INSTRUCTION_STACK.get().instructionList.add(new Start(e));
		}
	}
	
	// TODO count excludes?
	private static boolean isExcludedFromTrace(Object e) {
		return "freemarker.core.TextBlock".equals(e.getClass().getCanonicalName()) && false; // currently dump everything
	}

	public static void end(Object e){
		if(!isExcludedFromTrace(e)){
			INSTRUCTION_STACK.get().instructionList.add(new End(e));
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
	
	public static String instructionToString(Instruction i, long startMs, int maxTimeLetters, FMTraceContext context){
		
		final String templateElementClassName = i.templateElement.getClass().getCanonicalName();
		final long callMs = i.instructionTime - startMs;
		final String callMsFormatted = String.format("%0"+maxTimeLetters+"d", callMs);
		
		final StringBuilder sb = new StringBuilder("["+context.traceUniqueId+"] ["+callMsFormatted + "ms] - " + String.format("%1$5s", i.getClass().getSimpleName().toUpperCase()) + " - " + templateElementClassName + "(" + System.identityHashCode(i.templateElement)+")");

		
		if(i instanceof Start){

			
			Object template = getFieldFromObject("template", i.templateElement);
			String templateName = getFieldFromObject("name", template).toString();
			
			sb.append(" - " + templateName);
			
			final StringBuilder expressionContent = new StringBuilder();
			
			switch(templateElementClassName){
	
			case "freemarker.core.BlockAssignment":
			case "freemarker.core.BodyInstruction":
			case "freemarker.core.BreakInstruction":
			case "freemarker.core.Include":
			case "freemarker.core.LibraryLoad":
			case "freemarker.core.PropertySetting":
			case "freemarker.core.ReturnInstruction":
				expressionContent.append(i.templateElement);
				break;
			
			case "freemarker.core.Comment":
			case "freemarker.core.TextBlock":
				expressionContent.append(toMax50CharsWith3DotSuffixAndWhitespaceTrim(i.templateElement));
				break;
				
			case "freemarker.core.CompressedBlock": // white space compressor block
			case "freemarker.core.IfBlock": // container for conditional block, output only for conditional block
			case "freemarker.core.MixedContent":
			case "freemarker.core.UnifiedCall":
				// append no content, mostly for container elements with no side effects
				break;
				
			case "freemarker.core.ConditionalBlock":
				expressionContent.append("Condition: " + getFieldFromObject("condition", i.templateElement));
				break;
				
			case "freemarker.core.DollarVariable":
				expressionContent.append("Name: " + getFieldFromObject("expression", i.templateElement));
				break;
				
			case "freemarker.core.Assignment":
				expressionContent.append("VariableName: " + getFieldFromObject("variableName", i.templateElement));
				break;
				
			case "freemarker.core.IteratorBlock":
				expressionContent.append("ListExpression: " + getFieldFromObject("listExpression", i.templateElement) + " LoopVariableName: " + getFieldFromObject("loopVariableName", i.templateElement));
				// TODO CHECK NETT?
				break;
				
			case "freemarker.core.Macro":
				expressionContent.append("Name: " + getFieldFromObject("name", i.templateElement));
				//  TODO check, mehr ausgeben (alle parames etc!) ??
				break;
				
	
			default:
				expressionContent.append("UNKNOWN TEMPLATE ELEMENT - DUMPING 50 CHARS - FIXME: " + toMax50CharsWith3DotSuffixAndWhitespaceTrim(i.templateElement));
				break;
			}
			
			if(expressionContent.length() > 0){
				expressionContent.insert(0, " - ");
			}
			
			sb.append(expressionContent);
		}
		
		return sb.toString();
	}
	
	
	private static final ConcurrentHashMap<String, Field> REFLECTION_CACHE_MAP = new ConcurrentHashMap<>();
	
	private static String toMax50CharsWith3DotSuffixAndWhitespaceTrim(Object o){
		
		final String trimmed = o.toString().replaceAll("\\s+", " "); // remove double spaces / tabs, replace with one whitespace.
		final String shortened = trimmed.length() > 50 ? trimmed.substring(0,47) + "..." : trimmed ;
		
		return shortened;
	}
	
	private static Object getFieldFromObject(String fieldName, Object o){
		
		final String cacheKey = o.getClass().getCanonicalName()+"."+fieldName;
		
		try {
			
			if(!REFLECTION_CACHE_MAP.containsKey(cacheKey)){
				final Field f = getField(fieldName, o);
				REFLECTION_CACHE_MAP.put(cacheKey, f);
			}
	
			return REFLECTION_CACHE_MAP.get(cacheKey).get(o);
			
		} catch(Exception e){
			return "Error looking up fieldName of Object " + o.getClass().getCanonicalName() + " - " + e.getMessage();
		}
		
	}

	private static Field getField(String fieldName, Object o) throws NoSuchFieldException {
		
		Class objectClass = o.getClass();
		
		while(objectClass != null){
			
			try {
				final Field f = objectClass.getDeclaredField(fieldName);
				f.setAccessible(true);
				return f;
			} catch(NoSuchFieldException e) {
				// do nothing, try super class again.
			}
			
			objectClass = objectClass.getSuperclass();
		}
		
		throw new NoSuchFieldException();
		
	}

	
}
