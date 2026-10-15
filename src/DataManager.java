import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String FILE = "output/customers.txt";

    public static void saveCustomer(String name, String phone, String address) {
        try {
            new File("output").mkdirs();
            try (FileWriter fw = new FileWriter(FILE, true)) {
                fw.write(name + "|" + phone + "|" + address + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> loadCustomers() {
        List<String[]> list = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) list.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}