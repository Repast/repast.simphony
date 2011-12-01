package repast.simphony.data.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PluginOutputStream extends Thread {

	InputStream stream;
	
	public PluginOutputStream(InputStream stream){
		this.stream = stream;
	}

	public void run(){
		try{
			InputStreamReader isr = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				System.out.println(line);    
		} catch (IOException e){
			e.printStackTrace();  
		}
	}

}
