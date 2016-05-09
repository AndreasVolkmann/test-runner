import annotations.SetUp;
import annotations.TearDown;
import annotations.TestClass;
import annotations.TestMethod;
import exceptions.AssertException;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        //Class[] classes = getClasses("tests");
        List<Class> classes = new ArrayList<>();

        File file = new File("/Users/av/Desktop/dev/Java/TestReflection/bin/");
        for (String arg : args) {
            try {
                URL url = file.toURL();
                URL[] urls = new URL[] {url};
                ClassLoader cl = new URLClassLoader(urls);


                Class cls = cl.loadClass(arg);
                classes.add(cls);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        testClasses(classes.toArray(new Class[classes.size()]));
    }

    public static void testClasses(Class[] classes) throws Exception {

        for (Class aClass : classes) {
            Annotation annotation = aClass.getAnnotation(TestClass.class);
            if (annotation != null) {
                testClass(aClass);
            }
        }

    }

    public static void testClass(Class testClass) throws Exception {
        System.out.println("Testing: "+ testClass.getName());
        Object instance = testClass.newInstance();

        List<Method> setUpMethods = findMethodsWithAnnotation(testClass, SetUp.class);
        if (setUpMethods.size() > 1) throw new Exception("More than one SetUp method");
        Method setUpMethod = setUpMethods.get(0);

        List<Method> tearDownMethods = findMethodsWithAnnotation(testClass, TearDown.class);
        if (tearDownMethods.size() > 1) throw new Exception("More than one tear down emthod");
        Method tearDownMethod = tearDownMethods.get(0);

        // TEST METHODS
        List<Method> testMethods = findMethodsWithAnnotation(testClass, TestMethod.class);
        for (Method method : testMethods) {
            // run test
            setUpMethod.invoke(instance);
            try {
                method.invoke(instance);

            } catch (Exception ex) {
                if (ex.getCause().getClass().equals(AssertException.class)) {
                    System.out.println(method.getName() + " failed");

                }

            }
            tearDownMethod.invoke(instance);
        }

    }


    private static List<Method> findMethodsWithAnnotation(Class aClass, Class annotation) {
        List<Method> methodList = new ArrayList();

        for (Method method : aClass.getMethods()) {

            if (method.getAnnotation(annotation) != null) {
                methodList.add(method);
            }
        }

        return methodList;
    }


    public static Class[] getClasses(String packageName) throws IOException, ClassNotFoundException {

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        String path = "tests";
        Enumeration resources = classLoader.getResources(path);

        List<File> dirs = new ArrayList();

        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList();

        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
            }
        }


        return classes;
    }


}
