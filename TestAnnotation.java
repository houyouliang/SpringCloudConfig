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
			// ��ȡע��nameֵ ������
			String tableName = dbtable.name();
			// ��ǰnameֵ�����ڣ�ֱ��ʹ��������Ϊ������
			if (tableName.length() < 1) {
				tableName = entity.getName().toUpperCase(); // ת��д
			}
			// ׼�������ֶ�ע��
			List<String> columnDefs = new ArrayList<>();
			for (Field field : entity.getDeclaredFields()) {
				String columnName = null;
				// ��ȡ���ֶ����е�ע��
				Annotation[] anns = field.getDeclaredAnnotations();
				if (anns.length > 0) {
					for (Annotation ann : anns) {
						if (ann instanceof Id) {
							Id id = (Id)ann;
						}
						if (ann instanceof SqlInteger) {
							SqlInteger sInt = (SqlInteger) ann;
							// ��ǰû��nameʱ�򣬽��ֶδ�дΪ����
							if (sInt.name().length() < 1)
								columnName = field.getName().toUpperCase();
							else
								columnName = sInt.name();
							columnDefs.add(columnName + " INT" + getConstraints(sInt.constraints()));
						}

						if (ann instanceof SqlString) {
							SqlString sString = (SqlString) ann;
							// ��û��nameʱ�򣬽��ֶδ�дΪ����
							if (sString.name().length() < 1)
								columnName = field.getName().toUpperCase();
							else
								columnName = sString.name();
							columnDefs.add(columnName + " VARCHAR(" + sString.value() + ")"
									+ getConstraints(sString.constraints()));
						}
					}
					
					// �ж�ע������
					if (anns[0] instanceof SqlInteger) {
						SqlInteger sInt = (SqlInteger) anns[0];
						// ��ǰû��nameʱ�򣬽��ֶδ�дΪ����
						if (sInt.name().length() < 1)
							columnName = field.getName().toUpperCase();
						else
							columnName = sInt.name();
						columnDefs.add(columnName + " INT" + getConstraints(sInt.constraints()));
					}

					if (anns[0] instanceof SqlString) {
						SqlString sString = (SqlString) anns[0];
						// ��û��nameʱ�򣬽��ֶδ�дΪ����
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
