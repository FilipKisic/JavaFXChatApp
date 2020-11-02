package hr.algebra.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;

public class ReflectionUtils {
    public static void readClassAndMembersInfo(Class<?> clazz) {
        try (FileWriter docGenerator = new FileWriter("documentation.html")) {
            docGenerator.write("<!DOCTYPE html>");
            docGenerator.write("<html>");
            docGenerator.write("<head>");
            docGenerator.write("<title>Class documentation</title>");
            docGenerator.write("</head>");
            docGenerator.write("<body>");
            readClassInfo(clazz, docGenerator);
            appendFields(clazz, docGenerator);
            appendMethods(clazz, docGenerator);
            appendConstructors(clazz, docGenerator);
            docGenerator.write("</body>");
            docGenerator.write("</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readClassInfo(Class<?> clazz, FileWriter docGenerator) throws IOException {
        appendPackage(clazz, docGenerator);
        appendModifiers(clazz, docGenerator);
        appendParent(clazz, docGenerator, true);
        appendInterfaces(clazz, docGenerator);
    }

    private static void appendPackage(Class<?> clazz, FileWriter docGenerator) throws IOException {
        docGenerator.write("<p> " + clazz.getPackage().toString() + "</p>");
    }

    private static void appendModifiers(Class<?> clazz, FileWriter docGenerator) throws IOException {
        docGenerator.write("<h3>" + Modifier.toString(clazz.getModifiers()) + " " + clazz.getSimpleName());
    }

    private static void appendParent(Class<?> clazz, FileWriter docGenerator, boolean first) throws IOException {
        Class<?> parent = clazz.getSuperclass();
        if (parent == null) return;
        if (first) docGenerator.write(" extends ");
        docGenerator.write(parent.getSimpleName());
        appendParent(parent, docGenerator, false);
    }

    private static void appendInterfaces(Class<?> clazz, FileWriter docGenerator) throws IOException {
        if (clazz.getInterfaces().length > 0)
            docGenerator.write(" implements");
        else {
            docGenerator.write("</h3>");
            return;
        }
        for (Class<?> infce : clazz.getInterfaces()) {
            docGenerator.write(" " + infce.getSimpleName());
        }
        docGenerator.write("</h3>");
    }

    private static void appendFields(Class<?> clazz, FileWriter docGenerator) throws IOException {
        Field[] fields = clazz.getDeclaredFields();
        docGenerator.write("<h4>Fields:</h4>");
        String fieldType;
        for (Field field : fields) {
            fieldType = field.getType().toString();
            fieldType = fieldType.substring(fieldType.lastIndexOf(".") + 1);
            docGenerator.write("<p>" + Modifier.toString(field.getModifiers()));
            docGenerator.write(" " + fieldType + " " + field.getName() + "</p>");
        }
    }

    private static void appendMethods(Class<?> clazz, FileWriter docGenerator) throws IOException {
        Method[] methods = clazz.getDeclaredMethods();
        docGenerator.write("<h4>Methods:</h4>");
        String returnType;
        for (Method method : methods) {
            returnType = method.getReturnType().toString();
            returnType = returnType.substring(returnType.lastIndexOf(".") + 1);
            docGenerator.write("<p>" + Modifier.toString(method.getModifiers()));
            docGenerator.write(" " + returnType + " " + method.getName());
            appendParameters(method, docGenerator);
            appendExceptions(method, docGenerator);
        }
    }

    private static void appendParameters(Executable executable, FileWriter docGenerator) throws IOException {
        docGenerator.write("(");
        String parameterType;
        Parameter[] parameters = executable.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            parameterType = parameters[i].getType().getName();
            parameterType = parameterType.substring(parameterType.lastIndexOf(".") + 1);
            docGenerator.write(parameterType + " " + parameters[i].getName());
            if (i != parameters.length - 1)
                docGenerator.write(", ");
        }
        docGenerator.write(")");
    }

    private static void appendExceptions(Executable Executable, FileWriter docGenerator) throws IOException {
        Class<?>[] exceptionTypes = Executable.getExceptionTypes();
        if (exceptionTypes.length > 0) {
            docGenerator.write(" throws ");
            for(int i = 0; i < exceptionTypes.length; i++){
                docGenerator.write(exceptionTypes[i].toString());
                if(i != exceptionTypes.length - 1)
                    docGenerator.write(", ");
            }
        } else docGenerator.write("</p>");
    }

    private static void appendConstructors(Class<?> clazz, FileWriter docGenerator) throws IOException {
        Constructor[] constructors = clazz.getConstructors();
        docGenerator.write("<h4>Constructors:</h4>");
        String name;
        for (Constructor constructor : constructors) {
            name = constructor.getName();
            name = name.substring(name.lastIndexOf(".") + 1);
            docGenerator.write("<p>" + Modifier.toString(constructor.getModifiers()) + " " + name);
            appendParameters(constructor, docGenerator);
            appendExceptions(constructor, docGenerator);
        }
    }
}
