import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        try {
            List<Employee> list = parseCSV(columnMapping, fileName);
            String json = listToJson(list);
            writeStringToFile(json);
        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) throws IOException {
        try (Reader reader = new FileReader(fileName)) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(reader)
                    .withType(Employee.class)
                    .withMappingStrategy(strategy)
                    .build();

            return csvToBean.parse();
        }
    }

    public static String listToJson(List<Employee> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type type = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, type);
    }

    public static void writeStringToFile(String string) throws IOException {
        try {
            FileWriter writer = new FileWriter("data.json");
            writer.write(string);
            writer.close();
        } catch (IOException e) {
           System.out.println(e.toString());
        }

    }
}
