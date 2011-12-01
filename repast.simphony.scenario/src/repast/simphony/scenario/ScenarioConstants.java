package repast.simphony.scenario;

public interface ScenarioConstants {
  
  static final String USER_PATH_FILE_NAME = "user_path.xml";
  static final String CONTEXT_FILE_NAME = "context.xml";
  static final String DEFAULT_FRAME_LAYOUT = "default_frame_layout.xml";
  
  
  // maintain the old score file name here which is used to prevent the score
  // file from being erased during scenario save.  eventually this reference
  // may be removed.
  public static final String LEGACY_SCORE_FILE_NAME = "model.score";

}
