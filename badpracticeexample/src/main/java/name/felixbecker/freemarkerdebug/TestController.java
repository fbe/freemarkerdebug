package name.felixbecker.freemarkerdebug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class TestController {
	
	@Autowired private SlowSampleService slowSampleService;
	@Autowired private CrashService crashService;
	
	@RequestMapping("/slow")
	public ModelAndView test(){
		ModelAndView mav = new ModelAndView("slow");
		// malicious code - add callable service to model (happens in production systems :( )
		mav.addObject("slowSampleService", slowSampleService);
		return mav;
	}
	
	@RequestMapping("/crash")
	public ModelAndView callCrashFunctionForStackTrace(){
		ModelAndView mav = new ModelAndView("crash");
		mav.addObject("crashService", crashService);
		return mav;
	}
	
	@RequestMapping("/awesome")
	public ModelAndView awesome(){
		return new ModelAndView("awesome");
	}
}
