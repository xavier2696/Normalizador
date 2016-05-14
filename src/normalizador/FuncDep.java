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
public class FuncDep {
    
	public static class Builder{
		private Set<Attribute> left;
		private Set<Attribute> right;
		
		public Builder(){
			this.left = new HashSet<>();
			this.right = new HashSet<>();
		}
		
		
		public FuncDep build(){
			return new FuncDep(this.left, this.right);
		}
		
		
		public Builder left(Attribute... as){
			this.left.addAll(Arrays.asList(as));
			return this;
		}
		
		
		public Builder left(Set<Attribute> as){
			this.left.addAll(as);
			return this;
		}
		
		
		public Builder right(Attribute... as){
			this.right.addAll(Arrays.asList(as));
			return this;
		}
		
		
		public Builder right(Set<Attribute> as){
			this.right.addAll(as);
			return this;
		}
		
	}
	
	public static Set<FuncDep> getSet(String exprs){
		if(exprs.equals("")){
			return new HashSet<>();
		}
		exprs = exprs.replaceAll("\\s+","");
		return getSet(exprs.split(";"));
	}
	
	
	public static Set<FuncDep> getSet(String[] exprs){
		Set<FuncDep> fds = new HashSet<>();
		for(String s : exprs){
			fds.add(FuncDep.of(s));
		}
		return fds;
	}
	
	
	public static FuncDep of(String expr){
		String[] halves = expr.split("-->");
		return of(halves[0], halves[1]);
	}
	
	
	public static FuncDep of(String left, String right){
		left = left.replaceAll("\\s+","");
		right = right.replaceAll("\\s+","");
		String[] lefts = left.split(",");
		String[] rights = right.split(",");
		Builder bd = new Builder();
		for(String s : lefts){
			bd.left(Attribute.of(s));
		}
		for(String s : rights){
			bd.right(Attribute.of(s));
		}
		return bd.build();
	}
	
	protected final Set<Attribute> left;
	
	protected final Set<Attribute> right;
	
	
	public FuncDep(Set<Attribute> left, Set<Attribute> right){
		this.left = new HashSet<>(left);
		this.right = new HashSet<>(right);
	}
	
	
	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(!(o instanceof FuncDep)){
			return false;
		}
		FuncDep fd = (FuncDep)o;
		return this.left.equals(fd.left) && this.right.equals(fd.right);
	}
	
	
	public Set<Attribute> getLeft(){
		return new HashSet<>(this.left);
	}
	
	
	public Set<Attribute> getRight(){
		return new HashSet<>(this.right);
	}
	
	@Override
	public int hashCode(){
		int result = 17;
		for(Attribute at : this.left){
			result = 31 * result + at.hashCode();
		}
		for(Attribute at : this.right){
			result = 31 * result + at.hashCode();
		}
		return result;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder((this.left.size() + this.right.size()) * Attribute.AVERAGE_LENGTH);
		for(Attribute at : this.left){
			sb.append(at.toString());
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(" --> ");
		for(Attribute at : this.right){
			sb.append(at.toString());
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}
}
