package repast.simphony.relogo.factories;

import repast.simphony.relogo.BaseObserver;
import repast.simphony.relogo.Observer;
import simphony.util.messages.MessageCenter;

public class ObserverFactory {
	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(ObserverFactory.class);
	
	private Class<? extends BaseObserver> obsType;
	private String observerID;
	private ReLogoWorldFactory wf;
	
	public ObserverFactory(String observerID, Class<? extends BaseObserver> obsType, ReLogoWorldFactory wf){
		if (BaseObserver.class.isAssignableFrom(obsType)){
			this.obsType = obsType;
			this.observerID = observerID;
			this.wf = wf;
		}
		else {
			throw new RuntimeException("Second argument to ObserverFactory constructor needs to extend BaseObserver.");
		}
	}

	public Observer createObserver() {
		BaseObserver baseObserver = null;
		try {
			baseObserver = obsType.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		baseObserver.initializeBaseObserver(observerID, wf.getContext(), wf.getTurtleFactory(), wf.getPatchFactory(), wf.getLinkFactory(), wf.getrLWorldDimensions());
		wf.createWorld(baseObserver);
		return baseObserver;
	}
}
