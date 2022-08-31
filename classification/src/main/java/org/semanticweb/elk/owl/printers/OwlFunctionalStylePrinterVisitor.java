/*
 * #%L
 * ELK OWL Object Interfaces
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Department of Computer Science, University of Oxford
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.semanticweb.elk.owl.printers;

import java.io.IOException;

import org.semanticweb.elk.owl.interfaces.ElkAnnotation;
import org.semanticweb.elk.owl.interfaces.ElkAnnotationAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkAnnotationProperty;
import org.semanticweb.elk.owl.interfaces.ElkAnnotationPropertyDomainAxiom;
import org.semanticweb.elk.owl.interfaces.ElkAnnotationPropertyRangeAxiom;
import org.semanticweb.elk.owl.interfaces.ElkAnonymousIndividual;
import org.semanticweb.elk.owl.interfaces.ElkAsymmetricObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkCardinalityRestriction;
import org.semanticweb.elk.owl.interfaces.ElkCardinalityRestrictionQualified;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owl.interfaces.ElkClassAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDataAllValuesFrom;
import org.semanticweb.elk.owl.interfaces.ElkDataComplementOf;
import org.semanticweb.elk.owl.interfaces.ElkDataExactCardinalityUnqualified;
import org.semanticweb.elk.owl.interfaces.ElkDataExactCardinalityQualified;
import org.semanticweb.elk.owl.interfaces.ElkDataHasValue;
import org.semanticweb.elk.owl.interfaces.ElkDataIntersectionOf;
import org.semanticweb.elk.owl.interfaces.ElkDataMaxCardinalityUnqualified;
import org.semanticweb.elk.owl.interfaces.ElkDataMaxCardinalityQualified;
import org.semanticweb.elk.owl.interfaces.ElkDataMinCardinalityUnqualified;
import org.semanticweb.elk.owl.interfaces.ElkDataMinCardinalityQualified;
import org.semanticweb.elk.owl.interfaces.ElkDataOneOf;
import org.semanticweb.elk.owl.interfaces.ElkDataProperty;
import org.semanticweb.elk.owl.interfaces.ElkDataPropertyAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDataPropertyDomainAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDataPropertyRangeAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDataSomeValuesFrom;
import org.semanticweb.elk.owl.interfaces.ElkDataUnionOf;
import org.semanticweb.elk.owl.interfaces.ElkDatatype;
import org.semanticweb.elk.owl.interfaces.ElkDatatypeDefinitionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDatatypeRestriction;
import org.semanticweb.elk.owl.interfaces.ElkDeclarationAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDifferentIndividualsAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDisjointClassesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDisjointDataPropertiesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDisjointObjectPropertiesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDisjointUnionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkEntity;
import org.semanticweb.elk.owl.interfaces.ElkEquivalentClassesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkEquivalentDataPropertiesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkEquivalentObjectPropertiesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkFacetRestriction;
import org.semanticweb.elk.owl.interfaces.ElkFunctionalDataPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkFunctionalObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkHasKeyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkInverseObjectPropertiesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkIrreflexiveObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkLiteral;
import org.semanticweb.elk.owl.interfaces.ElkNamedIndividual;
import org.semanticweb.elk.owl.interfaces.ElkNegativeDataPropertyAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObject;
import org.semanticweb.elk.owl.interfaces.ElkObjectAllValuesFrom;
import org.semanticweb.elk.owl.interfaces.ElkObjectComplementOf;
import org.semanticweb.elk.owl.interfaces.ElkObjectExactCardinalityUnqualified;
import org.semanticweb.elk.owl.interfaces.ElkObjectExactCardinalityQualified;
import org.semanticweb.elk.owl.interfaces.ElkObjectHasSelf;
import org.semanticweb.elk.owl.interfaces.ElkObjectHasValue;
import org.semanticweb.elk.owl.interfaces.ElkObjectIntersectionOf;
import org.semanticweb.elk.owl.interfaces.ElkObjectInverseOf;
import org.semanticweb.elk.owl.interfaces.ElkObjectMaxCardinalityUnqualified;
import org.semanticweb.elk.owl.interfaces.ElkObjectMaxCardinalityQualified;
import org.semanticweb.elk.owl.interfaces.ElkObjectMinCardinalityUnqualified;
import org.semanticweb.elk.owl.interfaces.ElkObjectMinCardinalityQualified;
import org.semanticweb.elk.owl.interfaces.ElkObjectOneOf;
import org.semanticweb.elk.owl.interfaces.ElkObjectProperty;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyChain;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyDomainAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyRangeAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObjectSomeValuesFrom;
import org.semanticweb.elk.owl.interfaces.ElkObjectUnionOf;
import org.semanticweb.elk.owl.interfaces.ElkPropertyAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkPropertyDomainAxiom;
import org.semanticweb.elk.owl.interfaces.ElkPropertyRangeAxiom;
import org.semanticweb.elk.owl.interfaces.ElkPropertyRestriction;
import org.semanticweb.elk.owl.interfaces.ElkPropertyRestrictionQualified;
import org.semanticweb.elk.owl.interfaces.ElkReflexiveObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSWRLRule;
import org.semanticweb.elk.owl.interfaces.ElkSameIndividualAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSubAnnotationPropertyOfAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSubClassOfAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSubDataPropertyOfAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSubObjectPropertyOfAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSymmetricObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkTransitiveObjectPropertyAxiom;
import org.semanticweb.elk.owl.iris.ElkAbbreviatedIri;
import org.semanticweb.elk.owl.iris.ElkFullIri;
import org.semanticweb.elk.owl.predefined.PredefinedElkIris;
import org.semanticweb.elk.owl.visitors.ElkEntityVisitor;
import org.semanticweb.elk.owl.visitors.ElkObjectVisitor;

/**
 * A ELK Object visitor pattern implementation for printing ELK Objects in OWL 2
 * functional-style syntax.
 * 
 * @author "Yevgeny Kazakov"
 * 
 */
class OwlFunctionalStylePrinterVisitor implements ElkObjectVisitor<Void> {

	private final Appendable writer;

	private final EntityPrinter entityPrinter;
	private boolean expandAbbreviatedIris = false;

	/**
	 * Create a printer using a writer which can append strings.
	 * 
	 * @param writer
	 *            the string appender used for printing
	 * @param expandAbbreviatedIris
	 *            if true, the printer will print abbreviated IRIs as full IRIs
	 *            (in "<>")
	 * 
	 */
	OwlFunctionalStylePrinterVisitor(Appendable writer,
			boolean expandAbbreviatedIris) {
		this.writer = writer;
		this.entityPrinter = new EntityPrinter();
		this.expandAbbreviatedIris = expandAbbreviatedIris;
	}

	OwlFunctionalStylePrinterVisitor(Appendable writer) {
		this(writer, false);
	}

	@Override
	public Void visit(ElkAnnotationProperty elkAnnotationProperty) {
		write(elkAnnotationProperty);
		return null;
	}

	@Override
	public Void visit(ElkAnonymousIndividual elkAnonymousIndividual) {
		write(elkAnonymousIndividual.getNodeId());
		return null;
	}

	@Override
	public Void visit(
			ElkAsymmetricObjectPropertyAxiom elkAsymmetricObjectPropertyAxiom) {
		write("AsymmetricObjectProperty(");
		write(elkAsymmetricObjectPropertyAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkClass elkClass) {
		write(elkClass);
		return null;
	}

	@Override
	public Void visit(ElkClassAssertionAxiom elkClassAssertionAxiom) {
		write("ClassAssertion(");
		write(elkClassAssertionAxiom.getClassExpression());
		write(' ');
		write(elkClassAssertionAxiom.getIndividual());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataAllValuesFrom elkDataAllValuesFrom) {
		write("DataAllValuesFrom(");
		write(elkDataAllValuesFrom.getDataPropertyExpressions());
		write(' ');
		write(elkDataAllValuesFrom.getDataRange());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataComplementOf elkDataComplementOf) {
		write("DataComplementOf(");
		write(elkDataComplementOf.getDataRange());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataExactCardinalityUnqualified elkDataExactCardinality) {
		write("DataExactCardinality(");
		write(elkDataExactCardinality);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkDataExactCardinalityQualified elkDataExactCardinalityQualified) {
		write("DataExactCardinality(");
		write(elkDataExactCardinalityQualified);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataHasValue elkDataHasValue) {
		write("DataHasValue(");
		write(elkDataHasValue);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataIntersectionOf elkDataIntersectionOf) {
		write("DataIntersectionOf(");
		write(elkDataIntersectionOf.getDataRanges());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataMaxCardinalityUnqualified elkDataMaxCardinality) {
		write("DataMaxCardinality(");
		write(elkDataMaxCardinality);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkDataMaxCardinalityQualified elkDataMaxCardinalityQualified) {
		write("DataMaxCardinality(");
		write(elkDataMaxCardinalityQualified);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataMinCardinalityUnqualified elkDataMinCardinality) {
		write("DataMinCardinality(");
		write(elkDataMinCardinality);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkDataMinCardinalityQualified elkDataMinCardinalityQualified) {
		write("DataMinCardinality(");
		write(elkDataMinCardinalityQualified);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataOneOf elkDataOneOf) {
		write("DataOneOf(");
		write(elkDataOneOf.getLiterals());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataProperty elkDataProperty) {
		write(elkDataProperty);
		return null;
	}

	@Override
	public Void visit(
			ElkDataPropertyAssertionAxiom elkDataPropertyAssertionAxiom) {
		write("DataPropertyAssertion(");
		write(elkDataPropertyAssertionAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataPropertyDomainAxiom elkDataPropertyDomainAxiom) {
		write("DataPropertyDomain(");
		write(elkDataPropertyDomainAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataPropertyRangeAxiom elkDataPropertyRangeAxiom) {
		write("DataPropertyRange(");
		write(elkDataPropertyRangeAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataSomeValuesFrom elkDataSomeValuesFrom) {
		write("DataSomeValuesFrom(");
		write(elkDataSomeValuesFrom.getDataPropertyExpressions());
		write(' ');
		write(elkDataSomeValuesFrom.getDataRange());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDatatype elkDatatype) {
		write(elkDatatype);
		return null;
	}

	@Override
	public Void visit(ElkDatatypeRestriction elkDatatypeRestriction) {
		write("DatatypeRestriction(");
		write(elkDatatypeRestriction.getDatatype());
		write(' ');
		write(elkDatatypeRestriction.getFacetRestrictions());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDataUnionOf elkDataUnionOf) {
		write("DataUnionOf(");
		write(elkDataUnionOf.getDataRanges());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDeclarationAxiom elkDeclarationAxiom) {
		write("Declaration(");
		elkDeclarationAxiom.getEntity().accept(this.entityPrinter);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDifferentIndividualsAxiom elkDifferentIndividualsAxiom) {
		write("DifferentIndividuals(");
		write(elkDifferentIndividualsAxiom.getIndividuals());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDisjointClassesAxiom elkDisjointClasses) {
		write("DisjointClasses(");
		write(elkDisjointClasses.getClassExpressions());
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkDisjointDataPropertiesAxiom elkDisjointDataPropertiesAxiom) {
		write("DisjointDataProperties(");
		write(elkDisjointDataPropertiesAxiom.getDataPropertyExpressions());
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkDisjointObjectPropertiesAxiom elkDisjointObjectPropertiesAxiom) {
		write("DisjointObjectProperties(");
		write(elkDisjointObjectPropertiesAxiom.getObjectPropertyExpressions());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkDisjointUnionAxiom elkDisjointUnionAxiom) {
		write("DisjointUnion(");
		write(elkDisjointUnionAxiom.getDefinedClass());
		write(' ');
		write(elkDisjointUnionAxiom.getClassExpressions());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkEquivalentClassesAxiom elkEquivalentClassesAxiom) {
		write("EquivalentClasses(");
		write(elkEquivalentClassesAxiom.getClassExpressions());
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkEquivalentDataPropertiesAxiom elkEquivalentDataProperties) {
		write("EquivalentDataProperties(");
		write(elkEquivalentDataProperties.getDataPropertyExpressions());
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkEquivalentObjectPropertiesAxiom elkEquivalentObjectProperties) {
		write("EquivalentObjectProperties(");
		write(elkEquivalentObjectProperties.getObjectPropertyExpressions());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkFacetRestriction restricition) {
		write(restricition.getConstrainingFacet());
		write(' ');
		write(restricition.getRestrictionValue());
		return null;
	}

	@Override
	public Void visit(
			ElkFunctionalDataPropertyAxiom elkFunctionalDataPropertyAxiom) {
		write("FunctionalDataProperty(");
		write(elkFunctionalDataPropertyAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkFunctionalObjectPropertyAxiom elkFunctionalObjectPropertyAxiom) {
		write("FunctionalObjectProperty(");
		write(elkFunctionalObjectPropertyAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkInverseFunctionalObjectPropertyAxiom elkInverseFunctionalObjectPropertyAxiom) {
		write("InverseFunctionalObjectProperty(");
		write(elkInverseFunctionalObjectPropertyAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkInverseObjectPropertiesAxiom elkInverseObjectPropertiesAxiom) {
		write("InverseObjectProperties(");
		write(elkInverseObjectPropertiesAxiom
				.getFirstObjectPropertyExpression());
		write(' ');
		write(elkInverseObjectPropertiesAxiom
				.getSecondObjectPropertyExpression());
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkIrreflexiveObjectPropertyAxiom elkIrreflexiveObjectPropertyAxiom) {
		write("IrreflexiveObjectProperty(");
		write(elkIrreflexiveObjectPropertyAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkLiteral elkLiteral) {
		write("\"");
		write(elkLiteral.getLexicalForm());
		write("\"");

		if (!isPlain(elkLiteral)) {
			write("^^");
			write(elkLiteral.getDatatype());
		}

		return null;
	}

	private static boolean isPlain(ElkLiteral elkLiteral) {

		return elkLiteral.getDatatype() == null
				|| PredefinedElkIris.RDF_PLAIN_LITERAL.equals(elkLiteral
						.getDatatype().getIri());
	}

	@Override
	public Void visit(ElkNamedIndividual elkNamedIndividual) {
		write(elkNamedIndividual);
		return null;
	}

	@Override
	public Void visit(
			ElkNegativeDataPropertyAssertionAxiom elkNegativeDataPropertyAssertion) {
		write("NegativeDataPropertyAssertion(");
		write(elkNegativeDataPropertyAssertion);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkNegativeObjectPropertyAssertionAxiom elkNegativeObjectPropertyAssertion) {
		write("NegativeObjectPropertyAssertion(");
		write(elkNegativeObjectPropertyAssertion);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectAllValuesFrom elkObjectAllValuesFrom) {
		write("ObjectAllValuesFrom(");
		write(elkObjectAllValuesFrom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectComplementOf elkObjectComplementOf) {
		write("ObjectComplementOf(");
		write(elkObjectComplementOf.getClassExpression());
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkObjectExactCardinalityUnqualified elkObjectExactCardinality) {
		write("ObjectExactCardinality(");
		write(elkObjectExactCardinality);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkObjectExactCardinalityQualified elkObjectExactCardinalityQualified) {
		write("ObjectExactCardinality(");
		write(elkObjectExactCardinalityQualified);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectHasSelf elkObjectHasSelf) {
		write("ObjectHasSelf(");
		write(elkObjectHasSelf);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectHasValue elkObjectHasValue) {
		write("ObjectHasValue(");
		write(elkObjectHasValue);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectIntersectionOf elkObjectIntersectionOf) {
		write("ObjectIntersectionOf(");
		write(elkObjectIntersectionOf.getClassExpressions());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectInverseOf elkObjectInverseOf) {
		write("ObjectInverseOf(");
		write(elkObjectInverseOf.getObjectProperty());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectMaxCardinalityUnqualified elkObjectMaxCardinality) {
		write("ObjectMaxCardinality(");
		write(elkObjectMaxCardinality);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkObjectMaxCardinalityQualified elkObjectMaxCardinalityQualified) {
		write("ObjectMaxCardinality(");
		write(elkObjectMaxCardinalityQualified);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectMinCardinalityUnqualified elkObjectMinCardinality) {
		write("ObjectMinCardinality(");
		write(elkObjectMinCardinality);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkObjectMinCardinalityQualified elkObjectMinCardinalityQualified) {
		write("ObjectMinCardinality(");
		write(elkObjectMinCardinalityQualified);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectOneOf elkObjectOneOf) {
		write("ObjectOneOf(");
		write(elkObjectOneOf.getIndividuals());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectProperty elkObjectProperty) {
		write(elkObjectProperty);
		return null;
	}

	@Override
	public Void visit(
			ElkObjectPropertyAssertionAxiom elkObjectPropertyAssertionAxiom) {
		write("ObjectPropertyAssertion(");
		write(elkObjectPropertyAssertionAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectPropertyChain elkObjectPropertyChain) {
		write("ObjectPropertyChain(");
		write(elkObjectPropertyChain.getObjectPropertyExpressions());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectPropertyDomainAxiom elkObjectPropertyDomainAxiom) {
		write("ObjectPropertyDomain(");
		write(elkObjectPropertyDomainAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectPropertyRangeAxiom elkObjectPropertyRangeAxiom) {
		write("ObjectPropertyRange(");
		write(elkObjectPropertyRangeAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectSomeValuesFrom elkObjectSomeValuesFrom) {
		write("ObjectSomeValuesFrom(");
		write(elkObjectSomeValuesFrom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkObjectUnionOf elkObjectUnionOf) {
		write("ObjectUnionOf(");
		write(elkObjectUnionOf.getClassExpressions());
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkReflexiveObjectPropertyAxiom elkReflexiveObjectPropertyAxiom) {
		write("ReflexiveObjectProperty(");
		write(elkReflexiveObjectPropertyAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkSameIndividualAxiom elkSameIndividualAxiom) {
		write("SameIndividual(");
		write(elkSameIndividualAxiom.getIndividuals());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkSubClassOfAxiom elkSubClassOfAxiom) {
		write("SubClassOf(");
		write(elkSubClassOfAxiom.getSubClassExpression());
		write(' ');
		write(elkSubClassOfAxiom.getSuperClassExpression());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkSubDataPropertyOfAxiom elkSubDataPropertyOfAxiom) {
		write("SubDataPropertyOf(");
		write(elkSubDataPropertyOfAxiom.getSubDataPropertyExpression());
		write(' ');
		write(elkSubDataPropertyOfAxiom.getSuperDataPropertyExpression());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkSubObjectPropertyOfAxiom elkSubObjectPropertyOfAxiom) {
		write("SubObjectPropertyOf(");
		write(elkSubObjectPropertyOfAxiom.getSubObjectPropertyExpression());
		write(' ');
		write(elkSubObjectPropertyOfAxiom.getSuperObjectPropertyExpression());
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkSymmetricObjectPropertyAxiom elkSymmetricObjectPropertyAxiom) {
		write("SymmetricObjectProperty(");
		write(elkSymmetricObjectPropertyAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkTransitiveObjectPropertyAxiom elkTransitiveObjectPropertyAxiom) {
		write("TransitiveObjectProperty(");
		write(elkTransitiveObjectPropertyAxiom);
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkHasKeyAxiom elkHasKey) {
		write("HasKey( ");
		write(elkHasKey.getClassExpression());
		write(" ( ");
		write(elkHasKey.getObjectPropertyExpressions());
		write(" )( ");
		write(elkHasKey.getDataPropertyExpressions());
		write(" )) ");
		return null;
	}

	@Override
	public Void visit(ElkDatatypeDefinitionAxiom elkDatatypeDefn) {
		write("DatatypeDefinition( ");
		write(elkDatatypeDefn.getDatatype());
		write(" ");
		write(elkDatatypeDefn.getDataRange());
		write(" )");
		return null;
	}

	@Override
	public Void visit(ElkAnnotationAssertionAxiom annAssertionAxiom) {
		write("AnnotationAssertion( ");
		write(annAssertionAxiom.getProperty());
		write(' ');
		write(annAssertionAxiom.getSubject());
		write(' ');
		write(annAssertionAxiom.getValue());
		write(" )");
		return null;
	}

	@Override
	public Void visit(ElkFullIri iri) {
		write(iri.toString());
		return null;
	}

	@Override
	public Void visit(ElkAbbreviatedIri iri) {
		write(expandAbbreviatedIris ? "<" + iri.getFullIriAsString() + ">"
				: iri.toString());
		return null;
	}

	@Override
	public Void visit(
			ElkSubAnnotationPropertyOfAxiom subAnnotationPropertyOfAxiom) {
		write("SubAnnotationPropertyOf(");
		write(subAnnotationPropertyOfAxiom.getSubAnnotationProperty());
		write(' ');
		write(subAnnotationPropertyOfAxiom.getSuperAnnotationProperty());
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkAnnotationPropertyDomainAxiom annotationPropertyDomainAxiom) {
		write("AnnotationPropertyDomain(");
		write(annotationPropertyDomainAxiom.getProperty());
		write(' ');
		write(annotationPropertyDomainAxiom.getDomain());
		write(')');
		return null;
	}

	@Override
	public Void visit(
			ElkAnnotationPropertyRangeAxiom annotationPropertyRangeAxiom) {
		write("AnnotationPropertyRange(");
		write(annotationPropertyRangeAxiom.getProperty());
		write(' ');
		write(annotationPropertyRangeAxiom.getRange());
		write(')');
		return null;
	}

	@Override
	public Void visit(ElkAnnotation elkAnnotation) {
		write("Annotation(");
		write(elkAnnotation.getProperty());
		write(' ');
		write(elkAnnotation.getValue());
		write(')');
		return null;
	}

	private final void write(char ch) {
		try {
			writer.append(ch);
		} catch (IOException e) {
			throw new PrintingException(e.getMessage(), e.getCause());
		}
	}

	private final void write(ElkEntity elkEntity) {
		write(elkEntity.getIri());
	}

	private final void write(ElkObject elkObject) {
		elkObject.accept(this);
	}

	private final void write(Integer n) {
		try {
			writer.append(n.toString());
		} catch (IOException e) {
			throw new PrintingException(e.getMessage(), e.getCause());
		}
	}

	private final void write(Iterable<? extends ElkObject> elkObjects) {
		boolean first = true;
		for (ElkObject elkObject : elkObjects) {
			if (!first)
				write(' ');
			else
				first = false;
			write(elkObject);
		}
	}

	private final void write(String string) {
		try {
			writer.append(string);
		} catch (IOException e) {
			throw new PrintingException(e.getMessage(), e.getCause());
		}
	}

	private final <P extends ElkObject> void write(
			ElkPropertyRestriction<P> elkPropertyRestriction) {
		write(elkPropertyRestriction.getProperty());
	}

	private final <P extends ElkObject> void write(
			ElkPropertyAxiom<P> elkPropertyAxiom) {
		write(elkPropertyAxiom.getProperty());
	}

	private final <P extends ElkObject, F extends ElkObject> void write(
			ElkPropertyRestrictionQualified<P, F> elkQualifiedPropertyRestriction) {
		write((ElkPropertyRestriction<P>) elkQualifiedPropertyRestriction);
		write(' ');
		write(elkQualifiedPropertyRestriction.getFiller());
	}

	private final <P extends ElkObject> void write(
			ElkCardinalityRestriction<P> elkCardinalityRestriction) {
		write(elkCardinalityRestriction.getCardinality());
		write(' ');
		write((ElkPropertyRestriction<P>) elkCardinalityRestriction);
	}

	private final <P extends ElkObject, F extends ElkObject> void write(
			ElkCardinalityRestrictionQualified<P, F> elkQualifiedCardinalityRestriction) {
		write((ElkCardinalityRestriction<P>) elkQualifiedCardinalityRestriction);
		write(' ');
		write(elkQualifiedCardinalityRestriction.getFiller());
	}

	private final <P extends ElkObject, O extends ElkObject, R extends ElkObject> void write(
			ElkPropertyAssertionAxiom<P, O, R> elkPropertyAssertionAxiom) {
		write((ElkPropertyAxiom<P>) elkPropertyAssertionAxiom);
		write(' ');
		write(elkPropertyAssertionAxiom.getSubject());
		write(' ');
		write(elkPropertyAssertionAxiom.getObject());
	}

	private final <P extends ElkObject, D extends ElkObject> void write(
			ElkPropertyDomainAxiom<P, D> elkPropertyDomainAxiom) {
		write((ElkPropertyAxiom<P>) elkPropertyDomainAxiom);
		write(' ');
		write(elkPropertyDomainAxiom.getDomain());
	}

	private final <P extends ElkObject, R extends ElkObject> void write(
			ElkPropertyRangeAxiom<P, R> elkPropertyRangeAxiom) {
		write((ElkPropertyAxiom<P>) elkPropertyRangeAxiom);
		write(' ');
		write(elkPropertyRangeAxiom.getRange());
	}

	/**
	 * An auxiliary visitor class to print declarations.
	 * 
	 * @author "Yevgeny Kazakov"
	 * 
	 */
	class EntityPrinter implements ElkEntityVisitor<Void> {

		@Override
		public Void visit(ElkAnnotationProperty elkAnnotationProperty) {
			write("AnnotationProperty(");
			write(elkAnnotationProperty);
			write(')');
			return null;
		}

		@Override
		public Void visit(ElkClass elkClass) {
			write("Class(");
			write(elkClass);
			write(')');
			return null;
		}

		@Override
		public Void visit(ElkDataProperty elkDataProperty) {
			write("DataProperty(");
			write(elkDataProperty);
			write(')');
			return null;
		}

		@Override
		public Void visit(ElkDatatype elkDatatype) {
			write("Datatype(");
			write(elkDatatype);
			write(')');
			return null;
		}

		@Override
		public Void visit(ElkNamedIndividual elkNamedIndividual) {
			write("NamedIndividual(");
			write(elkNamedIndividual);
			write(')');
			return null;
		}

		@Override
		public Void visit(ElkObjectProperty elkObjectProperty) {
			write("ObjectProperty(");
			write(elkObjectProperty);
			write(')');
			return null;
		}

	}

	@Override
	public Void visit(ElkSWRLRule rule) {
		// we punt on this
		write("DLSafeRule(  )");
		return null;
	}

}
