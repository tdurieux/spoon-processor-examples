package github.tdurieux.spoonExamples.spoon.processor;

import org.junit.Test;
import spoon.Launcher;
import spoon.OutputType;
import spoon.compiler.SpoonCompiler;
import spoon.processing.ProcessingManager;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import java.io.File;

import static org.junit.Assert.assertNotNull;

public class AddFieldProcessorTest {
    @Test
    public void AddFieldTest() throws Exception {
        Launcher spoon = new Launcher();
        spoon.addInputResource("src/testProject/java");
        spoon.getEnvironment().getDefaultFileGenerator().setOutputDirectory(new File("target/spooned"));
        Factory factory = spoon.getFactory();
        spoon.buildModel();

        ProcessingManager p = new QueueProcessingManager(factory);
        AddFieldProcessor proc = new AddFieldProcessor<>("github.tdurieux.testProject.entity.User", String.class,
                                                                "password");
        p.addProcessor(proc);
        p.process(factory.Class().getAll());

        spoon.prettyprint();

        SpoonCompiler compiler = spoon.createCompiler(factory);
        compiler.generateProcessedSourceFiles(OutputType.CLASSES);

        Launcher spoonCheck = new Launcher();
        spoonCheck.getEnvironment().getDefaultFileGenerator().setOutputDirectory(new File("target/spooned"));
        spoonCheck.addInputResource("target/spooned");
        spoonCheck.buildModel();

        CtClass<Object> spoonedClass = spoonCheck.getFactory().Class().get("github.tdurieux.testProject.entity.User");

        assertNotNull(spoonedClass.getField("password"));
    }

}
