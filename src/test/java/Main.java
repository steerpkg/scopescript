import org.steerpkg.ssc.Scope;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        InputStream in = Files.newInputStream(Paths.get("example.ssc"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int read;
        while ((read = in.read(buffer)) != -1)
            out.write(buffer, 0, read);

        Scope scope = Scope.parse(out.toString());
        recursivePrintScope(scope);
        in.close();
    }

    private static void recursivePrintScope(Scope scope) {
        System.out.println("{");
        for (Map.Entry<String, Object> entry : scope.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            if (entry.getValue() instanceof Object[])
                System.out.println(Arrays.toString((Object[]) entry.getValue()));
            else if (entry.getValue() instanceof Scope)
                recursivePrintScope((Scope) entry.getValue());
            else
                System.out.println(entry.getValue());
        }
        System.out.println("}");
    }
}