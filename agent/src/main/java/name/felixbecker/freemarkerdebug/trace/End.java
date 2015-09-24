package name.felixbecker.freemarkerdebug.trace;


public class End extends Instruction {

	public End(Object e) {
		super(e);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("["+instructionTime+"] END for " + e.getClass().getCanonicalName() + "("+instructionIdentityCode+")");
		// TODO MORE INFO
		return sb.toString();
	}
}