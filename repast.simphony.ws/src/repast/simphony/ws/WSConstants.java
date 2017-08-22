package repast.simphony.ws;

public interface WSConstants {
  
  String WORKING_DIRECTORY = "working.directory";
  String SCENARIO_DIRECTORY = "scenario.directory";
  String REPAST_LIB_DIRECTORY = "repast.lib.directory";
  String VM_ARGS = "vm.args";
  String HOST = "host";
  String IDENTITY = "identity";
  
  String START = "start";
  String STOP = "stop";
  String PAUSE = "pause";
  String RESUME = "resume";
  String EXIT = "exit";
  
  String ID = "id";
  String STATUS = "status";
  String VALUE = "value";
  String TYPE = "type";
  String DATA = "data";
  String HEADER = "header";
  String ROW = "row";
  
  String READY = "ready";
  String READY_JSON = "{\"" + ID + "\" : \"" + STATUS + "\", \"" + VALUE + "\" : \"" + READY + "\"}";

}
