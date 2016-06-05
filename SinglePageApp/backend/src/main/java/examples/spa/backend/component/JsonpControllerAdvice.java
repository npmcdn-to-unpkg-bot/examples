package examples.spa.backend.component;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

@ControllerAdvice
public class JsonpControllerAdvice extends AbstractJsonpResponseBodyAdvice {

	public JsonpControllerAdvice() {
		super("callback", "json_callback");
	}
}
