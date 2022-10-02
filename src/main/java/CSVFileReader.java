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
        dbconnection();
        DAO();
    }
    public static void DAO(){
        String filePath = "C:\\Users\\ABRAHAM\\Desktop\\People-data.csv";

        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
            CSVParser records = CSVParser.parse(lineReader, CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            ArrayList<PeopleData> people = new ArrayList<PeopleData>();
            //Iterate through the csv file
            for(CSVRecord record: records){
                PeopleData pd = new PeopleData();
                pd.setFirst_name(record.get(0));
                pd.setLast_name(record.get(1));
                pd.setCompany(record.get(2));
                pd.setAddress(record.get(3));
                pd.setCity(record.get(4));
                pd.setPhone(record.get(7));
                pd.setEmail(record.get(9));

                people.add(pd);
            }

            //database query
            PreparedStatement statement = null;
            Connection con = dbconnection();
            String sqlSelect = "SELECT * FROM record";//select from db to compare if record exist
            String sql = "INSERT INTO record (first_name, last_name, company, address, city, phone, email) VALUES (?,?,?,?,?,?,?)";//insert new records to db
            statement = con.prepareStatement(sql);
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(sqlSelect);
            ArrayList<ResultSet> resdata = new ArrayList<ResultSet>();//store the queried data into an array
            Map<String,ArrayList<ResultSet>> map = new HashMap<String, ArrayList<ResultSet>>();//map and set session
            while (res.next()) {
                map.put(res.getString("email"), resdata);
            }
            for(PeopleData st:people){
                if(!map.containsKey(st.getEmail())){
                    statement.setString(1, st.getFirst_name());
                    statement.setString(2, st.getLast_name());
                    statement.setString(3, st.getCompany());
                    statement.setString(4, st.getAddress());
                    statement.setString(5, st.getCity());
                    statement.setString(6, st.getPhone());
                    statement.setString(7, st.getEmail());
                    statement.addBatch();
                }else {
                    System.out.println("Records already exist!");
                }
            }
            statement.executeBatch();
            con.commit();
            con.close();
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }catch (IOException ex){
            ex.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }catch (NumberFormatException e){
            e.printStackTrace();
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
