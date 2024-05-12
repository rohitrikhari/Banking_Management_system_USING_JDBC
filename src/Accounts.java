import java.sql.*;
import java.util.Scanner;

public class Accounts {

    Connection connection;
    Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    public  long open_account(String email) {
        if (!account_exist(email)) {
            String open_account_query = "INSERT INTO accounts(account_number,name,email,balance,security_pin )VALUES(?,?,?,?,?);";
            scanner.nextLine();

            System.out.println("Enter full name");
            String name = scanner.nextLine();

            System.out.println("Enter Initial Amount");
            double balance = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Enter Security Pin :");
            String security_pin = scanner.nextLine();

            try {
                long generated_accountNumber = generateAccountsNumber();
                PreparedStatement ps = connection.prepareStatement(open_account_query);
                ps.setLong(1, generated_accountNumber);
                ps.setString(2, name);
                ps.setString(3, email);
                ps.setDouble(4, balance);
                ps.setString(5, security_pin);
                int rows_affected = ps.executeUpdate();
                if (rows_affected > 0) {
                    return generated_accountNumber;
                } else {
                    throw new RuntimeException("account creation failed");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        return 0;
    }
    public long getAcccount_number(String email){
        String query="SELECT account_number FROM accounts WHERE email=?;";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet rs=preparedStatement.executeQuery();
            if (rs.next()){
                return rs.getLong("account_number");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("account does not exist");
    }
    public long generateAccountsNumber(){
        String qry="SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1;";
        try {
            Statement st=connection.createStatement();
            ResultSet rs=st.executeQuery(qry);
            if (rs.next()){
                long last_account_number=rs.getLong("account_number");
                return last_account_number+1;
            }else {
                return 10000100;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean account_exist(String email){
        String qry="SELECT  account_number FROM accounts WHERE email = ?;";

        try {
            PreparedStatement ps=connection.prepareStatement(qry);
            ps.setString(1,email);
            ResultSet rs=ps.executeQuery();
            if (rs.next()){
                return true;

            }else {
                return  false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
