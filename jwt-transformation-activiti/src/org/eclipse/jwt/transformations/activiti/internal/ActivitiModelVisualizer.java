package org.eclipse.jwt.transformations.activiti.internal;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.designer.bpmn2.model.Artifact;
import org.activiti.designer.bpmn2.model.BaseElement;
import org.activiti.designer.bpmn2.model.BoundaryEvent;
import org.activiti.designer.bpmn2.model.FlowElement;
import org.activiti.designer.bpmn2.model.Lane;
import org.activiti.designer.bpmn2.model.Pool;
import org.activiti.designer.bpmn2.model.Process;
import org.activiti.designer.bpmn2.model.SequenceFlow;
import org.activiti.designer.bpmn2.model.SubProcess;
import org.activiti.designer.bpmn2.model.Activity;
import org.activiti.designer.diagram.ActivitiBPMNDiagramTypeProvider;
import org.activiti.designer.eclipse.editor.Bpmn2DiagramCreator;
import org.activiti.designer.eclipse.editor.FileService;
import org.activiti.designer.util.editor.Bpmn2MemoryModel;
import org.activiti.designer.util.editor.GraphicInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jwt.transformations.activiti.internal.hacks.ActivitiDiagramEditor;

public class ActivitiModelVisualizer {
	private Diagram diagram;
	private TransactionalEditingDomain editingDomain;
	private IDiagramTypeProvider diagramTypeProvider;
	
	/**
	 * 
	 */
	public ActivitiModelVisualizer() {
		this.diagram = this.createDiagram();
		
		this.diagramTypeProvider = new ActivitiBPMNDiagramTypeProvider();		

		this.editingDomain = this.createEditingDomain(this.diagram);
		
		this.diagramTypeProvider.init(this.diagram, new ActivitiDiagramEditor(this.editingDomain));
	}
		
	/**
	 * 
	 * @return
	 */
	public IFeatureProvider getFeatureProvider(){
		return this.diagramTypeProvider.getFeatureProvider();
	}
	
	/**
	 * 
	 * @return
	 */
	public IDiagramTypeProvider getDiagramTypeProvider() {
		return this.diagramTypeProvider;
	}
	
	/**
	 * 
	 * @return
	 */
	public TransactionalEditingDomain getEditingDomain(){
		return this.editingDomain;
	}
	
	/**
	 * 
	 * @param model
	 */
	public void renderModel(final Bpmn2MemoryModel model) {
		
		BasicCommandStack basicCommandStack = (BasicCommandStack) getEditingDomain().getCommandStack();
		
		basicCommandStack.execute(new RecordingCommand(getEditingDomain()) {
			
				@Override
				protected void doExecute() {
					importDiagram(model);
				}
		});
		
		basicCommandStack.saveIsDone();
  		basicCommandStack.flush();
	}
	
	/**
	 * 
	 * @return
	 */
	private Diagram createDiagram(){				
		return Graphiti.getPeCreateService().createDiagram(
			    "BPMNdiagram", "BPMNDiagram", true);		
	}
	
	/**
	 * 
	 * @return
	 */
	private TransactionalEditingDomain createEditingDomain(Diagram diagram){
		File diagramFile = new File("diagram.bpmnd");
		
		URI uri = URI.createFileURI(diagramFile.getAbsolutePath());
		
		return FileService.createEmfFileForDiagram(uri, diagram);
	}
	
	/**
	 * 
	 */
	private void importDiagram(final Bpmn2MemoryModel model) {
		final Diagram diagram = getDiagramTypeProvider().getDiagram();
		diagram.setActive(true);
		
		getEditingDomain().getCommandStack().execute(new RecordingCommand(getEditingDomain()) {
			@Override
			protected void doExecute() {
				//Bpmn2MemoryModel model = ModelHandler.getModel(EcoreUtil.getURI(diagram));
				
//				if(model.getPools().size() > 0) {
//					for (Pool pool : model.getPools()) {
//						PictogramElement poolElement = addContainerElement(pool, model, diagram);
//						
//						if(poolElement == null) 
//							continue;
//				    
//						Process process = model.getProcess(pool.getId());
//				    
//						for (Lane lane : process.getLanes()) {
//							addContainerElement(lane, model, (ContainerShape) poolElement);
//						}
//					}
//				}
				
				for (Process process : model.getProcesses()) {
						drawFlowElements(process.getFlowElements(), model.getLocationMap(), diagram, process);
						//drawArtifacts(process.getArtifacts(), model.getLocationMap(), diagram, process);
						drawSequenceFlows(process.getFlowElements());
				}
				
				
			}
		});			
	}
	
	
	/**
	 * 
	 * @param element
	 * @param model
	 * @param parent
	 * @return
	 */
//	private PictogramElement addContainerElement(
//			BaseElement element, 
//			Bpmn2MemoryModel model, 
//			ContainerShape parent) {
//		
//		GraphicInfo graphicInfo = model.getLocationMap().get(element.getId());
//	    
//		if(graphicInfo == null) 
//	    	return null;
//	    
//	    final IFeatureProvider featureProvider = this.getFeatureProvider();
//	    
//	    AddContext context = new AddContext(new AreaContext(), element);
//	    IAddFeature addFeature = featureProvider.getAddFeature(context);
//	    context.setNewObject(element);
//	    context.setSize(graphicInfo.width, graphicInfo.height);
//	    context.setTargetContainer(parent);
//	    
//	    int x = graphicInfo.x;
//	    int y = graphicInfo.y;
//	    
//	    if(!(parent instanceof Diagram)) {
//	    	x = x - parent.getGraphicsAlgorithm().getX();
//	    	y = y - parent.getGraphicsAlgorithm().getY();
//	    }
//	    
//	    context.setLocation(x, y);
//	    
//	    PictogramElement pictElement = null;
//	    
//	    if (addFeature.canAdd(context)) {
//	    	pictElement = addFeature.add(context);
//	    	featureProvider.link(pictElement, new Object[] { element });
//	    }
//	    
//	    return pictElement;
//	}
	
	private void drawFlowElements(
			List<FlowElement> elementList, 
			Map<String, GraphicInfo> locationMap, 
	        ContainerShape parentShape, Process process) {
		
		final IFeatureProvider featureProvider = getDiagramTypeProvider().getFeatureProvider();
		
		List<FlowElement> noDIList = new ArrayList<FlowElement>();
		
		for (FlowElement flowElement : elementList) {
			
			AddContext context = new AddContext(new AreaContext(), flowElement);
			IAddFeature addFeature = featureProvider.getAddFeature(context);
			
			if (addFeature == null) {
				System.out.println("Element not supported: " + flowElement);
				return;
			}
			
			GraphicInfo graphicInfo = locationMap.get(flowElement.getId());
			if(graphicInfo == null) {
			
				noDIList.add(flowElement);
				
			} else {
				
				context.setNewObject(flowElement);
				context.setSize(graphicInfo.width, graphicInfo.height);
				
				ContainerShape parentContainer = null;
				if(parentShape instanceof Diagram) {
				  parentContainer = getParentContainer(flowElement.getId(), process, (Diagram) parentShape);
				} else {
				  parentContainer = parentShape;
				}
				
				context.setTargetContainer(parentContainer);
				if(parentContainer instanceof Diagram == false) {
					Point location = getLocation(parentContainer);
					context.setLocation(graphicInfo.x - location.x, graphicInfo.y - location.y);
				} else {
					context.setLocation(graphicInfo.x, graphicInfo.y);
				}
												
				if (addFeature.canAdd(context)) {
					PictogramElement newContainer = addFeature.add(context);
					featureProvider.link(newContainer, new Object[] { flowElement });
					
					if (flowElement instanceof SubProcess) {
						drawFlowElements(((SubProcess) flowElement).getFlowElements(), locationMap, 
						        (ContainerShape) newContainer, process);
					}
					
					if (flowElement instanceof Activity) {
						Activity activity = (Activity) flowElement;
						for (BoundaryEvent boundaryEvent : activity.getBoundaryEvents()) {
							AddContext boundaryContext = new AddContext(new AreaContext(), boundaryEvent);
							IAddFeature boundaryAddFeature = featureProvider.getAddFeature(boundaryContext);
							
							if (boundaryAddFeature == null) {
								System.out.println("Element not supported: " + boundaryEvent);
								return;
							}
							
							GraphicInfo boundaryGraphicInfo = locationMap.get(boundaryEvent.getId());
							if(boundaryGraphicInfo == null) {
								
								noDIList.add(boundaryEvent);
							
							} else {

								context.setNewObject(boundaryEvent);
								context.setSize(boundaryGraphicInfo.width, boundaryGraphicInfo.height);
								
								if(boundaryEvent.getAttachedToRef() != null) {
									ContainerShape container = (ContainerShape) featureProvider.getPictogramElementForBusinessObject(
											boundaryEvent.getAttachedToRef());
									
									if(container != null) {
									
										boundaryContext.setTargetContainer(container);
										Point location = getLocation(container);
										boundaryContext.setLocation(boundaryGraphicInfo.x - location.x, boundaryGraphicInfo.y - location.y);
					
										if (boundaryAddFeature.canAdd(boundaryContext)) {
											PictogramElement newBoundaryContainer = boundaryAddFeature.add(boundaryContext);
											featureProvider.link(newBoundaryContainer, new Object[] { boundaryEvent });
										}
									}
								}
							}
						}
					}
				}
			}
        }
		
//		for (FlowElement flowElement : noDIList) {
//			if(flowElement instanceof BoundaryEvent) {
//				((BoundaryEvent) flowElement).getAttachedToRef().getBoundaryEvents().remove(flowElement);
//			} else {
//				elementList.remove(flowElement);
//			}
//		}
	}
	
	private ContainerShape getParentContainer(String flowElementId, Process process, Diagram diagram) {
		  Lane foundLane = null;
		  for (Lane lane : process.getLanes()) {
	      if (lane.getFlowReferences().contains(flowElementId)) {
	        foundLane = lane;
	        break;
	      }
	    }
		  
		  if(foundLane != null) {
		    final IFeatureProvider featureProvider = getDiagramTypeProvider().getFeatureProvider();
		    return (ContainerShape) featureProvider.getPictogramElementForBusinessObject(foundLane);
		  } else {
		    return diagram;
		  }
		}
		
		private Point getLocation(ContainerShape containerShape) {
			if(containerShape instanceof Diagram == true) {
				return new Point(containerShape.getGraphicsAlgorithm().getX(), containerShape.getGraphicsAlgorithm().getY());
			}
			
			Point location = getLocation(containerShape.getContainer());
			return new Point(location.x + containerShape.getGraphicsAlgorithm().getX(), location.y + containerShape.getGraphicsAlgorithm().getY());
		}
		
//		private void drawArtifacts(final List<Artifact> artifacts, final Map<String, GraphicInfo> locationMap,
//		        final ContainerShape parent, final Process process) {
//
//		  final IFeatureProvider featureProvider = getDiagramTypeProvider().getFeatureProvider();
//
//		  final List<Artifact> artifactsWithoutDI = new ArrayList<Artifact>();
//		  for (final Artifact artifact : artifacts) {
//		    final AddContext context = new AddContext(new AreaContext(), artifact);
//		    final IAddFeature addFeature = featureProvider.getAddFeature(context);
//	 
//		    if (addFeature == null) {
//		      System.out.println("Element not supported: " + artifact);
//		      return;
//		    }
//	 
//		    final GraphicInfo gi = locationMap.get(artifact.getId());
//		    if (gi == null) {
//		      artifactsWithoutDI.add(artifact);
//		    } else {
//		      context.setNewObject(artifact);
//		      context.setSize(gi.width, gi.height);
//	   
//		      ContainerShape parentContainer = null;
//		      if (parent instanceof Diagram) {
//		        parentContainer = getParentContainer(artifact.getId(), process, (Diagram) parent);
//		      } else {
//		        parentContainer = parent;
//		      }
//	   
//		      context.setTargetContainer(parentContainer);
//		      if (parentContainer instanceof Diagram) {
//		        context.setLocation(gi.x, gi.y);
//		      } else {
//		        final Point location = getLocation(parentContainer);
//	     
//		        context.setLocation(gi.x - location.x, gi.y - location.y);
//		      }
//	   
//		      if (addFeature.canAdd(context)) {
//		        final PictogramElement newContainer = addFeature.add(context);
//		        featureProvider.link(newContainer, new Object[] { artifact });
//		      }
//		    }
//		  }
//
//		  for (final Artifact artifact : artifactsWithoutDI) {
//		    artifacts.remove(artifact);
//		  }
//		}	
		
		private void drawSequenceFlows(List<FlowElement> flowElements) {
     
		  for(FlowElement flowElement : flowElements) {
			  if(flowElement instanceof SequenceFlow) {
				  SequenceFlow sequenceFlow = (SequenceFlow)flowElement;
				  
				  Anchor sourceAnchor = null;
				  Anchor targetAnchor = null;
				  ContainerShape sourceShape = (ContainerShape) getDiagramTypeProvider()
						  .getFeatureProvider().getPictogramElementForBusinessObject(sequenceFlow.getSourceRef());
      
				  if(sourceShape == null) continue;
      
				  EList<Anchor> anchorList = sourceShape.getAnchors();
				  for (Anchor anchor : anchorList) {
					  if(anchor instanceof ChopboxAnchor) {
						  sourceAnchor = anchor;
						  break;
					  }
				  }
      
				  ContainerShape targetShape = (ContainerShape) getDiagramTypeProvider()
						  .getFeatureProvider().getPictogramElementForBusinessObject(sequenceFlow.getTargetRef());
      	
				  if(targetShape == null) continue;
      
				  anchorList = targetShape.getAnchors();
				  for (Anchor anchor : anchorList) {
					  if(anchor instanceof ChopboxAnchor) {
						  targetAnchor = anchor;
						  break;
					  }
				  }
      
				  AddConnectionContext addContext = new AddConnectionContext(sourceAnchor, targetAnchor);
      
//				  List<GraphicInfo> bendpointList = new ArrayList<GraphicInfo>();
//				  if (parser.flowLocationMap.containsKey(sequenceFlowModel.id)) {
//					  List<GraphicInfo> pointList = parser.flowLocationMap.get(sequenceFlowModel.id);
//					  if(pointList.size() > 2) {
//						  for(int i = 1; i < pointList.size() - 1; i++) {
//							  bendpointList.add(pointList.get(i));
//						  }
//					  }
//				  }
				  
				  //addContext.putProperty("org.activiti.designer.bendpoints", bendpointList);
				  //addContext.putProperty("org.activiti.designer.connectionlabel", parser.labelLocationMap.get(sequenceFlowModel.id));
      
				  addContext.setNewObject(sequenceFlow);
				  getDiagramTypeProvider().getFeatureProvider().addIfPossible(addContext);
		  }
	  }
     
  }
		
}
