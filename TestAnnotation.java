package com.db.run;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.db.annotation.Constraints;
import com.db.annotation.DBTable;
import com.db.annotation.Id;
import com.db.annotation.SqlInteger;
import com.db.annotation.SqlString;
import com.db.entity.Member;

public class TestAnnotation {

	public static void main(String[] args) {
		TestAnnotation tc = new TestAnnotation();
		tc.executeCreateDB(Member.class);
	}

	public void executeCreateDB(Class<?> entity) {
		String sqlStr = explainAnnotation(entity);
		System.out.println(sqlStr);
		Connection con = getConnection();
		PreparedStatement psql = null;
		if (con != null && !sqlStr.equals("error")) {
			try {
				psql = con.prepareStatement(sqlStr);
				psql.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (psql != null)
						psql.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (con != null)
						psql.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			System.out.println("failure to...");
	}

	private String explainAnnotation(Class<?> entity) {
		DBTable dbtable = entity.getAnnotation(DBTable.class);
		if (dbtable == null) {
			System.out.println("No DBTable annotation in class" + entity.getName());
			return "error";
		} else {
			// 获取注解name值 表名称
			String tableName = dbtable.name();
			// 当前name值不存在，直接使用类名作为表名称
			if (tableName.length() < 1) {
				tableName = entity.getName().toUpperCase(); // 转大写
			}
			// 准备处理字段注解
			List<String> columnDefs = new ArrayList<>();
			for (Field field : entity.getDeclaredFields()) {
				String columnName = null;
				// 获取该字段所有的注解
				Annotation[] anns = field.getDeclaredAnnotations();
				if (anns.length > 0) {
					for (Annotation ann : anns) {
						if (ann instanceof Id) {
							Id id = (Id)ann;
						}
						if (ann instanceof SqlInteger) {
							SqlInteger sInt = (SqlInteger) ann;
							// 当前没有name时候，将字段大写为列名
							if (sInt.name().length() < 1)
								columnName = field.getName().toUpperCase();
							else
								columnName = sInt.name();
							columnDefs.add(columnName + " INT" + getConstraints(sInt.constraints()));
						}

						if (ann instanceof SqlString) {
							SqlString sString = (SqlString) ann;
							// 当没有name时候，将字段大写为列名
							if (sString.name().length() < 1)
								columnName = field.getName().toUpperCase();
							else
								columnName = sString.name();
							columnDefs.add(columnName + " VARCHAR(" + sString.value() + ")"
									+ getConstraints(sString.constraints()));
						}
					}
					
					// 判断注解类型
					if (anns[0] instanceof SqlInteger) {
						SqlInteger sInt = (SqlInteger) anns[0];
						// 当前没有name时候，将字段大写为列名
						if (sInt.name().length() < 1)
							columnName = field.getName().toUpperCase();
						else
							columnName = sInt.name();
						columnDefs.add(columnName + " INT" + getConstraints(sInt.constraints()));
					}

					if (anns[0] instanceof SqlString) {
						SqlString sString = (SqlString) anns[0];
						// 当没有name时候，将字段大写为列名
						if (sString.name().length() < 1)
							columnName = field.getName().toUpperCase();
						else
							columnName = sString.name();
						columnDefs.add(columnName + " VARCHAR(" + sString.value() + ")"
								+ getConstraints(sString.constraints()));
					}
				}
			}
			StringBuilder createDB = new StringBuilder("CREATE TABLE " + tableName + "(");
			for (String cols : columnDefs) {
				createDB.append(" " + cols + ",");
			}
			String tableSql = createDB.substring(0, createDB.length() - 1) + ");";
			System.out.println("Table Creation SQL is : \n" + tableSql);
			return tableSql;
		}
	}

	private String getConstraints(Constraints constraints) {
		String constras = "";
		if (!constraints.allowNull())
			constras += " NOT NULL";
		if (constraints.primaryKey())
			constras += " PRIMARY KEY";
		if (constraints.unique())
			constras += " UNIQUE";
		return constras;
	}

	public Connection getConnection() {
		String user = "prodba";
		String password = "12wsxCDE#";
		String serverUrl = "jdbc:mysql://172.20.29.122:3306/test?useUnicode=true&characterEncoding=utf8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(serverUrl, user, password);
			return con;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
