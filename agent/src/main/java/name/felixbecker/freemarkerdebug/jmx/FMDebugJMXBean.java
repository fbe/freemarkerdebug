package name.felixbecker.freemarkerdebug.jmx;

public interface FMDebugJMXBean {
	public long getAlertThresholdInMs();
	public void setAlertThresholdInMs(long threshold);
}
