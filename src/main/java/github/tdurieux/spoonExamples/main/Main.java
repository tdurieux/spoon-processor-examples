package github.tdurieux.spoonExamples.main;

import github.tdurieux.spoonExamples.spoon.processor.AddFieldProcessor;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import java.io.File;

/**
 * is the launcher of the project
 *
 * @author Thomas Durieux
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // create spoon
        Launcher spoon = new Launcher();
        // set the output folder
        spoon.getEnvironment().getDefaultFileGenerator().setOutputDirectory(new File("target/spooned"));
        // set the project root
        spoon.addInputResource("src/testProject/java");

        Factory factory = spoon.getFactory();
        // create a list of spoon processor
        ProcessingManager p = new QueueProcessingManager(factory);
        // create the processor
        AddFieldProcessor proc = new AddFieldProcessor<>("github.tdurieux.testProject.entity.User", String.class,
                                                                "password");
        p.addProcessor(proc);
        // process all the classes of the project
        p.process();
        // generate the output
        spoon.prettyprint();
    }

}
