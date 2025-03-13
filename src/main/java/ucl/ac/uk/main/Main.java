package ucl.ac.uk.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.*;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

// FOR MAIN TESTING ==========================
import ucl.ac.uk.classes.Block;
import ucl.ac.uk.classes.ImageBlock;
import ucl.ac.uk.classes.Note;
import ucl.ac.uk.classes.TextBlock;
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

  public static void testing()
  {
    Model notes = new Model();
    System.out.println("yes?!");
//    List<Block> blocks = notes.getCategoryMap().get("category 1").get("note 1").getBlocksList();
//    for (Block b: blocks)
//    {
//      if (b instanceof TextBlock)
//      {
//        System.out.println(((TextBlock) b).getText());
//      }
//      else if (b instanceof ImageBlock)
//      {
//        System.out.println(((ImageBlock) b).getImagePath());
//      }
//    }
    notes.addCategory("category 1");
    notes.addCategory("category 2");

    Note note1 = new Note("n1", "note 1", new ArrayList<>(), "now", "then");
    Block block1 = new TextBlock("this is the text");
    notes.addNote("category 1", note1);
    notes.addBlock("category 1", note1.getId(), block1);

    Note note2 = new Note("n2", "note 2", new ArrayList<>(), "now", "then");
    Block block2 = new ImageBlock("/images/this.png");
    notes.addNote("category 1", note2);
    notes.addBlock("category 1", note2.getId(), block2);


    Note note3 = new Note("n3", "note 3", new ArrayList<>(), "now", "then");
    Block block3 = new TextBlock("more text!");
    notes.addNote("category 2", note3);
    notes.addBlock("category 2", note3.getId(), block3);
    notes.saveNotes();
  }
}