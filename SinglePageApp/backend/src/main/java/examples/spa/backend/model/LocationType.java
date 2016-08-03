package examples.spa.backend.model;

public enum LocationType {
	Unknown(0),
	Headquarter(1),
	Office(2),
	Home(3);
	
	public final int id;
	
	private LocationType(int id) {
		this.id = id;
	}
	
	public static LocationType fromId(Integer id) {
		if (id == null)
			return null;
		int tmpId = id;
		for (LocationType t : values()) {
			if (t.id == tmpId)
				return t;
		}
		return null;
	}
}
