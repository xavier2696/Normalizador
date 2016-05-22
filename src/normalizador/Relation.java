/*
 * Copyright (c) 2015 SUN XIMENG (Nathaniel). All rights reserved.
 */

package normalizador;

import java.util.HashSet;
import java.util.Set;

/**
 * The {@code Relation} class represent a relation consisting a set of attributes and a set of FD's
 * @author SUN XIMENG (Nathaniel)
 *
 */
public final class Relation {
	
	private final Set<Elemento> attrs;
	private final Set<DependenciaFuncional> fds;

	/**
	 * The default constructors
	 * @param attrs a set of attributes
	 * @param fds a set of FD's
	 */
	public Relation(Set<Elemento> attrs, Set<DependenciaFuncional> fds) {
		this.attrs = new HashSet<>(attrs);
		this.fds = new HashSet<>(fds);
	}
	
	/**
	 * Quickly construct a {@code Relation} object with two formatted strings, one for attributes and another for FD's
	 * @param names a string formatted as the following example: "name, application, date, gender"
	 * @param exprs a string formatted as the following example: "{@code a, b --> c; d --> e, f}"
	 */
	public Relation(String names, String exprs){
		this.attrs = Elemento.crearElemento(names);
		this.fds = DependenciaFuncional.crearDependencia(exprs);
	}
	
	/**
	 * Quickly construct a {@code Relation} object with two string arrays
	 * @param names each element will be used as the {@code name} of an {@code Elemento} object
	 * @param exprs each element is formatted as the following example: "{@code a, b --> c, d}"
	 */
	public Relation(String[] names, String[] exprs){
		this.attrs = Elemento.crearElemento(names);
		this.fds = DependenciaFuncional.crearDependencia(exprs);
	}

	/**
	 * Decompose the current relation into a set of relations that satisfy 3NF, 
	 * by using the Lossless Join &amp; Dependency Preservation algorithm
	 * @return a set of decomposed relations
	 */
	public Set<Relation> decomposeTo3NF(){
		Set<Relation> result = new HashSet<>();
		Set<DependenciaFuncional> mb = Algoritmos.minimalBasis(this.fds);
		for(DependenciaFuncional fd : mb){
			Set<Elemento> attrsNow = new HashSet<>(fd.getLeft());
			attrsNow.addAll(fd.getRight());
			Set<DependenciaFuncional> proj = Algoritmos.projection(attrsNow, mb);
			result.add(new Relation(attrsNow, proj));
		}
		Set<Relation> toRemove = new HashSet<>();
		for(Relation a : result){
			for(Relation b : result){
				if(a != b && a.attrs.containsAll(b.attrs)){
					toRemove.add(b);
				}
			}
		}
		result.removeAll(toRemove);
		Set<Set<Elemento>> keys = Algoritmos.llavesCandidatas(this.attrs, mb);
		boolean contains = false;
		for(Relation r : result){
			for(Set<Elemento> k : keys){
				if(r.attrs.containsAll(k)){
					contains = true;
					break;
				}
			}
			if(contains){
				break;
			}
		}
		if(!contains){
			Set<Elemento> key = null;
			for(Set<Elemento> k : keys){
				key = k;
				break;
			}
			Set<DependenciaFuncional> proj = Algoritmos.projection(key, mb);
			result.add(new Relation(key, proj));
		}
		return result;
	}
	
	/**
	 * Decompose the current relation into a set of relations that satisfies BCNF, 
	 * regardless the possible loss of FD's
	 * @return a set of decomposed relations
	 */
	
	/**
	 * A {@code Relation} object will equal to another if and only if: 1) they have exactly the same 
	 * set of attributes; 2) they have exactly the same set of FD's
	 */
	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(!(o instanceof Relation)){
			return false;
		}
		Relation r = (Relation)o;
		return r.attrs.equals(this.attrs) && r.fds.equals(this.fds);
	}
	
	/**
	 * 
	 * @return a set of {@code Elemento} objects that appear in this relations
	 */
	public Set<Elemento> getElementos(){
		return new HashSet<>(this.attrs);
	}
	
	/**
	 * 
	 * @return all FD's that violate the 3NF; an empty set if it's already in 3NF
	 */
	public Set<DependenciaFuncional> getFdsViolating3NF(){
		return Algoritmos.check3NF(this.attrs, this.fds);
	}
	
	/**
	 * 
	 * @return all FD's that violate the BCNF; an empty set if it's already in BCNF
	 */
	
	/**
	 * 
	 * @return a set of {@code DependenciaFuncional} objects that involved in this relation
	 */
	public Set<DependenciaFuncional> getDependenciaFuncionals(){
		return new HashSet<>(this.fds);
	}
	
	/**
	 * Compute all the candidate keys in this relation
	 * @return a set of candidate keys, and each itself is a set of attributes
	 */
	public Set<Set<Elemento>> getKeys(){
		return Algoritmos.llavesCandidatas(this.attrs, this.fds);
	}
	
	/**
	 * Compute all the superkeys (including candidate keys) in this relation
	 * @return a set of superkeys, and each itself is a set of attributes
	 */
	public Set<Set<Elemento>> getSuperkeys(){
		return Algoritmos.llaves(this.attrs, this.fds);
	}
	
	@Override
	public int hashCode(){
		int hash = 17;
		for(Elemento a : this.attrs){
			hash = 31 * hash + a.hashCode();
		}
		for(DependenciaFuncional fd : this.fds){
			hash = 31 * hash + fd.hashCode();
		}
		return hash;
	}
	
	/**
	 * 
	 * @return {@code true} if this relation is already in the third normal form (3NF)
	 */
	public boolean is3NF(){
		return Algoritmos.check3NF(this.attrs, this.fds).isEmpty();
	}
	
	/**
	 *  
	 * @return {@code true} if this relation is already in Boyce-Codd normal form (BCNF)
	 */
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(Elemento.AVERAGE_LENGTH * 50);
		sb.append("Elementos:\n");
		for(Elemento a : this.attrs){
			sb.append(a);
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length() - 1);
		sb.append("\nFunctional Dependencies: \n");
		for(DependenciaFuncional fd : this.fds){
			sb.append(fd);
			sb.append('\n');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

}
