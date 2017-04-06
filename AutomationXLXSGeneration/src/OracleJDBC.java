import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

public class OracleJDBC {

	public static Connection getConnection() {

		System.out.println("-------- Oracle JDBC Connection Testing ------");

		Connection connection = null;
		try {

			Class.forName("com.ibm.db2.jcc.DB2Driver");

			System.out.println("Oracle JDBC Driver Registered!");

			

			connection = DriverManager.getConnection("jdbc:db2://t4ugxodsd.aig.net:60560/GEXED360", "gesgpct",
					"Vvj8bj5t");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
			System.out.println("----------------------------------------------");
		} else {
			System.out.println("Failed to make connection!");
		}
		return connection;
	}
	
	public static void main(String[] args) throws SQLException {
		Connection connection = OracleJDBC.getConnection();
		PreparedStatement preparedStatement =connection.prepareStatement( "Select distinct BRNCH_OFFC_ID,BRNCH_OFFC_NM from VW_BRANCH_OFFICE_GPC where cntry_id = 1325 order by brnch_offc_nm");
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next())
			System.out.println(resultSet.getLong(0));
	}

}