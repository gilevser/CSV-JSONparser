import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        try {
            List<Employee> list = parseCSV(columnMapping, fileName);
            String json = listToJson(list);
            writeStringToFile(json, "data.json");
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        String xmlFile = "data.xml";
        String jsonFile = "data2.json";

        try {
            List<Employee> list = parseXML(xmlFile);
            String json = listToJson(list);
            writeStringToFile(json, jsonFile);
        } catch (IOException | ParserConfigurationException | SAXException e) {
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

    public static void writeStringToFile(String string , String fileName) throws IOException {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(string);
            writer.close();
        } catch (IOException e) {
           System.out.println(e.toString());
        }
    }

    public static List<Employee> parseXML(String fileName) throws IOException, ParserConfigurationException, SAXException {
        List<Employee> employees = new ArrayList<>();

      try {
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder = factory.newDocumentBuilder();
          Document document = builder.parse(fileName);
          document.getDocumentElement().normalize();



          NodeList nodeList = document.getDocumentElement().getChildNodes();
          for (int i = 0; i < nodeList.getLength(); i++) {
              Node node = nodeList.item(i);

              if (node.getNodeType() == Node.ELEMENT_NODE) {
                  Element element = (Element) node;
                  int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                  String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                  String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                  String country = element.getElementsByTagName("country").item(0).getTextContent();
                  int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());

                  Employee employee = new Employee(id, firstName, lastName, country, age);
                  employees.add(employee);
              }
          }

      } catch (IOException | ParserConfigurationException | SAXException e) {
          System.out.println(e.toString());
      }
        return employees;
    }
}
