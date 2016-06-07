package examples.spa.backend.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FilterItemResponse<T> {
	public int draw;
	public int recordsTotal;
	public int recordsFiltered;
	public List<T> item;
	public String error;
}
