/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package normalizador;

import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author xavie
 */
public class Attribute {
    protected static final int AVERAGE_LENGTH = 10;
	
	
	public static Set<Attribute> getSet(String names){
		if(names.equals("")){
			return new HashSet<>();
		}
		names = names.replaceAll("\\s+","");
		return getSet(names.split(","));
	}
	
	
	public static Set<Attribute> getSet(String[] names){
		Set<Attribute> attrs = new HashSet<>();
		for(String s : names){
			attrs.add(Attribute.of(s));
		}
		return attrs;
	}
	
	
	public static Attribute of(String name){
		return new Attribute(name);
	}
	
	private final String name;
	
	
	public Attribute(String name){
		this.name = name;
	}
	
	
	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(!(o instanceof Attribute)){
			return false;
		}
		Attribute a = (Attribute)o;
		return a.name.equals(this.name);
	}
	
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public int hashCode(){
		return this.name.hashCode();
	}
	
	@Override
	public String toString(){
		return this.name;
	}
}
