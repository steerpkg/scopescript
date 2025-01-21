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

        char[] output = out.toString().toCharArray();

        System.out.println("Starting: Parsing");
        int iterations = 100000;

        System.out.println(Scope.parse(output));

        for (int i = 0; i < iterations; i++) {
            long t1 = System.nanoTime();
            Scope.parse(output);
            long t2 = System.nanoTime();

            System.out.print("\r" + ((t2 - t1) * 1e-6) + "ms");
        }
    }
}