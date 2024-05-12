import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    Scanner scanner;
    Connection connection;

    public AccountManager( Connection connection,Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }

    //credit mo0ney
    public void credit_money(long account_number) throws SQLException {

        scanner.nextLine();
        System.out.println("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if (account_number!=0){

                PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();

                if (resultSet.next()){
                    String credit_qry= "UPDATE accounts SET balance = balance + ? WHERE account_number=?;";
                    PreparedStatement preparedStatement1=connection.prepareStatement(credit_qry);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,account_number);
                    int rowsAffected=preparedStatement1.executeUpdate();

                    if (rowsAffected>0){
                        System.out.println("RS "+amount+" credited succesfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }else {
                        System.out.println("transaction failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }


                }else {
                    System.out.println("invalid security pin");
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connection.setAutoCommit(true);
    }
    ////////////////////////
    //debit money
    public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ?AND security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {

                    double current_balance = rs.getDouble("balance");

                    if (amount <= current_balance) {

                        String credit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number=?;";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);
                        int rows_affected = preparedStatement1.executeUpdate();
                        if (rows_affected > 0) {
                            System.out.println("Rs " + amount + " debited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                    } else {
                        System.out.println("Transaction failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else {
                    System.out.println("invalid pin");
                }
            }
        }catch(SQLException e){
                throw new RuntimeException(e);
            }
        connection.setAutoCommit(true);
        }
    ///get balance
    public  void getBalance(long account_number){
        scanner.nextLine();
    System.out.println("Enter security pin");
    String security_pin=scanner.nextLine();
    try{
        try {
            PreparedStatement ps=connection.prepareStatement("SELECT balance FROM accounts WHERE account_number=? AND security_pin =?");
            ps.setLong(1,account_number);
            ps.setString(2,security_pin);
            ResultSet rs=ps.executeQuery();
            if (rs.next()){
                double balance= rs.getDouble("balance");
                System.out.println(" Balance "+balance);
            }else {
                System.out.println("invalid pin");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } catch (RuntimeException e) {
        throw new RuntimeException(e);
    }
}

//  transfer money
    public void transfer_money(long sender_account_number){
        scanner.nextLine();
        System.out.println("Enter reciever acccount number");
        long receiver_account_number=scanner.nextLong();
        System.out.println("enter amount: ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String pin= scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if (sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement ps=connection.prepareStatement("SELECT balance FROM accounts WHERE account_number=? AND security_pin =?");
                ps.setLong(1,sender_account_number);
                ps.setString(2,pin);
                ResultSet rs=ps.executeQuery();

                if (rs.next()){
                    double current_balance=rs.getDouble("balance");
                    if (amount<=current_balance){
                        String debit_query ="UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                                String credit_query="UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                                PreparedStatement creditPreparedStatement=connection.prepareStatement(credit_query);
                                PreparedStatement debitPreparedStatement=connection.prepareStatement(debit_query);
                                creditPreparedStatement.setDouble(1,amount);
                                creditPreparedStatement.setLong(2,receiver_account_number);
                                debitPreparedStatement.setDouble(1,amount);
                                debitPreparedStatement.setLong(2,sender_account_number);
                                int rowsAffected1=debitPreparedStatement.executeUpdate();
                                int rowsAffected2=creditPreparedStatement.executeUpdate();

                                if (rowsAffected1>0 &&rowsAffected2 >0){
                                    System.out.println("Transaction Succesfull");
                                    System.out.println("Rs "+amount+" treansaction failed");
                                    connection.commit();
                                    connection.setAutoCommit(true);
                                    return;
                                }
                                else {
                                    System.out.println("Transaction failed");
                                    connection.rollback();
                                    connection.setAutoCommit(true);
                                }
                    }else {
                        System.out.println("invalid security pin");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
