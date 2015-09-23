package name.felixbecker.freemarkerdebug.trace;

import freemarker.core.TemplateElement;

@SuppressWarnings("deprecation")
public class End extends Instruction {

	public End(TemplateElement e) {
		super(e);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("["+instructionTime+"] END for " + e.getClass().getCanonicalName() + "("+instructionIdentityCode+")");
		// TODO MORE INFO
		return sb.toString();
	}
}