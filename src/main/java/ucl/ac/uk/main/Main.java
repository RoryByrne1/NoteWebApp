package ucl.ac.uk.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.*;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

// FOR MAIN TESTING ==========================
import ucl.ac.uk.classes.*;
import ucl.ac.uk.model.Model;

import java.util.List;
// ===========================================

public class Main
{
  private static final int DEFAULT_PORT = 8080;
  private static final String DEFAULT_WEBAPP_DIR = "src/main/webapp/";
  private static final String DEFAULT_TARGET_CLASSES = "target/classes";
  private static final String WEB_INF_CLASSES = "/WEB-INF/classes";
  private static final String LOGFILE = "logfile.txt";

  public static Thread addShutdown(final Tomcat tomcat, final Logger logger)
  {
    Thread shutdownHook = new Thread(() -> {
      try
      {
        if (tomcat != null)
        {
          tomcat.stop();
          tomcat.destroy();
          logger.info("Tomcat has shut down normally.");
        }
      } catch (Exception e)
      {
        logger.log(Level.SEVERE, "Error shutting down Tomcat", e);
      }
    });
    Runtime.getRuntime().addShutdownHook(shutdownHook);
    return shutdownHook;
  }

  private static Logger initialiseLogger()
  {
    Logger logger = Logger.getLogger(Main.class.getName());

    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.INFO);
    logger.addHandler(consoleHandler);

    try {
      FileHandler fileHandler = new FileHandler(LOGFILE);
      fileHandler.setFormatter(new SimpleFormatter());
      fileHandler.setLevel(Level.INFO);
      logger.addHandler(fileHandler);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to create log file", e);
    }

    logger.setLevel(Level.INFO);
    return logger;
  }

  private static Context getContext(Path webappDirectory, Tomcat tomcat)
  {
    if (!Files.exists(webappDirectory) || !Files.isDirectory(webappDirectory))
    {
      throw new IllegalArgumentException("Webapp directory does not exist: " + webappDirectory);
    }
    return tomcat.addWebapp("/", webappDirectory.toAbsolutePath().toString());
  }

  private static void setResources(Context context, Path targetClassesDirectory)
  {
    WebResourceRoot resources = new StandardRoot(context);
    resources.addPreResources(new DirResourceSet(resources, WEB_INF_CLASSES,
      targetClassesDirectory.toAbsolutePath().toString(), "/"));
    context.setResources(resources);
  }

  public static void main(String[] args)
  {
//    testing();
    final Logger logger = initialiseLogger();
    final Path webappDirectory = Paths.get(DEFAULT_WEBAPP_DIR);
    final Path targetClassesDirectory = Paths.get(DEFAULT_TARGET_CLASSES);
    final Tomcat tomcat = new Tomcat();

    try
    {
      tomcat.setPort(DEFAULT_PORT);
      tomcat.getConnector();
      addShutdown(tomcat, logger);

      Context context = getContext(webappDirectory, tomcat);
      setResources(context, targetClassesDirectory);

      tomcat.start();
      logger.info("Server started successfully on port " + DEFAULT_PORT);
      tomcat.getServer().await();
    } catch (IllegalArgumentException e)
    {
      logger.log(Level.SEVERE, "Configuration error", e);
    } catch (Exception e)
    {
      logger.log(Level.SEVERE, "Error occurred while starting the server", e);
    }
  }

  public static void main2(String[] args)
  {
    System.out.println("yes?!");
//    Folder folder = new Folder("folder-1", "folder 1", new HashMap<>(), "then", "now");
//    Note note = new Note("n1", "note 1", new ArrayList<>(), "then", "now");
//    System.out.println(folder.name);
    Model notes = new Model();
//    notes.addItem(new ArrayList<>(), folder);
    List<String> path = new ArrayList<>();
//    notes.addItem(path, note);
//    notes.saveNotes();
//    Note note = new Note("n2", "note 2", new ArrayList<>(), "then", "now");
//    notes.addItem(path, note);
    path.add("folder-1");
    Folder folder = new Folder("folder-2", "folder 2", new HashMap<>(), "then", "now");
//    Note note2 = new Note("n3", "note 3", new ArrayList<>(), "then", "now");
    notes.addItem(path, folder);
  }
}