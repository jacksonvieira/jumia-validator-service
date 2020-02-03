package com.jumia.validator.config.dialect;

import java.sql.Types;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;

/**
 * @author jacksonvieira
 * 
 *         Hibernate doesn't ship with a Dialect for SQLite. We need to create
 *         one ourselves.
 * 
 *         We needs to extend org.hibernate.dialect.Dialect class to register
 *         the data types provided by SQLite
 *
 */
public class JumiaSQLiteDialect extends Dialect {

	public JumiaSQLiteDialect() {
		registerColumnType(Types.BIT, "integer");
		registerColumnType(Types.TINYINT, "tinyint");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.INTEGER, "integer");
		registerColumnType(Types.BIGINT, "bigint");
		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.REAL, "real");
		registerColumnType(Types.DOUBLE, "double");
		registerColumnType(Types.NUMERIC, "numeric");
		registerColumnType(Types.DECIMAL, "decimal");
		registerColumnType(Types.CHAR, "char");
		registerColumnType(Types.VARCHAR, "varchar");
		registerColumnType(Types.LONGVARCHAR, "longvarchar");
		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "timestamp");
		registerColumnType(Types.BINARY, "blob");
		registerColumnType(Types.VARBINARY, "blob");
		registerColumnType(Types.LONGVARBINARY, "blob");
		registerColumnType(Types.BLOB, "blob");
		registerColumnType(Types.CLOB, "clob");
		registerColumnType(Types.BOOLEAN, "integer");
	}

	public IdentityColumnSupport getIdentityColumnSupport() {
		return new JumiaSQLiteIdentityColumn();
	}

	public boolean hasAlterTable() {
		return false;
	}

	public boolean dropConstraints() {
		return false;
	}

	public String getDropForeignKeyString() {
		return "";
	}

	public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable,
			String[] primaryKey, boolean referencesPrimaryKey) {
		return "";
	}

	public String getAddPrimaryKeyConstraintString(String constraintName) {
		return "";
	}

	public String getForUpdateString() {
		return "";
	}

	public String getAddColumnString() {
		return "add column";
	}

	public boolean supportsOuterJoinForUpdate() {
		return false;
	}

	public boolean supportsIfExistsBeforeTableName() {
		return true;
	}

	public boolean supportsCascadeDelete() {
		return false;
	}
}
