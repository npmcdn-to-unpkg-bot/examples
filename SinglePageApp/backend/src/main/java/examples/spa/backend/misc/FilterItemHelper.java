package examples.spa.backend.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slavi.math.MathUtil;

import examples.spa.backend.component.EntityWithIdJpa;
import examples.spa.backend.model.FieldDef;
import examples.spa.backend.model.FilterItemResponse;

/**
 */
public class FilterItemHelper<T> {
	protected final Logger logger;

	protected EntityManager em;
	protected final Class<T> entityClass;
	
	protected List<FieldDefinition> orderByFields;
	protected Map<String, FieldDefinition> fields;
	
	public FilterItemHelper(EntityManager em, Class<T> entityClass) {
		this.em = em;
		this.entityClass = entityClass;
		logger = LoggerFactory.getLogger(this.getClass());
		initializeFieldDefinitions();
	}
	
	private FieldDefinition getFieldDef(String attributeName, Class fieldClass) {
		String getMethodName = "get" + attributeName;
		String isMethodName = Boolean.class.equals(fieldClass) || boolean.class.equals(fieldClass) ? "is" + attributeName : null;
		Class cl = entityClass;
		while (cl != null) {
			// First check getter methods. This is the way ot override an annotation
			for (Method m : cl.getDeclaredMethods()) {
				String name = m.getName();
				if (m.getReturnType().equals(fieldClass) &&
					(name.equalsIgnoreCase(getMethodName) || name.equalsIgnoreCase(isMethodName))) {
					FieldDef fd = m.getAnnotation(FieldDef.class);
					if (fd != null) {
						return new FieldDefinition(attributeName, fd);
					}
				}
			}

			for (Field f : cl.getDeclaredFields()) {
				if (f.getName().equalsIgnoreCase(attributeName) && f.getType().equals(fieldClass)) {
					FieldDef fd = f.getAnnotation(FieldDef.class);
					if (fd != null) {
						return new FieldDefinition(attributeName, fd);
					}
				}
			}
			cl = cl.getSuperclass();
		}
		return new FieldDefinition(attributeName);
	}
	
	protected void initializeFieldDefinitions() {
		orderByFields = new ArrayList<>();
		fields = new HashMap<>();

		EntityType etype = em.getMetamodel().entity(entityClass);
		for (Attribute attr : (Set<Attribute>) etype.getAttributes()) {
			String name = attr.getName();
			String uname = name.toUpperCase();
			if (fields.containsKey(uname))
				continue;
			FieldDefinition fd = getFieldDef(name, attr.getJavaType());
			fields.put(uname, fd);
			if (fd.order != FieldDef.Order.NOT_SORTED)
				orderByFields.add(fd);
		}
		
		orderByFields.sort(new Comparator<FieldDefinition>() {
			public int compare(FieldDefinition o1, FieldDefinition o2) {
				int r = Integer.compare(o1.orderPrecednce, o2.orderPrecednce);
				if (r == 0)
					r = o1.name.compareToIgnoreCase(o2.name);
				return r;
			}
		});
	}
	
	/**
	 * Order by format is: 
	 * <column_name[|sort_direction]>[,<column_name[|sort_direction]> ...]
	 * Where sort_direction is "asc" or "desc". Default is "asc".
	 * Example:
	 * name|asc, createDate,id|desc
	 */
	public String getOrderBy() {
		return "";
	}
	
	protected String buildSqlOrderBy(String orderBy) {
		Map<String, FieldDefinition> fields = new HashMap<>(this.fields);
		StringBuilder r = new StringBuilder();
		String prefix = "ORDER BY ";
		
		orderBy = StringUtils.trimToEmpty(orderBy) + "," + StringUtils.trimToEmpty(getOrderBy());
		for (String item : orderBy.split(",")) {
			item = StringUtils.trimToNull(item);
			if (item == null)
				continue;
			String split[] = item.split("|");
			if (split.length == 0)
				continue;
			String name = StringUtils.trimToNull(split[0]);
			if (name == null)
				continue;
			FieldDefinition fd = fields.remove(name.toUpperCase());
			if (fd == null)
				continue;
			r.append(prefix);
			r.append("t.");
			r.append(name);
			boolean descending = "DESC".equalsIgnoreCase(split.length >= 2 ? StringUtils.trimToNull(split[1]) : null);
			if (descending)
				r.append(" DESC");
			prefix = ",";
		}
		
		for (FieldDefinition i : orderByFields) {
			FieldDefinition fd = fields.remove(i);
			if (fd == null)
				continue;
			r.append(prefix);
			r.append("t.");
			r.append(fd.name);
			if (FieldDef.Order.DESC.equals(fd.order))
				r.append(" DESC");
			prefix = ",";
		}
		
		return r.toString();
	}
	
	public String buildSqlWhere(Map<String, Object> paramMap) {
		return "";
	}
	
	public String buildSearchSqlWhere(Map<String, Object> paramMap, String searchString) {
		searchString = searchString == null ? "" : searchString.toUpperCase(); // No trim here
		String searchStringWildcards = searchString = "%" + searchString + "%";

		StringBuilder where = new StringBuilder();
		String prefix = "";
		EntityType etype = em.getMetamodel().entity(entityClass);
		
		if (!"".equals(searchString)) {
			for (FieldDefinition fd : fields.values()) {
				if (!fd.searchable)
					continue;
				where.append(prefix);
				if (fd.ignoreCase) {
					where.append("UPPER(");
				}
				if (String.class.equals(etype.getAttribute(fd.name).getJavaType())) {
					where.append("t.");
					where.append(fd.name);
				} else {
					where.append("CAST(t.");
					where.append(fd.name);
					where.append(" AS CHAR(100))"); // TODO: This is hard-coded value - any integer/float/date is converted to char(100)
				}
				if (fd.ignoreCase) {
					where.append(")");
				}
				if (fd.appendWildcards) {
					where.append("LIKE :searchWildcard");
					paramMap.put("searchWildcard", searchStringWildcards);
				} else {
					where.append("LIKE :search");
					paramMap.put("search", searchString);
				}
				prefix = " OR ";
			}
		}

		return where.toString();
	}
	
	public String getFrom() {
		EntityType etype = em.getMetamodel().entity(entityClass);
		return etype.getName() + " t";
	}
	
	public FilterItemResponse<T> find(int page, int size, String search, String orderBy) throws Exception {
		FilterItemResponse<T> result = new FilterItemResponse<>();
		try {
			Map<String, Object> paramMapFilterWhere = new HashMap<>();
			String filterWhere = StringUtils.trimToNull(buildSqlWhere(paramMapFilterWhere));
			Map<String, Object> paramMapAll = new HashMap<>(paramMapFilterWhere);
			String searchWhere = StringUtils.trimToNull(buildSearchSqlWhere(paramMapAll, search));
			
			String where = filterWhere;
			if (filterWhere == null) {
				where = searchWhere;
			} else if (searchWhere != null) {
				where = "(" + filterWhere + ") AND (" + searchWhere + ")";
			}
			where = where == null ? "" : " WHERE " + where;
			String order = StringUtils.trimToEmpty(buildSqlOrderBy(orderBy));
	
			String fromClause = getFrom();
			TypedQuery<T> q = em.createQuery("SELECT t from " + fromClause + where + " " + order, entityClass);
			for (Map.Entry<String, Object> i : paramMapAll.entrySet()) {
				q.setParameter(i.getKey(), i.getValue());
			}
	
			q.setMaxResults(MathUtil.clipValue(size, 0, EntityWithIdJpa.maxRecordsetRecords));
			q.setFirstResult(Math.max(page * size, 0));
			List<T> items = q.getResultList();
		
			TypedQuery<Long> countQ = em.createQuery("SELECT count(t) from " + fromClause + where, Long.class);
			for (Map.Entry<String, Object> i : paramMapAll.entrySet()) {
				countQ.setParameter(i.getKey(), i.getValue());
			}
			Long filteredCount = countQ.getSingleResult();
		
			where = StringUtils.isEmpty(filterWhere) ? "" : (" WHERE " + filterWhere);
			countQ = em.createQuery("SELECT count(t) from " + fromClause + where, Long.class);
			for (Map.Entry<String, Object> i : paramMapFilterWhere.entrySet()) {
				countQ.setParameter(i.getKey(), i.getValue());
			}
			Long totalCount = countQ.getSingleResult();
				
			result.recordsFiltered = filteredCount.intValue();
			result.recordsTotal = totalCount.intValue();
			result.data = items;
		} catch (Exception e) {
			logger.error("Error filtering datatables", e);
			result.error = e.getMessage();
		}
		return result;
	}
}
