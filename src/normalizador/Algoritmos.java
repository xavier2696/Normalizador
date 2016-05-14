/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package normalizador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 *
 * @author xavie
 */
public class Algoritmos {
    
    public static Set<Set<Elemento>> llavesCandidatas(Set<Elemento> attrs, Set<DependenciaFuncional> fds){
		Set<Set<Elemento>> superkeys = llaves(attrs, fds);
		Set<Set<Elemento>> toRemove = new HashSet<>();
		for(Set<Elemento> key : superkeys){
			for(Elemento a : key){
				Set<Elemento> remaining = new HashSet<>(key);
				remaining.remove(a);
				if(superkeys.contains(remaining)){
					toRemove.add(key);
					break;
				}
			}
		}
		superkeys.removeAll(toRemove);
		return superkeys;
	}
    
    public static Set<Elemento> dependientes(Set<Elemento> attrs, Set<DependenciaFuncional> fds){
		Set<Elemento> result = new HashSet<>(attrs);
		
		boolean found = true;
		while(found){
			found = false;
			for(DependenciaFuncional fd : fds){
				if(result.containsAll(fd.left) && !result.containsAll(fd.right)){
					result.addAll(fd.right);
					found = true;
				}
			}
		}
		
		return result;
	}
    
    public static <T> Set<Set<T>> setPotencia(Set<T> originalSet) {
	    Set<Set<T>> sets = new HashSet<>();
	    if (originalSet.isEmpty()) {
	    	sets.add(new HashSet<T>());
	    	return sets;
	    }
	    List<T> list = new ArrayList<>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<>(list.subList(1, list.size()));
	    for (Set<T> set : setPotencia(rest)) {
	    	Set<T> newSet = new HashSet<>();
	    	newSet.add(head);
	    	newSet.addAll(set);
	    	sets.add(newSet);
	    	sets.add(set);
	    }
	    //sets.remove(new HashSet<T>());
	    return sets;
	}
    public static <T> Set<Set<T>> setPotenciaSinNulo(Set<T> originalSet){
		Set<Set<T>> result = setPotencia(originalSet);
		result.remove(new HashSet<T>());
		return result;
	}
    public static Set<Set<Elemento>> llaves(Set<Elemento> attrs, Set<DependenciaFuncional> fds){
		Set<Set<Elemento>> keys = new HashSet<>();
		if(attrs.isEmpty()){
			for(DependenciaFuncional fd : fds){
				attrs.addAll(fd.left);
				attrs.addAll(fd.right);
			}
		}
		Set<Set<Elemento>> powerset = setPotenciaSinNulo(attrs);
		for(Set<Elemento> sa : powerset){
			if(dependientes(sa, fds).equals(attrs)){
				keys.add(sa);
			}
		}
		return keys;
	}
}
