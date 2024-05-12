import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static String url="jdbc:mysql://localhost:3306/banking";
    private static String username="root";
    private static String password="root";
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            Scanner scanner=new Scanner(System.in);
            Connection connection= DriverManager.getConnection(url,username,password);
            System.out.println("succcedsss");

            //database conected


            User user=new User(connection,scanner);
            Accounts accounts=new Accounts(connection,scanner);
            AccountManager accountManager=new AccountManager(connection,scanner);

            String email;
            long account_number;


while (true){
    System.out.println("Welcome to the banking system");
    System.out.println();
    System.out.println(" 1. Register");
    System.out.println(" 2. Login");
    System.out.println(" 3. Exit");
    System.out.println();
    System.out.println("Enter Your Choice");
    int choice=scanner.nextInt();
    switch (choice){
        case 1:
            System.out.println("you chose 1");
            user.register();
            break;


        case 2:
            System.out.println("you chose 2");
            email=user.login();
            if (email != null){
                System.out.println();
                System.out.println("user logged in");
                if (!accounts.account_exist(email)){
                    System.out.println();
                    System.out.println("1. open a new bank account");
                    System.out.println("2. Exit");
                    if (scanner.nextInt()==1){
                        account_number=accounts.open_account(email);
                        System.out.println("Account created successfully");
                        System.out.println("Your account number is: "+ account_number);
                    }else {
                        break;
                    }
                }
                account_number=accounts.getAcccount_number(email);
                int choice2 =0;
                while (choice2 != 5){
                    System.out.println();
                    System.out.println("1. Dredit money");
                    System.out.println("2. Credit |Money");
                    System.out.println("3.Transfer money");
                    System.out.println("4. Check Balance");
                    System.out.println("5.Log Out");
                    System.out.println("Enter your choice");
                    choice2=scanner.nextInt();
                    switch (choice2){
                        case 1:
                            accountManager.debit_money(account_number);
                            break;
                        case 2:
                            accountManager.credit_money(account_number);
                            break;
                        case 3:
                            accountManager.transfer_money(account_number);
                            break;
                        case 4:
                            accountManager.getBalance(account_number);
                            break;
                        case 5:
                            break;
                        default:
                            System.out.println("invalid choice");
                            break;
                    }
                }
            }else {
                System.out.println("Incorrect  or password");
            }


        case 3:
            System.out.println("thankyou for using banking system");
            break;
        default:
            System.out.println("enter vaild choice");
    }
}




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}