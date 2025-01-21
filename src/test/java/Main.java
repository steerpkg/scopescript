import org.steerpkg.ssc.Scope;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        InputStream in = Files.newInputStream(Paths.get("example.ssc"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int read;
        while ((read = in.read(buffer)) != -1)
            out.write(buffer, 0, read);
        in.close();

        int iterations = 100000;
        char[] input = out.toString().toCharArray();

        Scope scope = Scope.parse(input);
        System.out.println(scope);

        System.out.println("Starting: Parsing");
        long totalParsingTime = 0;
        for (int i = 0; i < iterations; i++) {
            long t1 = System.nanoTime();
            Scope.parse(input);
            long t2 = System.nanoTime();
            totalParsingTime += (t2 - t1);
        }
        double averageParsingTime = (totalParsingTime / (double) iterations) * 1e-6;
        System.out.println("Average Parsing Time: " + averageParsingTime + "ms");

        System.out.println("Starting: Serializing");
        long totalSerializingTime = 0;
        for (int i = 0; i < iterations; i++) {
            long t1 = System.nanoTime();
            scope.toString();
            long t2 = System.nanoTime();
            totalSerializingTime += (t2 - t1);
        }
        double averageSerializingTime = (totalSerializingTime / (double) iterations) * 1e-6;
        System.out.println("Average Serializing Time: " + averageSerializingTime + "ms");
    }
}
