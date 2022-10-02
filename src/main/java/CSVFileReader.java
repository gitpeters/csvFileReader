import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CSVFileReader {
    public static void main(String[] args) {

    }

    public static void DAO(){
        String filePath = "C:\\Users\\ABRAHAM\\Desktop\\People-data.csv";
        try{
            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
