package repast.simphony.systemdynamics.translator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.SystemModel;

public class Reader {

    private String filename;

    public Reader() {

    }

    public Reader(String filename) {
	this();
	this.filename = filename;
    }
    
    public SystemModel readRSDFile() {
    	XMIResourceImpl resource = new XMIResourceImpl();
    	try {
    		resource.load(new FileInputStream(filename), new HashMap<Object, Object>());

    		SystemModel systemModel = null;
    		for (EObject obj : resource.getContents()) {
    			if (obj.eClass().equals(SDModelPackage.Literals.SYSTEM_MODEL)) {
    				systemModel = (SystemModel)obj;
    				return systemModel;
    			}
    		}
    	} catch (FileNotFoundException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	return null;
    }

    public List<String> readMDLFile() {
	List<String> mdlContents = new ArrayList<String>();
	
	BufferedReader fileReader = null;

	String aLine;
	// open the file for reading
	try {
	    FileInputStream fstream = new FileInputStream(filename);
	    DataInputStream in = new DataInputStream(fstream);
	    try {
		fileReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
	    } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	   
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	// should have {UTF-8} as first line
	try {
	    aLine = fileReader.readLine();
	    if (!aLine.contains("{UTF-8}")) {
	    	System.out.println("Bad initial record: "+aLine);
	    	System.out.println("Will try to comtinue");
	    	boolean plusTab = false;
			if (aLine.endsWith("\t"))
			    plusTab = true;
			mdlContents.add(aLine.trim()+(plusTab ? "\t" : ""));
//		return null;
	    }

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	try {
	    aLine = fileReader.readLine();
	    while (aLine != null) {
		boolean plusTab = false;
		if (aLine.endsWith("\t"))
		    plusTab = true;
		mdlContents.add(aLine.trim()+(plusTab ? "\t" : ""));
		aLine = fileReader.readLine();
	    }
	    fileReader.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}	
	
//	for (String mdl : mdlContents) {
//		System.out.println("MDLCont: <"+mdl+">");
//	}
	
	return mdlContents;
    }
}
