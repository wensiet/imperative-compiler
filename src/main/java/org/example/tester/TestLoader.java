package org.example.tester;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestLoader {
    private final List<TestCase> testCases = new ArrayList<>();

    public TestLoader(String conftestPath) throws IOException {
        loadTestCases(conftestPath);
    }

    private void loadTestCases(String fileName) throws IOException {
        Yaml yaml = new Yaml();

        try (InputStream inputStream = TestLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new TestCaseException("File not found: " + fileName);
            }

            Map<String, Object> config = yaml.load(inputStream);

            // Read general.directory
            Map<String, Object> general = (Map<String, Object>) config.get("general");
            if (general == null || !general.containsKey("directory")) {
                throw new TestCaseException("Invalid config: general.directory is missing");
            }
            String directory = (String) general.get("directory");

            // Read suites
            List<Map<String, Object>> suites = (List<Map<String, Object>>) config.get("suites");
            if (suites == null) {
                throw new TestCaseException("Invalid config: No suites found");
            }

            for (Map<String, Object> suite : suites) {
                String name = (String) suite.get("name");
                Map<String, Object> params = (Map<String, Object>) suite.get("params");

                if (params != null) {
                    int expectedStatusCode = (int) params.get("expectedStatusCode");
                    String expectedOutput = (String) params.get("expectedOutput");

                    // Read content of the file: directory/name
                    String testFilePath = directory + "/" + name;
                    String fileContent = readFileContent(testFilePath);

                    RuntimeTestCaseParams testCaseParams = new RuntimeTestCaseParams(expectedStatusCode, expectedOutput);
                    if ((boolean) suite.get("enabled")) {
                        testCases.add(new TestCase(name, fileContent));
                    }
                }
            }
        }
    }

    private String readFileContent(String filePath) throws IOException {
        InputStream inputStream = TestLoader.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new TestCaseException("Test case file not found: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString().trim();
        }
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }
}






