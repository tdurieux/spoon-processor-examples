package github.tdurieux.spoonExamples.main;

import github.tdurieux.spoonExamples.spoon.processor.AddFieldProcessor;

import java.io.File;

import spoon.Launcher;
import spoon.OutputType;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;
import spoon.support.compiler.FileSystemFolder;

/**
 * is the launcher of the project
 * 
 * @author Thomas Durieux
 * 
 */
public class Main {
	public static void main(String[] args) throws Exception {
		Launcher spoon = new Launcher();
		spoon.createOutputWriter(new File("target/genereted-classes"));
		spoon.addInputResource(new FileSystemFolder(new File(
				"src/testProject/java")));
		spoon.setArgs(new String[]{"-f"});
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
	}

}
