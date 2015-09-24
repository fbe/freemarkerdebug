package name.felixbecker.freemarkerdebug.trace;

public class Start extends Instruction {

	public Start(Object e) {
		super(e);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("["+instructionTime+"] START for " + templateElement.getClass().getCanonicalName() + "("+instructionIdentityCode+")" + " -" + templateElement);
		// TODO MORE INFO
		return sb.toString();
	}

}