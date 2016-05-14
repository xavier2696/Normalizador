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
public class Normalizador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       String[] exprs = {
				"A, B --> C", 
				"A, C --> B",
				"B, C --> A",
				"D --> E",
				"E --> F"
		};
		
		Set<FuncDep> fds = FuncDep.getSet(exprs);
		Set<Attribute> atts = Attribute.getSet("A, B, C, D, E, F");
		
		Set<Set<Attribute>> keys = Algos.keys(atts, fds);

		System.out.println("Llaves Candidatas: ");
		for(Set<Attribute> sa : keys){
			System.out.println(sa);
		}
    }
    
}
