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
public class Elemento {
    protected static final int AVERAGE_LENGTH = 10;

    public static Set<Elemento> crearElemento(String names) {
        if (names.equals("")) {
            return new HashSet<>();
        }
        names = names.replaceAll("\\s+", "");
        return crearElemento(names.split(","));
    }

    public static Set<Elemento> crearElemento(String[] names) {
        Set<Elemento> attrs = new HashSet<>();
        for (String s : names) {
            attrs.add(Elemento.newElemento(s));
        }
        return attrs;
    }

    public static Elemento newElemento(String name) {
        return new Elemento(name);
    }

    private final String name;

    public Elemento(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Elemento)) {
            return false;
        }
        Elemento a = (Elemento) o;
        return a.name.equals(this.name);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
