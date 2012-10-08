package cl.automind.gameframework;


public class StorageContext {

	private static StorageContext instance = new StorageContext();
//	private Connection con;
//	private String name;

	public static StorageContext getInstance() {
		return instance;
	}

	private StorageContext() {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
//			name = "jdbc:hsqldb:mem:" + System.currentTimeMillis();
//			con = DriverManager.getConnection(name, "SA","");


		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}



}