package name.felixbecker.freemarkerdebug.trace;

import freemarker.core.TemplateElement;

@SuppressWarnings("deprecation")
public abstract class Instruction {

	public final long instructionTime = System.currentTimeMillis();
	public final int instructionIdentityCode;
	protected final TemplateElement e;

	public Instruction(TemplateElement e) {
		this.e = e;
		this.instructionIdentityCode = System.identityHashCode(e);
	}

	@Override
	public abstract String toString();
}