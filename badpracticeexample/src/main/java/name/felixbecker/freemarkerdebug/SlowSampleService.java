package name.felixbecker.freemarkerdebug;

import java.util.Date;
import java.util.Random;

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
	
	public boolean slowRandomBoolean() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Random().nextInt() % 2 == 0;
	}
}
