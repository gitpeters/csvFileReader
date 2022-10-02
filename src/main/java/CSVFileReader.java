import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CSVFileReader {
    public static void main(String[] args) {

    }

    public static void DAO(){
        String filePath = "C:\\Users\\ABRAHAM\\Desktop\\People-data.csv";
        try{
            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
            CSVParser records = CSVParser.parse(lineReader, CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            ArrayList<PeopleData> data = new ArrayList<PeopleData>();
            for (CSVRecord record: records) {
                PeopleData pd = new PeopleData();
                pd.setFirst_name(record.get(0));
                pd.setLast_name(record.get(1));
                pd.setCompany(record.get(2));
                pd.setAddress(record.get(3));
                pd.setCity(record.get(4));
                pd.setPhone(record.get(7));
                pd.setEmail(record.get(9));

                data.add(pd);
            }//end of record iteration

            PreparedStatement pst = null;
            Connection con = dbconnection();
            String sql = "INSERT INTO record(first_name, last_name, company, address, city, phone, email) VALUES (?,?,?,?,?,?,?)";
            String sqlSelect = "SELECT * FROM record";

            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlSelect);
            pst = con.prepareStatement(sql);

            //pass the resultset data to array
            ArrayList<ResultSet> resData = new ArrayList<ResultSet>();

            //create a map to hold the result data and set a session
            Map<String, ArrayList<ResultSet>> map = new HashMap<String, ArrayList<ResultSet>>();
            //loop through the resultset
            while(resultSet.next()){
                //set session
                map.put(resultSet.getString("email"), resData);
            }

            //iterate encapsulation class
            for(PeopleData pd: data){
                //check if record existed in database or not
                if(!map.containsKey(pd.getEmail())){
                    pst.setString(1, pd.getFirst_name());
                    pst.setString(2, pd.getLast_name());
                    pst.setString(3, pd.getCompany());
                    pst.setString(4, pd.getAddress());
                    pst.setString(5, pd.getCity());
                    pst.setString(6, pd.getPhone());
                    pst.setString(7, pd.getEmail());
                }else{
                    System.out.println("Record already existed");
                }
            }
            pst.executeBatch();
            con.commit();
            con.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection dbconnection(){
        Connection connection= null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/people_data?", "root", "Ab@230596");
            System.out.println("Connection successful");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
