package dk.nerd.sqlwlid;

import com.azure.core.credential.TokenRequestContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;

public class App {

  private static String[] AZURE_SQL_SCOPES = {"https://database.windows.net//.default"};

  public static void main(String[] args) {
    final String connectionUrl = "jdbc:sqlserver://oidc-test.database.windows.net:1433;databaseName=wlid";
    final TokenRequestContext tokenRequestContext = new TokenRequestContext();
    tokenRequestContext.setScopes(Arrays.asList(AZURE_SQL_SCOPES));
    final Properties props = new Properties();
    // This just proves that you CAN connect using a token. Rollover is not handled, so this is unsuitable for production!
    props.setProperty("accessToken", new CustomTokenCredential().getToken(tokenRequestContext).block().getToken());

    try (Connection con = DriverManager.getConnection(connectionUrl, props); Statement stmt = con.createStatement();) {
      final String SQL = "SELECT * FROM testhest";
      final ResultSet rs = stmt.executeQuery(SQL);

      // Iterate through the data in the result set and display it.
      while (rs.next()) {
        System.out.println(rs.getString("hest"));
      }
    }
    // Handle any errors that may have occurred.
    catch (SQLException e) {
      e.printStackTrace();
    }

  }
}
