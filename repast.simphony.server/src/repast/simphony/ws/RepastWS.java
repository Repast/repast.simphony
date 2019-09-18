package repast.simphony.ws;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.BasicConfigurator;
import org.xml.sax.SAXException;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;
import org.zeromq.ZPoller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import repast.simphony.parameter.ListTokenizer;
import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.parameter.Schema;
import repast.simphony.scenario.data.UserPathData;
import repast.simphony.scenario.data.UserPathFileReader;
import simphony.util.messages.MessageCenter;

public class RepastWS {

    private static String OS = System.getProperty("os.name").toLowerCase();

    private static class MessageHandler implements Runnable {

        private static final MessageCenter MSG_LOG = MessageCenter.getMessageCenter(MessageHandler.class);

        private static final String INCOMING_ADDR = "tcp://127.0.0.1:5556";
        private static final String OUTGOING_ADDR = "tcp://127.0.0.1:5555";

        private Socket outgoing, incoming;
        private ZPoller zpoller;
        private ZContext zCtx;

        private TypeReference<HashMap<String, Object>> msgTypeRef = new TypeReference<HashMap<String, Object>>() {
        };
        private ObjectMapper mapper = new ObjectMapper();

        private WsContext ctx;
        private ConcurrentLinkedQueue<HashMap<String, Object>> queue = new ConcurrentLinkedQueue<>();
        private boolean started = false;
        private String scenarioDirectory;

        private boolean run = true;
        private Process p; // the repast runner process
        private String initMsg;
        private String modelClasspath;
        private String serverPluginPath;

        public MessageHandler(WsContext ctx, String scenarioDirectory, String modelClasspath, String serverPluginPath) {
            this.ctx = ctx;
            zCtx = new ZContext();
            incoming = zCtx.createSocket(SocketType.PULL);
            incoming.bind(INCOMING_ADDR);
            incoming.setReceiveTimeOut(500);
            outgoing = zCtx.createSocket(SocketType.PUSH);
            outgoing.bind(OUTGOING_ADDR);
            zpoller = new ZPoller(zCtx);
            this.scenarioDirectory = scenarioDirectory;
            this.modelClasspath = modelClasspath;
            this.serverPluginPath = serverPluginPath;
        }
        

        private void runModel(boolean debug) throws IOException {

            // Run OneRunRunner
            String debugArgs = "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000";

            List<String> command = new ArrayList<String>();
            command.add("java");
            command.add("-cp");

//            if (OS.contains("win"))
//                classpath = "bin;lib/*;simphony_lib/*;simphony_lib_gis/*";
//
//            else
//                classpath = "./bin:./lib/*:./simphony_lib/*:./simphony_lib_gis/*";
            
            MSG_LOG.info(modelClasspath);

            command.add(modelClasspath);
            command.add("repast.simphony.ws.OneRunRunner");
            command.add(scenarioDirectory + "/launch.props");

            p = new ProcessBuilder(command).inheritIO().start();
        }

        public void pushCommand(HashMap<String, Object> msg) {
            queue.add(msg);
        }

        private void handleCommand(HashMap<String, Object> msg) {
            if (msg == null)
                return;

            String cmd = msg.get("value").toString();

            if (cmd.equals("init_run")) {
                started = true;
                outgoing.send("init");

            } else if (cmd.equals("start")) {
                if (!started) {
                    started = true;
                    outgoing.send("start");
                } else {
                    outgoing.send("start");
                }
            }

            else if (cmd.equals("pause")) {
                outgoing.send("pause");
            }

            else if (cmd.equals("stop")) {
                outgoing.send("stop");
            }

            else if (cmd.equals("init_params")) {
                try {
                    shutdownModel();
                    runModel(false);
                    @SuppressWarnings("unchecked")
                    List<Map<String, String>> pvals = (List<Map<String, String>>) msg.get("params");
                    initMsg = "{\"params\" : " + mapper.writeValueAsString(pvals) + "}";
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (cmd.equals("picked")) {
                try {
                    String s = "{\"picked\": " + mapper.writeValueAsString(msg.get("ids")) + ", \"display_id\" : " 
                            + mapper.writeValueAsString(msg.get("display_id")) + ", \"display_type\" : "
                            + mapper.writeValueAsString(msg.get("display_type")) + "}";
                    outgoing.send(s);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (cmd.equals("shutdown_model")) {
                shutdownModel();
            }
        }
        
        private void shutdownModel() {
            if (p != null) {
                outgoing.send("shutdown");
                try {
                    if (!p.waitFor(5, TimeUnit.SECONDS)) {
                        // sending stop etc. should shutdown the model process,
                        // but if that's not possible then kill it
                        MSG_LOG.info("Shutdown: killing rogue model process");
                        p.destroy(); // Destroy the Repast process
                    }
                } catch (InterruptedException e) {
                    MSG_LOG.error("Error killing model process", e);
                } finally {
                    p = null;
                }
            }
            started = false;
        }

        public void run() {
            try {
                zpoller.register(incoming, ZPoller.POLLIN);
                while (run) {
                    ZMsg zmsg = null;
                    zpoller.poll(500);
                    if (zpoller.isReadable(incoming)) {
                        zmsg = ZMsg.recvMsg(incoming, false);
                    }
                    // String str = incoming.recvStr();
                    if (zmsg != null) {
                        String str = zmsg.popString();
                        if (str.equals("json")) {
                            str = zmsg.popString();
                            HashMap<String, Object> json = mapper.readValue(str, msgTypeRef);
                            String id = json.get("id").toString();
                            if (id.equals("status") || id.equals("req")) {
                                MSG_LOG.info("Received Status: " + str);
                                String value = json.get("value").toString();
                                if (value.equals("stopped")) {
                                    // {"id": "status", "value": "stopped"}
                                    started = false;
                                    ctx.send(json);
                                } else if (value.equals("ready")) {
                                    // {"id": "status", "value": "ready"}
                                    // sent when model process has started and model is loaded
                                    outgoing.send(initMsg);
                                    initMsg = null;
                                } else if (value.equals("copy_icon")) {
                                    // {"id": "status", "value": "copy_icon", "icon" : ...}
                                    String icon = json.get("icon").toString();
                                    MSG_LOG.info("Copying icon " + icon);
                                    Files.copy(Paths.get(scenarioDirectory.toString(), "..", icon),
                                            Paths.get(serverPluginPath + "/web/static/textures/", Paths.get(icon).getFileName().toString()),
                                            StandardCopyOption.REPLACE_EXISTING);
                                } else {
                                    
                                    ctx.send(str);
                                }
                            } else {
                                ctx.send(str);
                            }

                        } else if (str.equals("binary")) {
                            ctx.send(ByteBuffer.wrap(zmsg.getLast().getData()));
                        }
                        zmsg.destroy();
                    }

                    HashMap<String, Object> msg = queue.poll();
                    handleCommand(msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                MSG_LOG.info("MessageHandler.run.finalize");
                incoming.close();
                outgoing.close();
                zpoller.destroy();
                zCtx.close();
                zCtx.destroy();
                outgoing = incoming = null;
                zCtx = null;
            }
        }
        
        // Stop the MessageHandler
        public void stop() {
            MSG_LOG.info("MessageHandler.stop");
            if (outgoing != null && p != null && p.isAlive()) {
                outgoing.send("stop");
                outgoing.send("shutdown");
            }

            try {
                if (p != null && !p.waitFor(5, TimeUnit.SECONDS)) {
                    // sending stop etc. should shutdown the model process,
                    // but if that's not possible then kill it
                    MSG_LOG.info("Killing rogue model process");
                    p.destroy(); // Destroy the Repast process
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                run = false; // finalize thread
            }
        }
    }

    private static final MessageCenter LOG = MessageCenter.getMessageCenter(RepastWS.class);

    private MessageHandler msgHandler;
    private Thread msgHandlerThread;
    private TypeReference<HashMap<String, Object>> msgTypeRef = new TypeReference<HashMap<String, Object>>() {
    };
    private ObjectMapper mapper = new ObjectMapper();
    private Javalin app;
    private String serverPluginPath = null;
    private String modelClasspath = null;
    private String scenarioDirectory;
    
    public RepastWS(String scenarioDirectory) {
        this.scenarioDirectory = scenarioDirectory;
        URL runtimeSource = getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            serverPluginPath = Paths.get(runtimeSource.toURI()).resolve("..").toString();
            modelClasspath = new ModelClasspathBuilder().run(Paths.get(serverPluginPath, "..").toAbsolutePath());
            Properties props = new Properties();
            props.put("working.directory", "../");
            props.put("incoming", "tcp://127.0.0.1:5555");
            props.put("outgoing", "tcp://127.0.0.1:5556");
            props.put("scenario", Paths.get("../", Paths.get(scenarioDirectory).getFileName().toString()).toString());
            try (BufferedWriter out = Files.newBufferedWriter(Paths.get(scenarioDirectory, "launch.props"), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                props.store(out, "");
            }
           
            System.out.println(modelClasspath);
        } catch (URISyntaxException | IOException e) {
            LOG.error("Error finding server location", e);
        }
        
    }

    private String parseParameters(Path scenario) throws ParserConfigurationException, SAXException, IOException {
        Path parameters = scenario.resolve("parameters.xml");
        ParametersParser parser = new ParametersParser(parameters.toFile());
        Parameters params = parser.getParameters();

        Schema schema = params.getSchema();
        StringBuilder builder = new StringBuilder("{\"id\":\"parameters\", \"value\":[");

        boolean first = true;
        for (String pname : schema.parameterNames()) {
            if (!first) {
                builder.append(",");
            }
            ParameterSchema pschema = schema.getDetails(pname);
            builder.append("{\"name\":\"");
            builder.append(pname);
            builder.append("\", \"display_name\": \"");
            builder.append(params.getDisplayName(pname));
            builder.append("\", \"default_value\": \"");
            builder.append(params.getValueAsString(pname));
            builder.append("\", \"type\":\"");
            builder.append(pschema.getType().getSimpleName());
            builder.append("\", \"readonly\":\"");
            builder.append(params.isReadOnly(pname));
            builder.append("\", \"values\":[");

            if (pschema.getConstraintString().length() > 0) {
                String[] vals = ListTokenizer.parseStringValues(pschema.getConstraintString());
                boolean vfirst = true;

                for (String val : vals) {
                    if (!vfirst) {
                        builder.append(",");
                    }
                    builder.append("\"");
                    builder.append(val);
                    builder.append("\"");
                    vfirst = false;
                }
            }
            builder.append("]}");
            first = false;
        }
        builder.append("]}");
        return builder.toString();
    }
    
    private String getModelName(Path scenarioDirectory) throws IOException, XMLStreamException {
        UserPathFileReader fr = new UserPathFileReader();
        UserPathData upd = fr.read(scenarioDirectory.resolve("user_path.xml").toFile());
        StringBuilder builder = new StringBuilder("{\"id\":\"name\", \"value\":\"");
        builder.append(upd.getName());
        builder.append("\"}");
        
        return builder.toString();
    }

    private void start(int port) {
        
        //String path = runtimeSource.getFile().replaceAll("%20", " ");
        app = Javalin.create(config -> {
            config.addStaticFiles(serverPluginPath + "/web/static", Location.EXTERNAL);
            config.addSinglePageRoot("/", serverPluginPath + "/web/index.html", Location.EXTERNAL);
        }).start(port);

        // ASSUMES ONE CONNECTION AT A TIME
        app.ws("/simphony/simsocket", ws -> {
            ws.onConnect(ctx -> {
                LOG.info("connected");
                try {
                    Path sd = Paths.get(scenarioDirectory);
                    String nameMsg = getModelName(sd);
                    ctx.send(nameMsg);
                    String paramsMsg = parseParameters(sd);
                    ctx.send(paramsMsg);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (msgHandler != null) {
                    msgHandler.stop();
                    msgHandlerThread.join();
                }

                msgHandler = new MessageHandler(ctx, scenarioDirectory, modelClasspath, serverPluginPath);
                // TODO make this a field indexed ctx
                msgHandlerThread = new Thread(msgHandler);
                msgHandlerThread.start();
            });
            
            ws.onClose(ctx -> {
                LOG.info("disconnect");
                if (msgHandler != null) {
                    msgHandler.stop();
                    msgHandlerThread.join();
                    msgHandler = null;
                }
            });

            // json messages from the front end display layer
            ws.onMessage(ctx -> {
                String msg = ctx.message();
                LOG.info("Server Received " + msg);
                HashMap<String, Object> map = mapper.readValue(msg, msgTypeRef);
                String cmd = map.get("value").toString();
                if (cmd.equals("quit")) {
                    shutDown();
                    System.exit(0);
                } else {
                    // store the original message in the map
                    map.put("msg", msg);
                    msgHandler.pushCommand(map);
                }
            });
        });
    }
    

    // Shutdown the Repast server nicely
    public void shutDown() {
        if (msgHandler != null) {
            msgHandler.stop();

            try {
                if (msgHandlerThread.isAlive()) {
                    msgHandlerThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        app.stop(); // Stop the Javalin app
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        RepastWS ws = new RepastWS(args[1]);
        ws.start(Integer.parseInt(args[0]));

    }
}