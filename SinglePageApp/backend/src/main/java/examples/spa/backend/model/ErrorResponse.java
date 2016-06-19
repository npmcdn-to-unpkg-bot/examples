package examples.spa.backend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

@XmlRootElement(name = "error")
@Access(AccessType.FIELD)
public class ErrorResponse {
	public List<String> errors = new ArrayList<>();
	public Map<String, List<String>> fieldErrors = new HashMap<>();
	
	public List<String> getList(String field) {
		if (StringUtils.isEmpty(field))
			return errors;
		List<String> r = fieldErrors.get(field);
		if (r == null) {
			r = new ArrayList<>();
			fieldErrors.put(field, r);
		}
		return r;
	}
}
