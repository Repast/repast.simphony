package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {

	private List<String> warningMessages = new ArrayList<String>();
	
	public MessageManager() {
		
	}
	
	public void addWarningMessage(String msg) {
		warningMessages.add(msg);
	}

	public List<String> getWarningMessages() {
		return warningMessages;
	}
	
	public boolean haveWarningMessages() {
		return warningMessages.size() > 0;
	}
	
	public void addToMessages(List<String> messages) {
		if (!haveWarningMessages())
			return;
		messages.add("\n--- WARNING MESSAGES ---\n");
		for (String msg : warningMessages)
			messages.add(msg);

	}
}
