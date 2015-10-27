
package org.eclipse.jwt.transformations.activiti.internal.hacks;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiDiagramEditor extends DiagramEditor {
	private DefaultEditDomain editDomain;
	private TransactionalEditingDomain editingDomain;
	
	/**
	 * 
	 * @param editingDomain
	 */
	public ActivitiDiagramEditor(TransactionalEditingDomain editingDomain){
		this.editingDomain = editingDomain;
		this.editDomain = new DefaultEditDomain(null);
	}
	
	/**
	 * 
	 */
	public DefaultEditDomain getEditDomain(){
		return this.editDomain;
	}
	
	/**
	 * 
	 */
	public TransactionalEditingDomain getEditingDomain(){
		return this.editingDomain;
	}
}
