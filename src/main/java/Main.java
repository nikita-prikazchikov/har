import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarHeader;
import de.sstoehr.harreader.model.HarQueryParam;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws HarReaderException, IOException {
        System.out.println("Enter path to .har file");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        HarReader harReader = new HarReader();
        Har har = harReader.readFromFile(new File(path)); //jcpenney.com.har
        ArrayList<HarEntry> jsonEntries = new ArrayList<HarEntry>();
        for (HarEntry arg : har.getLog().getEntries()) {
            if ((arg.getResponse().getContent().getMimeType().equals("application/json")
                    || arg.getResponse().getStatus() == 201) && arg
                    .getRequest().getUrl().contains("jcpenney")) {
                jsonEntries.add(arg);
            }
        }
        String directoryPath = path.replaceAll("//", "-").replaceAll("\\.", "");
        boolean isDirectoryCreated = new File(directoryPath).mkdir();
        System.out.println(
                "Directory " + directoryPath + "was created : " + isDirectoryCreated);
        if (isDirectoryCreated || new File(directoryPath).exists()) {
            for (HarEntry jsonEntry : jsonEntries) {
                harEntityToJson(jsonEntry, directoryPath);
            }
        }
    }

    public static void harEntityToJson(HarEntry jsonEntry, String directoryPath)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        ArrayNode predicates = objectNode.putArray("predicates");
        ArrayNode responses = objectNode.putArray("responses");
        ObjectNode is = responses.addObject().putObject("is");
        predicates.addObject().putObject("deepEquals")
                .put("path", getRelativePath(jsonEntry.getRequest().getUrl()));
        if (jsonEntry.getRequest().getQueryString() != null) {
            for (HarQueryParam harQueryParam : jsonEntry.getRequest().getQueryString()) {
                predicates.addObject().putObject("deepEquals").putObject("query")
                        .put(harQueryParam.getName(), harQueryParam.getValue());
            }
        }
        if (jsonEntry.getRequest().getBodySize() == 0) {
            predicates.addObject().putObject("deepEquals").put("body", "");
        } else {
            predicates.addObject().putObject("deepEquals")
                    .put("body",
                            jsonEntry.getRequest().getPostData().getText());
        }
        predicates.addObject().putObject("deepEquals")
                .put("method",
                        jsonEntry.getRequest().getMethod().name());

        is.put("statusCode", jsonEntry.getResponse().getStatus());
        ObjectNode headers = is.putObject("headers");
        ArrayNode setCookie = headers.putArray("Set-Cookie");
        for (HarHeader harHeader : jsonEntry.getResponse().getHeaders()) {
            if (harHeader.getName().equals("Set-Cookie")) {
                setCookie.add(harHeader.getValue());
            }
        }
        for (HarHeader harHeader : jsonEntry.getResponse().getHeaders()) {
            if (!harHeader.getName().equals("Set-Cookie")) {
                headers.put(harHeader.getName(), harHeader.getValue());
            }
        }
        if (jsonEntry.getResponse().getContent().getText() != null) {
            is.put("body", jsonEntry.getResponse().getContent().getText());
        }
        is.put("_mode", "text");

        printJsonToFile(directoryPath, jsonEntry, mapper, objectNode);
    }

    public static void printJsonToFile(String directoryPath,
            HarEntry jsonEntry, ObjectMapper mapper, Object objectNode) throws IOException {

        String jsonPath = directoryPath + "/" + getRelativePath(
                jsonEntry.getRequest().getUrl()).replaceAll("/", "_");
        int counter = 1;
        if (new File(jsonPath + ".json").exists()) {
            while (new File(jsonPath + "(" + counter + ")" + ".json").exists()) {
                counter++;
            }
            jsonPath = jsonPath + "(" + counter + ")" + ".json";
        } else {
            jsonPath = jsonPath + ".json";
        }
        FileWriter fileWriter = new FileWriter(
                new File(jsonPath));
        fileWriter.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode));
        fileWriter.close();
    }

    public static String getRelativePath(String absolutePath) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] strings = absolutePath.split("/");
        for (int i = 3; i < strings.length - 1; i++) {
            stringBuilder.append("/").append(strings[i]);
        }
        stringBuilder.append("/").append(strings[strings.length - 1].split("\\?")[0]);
        return stringBuilder.toString();
    }
}
