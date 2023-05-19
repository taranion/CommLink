package de.rpgframework.shadowrun6.updater;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.update4j.Configuration;
import org.update4j.LaunchContext;
import org.update4j.service.DefaultLauncher;
import org.update4j.service.Launcher;
import org.update4j.util.StringUtils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author prelle
 *
 */
public class CommlinkLauncher extends DefaultLauncher implements Launcher {

	private final static Logger logger = System.getLogger("commlink6.updater");
	public static Stage primaryStage;

	//-------------------------------------------------------------------
	public CommlinkLauncher() {
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.update4j.service.Launcher#run(org.update4j.LaunchContext)
	 */
    @Override
    public void run(LaunchContext context) {
        Configuration config = context.getConfiguration();

        String mainClass = config.getResolvedProperty(MAIN_CLASS_PROPERTY_KEY);

        List<String> localArgs = new ArrayList<>();
//        if (this.args != null)
//            localArgs.addAll(this.args);

        // use TreeMap to sort by key
        Map<Integer, String> argMap = new TreeMap<>();
        context.getConfiguration().getResolvedProperties().forEach((k, v) -> {
            String pfx = ARGUMENT_PROPERTY_KEY_PREFIX + ".";
            // starts with but not equals, to filter missing <num> part
            if (k.startsWith(pfx) && !k.equals(pfx)) {
                int num = Integer.parseInt(k.substring(pfx.length()));
                argMap.put(num, v);
            }
        });

        localArgs.addAll(argMap.values());

        if (mainClass == null && localArgs.isEmpty()) {
//            usage();

            throw new IllegalStateException(
                            "You must provide either a main class or arguments to be executed as commands.");
        }

        context.getConfiguration().getResolvedProperties().forEach((k, v) -> {
            String pfx = SYSTEM_PROPERTY_KEY_PREFIX + ".";
            if (k.startsWith(pfx) && !k.equals(pfx)) {
                System.setProperty(k.substring(pfx.length()), v);
            }
        });

        // we are fully aware, so no need to warn
        // if NoClassDefFoundError arises for any other reason
        System.setProperty("update4j.suppress.warning.access", "true");

        if (mainClass != null) {

            if (!StringUtils.isClassName(mainClass)) {
                throw new IllegalStateException("Main class '" + mainClass + "' is not a valid Java class name.");
            }

            try {
                Class<?> clazz = context.getClassLoader().loadClass(mainClass);

                // first check for JavaFx start method
                Class<?> javafx = null;
                try {
                    javafx = context.getClassLoader().loadClass("javafx.application.Application");
                } catch (ClassNotFoundException e) {
                    // no JavaFx present, skip.
                }
                logger.log(Level.INFO, "javafx = "+javafx);
                logger.log(Level.INFO, "is = "+javafx+" assignable from "+clazz+" = "+javafx.isAssignableFrom(clazz));

                String[] argsArray = localArgs.toArray(new String[localArgs.size()]);
                if (javafx != null && javafx.isAssignableFrom(clazz)) {
                	try {
                		logger.log(Level.DEBUG, "Call "+clazz.getName()+".<init>");
						javafx.application.Application app = (Application)clazz.getConstructor().newInstance();
						Platform.runLater( () -> {
//	                		logger.log(Level.DEBUG, "Call "+clazz.getName()+".init()");
//							app.init();
	                		logger.log(Level.DEBUG, "Call "+clazz.getName()+".start()");
							try {
								primaryStage.hide();
								primaryStage = new Stage(StageStyle.DECORATED);
								primaryStage.show();
								app.start(primaryStage);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                } else {
                    Method main = clazz.getMethod("main", String[].class);
                    main.invoke(null, new Object[] { argsArray });
                }

            } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException
                            | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        } else {

            try {
                new ProcessBuilder(localArgs).inheritIO().start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
