package repast.simphony.relogo.ide.gui;

import java.io.*;

public class ScanModels {

    private String separator;
    private NetlogoSimulation sim;

    public ScanModels() {
        separator = System.getProperty("file.separator");
        sim = new NetlogoSimulation();
    }

    public void walk(String source) {
        try {
            File f = new File(source);
            String[] subordinates = f.list();
            if (subordinates == null) {
                if (!source.endsWith(".nlogo")) {
                    return;
                }
                System.err.println(source);
                sim.scan(source);
            } else {
                System.err.println(source + separator);
                for (int i = 0; i < subordinates.length; i++) {
                    String dest = source + separator + subordinates[i];
                    walk(dest);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void main(String[] args) {
        // simple test: walk a directory tree
        ScanModels clt = new ScanModels();
        if (args.length >= 1) {
            clt.walk(args[0]);
        } else {
            //clt.walk("C:\\Documents and Settings\\cburke\\My Documents\\svn-checkout\\netlogo\\IBCT.nlogo");
            //clt.walk("C:\\Documents and Settings\\cburke\\My Documents\\svn-momays\\trunk\\samples\\Rebellion\\NetLogo\\Rebellion_logging.nlogo");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models\\Sample Models\\Chemistry & Physics\\Electromagnetism\\Ising.nlogo");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models\\Curricular Models\\BEAGLE Evolution\\Cooperation.nlogo");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models\\Code Examples\\Case Conversion Example.nlogo");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models\\Code Examples\\Hex Cells Example.nlogo");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models\\Code Examples\\Color Chart Example.nlogo");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models\\Code Examples");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models\\Code Examples\\Bounce Example.nlogo");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models\\Sample Models\\Earth Science\\Continental Divide.nlogo");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models\\Sample Models\\Biology\\Rabbits Grass Weeds.nlogo");
            clt.walk("/Applications/NetLogo 4.0.4/models/Sample Models/Biology/Rabbits Grass Weeds.nlogo");
//            clt.walk("/Volumes/Passport/Eclipse_Folder/ReLogoEclipseProjects/CarlProjects/relogo_090323Carl/sampleFiles/TestModel1.nlogo");
//            clt.walk("/Volumes/Passport/Eclipse_Folder/ReLogoEclipseProjects/CarlProjects/relogo_090323Carl/sampleFiles/Rabbits Grass Weeds.nlogo");
            //clt.walk("c:\\program files\\netlogo 4.0.2\\models\\Sample Models\\System Dynamics\\Unverified\\Tabonuco Yagrumo.nlogo");
        }
    }
    /* Notes:
     * models\Curricular Models\MaterialSim\Unverified\MaterialSim Grain Growth.nlogo
     *   - both element1s and element2s use element1 as the singular!
     *     this doesn't seem to be a problem for NetLogo, but I'm catching it.
     */
}


