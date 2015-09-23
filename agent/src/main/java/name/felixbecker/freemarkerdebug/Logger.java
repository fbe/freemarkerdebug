package name.felixbecker.freemarkerdebug;

public class Logger {
	
	private static volatile boolean debugEnabled;
	
	public static void debug(String message){
		if(debugEnabled){
			System.out.println("[Snailhunter DEBUG] " + message);
		}
	}
	
	public static void info(String message){
		System.out.println("[SnailHunter INFO] " + message);
	}

	
	public static boolean isDebugEnabled() {
		return Logger.debugEnabled;
	}

	public static void setDebugEnabled(boolean debugEnabled) {
		Logger.debugEnabled = debugEnabled;
	}

	public static void error(String message, Throwable e) {
		System.err.println("[SnailHunter ERROR] " + message);
		e.printStackTrace();
	}

	
}
