package name.felixbecker.freemarkerdebug;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class SlowSampleService {
	public String getSlowComputationResult(){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date().toString();
	}
}
