package repast.simphony.ws;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ModelClasspathBuilder {
    
    private Set<String> libs = new HashSet<>();
    
    public ModelClasspathBuilder() {
        libs.add("libs.bsf");
        libs.add("libs.ext");
        libs.add("libs.piccolo");
    }
    
    public String run(Path pluginRoot) throws IOException {
        StringBuilder b = new StringBuilder("./bin");
        b.append(File.pathSeparator);
        b.append("./lib/*");
        Files.walk(pluginRoot, 1).
            filter(p -> (p.getFileName().toString().startsWith("repast.simphony.") || libs.contains(p.getFileName().toString()))
                    && p.toFile().isDirectory()).
            forEach(p -> {
               b.append(File.pathSeparator);
               b.append(p.toString());
               b.append(("/bin"));
               b.append(File.pathSeparator);
               b.append(p.toString());
               b.append("/lib/*");
            });
        return b.toString();
    }
    
    public static void main(String[] args) {
        try {
            String cp = new ModelClasspathBuilder().run(Paths.get(args[0]));
            System.out.println(cp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


