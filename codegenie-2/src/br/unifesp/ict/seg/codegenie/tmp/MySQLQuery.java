package br.unifesp.ict.seg.codegenie.tmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.unifesp.ict.seg.codegenie.tmp.Servers;

public class MySQLQuery {

	public static String fixSolr(String fqn, String params){
		return "SELECT entity_id FROM entities WHERE fqn='"
				+fqn+"' AND params='"+params+"';";
	}


	public static long query(String query){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(
					Servers.jdbc+Servers.MySQLServer+Servers.sourcererDB+Servers.sourcererUser+Servers.sourcererPWD);

			// Statements allow to issue SQL queries to the database
			Statement statement = connection.createStatement();
			// Result set get the result of the SQL query
			ResultSet resultSet = statement.executeQuery(query);
			String firstID = getFirst(resultSet);
			statement.close();
			connection.close();
			return Long.valueOf(firstID);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return -1;
	}


	private static String getFirst(ResultSet rs) throws SQLException {
		if(rs.next()){
			return rs.getObject(1).toString();
		}
		return "-1";
	}
}
