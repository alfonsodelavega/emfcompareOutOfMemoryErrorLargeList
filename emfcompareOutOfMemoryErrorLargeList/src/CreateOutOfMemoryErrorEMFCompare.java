import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import boxes.BoxesPackage;

public class CreateOutOfMemoryErrorEMFCompare {

	public static void main(String[] args) {
		ResourceSet resourceSet = new ResourceSetImpl();
		
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(BoxesPackage.eNS_URI, BoxesPackage.eINSTANCE);
		
		Resource left = resourceSet.getResource(
				URI.createFileURI("model/100000elems-10conflicts_left.model"), true);
		Resource ancestor = resourceSet.getResource(
				URI.createFileURI("model/100000elems-10conflicts_ancestor.model"), true);
		Resource right = resourceSet.getResource(
				URI.createFileURI("model/100000elems-10conflicts_right.model"), true);


		List<Conflict> conflicts = comparisonThatFails(left, right, ancestor);
		
//		List<Conflict> conflicts = comparisonThatCompletes(left, right, ancestor);
		
		for (Conflict c : conflicts) {
			System.out.println(c);
		}
	}

	public static List<Conflict> comparisonThatFails(Resource left, Resource right, Resource ancestor) {

		IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		return comparison.getConflicts();
	}

	public static List<Conflict> comparisonThatCompletes(Resource left, Resource right, Resource ancestor) {
		IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);

		IDiffProcessor diffProcessor = new DiffBuilder();
		IDiffEngine diffEngine = new DefaultDiffEngine(diffProcessor) {
			@Override
			protected FeatureFilter createFeatureFilter() {
				return new FeatureFilter() {
					@Override
					public boolean checkForOrderingChanges(EStructuralFeature feature) {
						return false;
					}
				};
			}
		};

		Comparison comparison = EMFCompare.builder().setDiffEngine(diffEngine).build().compare(scope);

		return comparison.getConflicts();
	}

}
