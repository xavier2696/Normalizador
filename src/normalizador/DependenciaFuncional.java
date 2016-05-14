/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package normalizador;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author xavie
 */
public class DependenciaFuncional {
    
	public static class Constructor{
		private Set<Elemento> left;
		private Set<Elemento> right;
		
		public Constructor(){
			this.left = new HashSet<>();
			this.right = new HashSet<>();
		}
		
		
		public DependenciaFuncional construir(){
			return new DependenciaFuncional(this.left, this.right);
		}
		
		
		public Constructor izquierda(Elemento as){
			this.left.addAll(Arrays.asList(as));
			return this;
		}
		
		
		public Constructor izquierda(Set<Elemento> as){
			this.left.addAll(as);
			return this;
		}
		
		
		public Constructor derecha(Elemento as){
			this.right.addAll(Arrays.asList(as));
			return this;
		}
		
		
		public Constructor derecha(Set<Elemento> as){
			this.right.addAll(as);
			return this;
		}
		
	}
	
	public static Set<DependenciaFuncional> crearDependencia(String exprs){
		if(exprs.equals("")){
			return new HashSet<>();
		}
		exprs = exprs.replaceAll("\\s+","");
		return crearDependencia(exprs.split(";"));
	}
	
	
	public static Set<DependenciaFuncional> crearDependencia(String[] exprs){
		Set<DependenciaFuncional> fds = new HashSet<>();
		for(String s : exprs){
			fds.add(DependenciaFuncional.newDependencia(s));
		}
		return fds;
	}
	
	
	public static DependenciaFuncional newDependencia(String expr){
		String[] halves = expr.split("-->");
		return newDependencia(halves[0], halves[1]);
	}
	
	
	public static DependenciaFuncional newDependencia(String left, String right){
		left = left.replaceAll("\\s+","");
		right = right.replaceAll("\\s+","");
		String[] lefts = left.split(",");
		String[] rights = right.split(",");
		Constructor bd = new Constructor();
		for(String s : lefts){
			bd.izquierda(new Elemento(s));
		}
		for(String s : rights){
			bd.derecha(Elemento.newElemento(s));
		}
		return bd.construir();
	}
	
	protected final Set<Elemento> left;
	
	protected final Set<Elemento> right;
	
	
	public DependenciaFuncional(Set<Elemento> left, Set<Elemento> right){
		this.left = new HashSet<>(left);
		this.right = new HashSet<>(right);
	}
	
	
	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(!(o instanceof DependenciaFuncional)){
			return false;
		}
		DependenciaFuncional fd = (DependenciaFuncional)o;
		return this.left.equals(fd.left) && this.right.equals(fd.right);
	}
	
	
	public Set<Elemento> getLeft(){
		return new HashSet<>(this.left);
	}
	
	
	public Set<Elemento> getRight(){
		return new HashSet<>(this.right);
	}
	
	
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder((this.left.size() + this.right.size()) * 10);
		for(Elemento at : this.left){
			sb.append(at.toString());
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(" --> ");
		for(Elemento at : this.right){
			sb.append(at.toString());
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}
}
