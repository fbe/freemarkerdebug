package name.felixbecker.freemarkerdebug.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FMTraceContext {
	
	public final String rootTemplateName;
	
	public FMTraceContext(String rootTemplateName){
		this.rootTemplateName = rootTemplateName;
		
	}
	
	public final List<Instruction> instructionList = new ArrayList<>();
	public final String traceUniqueId = UUID.randomUUID().toString();
	
	
}
