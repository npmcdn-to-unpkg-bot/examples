package examples.spa.backend.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocationTypeConverter implements AttributeConverter<LocationType, Integer> {
	@Override
	public Integer convertToDatabaseColumn(LocationType attribute) {
		return attribute == null ? null : attribute.id;
	}

	@Override
	public LocationType convertToEntityAttribute(Integer dbData) {
		return LocationType.fromId(dbData);
	}
}
