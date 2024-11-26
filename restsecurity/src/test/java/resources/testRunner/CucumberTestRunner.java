package resources.testRunner;

import io.cucumber.junit.CucumberOptions;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Runs @BeforeAll once for the class
@CucumberOptions(
    features = "src/test/resources/features",  // Path to your feature files
    glue = "stepdefinitions",                  // Package where your step definition classes are located
    plugin = {"pretty", "html:target/cucumber-reports.html"} // Optional: for reporting
)
public class CucumberTestRunner {
}
