package edu.escuelaing.tdse.config;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import edu.escuelaing.tdse.anotations.GetMapping;
import edu.escuelaing.tdse.anotations.PostMapping;
import edu.escuelaing.tdse.anotations.RestController;

public class FrameWorkSetting {

    public static HashMap<String, Method> servicesGet = new HashMap<>();
    public static HashMap<String, Method> servicesPost = new HashMap<>();

    public static void loadComponents() throws URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String packagePath = "edu.escuelaing.arep.controller";
        URL resource = classLoader.getResource(packagePath.replace(".", "/"));

        if (resource == null) {
            System.err.println("No se encontró el paquete: " + packagePath);
            return;
        }

        File classes = new File(resource.toURI());
        if (classes.exists() && classes.isDirectory()) {
            try {
                for (File file : classes.listFiles()) {
                    if (file.getName().endsWith(".class")) {
                        String className = packagePath + "." + file.getName().replace(".class", "");
                        Class<?> controllerClass = Class.forName(className);
                        if (!controllerClass.isAnnotationPresent(RestController.class)) {
                            continue;
                        }
                        for (Method m : controllerClass.getDeclaredMethods()) {
                            if (m.isAnnotationPresent(GetMapping.class)) {
                                GetMapping a = m.getAnnotation(GetMapping.class);
                                servicesGet.put(a.value(), m);
                            } else if (m.isAnnotationPresent(PostMapping.class)) {
                                PostMapping a = m.getAnnotation(PostMapping.class);
                                servicesPost.put(a.value(), m);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error cargando componentes", e);
            }
        } else {
            System.err.println("El directorio de clases no existe o no es válido.");
        }
    }

    public static Method getGetService(String path) {
        return servicesGet.get(path);
    }

    public static Method getPostService(String path) {
        return servicesPost.get(path);
    }

}