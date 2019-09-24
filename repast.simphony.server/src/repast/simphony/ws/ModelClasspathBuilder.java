package repast.simphony.ws;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ModelClasspathBuilder {
    
    private List<String> libs = new ArrayList<>();
    private StringBuilder b;
    
    public ModelClasspathBuilder() {
        libs.add("repast.simphony.");
        libs.add("libs.bsf");
        libs.add("libs.ext");
        libs.add("libs.piccolo");
        libs.add("org.codehaus.groovy");
        
        b = new StringBuilder("./bin");
        b.append(File.pathSeparator);
        b.append("./lib/*");
    }
    
    private boolean isValid(Path p) {
        String fn = p.getFileName().toString();
        for (String lib : libs) {
            if (fn.startsWith(lib)) {
                return true;
            }
        }
        
        return false;
    }
    
    public void add(Path plugin) {
    	b.append(File.pathSeparator);
        b.append(plugin.toString());
        b.append(("/bin"));
        b.append(File.pathSeparator);
        b.append(plugin.toString());
        b.append("/lib/*");
    }
    
    public String getPath() {
    	return b.toString();
    }
    
    public void run(Path pluginRoot) throws IOException {
       
        Files.walk(pluginRoot, 1).
            filter(p -> isValid(p) && p.toFile().isDirectory()).
            forEach(p -> {
               b.append(File.pathSeparator);
               b.append(p.toString());
               b.append(("/bin"));
               b.append(File.pathSeparator);
               b.append(p.toString());
               b.append("/lib/*");
            });
    }
    
    public static void main(String[] args) {
        try {
        	ModelClasspathBuilder b = new ModelClasspathBuilder();
        	b.run(Paths.get(args[0]));
            String cp = b.getPath();
            System.out.println(cp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


