import java.io.Console;
import java.util.*;

public class BddFileDetails {
    private String fileName;
    private String scenarioName;
    private List<String> steps;

    private Queue<Integer> stepsDraftOrder = new LinkedList<>();

    BddFileDetails(String fileName, String scenarioName, String[] steps, HashMap<String, Integer> orderSteps) {
        this.fileName = fileName;
        this.scenarioName = scenarioName;
        this.steps = new ArrayList<String>();
        this.steps.addAll(Arrays.asList(steps));

        int stepId = 0;
        for (String step : steps) {

            for (String key : orderSteps.keySet()) {
                if (step.matches(key)) {
                    for (int j = 0; j < orderSteps.get(key); j++) {
                        stepsDraftOrder.offer(stepId);
                    }
                }
            }
            stepId++;
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    Integer getStepIdDraftOrder(){
        return stepsDraftOrder.poll();
    }

    public Queue<Integer> getStepsDraftOrder() {
        return stepsDraftOrder;
    }
}
