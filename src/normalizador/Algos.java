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
public class Algos {
    
    public static Set<Set<Attribute>> keys(Set<Attribute> attrs, Set<FuncDep> fds){
		Set<Set<Attribute>> superkeys = superKeys(attrs, fds);
		Set<Set<Attribute>> toRemove = new HashSet<>();
		for(Set<Attribute> key : superkeys){
			for(Attribute a : key){
				Set<Attribute> remaining = new HashSet<>(key);
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
    
    public static Set<Attribute> closure(Set<Attribute> attrs, Set<FuncDep> fds){
		Set<Attribute> result = new HashSet<>(attrs);
		
		boolean found = true;
		while(found){
			found = false;
			for(FuncDep fd : fds){
				if(result.containsAll(fd.left) && !result.containsAll(fd.right)){
					result.addAll(fd.right);
					found = true;
				}
			}
		}
		
		return result;
	}
    
    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
	    Set<Set<T>> sets = new HashSet<>();
	    if (originalSet.isEmpty()) {
	    	sets.add(new HashSet<T>());
	    	return sets;
	    }
	    List<T> list = new ArrayList<>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<>(list.subList(1, list.size()));
	    for (Set<T> set : powerSet(rest)) {
	    	Set<T> newSet = new HashSet<>();
	    	newSet.add(head);
	    	newSet.addAll(set);
	    	sets.add(newSet);
	    	sets.add(set);
	    }
	    //sets.remove(new HashSet<T>());
	    return sets;
	}
    public static <T> Set<Set<T>> reducedPowerSet(Set<T> originalSet){
		Set<Set<T>> result = powerSet(originalSet);
		result.remove(new HashSet<T>());
		return result;
	}
    public static Set<Set<Attribute>> superKeys(Set<Attribute> attrs, Set<FuncDep> fds){
		Set<Set<Attribute>> keys = new HashSet<>();
		if(attrs.isEmpty()){
			for(FuncDep fd : fds){
				attrs.addAll(fd.left);
				attrs.addAll(fd.right);
			}
		}
		Set<Set<Attribute>> powerset = reducedPowerSet(attrs);
		for(Set<Attribute> sa : powerset){
			if(closure(sa, fds).equals(attrs)){
				keys.add(sa);
			}
		}
		return keys;
	}
}
