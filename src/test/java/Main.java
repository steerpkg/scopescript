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
        while (in.read(buffer) != -1)
            out.write(buffer);

        System.out.println(Scope.parse(out.toString()));
        in.close();
    }
}