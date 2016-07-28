package examples.spa.backend.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.slavi.math.MathUtil;

import examples.spa.backend.misc.FilterItemHelper;
import examples.spa.backend.model.FieldDef;
import examples.spa.backend.model.FilterItemResponse;
import examples.spa.backend.model.ResponseWrapper;
import examples.spa.backend.myRest.DbField;
import examples.spa.backend.myRest.MyRestConfigField;
import examples.spa.backend.myRest.MyRestConfigItem;
import examples.spa.backend.myRest.MyRestConfigurer;
import examples.spa.backend.myRest.meta.MyTabColumnMeta;
import examples.spa.backend.myRest.meta.MyTableMeta;

@RestController
@RequestMapping("/rest")
public class MyRestController {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	MyRestConfigurer myRestConfigurer;
	
	QueryRunner queryRunner;
	
	MapListHandler mapListHandler = new MapListHandler();
	ScalarHandler<Integer> longHandler = new ScalarHandler<>();
	
	@PostConstruct
	void initialize() {
		queryRunner = new QueryRunner(dataSource);
	}

	@RequestMapping(value="{configName}/{id}", method=RequestMethod.GET)
	public ResponseEntity loadItem(
			@PathVariable("configName") String configName,
			@PathVariable("id") String id) throws Exception {
		MyTableMeta config = myRestConfigurer.getMyDatabaseMeta().tables.get(StringUtils.upperCase(configName));
		if (config == null) {
			// return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
			throw new Exception("Config " + configName + " not defined.");
		}
		String ids[] = StringUtils.split(StringUtils.trimToEmpty(id), ",");
		if (ids.length != config.bestRowIdColumns.size()) {
			throw new Exception("Invalid id " + id);
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ");
		sql.append(config.name);
		sql.append(" where ");
		String prefix = "";
		List params = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			String col = config.bestRowIdColumns.get(i);
			String val = StringUtils.trimToNull(ids[i]);
			sql.append(prefix);
			sql.append(col);
			if (val == null)
				sql.append(" is null");
			else {
				sql.append("= ?");
				params.add(val);
			}
			prefix = " and ";
		}
		List items = queryRunner.query(sql.toString(), mapListHandler, params.toArray());
		switch (items.size()) {
		case 0:
			return ResponseEntity.notFound().build();
		case 1:
			return ResponseEntity.ok().body(new ResponseWrapper(items.get(0)));
		default:
			return ResponseEntity.unprocessableEntity().build();
		}
	}
	
	@RequestMapping(value="{configName}", method=RequestMethod.DELETE)
	public void deleteItem(
			@PathVariable("configName") String configName,
			@PathVariable("id") String id) throws Exception {
		MyTableMeta config = myRestConfigurer.getMyDatabaseMeta().tables.get(StringUtils.upperCase(configName));
		if (config == null) {
			// return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
			throw new Exception("Config " + configName + " not defined.");
		}
		String ids[] = StringUtils.split(StringUtils.trimToEmpty(id), ",");
		if (ids.length != config.bestRowIdColumns.size()) {
			throw new Exception("Invalid id " + id);
		}
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ");
		sql.append(config.name);
		sql.append(" where ");
		String prefix = "";
		List params = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			String col = config.bestRowIdColumns.get(i);
			String val = StringUtils.trimToNull(ids[i]);
			sql.append(prefix);
			sql.append(col);
			if (val == null)
				sql.append(" is null");
			else {
				sql.append("= ?");
				params.add(val);
			}
			prefix = " and ";
		}
		queryRunner.update(sql.toString(), params.toArray());
	}
	
	private String objToString(Object o) {
		return o == null ? null : o.toString();
	}
	
	@RequestMapping(value="{configName}", method=RequestMethod.POST)
	public ResponseEntity saveItem(
			@PathVariable("configName") String configName,
			@RequestBody Map<?,?> item) throws Exception {
		MyTableMeta config = myRestConfigurer.getMyDatabaseMeta().tables.get(StringUtils.upperCase(configName));
		if (config == null) {
			// return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
			throw new Exception("Config " + configName + " not defined.");
		}
		
		// Convert to case insensitive map.
		Map<String, String> itemCI = new HashMap<>();
		for (Map.Entry i : item.entrySet()) {
			String key =  StringUtils.upperCase(objToString(i.getKey()));
			String val = StringUtils.trimToNull(objToString(i.getValue()));
			if (key != null)
				itemCI.put(key, val);
		}
		
		boolean keyHasNulls = false;
		boolean keyHasNullsThatAreNotNull = false;
		boolean keyHasNonNulls = false;
		List ids = new ArrayList<>();
		for (String col : config.bestRowIdColumns) {
			String val = itemCI.get(col);
			ids.add(val);
			if (val == null) {
				keyHasNulls = true;
				MyTabColumnMeta c = config.columns.get(col);
				if (!c.isNullable) {
					keyHasNullsThatAreNotNull = true;
				}
			} else {
				keyHasNonNulls = true;
			}
		}
		
		if (keyHasNonNulls && keyHasNullsThatAreNotNull) {
			// Incomplete primary/unique key
		}
		
		
		return ResponseEntity.ok(new ResponseWrapper(item));
/*		String ids[] = StringUtils.split(StringUtils.trimToEmpty(id), ",");
		if (ids.length != config.bestRowIdColumns.size()) {
			throw new Exception("Invalid id " + id);
		}
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ");
		sql.append(config.name);
		sql.append(" where ");
		String prefix = "";
		List params = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			String col = config.bestRowIdColumns.get(i);
			String val = StringUtils.trimToNull(ids[i]);
			sql.append(prefix);
			sql.append(col);
			if (val == null)
				sql.append(" is null");
			else {
				sql.append("= ?");
				params.add(val);
			}
			prefix = " and ";
		}
		queryRunner.update(sql.toString(), params.toArray());*/
	}
	
	@RequestMapping(value="{configName}", method=RequestMethod.GET)
	public ResponseEntity filterItems(
			@PathVariable("configName") String configName,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "20") int size,
			@RequestParam(defaultValue = "") String search,
			@RequestParam(defaultValue = "") String order,
			@RequestParam(defaultValue = "0") int draw) throws Exception {
		MyRestConfigItem config = myRestConfigurer.getConfigItem(configName);
		if (config == null) {
			// return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
			throw new Exception("Config " + configName + " not defined.");
		}
		FilterItemResponse r = find(config, page, size, search, order);
		r.draw = draw;
		return ResponseEntity.ok().body(r);
	}

	/**
	 * Order by format is: 
	 * <[sort_direction]column_name>[,<[sort_direction]column_name ...]
	 * Where sort_direction is "+" (ascending) or "-" (descending). Default is asccending.
	 * Example:
	 * -name,+createDate,id
	 */
	protected FilterItemResponse find(MyRestConfigItem config, int page, int size, String search, String orderBy) throws Exception {
		FilterItemResponse result = new FilterItemResponse<>();
		try {
			List paramFilterWhere = new ArrayList<>();
			String filterWhere = StringUtils.trimToNull(buildSqlWhere(config, paramFilterWhere));
			List paramAll = new ArrayList<>(paramFilterWhere);
			String searchWhere = StringUtils.trimToNull(buildSearchSqlWhere(config, paramAll, search));
			
			String where = filterWhere;
			if (filterWhere == null) {
				where = searchWhere;
			} else if (searchWhere != null) {
				where = "(" + filterWhere + ") AND (" + searchWhere + ")";
			}
			where = where == null ? "" : " WHERE " + where;
			String order = StringUtils.trimToEmpty(buildSqlOrderBy(config, orderBy));
	
			String fromClause = "(" + config.getSql() + ") t";
			String sql = "select * from " + fromClause + where + " " + order +
					" offset " + Math.max(page * size, 0) + " rows " +
					" fetch first " + MathUtil.clipValue(size, 1, FilterItemHelper.maxRecordsetRecords) + " rows only";
			logger.debug(sql);
			List items = queryRunner.query(sql, mapListHandler, paramAll.toArray());
		
			sql = "select count(*) from " + fromClause + where;
			logger.debug(sql);
			Integer filteredCount = queryRunner.query(sql, longHandler, paramAll.toArray());
			
			where = StringUtils.isEmpty(filterWhere) ? "" : (" WHERE " + filterWhere);
			sql = "select count(*) from " + fromClause + where;
			logger.debug(sql);
			Integer totalCount = queryRunner.query(sql, longHandler, paramFilterWhere.toArray());

			result.recordsFiltered = filteredCount.intValue();
			result.recordsTotal = totalCount.intValue();
			result.item = items;
		} catch (Exception e) {
			logger.error("Error filtering datatables", e);
			result.error = e.getMessage();
		}
		return result;
	}

	protected String buildSqlWhere(MyRestConfigItem config, List params) {
		return null;
	}
		
	protected String buildSearchSqlWhere(MyRestConfigItem config, List params, String searchString) {
		searchString = searchString == null ? "" : searchString.toUpperCase(); // No trim here
		String searchStringWildcards = searchString = "%" + searchString + "%";

		StringBuilder where = new StringBuilder();
		String prefix = "";
		
		if (!"".equals(searchString)) {
			for (DbField fd : config.getDbField()) {
				if (!fd.searchable)
					continue;
				where.append(prefix);
				if (fd.ignoreCase) {
					where.append("UPPER(");
				}
				if (String.class.getName().equals(fd.columnTypeName)) {
					where.append("t.");
					where.append(fd.columnName);
				} else {
					where.append("CAST(t.");
					where.append(fd.columnName);
					where.append(" AS CHAR(100))"); // TODO: This is hard-coded value - any integer/float/date is converted to char(100)
				}
				if (fd.ignoreCase) {
					where.append(")");
				}
				where.append("LIKE ?");
				if (fd.appendWildcards) {
					params.add(searchStringWildcards);
				} else {
					params.add(searchString);
				}
				prefix = " OR ";
			}
		}

		return where.toString();
	}
	
	protected String buildSqlOrderBy(MyRestConfigItem config, String orderBy) {
		Map<String, DbField> fields = new HashMap<>();
		for (DbField f : config.getDbField()) {
			fields.put(StringUtils.upperCase(f.columnName), f);
		}
		
		StringBuilder r = new StringBuilder();
		String prefix = "ORDER BY ";
		
		orderBy = StringUtils.trimToEmpty(orderBy);
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
			DbField fd = fields.remove(StringUtils.upperCase(item));
			if (fd == null)
				continue;
			r.append(prefix);
			r.append("t.");
			r.append(fd.columnName);
			if (descending)
				r.append(" DESC");
			prefix = ",";
		}
		
		for (MyRestConfigField i : config.getField()) {
			DbField fd = fields.remove(StringUtils.upperCase(i.name));
			if (fd == null)
				continue;
			r.append(prefix);
			r.append("t.");
			r.append(fd.columnName);
			if (FieldDef.Order.DESC.equals(fd.order))
				r.append(" DESC");
			prefix = ",";
		}
		
		return r.toString();
	}
	
}
