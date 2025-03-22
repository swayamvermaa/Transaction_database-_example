import java.sql.*;
import java.util.Scanner;
public class transaction {
    private static final String url="jdbc:mysql://localhost:3306/transaction";
    private static final String username="root";
    private static final String password="*****************";
    public static void main(String[] args) {
        try{
            // this will give error so use exception handling
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            // Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            String debit_query = "update account set balance = balance -? where account_number = ?";
            String credit_query = "update account set balance = balance + ? where account_number = ?";
            // create to prepare statement because 2 query
            PreparedStatement debitprPreparedStatement = connection.prepareStatement(debit_query);
            PreparedStatement creditprPreparedStatement = connection.prepareStatement(credit_query);
            Scanner sc = new Scanner(System.in);
            System.out.println("enter amount debit");
            Double amount = sc.nextDouble();
            System.out.println("enter account number for debit");
            int account_number = sc.nextInt();
            debitprPreparedStatement.setDouble(1,amount);
            debitprPreparedStatement.setInt(2,account_number);
            creditprPreparedStatement.setDouble(1, amount);
            creditprPreparedStatement.setInt(2, 102);
            debitprPreparedStatement.executeUpdate();
            creditprPreparedStatement.executeUpdate();
            if(isSufficient(connection, account_number, amount)){
                // int affectedrow1 = debitprPreparedStatement.executeUpdate();
                // int affectedrow2 = creditprPreparedStatement.executeUpdate();
                connection.commit();
                System.out.println("transaction successfull");
            }else{
                connection.rollback();
                System.out.println("transaction failed");
            }
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    // checking that balance is greater or lower the debit
    static boolean isSufficient(Connection connection, int account_number, Double amount){
        try{
            String query = "select balance from account where account_number = ?";
            PreparedStatement preparestatement = connection.prepareStatement(query);
            preparestatement.setInt(1,account_number);
            ResultSet result = preparestatement.executeQuery();
            if(result.next()){
                Double current_balance = result.getDouble("balance");
                if (amount > current_balance){
                    return false;
                }
                else{
                  return true;
                }
              }
            } 
            catch(SQLException e){
                System.out.println(e.getMessage());
            }    
            return false;
    }
    
}
