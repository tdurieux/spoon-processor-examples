package github.tdurieux.spoonExamples.spoon.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

/**
 * is Spoon processor used to add a field to a class
 * 
 * @author Thomas Durieux
 * 
 */
public class AddFieldProcessor<R extends Object> extends
		AbstractProcessor<CtClass<?>> {

	private String qualifiedClassName;
	private Class<R> fieldType;
	private String fieldName;

	public AddFieldProcessor(String qualifiedClassName, Class<R> fieldType,
			String fieldName) {
		this.qualifiedClassName = qualifiedClassName;
		this.fieldType = fieldType;
		this.fieldName = fieldName;
	}

	@Override
	public boolean isToBeProcessed(CtClass<?> candidate) {
		if (!super.isToBeProcessed(candidate))
			return false;
		if (candidate.getQualifiedName().equals(this.qualifiedClassName))
			return true;
		return false;
	}

	@Override
	public void process(CtClass<?> element) {

		Set<ModifierKind> modifiers = new HashSet<ModifierKind>(1);
		modifiers.add(ModifierKind.PRIVATE);
		CtTypeReference<R> typedReference = getFactory().Class()
				.createReference(this.fieldType);
		CtField<R> newField = getFactory().Field().create(element, modifiers,
				typedReference, fieldName);

		element.addField(newField);

		modifiers = new HashSet<ModifierKind>(1);
		modifiers.add(ModifierKind.PUBLIC);

		CtBlock<R> getterBlock = getFactory().Core().createBlock();
		getterBlock.addStatement(getFactory().Code()
				.createCodeSnippetStatement("return " + this.fieldName));

		getFactory().Method().create(element, modifiers, typedReference,
				"get" + fieldName, new ArrayList<CtParameter<?>>(),
				new HashSet<CtTypeReference<? extends Throwable>>(),
				getterBlock);

		CtBlock<R> setterBlock = getFactory().Core().createBlock();
		setterBlock.addStatement(getFactory().Code()
				.createCodeSnippetStatement(
						"this." + this.fieldName + " = value"));
		
		CtTypeReference<Object> returntypedReference = getFactory().Class()
				.createReference("void");
		
		CtMethod<?> method = getFactory().Method().create(element, modifiers, returntypedReference,
				"set" + fieldName, new ArrayList<CtParameter<?>>(),
				new HashSet<CtTypeReference<? extends Throwable>>(),
				setterBlock);
		
		getFactory().Method().createParameter(method, typedReference, "value");
	}

}