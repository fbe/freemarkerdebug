package name.felixbecker.freemarkerdebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired private SlowSampleService slowSampleService;
	
	@RequestMapping
	public ModelAndView test(){
		ModelAndView mav = new ModelAndView("test");
		mav.addObject("test", "xxx");
		// malicious code - add callable service to model (happens in production systems :( )
		mav.addObject("slowSampleService", slowSampleService);
		return mav;
	}
}
