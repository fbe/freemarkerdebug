package name.felixbecker.freemarkerdebug;

import org.springframework.stereotype.Service;

@Service
public class CrashService {
	
	public String getCrash(){
		throw new RuntimeException("Requested a crash - here it is");
	}
	
}
