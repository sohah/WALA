/*******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.wala.emf.wrappers;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;

import com.ibm.wala.ecore.common.CommonFactory;
import com.ibm.wala.ecore.common.ECollection;
import com.ibm.wala.ecore.common.EPair;
import com.ibm.wala.ecore.common.ERelation;
import com.ibm.wala.ecore.graph.EGraph;
import com.ibm.wala.ecore.graph.GraphFactory;
import com.ibm.wala.util.graph.NumberedGraph;
import com.ibm.wala.util.graph.impl.SlowSparseNumberedGraph;
import com.ibm.wala.util.intset.IntSet;

/**
 * 
 * An in-memory representation of a graph of EObjects. This class is often more
 * convenient for client-side programming than the "raw" EMF-generated graph
 * implementation.
 * 
 * @author sfink
 */
public class EObjectGraphImpl implements EObjectGraph {

  final private NumberedGraph<EObject> delegate = SlowSparseNumberedGraph.make();

  public void addEdge(EObject src, EObject dst) throws IllegalArgumentException {
    delegate.addEdge(src, dst);
  }

  public void removeEdge(EObject src, EObject dst) throws IllegalArgumentException {
    delegate.removeEdge(src, dst);
  }

  public void addNode(EObject n) {
    delegate.addNode(n);
  }

  public boolean containsNode(EObject N) {
    return delegate.containsNode(N);
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj;
  }

  public int getNumberOfNodes() {
    return delegate.getNumberOfNodes();
  }

  public int getPredNodeCount(EObject N) throws IllegalArgumentException {
    return delegate.getPredNodeCount(N);
  }

  public Iterator<? extends EObject> getPredNodes(EObject N) throws IllegalArgumentException {
    return delegate.getPredNodes(N);
  }

  public int getSuccNodeCount(EObject N) throws IllegalArgumentException {
    return delegate.getSuccNodeCount(N);
  }

  public Iterator<? extends EObject> getSuccNodes(EObject N) throws IllegalArgumentException {
    return delegate.getSuccNodes(N);
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  public Iterator<EObject> iterator() {
    return delegate.iterator();
  }

  public void removeAllIncidentEdges(EObject node) throws IllegalArgumentException {
    delegate.removeAllIncidentEdges(node);
  }

  public void removeNode(EObject n) {
    delegate.removeNode(n);
  }

  public void removeNodeAndEdges(EObject N) throws IllegalArgumentException {
    delegate.removeNodeAndEdges(N);
  }

  @Override
  public String toString() {
    return delegate.toString();
  }

  public void removeIncomingEdges(EObject node) throws IllegalArgumentException {
    delegate.removeIncomingEdges(node);
  }

  public void removeOutgoingEdges(EObject node) throws IllegalArgumentException {
    delegate.removeOutgoingEdges(node);
  }

  public int getMaxNumber() {
    return delegate.getMaxNumber();
  }

  public EObject getNode(int number) {
    return delegate.getNode(number);
  }

  public int getNumber(EObject N) {
    return delegate.getNumber(N);
  }

  public Iterator<EObject> iterateNodes(IntSet s) {
    return delegate.iterateNodes(s);
  }

  /**
   * @param g
   *          an EMF implementation of a graph
   * @return an EObjectGraph with the same nodes and edges of g
   * @throws IllegalArgumentException
   *           if g is null
   */
  @SuppressWarnings("unchecked")
  public static EObjectGraph fromEMF(EGraph g) {
    if (g == null) {
      throw new IllegalArgumentException("g is null");
    }
    EObjectGraphImpl result = new EObjectGraphImpl();
    for (Iterator<EObject> it = g.getNodes().getContents().iterator(); it.hasNext();) {
      result.addNode(it.next());
    }
    for (Iterator it = g.getEdges().getContents().iterator(); it.hasNext();) {
      EPair e = (EPair) it.next();
      result.addEdge(e.getX(), e.getY());
    }
    return result;
  }

  /**
   * @return an EGraph representing the contents of this EObjectGraph
   */
  @SuppressWarnings("unchecked")
  public EObject export() {
    EGraph result = GraphFactory.eINSTANCE.createEGraph();
    ECollection nodes = CommonFactory.eINSTANCE.createEContainer();
    for (Iterator it = iterator(); it.hasNext();) {
      nodes.getContents().add(it.next());
    }
    ERelation edges = CommonFactory.eINSTANCE.createERelation();
    for (Iterator it = iterator(); it.hasNext();) {
      EObject x = (EObject) it.next();
      for (Iterator it2 = getSuccNodes(x); it2.hasNext();) {
        EObject y = (EObject) it2.next();
        EPair p = CommonFactory.eINSTANCE.createEPair();
        p.setX(x);
        p.setY(y);
        edges.getContents().add(p);
      }
    }
    result.setNodes(nodes);
    result.setEdges(edges);
    return result;
  }

  public boolean hasEdge(EObject src, EObject dst) {
    return delegate.hasEdge(src, dst);
  }

  public IntSet getSuccNodeNumbers(EObject node) throws IllegalArgumentException {
    return delegate.getSuccNodeNumbers(node);
  }

  public IntSet getPredNodeNumbers(EObject node) throws IllegalArgumentException {
    return delegate.getPredNodeNumbers(node);
  }

}
