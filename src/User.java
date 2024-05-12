import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    Scanner scanner;
    Connection connection;

    public User(Connection connection, Scanner scanner){
       this.connection=connection;
        this.scanner=scanner;
    }

    public void register(){
        //
        //to get the user info
        scanner.nextLine();
        System.out.println(" Enter your Name:");
       String name= scanner.nextLine();
        System.out.println(" Enter your Email:");
        String email= scanner.nextLine();
        System.out.println(" Enter your password");
        String password= scanner.nextLine();
        if (user_exist(email)) {
            System.out.println("User already exist");
            return;
        }
        String register_query = "INSERT INTO user(name,email,PASSWORD)VALUES(?,?,?);";

        try {
            PreparedStatement ps = connection.prepareStatement(register_query);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            int rows_aff = ps.executeUpdate();
            if (rows_aff > 0) {
                System.out.println(rows_aff + " rows affected");
            }

            else {

                    System.out.println("register failed");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    public String login(){

        scanner.nextLine();
        System.out.println("Enter your email");
        String email=scanner.nextLine();

        System.out.println("Enter your password");
        String password=scanner.nextLine();

        String login_query="SELECT * FROM user WHERE email= ? AND PASSWORD = ?";

        try {
            PreparedStatement ps=connection.prepareStatement(login_query);
            ps.setString(1,email);
            ps.setString(2,password);


               ResultSet  rs=ps.executeQuery();

           
            if(rs.next()){
                return email;
            }
            else return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean user_exist(String email){
        String qry="SELECT * FROM user WHERE email = ?";
        try {
            PreparedStatement ps= connection.prepareStatement(qry);
            ps.setString(1,email);
            ResultSet rs=ps.executeQuery();
            if (rs.next()){
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
