import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
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
				

		IComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		comparison.getConflicts();

	}

}
