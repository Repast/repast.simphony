/*CopyrightHere*/
package repast.simphony.integration;

public interface DataFile extends IntegrationSource {
	String getReadDescriptorFileName();
	
	void setReadDescriptorFileName(String descriptorFileName);
	
	void setReadFileName(String fileName);
	
	Object getReadObject();
	
	
	
	String getWriteDescriptorFileName();
	
	void setWriteDescriptorFileName(String descriptorFileName);
	
	void setWriteFileName(String fileName);
}
