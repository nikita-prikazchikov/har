import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static String RESULTS_DIRECTORY = "results";
    private static String RESULTS_DIRECTORY_ALL = "results/all";
    private static HashMap<String, String> replaces = new HashMap<String, String>(10);

    private static String TEST_USER_ID = "Virt8gYfcGynEBUXKj";
    private static String TEST_ACCESS_TOKEN = "svGUAutomation0001.svJcp";
    private static String TEST_REFRESH_TOKEN = "svGUAutomation0001.svJcp.Rt0001";
    private static String TEST_DOMAIN = "services.matrix.dp-dev.jcpcloud2.net";

    public static void main(String[] args) throws HarReaderException, IOException {
        String HAR_DIRECTORY = "hars";

        File results = new File(RESULTS_DIRECTORY);
        createDirectory(results);

        File resultsAll = new File(RESULTS_DIRECTORY_ALL);
        createDirectory(resultsAll);

        File harDirectory = new File(HAR_DIRECTORY);
        if(!harDirectory.exists()){
            System.out.println("Har directory is missing: " + HAR_DIRECTORY);
            createDirectory(harDirectory);
            System.out.println("Put your har files into: " + HAR_DIRECTORY);
            return;
        }

        File[] files = harDirectory.listFiles();
        if (files == null){
            System.out.println("Har files are not found in directory: " + HAR_DIRECTORY);
            return;
        }
        for (File file:files){
            replaces.clear();
            if (file.getPath().endsWith(".har")) {
                processFile(file);
            }
        }
    }

    private static void processFile(File file) throws HarReaderException, IOException {
        System.out.println("Process file: " + file.getPath());
        if (!file.exists()) {
            System.out.println("File does not exist: " + file.getPath());
            return;
        }
        HarReader harReader = new HarReader();
        Har har = harReader.readFromFile(file);
        ArrayList<HarEntry> jsonEntries = filterEntries(har.getLog().getEntries());

        String directoryPath = RESULTS_DIRECTORY + "/" + file.getName().replaceAll(".har", "");
        createDirectory(new File(directoryPath));

        if (new File(directoryPath).exists()) {
            //Initial quick run to identify fields for replacement
            for (HarEntry jsonEntry : jsonEntries) {
                collectReplaces(jsonEntry);
            }
            System.out.println("Replaces will be done:" + replaces.toString());

            for (HarEntry jsonEntry : jsonEntries) {
                harEntityToJson(jsonEntry, directoryPath);
            }
        }
    }

    private static void createDirectory(File directory){
        if (!directory.exists()){
            if (directory.mkdir()){
                System.out.println("Directory \"" + directory.getPath() + "\" was created.");
            }
        }
    }

    private static ArrayList<HarEntry> filterEntries(List<HarEntry> harEntries) {
        ArrayList<HarEntry> entries = new ArrayList<HarEntry>();
        for (HarEntry arg : harEntries) {
            if (
                    (
                            (
                                    arg.getResponse().getContent().getMimeType().equals("application/json")
                                            || arg.getResponse().getContent().getMimeType().equals("text/json")
                                            || arg.getRequest().getMethod() == HttpMethod.POST
                            )
                                    && arg.getRequest().getUrl().matches("^https?://[^/]+jcpenney.*")
                    )
                            || arg.getRequest().getUrl().matches("^https?://[^/]+api\\.jcpenney.*")) {
                entries.add(arg);
            }
        }
        System.out.println(String.format("There are %d request filtered from %d", entries.size(), harEntries.size()));
        return entries;
    }

    private static void collectReplaces(HarEntry jsonEntry){
        for (HarHeader harHeader : jsonEntry.getResponse().getHeaders()){
            if(harHeader.getName().equalsIgnoreCase("DP_SFL_ID")){
                replaces.put(harHeader.getValue(), TEST_USER_ID);
            }
            if(harHeader.getName().equalsIgnoreCase("Access_Token")){
                replaces.put(harHeader.getValue(), TEST_ACCESS_TOKEN);
            }
            if(harHeader.getName().equalsIgnoreCase("Refresh_Token")){
                replaces.put(harHeader.getValue(), TEST_REFRESH_TOKEN);
            }
        }
        if(jsonEntry.getResponse().getContent().getText()!=null) {
            Pattern pattern = Pattern.compile("refresh_token\\W+([-\\w\\.]+)");
            Matcher matcher = pattern.matcher(jsonEntry.getResponse().getContent().getText());
            if (matcher.find()) {
                replaces.put(matcher.group(1), TEST_REFRESH_TOKEN);
            }

            pattern = Pattern.compile("access_token\\W+([-\\w\\.]+)");
            matcher = pattern.matcher(jsonEntry.getResponse().getContent().getText());

            if (matcher.find()) {
                replaces.put(matcher.group(1), TEST_ACCESS_TOKEN);
            }
        }
    }

    private static void harEntityToJson(HarEntry jsonEntry, String directoryPath)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        ArrayNode predicates = objectNode.putArray("predicates");
        ArrayNode responses = objectNode.putArray("responses");
        ObjectNode is = responses.addObject().putObject("is");
        predicates.addObject().putObject("deepEquals")
                .put("path", replaceData(getRelativePath(jsonEntry.getRequest().getUrl())));

        if (jsonEntry.getRequest().getQueryString() != null) {
            ObjectNode query = predicates.addObject().putObject("deepEquals")
                    .putObject("query");
            for (HarQueryParam harQueryParam : jsonEntry.getRequest().getQueryString()) {
                query.put(harQueryParam.getName(), harQueryParam.getValue());
            }
        }

        if (jsonEntry.getRequest().getBodySize() != 0) {
            predicates.addObject().putObject("deepEquals")
                    .put("body", replaceData(jsonEntry.getRequest().getPostData().getText()));
        }

        predicates.addObject().putObject("deepEquals")
                .put("method",
                        jsonEntry.getRequest().getMethod().name());

        is.put("statusCode", jsonEntry.getResponse().getStatus());
        ObjectNode headers = is.putObject("headers");
        ArrayNode setCookie = headers.putArray("Set-Cookie");
        for (HarHeader harHeader : jsonEntry.getResponse().getHeaders()) {
            if (harHeader.getName().equals("Set-Cookie")) {
                setCookie.add(replaceData(harHeader.getValue()));
            }
        }
        for (HarHeader harHeader : jsonEntry.getResponse().getHeaders()) {
            if (!harHeader.getName().equals("Set-Cookie")) {
                headers.put(harHeader.getName(), replaceData(harHeader.getValue()));
            }
        }
        headers.put("X-PROXY", "Service Virtualization");

        if (jsonEntry.getResponse().getContent().getText() != null) {
            is.put("body", replaceData(jsonEntry.getResponse().getContent().getText()));
        }

        is.put("_mode", "text");


        printJsonToFile(directoryPath, jsonEntry, mapper, objectNode);
        printJsonToFile(RESULTS_DIRECTORY_ALL, jsonEntry, mapper, objectNode);
    }

    private static String replaceData(String inputString){
        String result = inputString;
        for(String key : replaces.keySet()){
            result = result.replaceAll(key, replaces.get(key));
        }
        return replaceAccountHref(result);
    }

    private static String replaceAccountHref(String inputString){
        Pattern pattern = Pattern.compile("account_href\\W+https://([^/]+)");
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.find()) {
            return inputString.replaceAll(matcher.group(1), TEST_DOMAIN);
        }
        return inputString;
    }

    private static void printJsonToFile(String directoryPath,
                                        HarEntry jsonEntry, ObjectMapper mapper, Object objectNode) throws IOException {

        String folder = "";
        Pattern pattern = Pattern.compile("https?://([^/]+)");
        Matcher matcher = pattern.matcher(jsonEntry.getRequest().getUrl());
        if (matcher.find()){
            folder = "/" + matcher.group(1);
            File dir = new File(directoryPath + folder);
            createDirectory(dir);
        }

        String jsonPath = directoryPath + folder + "/" + jsonEntry.getRequest().getMethod() + getRelativePath(
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

    private static String getRelativePath(String absolutePath) {
        Pattern pattern = Pattern.compile("https?://(?:[^/]+)([^?]*)");
        Matcher matcher = pattern.matcher(absolutePath);

        if(matcher.find()){
            return matcher.group(1);
        }

        StringBuilder stringBuilder = new StringBuilder();
        String[] strings = absolutePath.split("/");
        for (int i = 3; i < strings.length - 1; i++) {
            stringBuilder.append("/").append(strings[i]);
        }
        stringBuilder.append("/").append(strings[strings.length - 1].split("\\?")[0]);
        return stringBuilder.toString();
    }
}
