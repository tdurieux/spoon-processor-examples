package github.tdurieux.spoonExamples.spoon.processor;

import static org.junit.Assert.*;
import github.tdurieux.spoonExamples.spoon.processor.AddFieldProcessor;

import java.io.File;

import org.junit.Test;

import spoon.Launcher;
import spoon.OutputType;
import spoon.processing.ProcessingManager;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;
import spoon.support.compiler.FileSystemFolder;

public class AddFieldProcessorTest {
	@Test
	public void AddFieldTest() throws Exception {
		Launcher spoon = new Launcher();
		spoon.addInputResource(new FileSystemFolder(new File(
				"src/testProject/java")));
		spoon.run();
		Factory factory = spoon.getFactory();
		ProcessingManager p = new QueueProcessingManager(factory);
		AddFieldProcessor proc = new AddFieldProcessor<String>(
				"github.tdurieux.testProject.entity.User", String.class,
				"password");
		p.addProcessor(proc);
		p.process(factory.Class().getAll());

		spoon.compiler.SpoonCompiler compiler = spoon.createCompiler(factory);
		compiler.generateProcessedSourceFiles(OutputType.CLASSES);
		
		Launcher spoonCheck = new Launcher();
		spoonCheck.addInputResource(new FileSystemFolder(new File(
				"spooned")));
		spoonCheck.run();
		
		CtClass<Object> spoonedClass = spoonCheck.getFactory().Class().get("github.tdurieux.testProject.entity.User");
		
		assertNotNull(spoonedClass.getField("password"));
	}

}
