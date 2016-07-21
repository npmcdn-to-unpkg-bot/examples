package examples.spa.backend.misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.EntityType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slavi.math.MathUtil;

import examples.spa.backend.model.FieldDef;
import examples.spa.backend.model.FilterItemResponse;

/**
 */
public class FilterItemHelper<T> {
	public static final int maxRecordsetRecords = 100;

	protected final Logger logger;

	protected EntityManager em;
	protected FilterItemClassData<T> classData;
	
	private static ConcurrentMap<Class, FilterItemClassData> filterItemHelpers = new ConcurrentHashMap<>();
	
	public FilterItemHelper(EntityManager em, Class<T> entityClass) {
		this.em = em;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.classData = filterItemHelpers.computeIfAbsent(entityClass, klazz -> new FilterItemClassData<>(em, klazz));
	}
	
	public String getOrderBy() {
		return "";
	}
	
	protected String buildSqlOrderBy(String orderBy) {
		Map<String, FieldDefinition> fields = new HashMap<>(this.classData.fields);
		StringBuilder r = new StringBuilder();
		String prefix = "ORDER BY ";
		
		orderBy = StringUtils.trimToEmpty(orderBy) + "," + StringUtils.trimToEmpty(getOrderBy());
		for (String item : orderBy.split(",")) {
			item = StringUtils.trimToNull(item);
			if (item == null)
				continue;
			boolean descending = false;
			switch (item.charAt(0)) {
			case '-':
				descending = true;
				// no break here
			case '+':
				item = StringUtils.trimToNull(item.substring(1));
				// no break needed
			}
			if (item == null)
				continue;
			FieldDefinition fd = fields.remove(item.toUpperCase());
			if (fd == null)
				continue;
			r.append(prefix);
			r.append("t.");
			r.append(item);
			if (descending)
				r.append(" DESC");
			prefix = ",";
		}
		
		for (FieldDefinition i : classData.orderByFields) {
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
		EntityType etype = em.getMetamodel().entity(classData.entityClass);
		
		if (!"".equals(searchString)) {
			for (FieldDefinition fd : classData.fields.values()) {
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
		EntityType etype = em.getMetamodel().entity(classData.entityClass);
		return etype.getName() + " t";
	}
	
	/**
	 * Order by format is: 
	 * <[sort_direction]column_name>[,<[sort_direction]column_name ...]
	 * Where sort_direction is "+" (ascending) or "-" (descending). Default is asccending.
	 * Example:
	 * -name,+createDate,id
	 */
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
			TypedQuery<T> q = em.createQuery("SELECT t from " + fromClause + where + " " + order, classData.entityClass);
			for (Map.Entry<String, Object> i : paramMapAll.entrySet()) {
				q.setParameter(i.getKey(), i.getValue());
			}
	
			q.setMaxResults(MathUtil.clipValue(size, 0, maxRecordsetRecords));
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
			result.item = items;
		} catch (Exception e) {
			logger.error("Error filtering datatables", e);
			result.error = e.getMessage();
		}
		return result;
	}
}
