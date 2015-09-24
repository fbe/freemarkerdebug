package name.felixbecker.freemarkerdebug.trace;

public abstract class Instruction {

	public final long instructionTime = System.currentTimeMillis();
	public final int instructionIdentityCode;
	protected final Object e;

	public Instruction(Object e) {
		this.e = e;
		this.instructionIdentityCode = System.identityHashCode(e);
	}

	@Override
	public abstract String toString();
}