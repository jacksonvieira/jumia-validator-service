package com.jumia.validator.config.dialect;

import org.hibernate.MappingException;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

/**
 * @author jacksonvieira
 * 
 *         we need to tell Hibernate how SQLite handles @Id columns, which we
 *         can do with a custom IdentityColumnSupport implementation
 */
public class JumiaSQLiteIdentityColumn extends IdentityColumnSupportImpl {

	@Override
	public boolean supportsIdentityColumns() {
		return true;
	}

	@Override
	public String getIdentitySelectString(String table, String column, int type) throws MappingException {
		return "select last_insert_rowid()";
	}

	@Override
	public String getIdentityColumnString(int type) throws MappingException {
		return "integer";
	}
}
