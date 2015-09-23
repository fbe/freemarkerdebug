package name.felixbecker.freemarkerdebug.trace;

import freemarker.core.TemplateElement;

@SuppressWarnings("deprecation")
public class Start extends Instruction {

	public Start(TemplateElement e) {
		super(e);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("["+instructionTime+"] START for " + e.getClass().getCanonicalName() + "("+instructionIdentityCode+")" + " -" + e);
		// TODO MORE INFO
		return sb.toString();
	}

}