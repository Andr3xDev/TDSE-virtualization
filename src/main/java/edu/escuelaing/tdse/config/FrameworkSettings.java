package edu.escuelaing.tdse.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import edu.escuelaing.tdse.anotations.GetMapping;
import edu.escuelaing.tdse.anotations.PostMapping;
import edu.escuelaing.tdse.anotations.RestController;

/**
 * FrameworkSettings is responsible for scanning and loading controller
 * components
 * annotated with
 * {@link edu.escuelaing.tdse.framework.annotations.RestController}
 * from the specified package. It maps HTTP GET and POST endpoints to their
 * corresponding
 * controller methods using
 * {@link edu.escuelaing.tdse.framework.annotations.GetMapping}
 * and {@link edu.escuelaing.tdse.framework.annotations.PostMapping}
 * annotations.
 * <p>
 * The class maintains two static maps:
 * <ul>
 * <li>{@code servicesGet}: Maps GET endpoint paths to their handling
 * methods.</li>
 * <li>{@code servicesPost}: Maps POST endpoint paths to their handling
 * methods.</li>
 * </ul>
 * <p>
 * The {@link #loadComponents()} method scans the target package for classes,
 * loads those annotated as controllers, and registers their annotated methods.
 */
public class FrameworkSettings {

    private static final Logger logger = Logger.getLogger(FrameworkSettings.class.getName());

    public static HashMap<String, Method> servicesGet = new HashMap<>();
    public static HashMap<String, Method> servicesPost = new HashMap<>();

    public static void loadComponents() throws ClassNotFoundException, IOException, URISyntaxException {
        String packagePath = "edu.escuelaing.tdse.framework.controllers";
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String path = packagePath.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            if ("jar".equals(protocol)) {
                String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                try (JarFile jar = new JarFile(jarPath)) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(path) && name.endsWith(".class") && !entry.isDirectory()) {
                            String className = name.substring(0, name.length() - 6).replace('/', '.');
                            classes.add(Class.forName(className));
                        }
                    }
                }
            } else if ("file".equals(protocol)) {
                File[] files = new File(resource.toURI()).listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".class")) {
                            String className = packagePath + "."
                                    + file.getName().substring(0, file.getName().length() - 6);
                            classes.add(Class.forName(className));
                        }
                    }
                }
            }
        }

        for (Class<?> clazz : classes) {
            processClass(clazz);
        }
    }

    private static void processClass(Class<?> controllerClass) {
        if (!controllerClass.isAnnotationPresent(RestController.class)) {
            return;
        }

        logger.info("Found controller: " + controllerClass.getName());
        for (Method m : controllerClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(GetMapping.class)) {
                GetMapping a = m.getAnnotation(GetMapping.class);
                servicesGet.put(a.value(), m);
                logger.info("  - Mapped GET " + a.value() + " to method " + m.getName());
            } else if (m.isAnnotationPresent(PostMapping.class)) {
                PostMapping a = m.getAnnotation(PostMapping.class);
                servicesPost.put(a.value(), m);
                logger.info("  - Mapped POST " + a.value() + " to method " + m.getName());
            }
        }
    }

    public static Method getGetService(String path) {
        return servicesGet.get(path);
    }

    public static Method getPostService(String path) {
        return servicesPost.get(path);
    }

}