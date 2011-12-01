package repast.simphony.relogo.ide.image;

import java.io.*;
import java.util.List;
import org.antlr.runtime.*;


public class ImageSectionParser {

    protected List<NLImage> model;
    protected int imageErrors;
    
    public List<NLImage> getModel() {
        return model;
    }
    
    public int errorCount() {
        return imageErrors;
    }
    
    public ImageSectionParser(StringBuffer sb) {
        this(sb.toString());
    }
    
    public ImageSectionParser(String s) {
        NetLogoImagesLexer lex = new NetLogoImagesLexer(new ANTLRStringStream(s));
        final CommonTokenStream tokens = new CommonTokenStream(lex);

        NetLogoImagesParser g = new NetLogoImagesParser(tokens) {
            public void recoverFromMismatchedToken(IntStream input, RecognitionException e, int ttype, BitSet follow) {
                try {
                    //String problemToken = (tokens.LT(1).getText());
                    imageErrors++;
                    super.recoverFromMismatchedToken(input, e, ttype, follow);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                //System.exit(1);
            }
        };
        try {
            imageErrors = 0;
            model = g.model();
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        NetLogoImagesLexer lex = new NetLogoImagesLexer(new ANTLRFileStream("C:\\Projects\\netlogo\\image_library.txt"));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        NetLogoImagesParser g = new NetLogoImagesParser(tokens);
        try {
            System.out.println(g.model());
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}