package name.felixbecker.freemarkerdebug.trace;

public abstract class Instruction {

	public final long instructionTime = System.currentTimeMillis();
	public final int instructionIdentityCode;
	public final Object templateElement;

	public Instruction(Object e) {
		this.templateElement = e;
		this.instructionIdentityCode = System.identityHashCode(e);
	}

	@Override
	public abstract String toString();
}