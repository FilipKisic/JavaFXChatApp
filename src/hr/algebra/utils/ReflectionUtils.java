package hr.algebra.utils;

import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {
    private static final String PACKAGE_NAME = "hr.algebra.model";
    private static final String PACKAGE_LOCATION = "D:\\ALGEBRA\\5.semestar - Programiranje u Javi 2\\ChatApp\\src\\hr\\algebra\\model";

    public static void createDocumentation() {
        try (FileWriter docGenerator = new FileWriter("htmlDocumentation.html")) {
            docGenerator.write("<!DOCTYPE html>");
            docGenerator.write("<html>");
            docGenerator.write("<head>");
            docGenerator.write("<title>Class documentation</title>");
            docGenerator.write("</head>");
            docGenerator.write("<body>");

            List<String> packageFiles = Files
                    .list(Paths.get(PACKAGE_LOCATION))
                    .map(file -> file.toFile().getName())
                    .collect(Collectors.toList());

            for (String fileName : packageFiles){
                if(fileName.indexOf(".") > 0)
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));

                Class<?> unknownObject = Class.forName(PACKAGE_NAME + "." + fileName);

                docGenerator.write("<h1> Class name: " + unknownObject.getName() + " </h1>");
                Field[] fields = unknownObject.getDeclaredFields();

                docGenerator.write("<h2>Fields:</h2>");
                for (Field field : fields){
                    int modifiers = field.getModifiers();
                    docGenerator.write(modifiers % 2 == 0 ? "private " : "public ");
                    String dataType = field.getType().getName();
                    if(dataType.indexOf(".") > 0)
                        dataType = dataType.substring(dataType.lastIndexOf(".") + 1);
                    docGenerator.write(dataType + " ");
                    docGenerator.write(field.getName() + " <br>");
                }

                docGenerator.write("<h2>Constructors:</h2>");
                Constructor[] constructors = unknownObject.getConstructors();

                for (Constructor constructor : constructors){
                    Parameter[] parameters = constructor.getParameters();

                    if(parameters.length > 0){
                        docGenerator.write("<h3>Constructor parameters:</h3>");
                        for (Parameter parameter : parameters){
                            String dataType = parameter.getType().getName();
                            if(dataType.indexOf(".") > 0)
                                dataType = dataType.substring(dataType.lastIndexOf(".") + 1);
                            docGenerator.write("Parameter: " + dataType + " ");
                            docGenerator.write(parameter.getName() + "<br>");
                        }
                    }
                    else
                        docGenerator.write("<h3>Default constructor without parameters</h3>");
                }
            }
            docGenerator.write("</body>");
            docGenerator.write("</html>");
            docGenerator.flush();
            System.out.println("Documentation successfully generated!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
