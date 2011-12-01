	package repast.simphony.gis.display;

  import com.vividsolutions.jts.geom.Envelope;

  import java.io.Serializable;

public class ZoomManager implements Serializable{
	private static final long serialVersionUID = -992182658968308417L;

	private Envelope currentAreaOfInterest;

	private Envelope previousAreaOfInterest;

	public Envelope getCurrentAreaOfInterest() {
		return currentAreaOfInterest;
	}

	public void setCurrentAreaOfInterest(Envelope currentAreaOfInterest) {
		this.previousAreaOfInterest = this.currentAreaOfInterest;
		this.currentAreaOfInterest = currentAreaOfInterest;
	}

	public Envelope getPreviousAreaOfInterest() {
		return previousAreaOfInterest;
	}

}
