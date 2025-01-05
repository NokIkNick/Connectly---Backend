package dk.connectly.runner;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;

import dk.connectly.config.ApplicationConfig;
import dk.connectly.config.HibernateConfig;
import dk.connectly.config.Routes;
import dk.connectly.utils.Populator;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Runs @BeforeAll once for the class
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",  // Path to your feature files
    glue = "dk.connectly.stepdefinitions",     // Package where your step definition classes are located
    plugin = {"pretty", "html:target/cucumber-reports.html"}, // Optional: for reporting
    monochrome = true
    , objectFactory = io.cucumber.core.backend.DefaultObjectFactory.class // Disable Spring
)
public class CucumberTestRunner {
}
