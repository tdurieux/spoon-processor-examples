package github.tdurieux.spoonExamples.spoon.processor;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * is Spoon processor used to add a field to a class
 *
 * @author Thomas Durieux
 */
public class AddFieldProcessor<R extends Object> extends
        AbstractProcessor<CtClass<?>> {

    private String qualifiedClassName;
    private Class<R> fieldType;
    private String fieldName;

    public AddFieldProcessor(String qualifiedClassName, Class<R> fieldType, String fieldName) {
        this.qualifiedClassName = qualifiedClassName;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
    }

    @Override
    public boolean isToBeProcessed(CtClass<?> candidate) {
        return candidate.getQualifiedName().equals(this.qualifiedClassName);
    }

    @Override
    public void process(CtClass<?> element) {

        // create the field
        Set<ModifierKind> modifiers = new HashSet<>(1);
        modifiers.add(ModifierKind.PRIVATE);
        CtTypeReference<R> typedReference = getFactory().Class().createReference(this.fieldType);
        CtField<R> newField = getFactory().Field().create(element, modifiers, typedReference, fieldName);
        element.addField(newField);

        modifiers = new HashSet<>(1);
        modifiers.add(ModifierKind.PUBLIC);

        CtBlock<R> getterBlock = getFactory().Core().createBlock();
        getterBlock.addStatement(getFactory().Code().createCodeSnippetStatement("return " + this.fieldName));

        // create the getter method
        getFactory().Method().create(element,
                                            modifiers,
                                            // the return type
                                            typedReference,
                                            // the method name
                                            "get" + fieldName,
                                            // the parameter list
                                            new ArrayList<CtParameter<?>>(),
                                            // the exception list
                                            new HashSet<CtTypeReference<? extends Throwable>>(),
                                            // the method body
                                            getterBlock);

        CtBlock<R> setterBlock = getFactory().Core().createBlock();
        setterBlock.addStatement(getFactory().Code().createCodeSnippetStatement("this." + this.fieldName + " = value"));

        CtTypeReference<Object> returnTypedReference = getFactory().Class().createReference("void");

        // create the setter method
        CtMethod<?> method = getFactory().Method().create(element,
                                                                 modifiers,
                                                                 returnTypedReference,
                                                                 "set" + fieldName,
                                                                 new ArrayList<CtParameter<?>>(),
                                                                 new HashSet<CtTypeReference<? extends Throwable>>(),
                                                                 setterBlock);
        getFactory().Method().createParameter(method, typedReference, "value");
    }

}