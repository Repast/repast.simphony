package repast.simphony.ui.widget;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.protocol.FileTypeDescriptor;

import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.NonModelAction;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.ui.Imageable;
import simphony.util.ThreadUtilities;
import simphony.util.messages.MessageCenter;

/**
 * Configuration info for making a movie.
 * 
 * @author Nick Collier
 */
public class MovieMakerConfig {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(MovieMakerConfig.class);

  private double tickInterval, startingTick;
  private File file;

  @NonModelAction
  private static class MovieAction implements IAction {

    public static long FRAME_UPDATE_INTERVAL = 34;

    private MovieMaker maker;
    private Imageable imageable;
    private boolean atEnd = false;

    private long lastTS = 0;

    private Runnable runner = new Runnable() {
      
      public void run() {
        try {
          BufferedImage img = imageable.getImage();
          maker.addImageAsFrame(img);
          if (atEnd)
            maker.cleanUp();
        } catch (IOException e) {
          msg.error("Error while capturing movie frame", e);
        }
      }
    };

    public MovieAction(MovieMaker maker, Imageable imageable) {
      this.maker = maker;
      this.imageable = imageable;
    }

    public void execute() {
      long ts = System.currentTimeMillis();

      if (ts - lastTS > FRAME_UPDATE_INTERVAL) {
        ThreadUtilities.runInEventThread(runner);
        lastTS = ts;
      }
    }
  }

  /**
   * Creates a MovieMakerConfig.
   * 
   * @param tickInterval
   *          the tick interval for the frame capture
   * @param file
   *          the file to write the movie to
   */
  public MovieMakerConfig(double startingTick, double tickInterval, File file) {
    this.tickInterval = tickInterval;
    this.startingTick = startingTick;
    this.file = file;
  }

  /**
   * Schedules the frame capture and movie creation from the specified imageable
   * on the specified schedule.
   * 
   * @param schedule
   *          the schedule to use
   * @param imageable
   *          the imageable that is the source of the movie frames
   */
  public void schedule(ISchedule schedule, Imageable imageable) {
    MovieMaker maker = new MovieMaker(20, file, FileTypeDescriptor.QUICKTIME);
    MovieAction action = new MovieAction(maker, imageable);
    schedule.schedule(
        ScheduleParameters.createRepeating(startingTick, tickInterval, ScheduleParameters.END),
        action);
    action = new MovieAction(maker, imageable);
    action.atEnd = true;
    schedule.schedule(ScheduleParameters.createAtEnd(ScheduleParameters.END), action);
  }
}
