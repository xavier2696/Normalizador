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

    public static Set<Set<Elemento>> llavesCandidatas(Set<Elemento> attrs, Set<DependenciaFuncional> fds) {
        Set<Set<Elemento>> superkeys = llaves(attrs, fds);
        Set<Set<Elemento>> toRemove = new HashSet<>();
        for (Set<Elemento> key : superkeys) {
            for (Elemento a : key) {
                Set<Elemento> remaining = new HashSet<>(key);
                remaining.remove(a);
                if (superkeys.contains(remaining)) {
                    toRemove.add(key);
                    break;
                }
            }
        }
        superkeys.removeAll(toRemove);
        return superkeys;
    }

    public static Set<Elemento> dependientes(Set<Elemento> attrs, Set<DependenciaFuncional> fds) {
        Set<Elemento> result = new HashSet<>(attrs);

        boolean found = true;
        while (found) {
            found = false;
            for (DependenciaFuncional fd : fds) {
                if (result.containsAll(fd.left) && !result.containsAll(fd.right)) {
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

    public static Set<DependenciaFuncional> minimalBasis(Set<DependenciaFuncional> fds) {

        Set<DependenciaFuncional> result = new HashSet<>(fds);

        //Step 1: split right sides
        splitRight(result);

        //Step 2: remove trivial FDs
        removeTrivial(result);

        //Step 3: remove unnecessary FD's and left side attributes
        int count = 1;
        while (count > 0) {
            count = removeUnnecessaryLeftSide(result) + removeUnnecessaryEntireFD(result);
        }

        return result;
    }

    public static void splitRight(Set<DependenciaFuncional> fds) {
        Set<DependenciaFuncional> toRemove = new HashSet<>();
        Set<DependenciaFuncional> toAdd = new HashSet<>();
        for (DependenciaFuncional fd : fds) {
            //Set<Elemento> right = fd.getRight();
            if (fd.right.size() > 1) {
                //Set<Elemento> left = fd.getLeft();
                for (Elemento a : fd.right) {
                    toAdd.add(new DependenciaFuncional.Constructor().izquierda(fd.left).derecha(a).construir());
                }
                toRemove.add(fd);
            }
        }
        fds.addAll(toAdd);
        fds.removeAll(toRemove);
    }

    public static void removeTrivial(Set<DependenciaFuncional> fds) {
        Set<DependenciaFuncional> toRemove = new HashSet<>();
        Set<DependenciaFuncional> toAdd = new HashSet<>();
        for (DependenciaFuncional fd : fds) {
            if (fd.left.containsAll(fd.right)) {
                toRemove.add(fd);
            } else {
                Set<Elemento> toRemoveFromRight = new HashSet<>();
                for (Elemento a : fd.right) {
                    if (fd.left.contains(a)) {
                        toRemoveFromRight.add(a);
                    }
                }
                if (!toRemoveFromRight.isEmpty()) {
                    Set<Elemento> right = fd.getRight();
                    right.removeAll(toRemoveFromRight);
                    toRemove.add(fd);
                    toAdd.add(new DependenciaFuncional.Constructor().izquierda(fd.left).derecha(right).construir());
                }
            }
        }
        fds.addAll(toAdd);
        fds.removeAll(toRemove);
    }

    public static int removeUnnecessaryEntireFD(Set<DependenciaFuncional> fds) {
        int count = 0;
        while (true) {
            DependenciaFuncional toRemove = null;
            boolean found = false;
            for (DependenciaFuncional fd : fds) {
                Set<DependenciaFuncional> remaining = new HashSet<>(fds);
                remaining.remove(fd);
                if (equivalent(remaining, fds)) {
                    ++count;
                    found = true;
                    toRemove = fd;
                    break;
                }
            }
            if (!found) {
                break;
            } else {
                fds.remove(toRemove);
            }
        }

        return count;
    }

    public static int removeUnnecessaryLeftSide(Set<DependenciaFuncional> fds) {
        int count = 0;
        while (true) {
            boolean found = false;
            DependenciaFuncional toRemove = null;
            DependenciaFuncional toAdd = null;
            int loop = 0;
            for (DependenciaFuncional fd : fds) {
                Set<Elemento> left = fd.getLeft();
                Set<Elemento> right = fd.getRight();
                if (left.size() > 1) {
                    for (Elemento a : left) {
                        Set<Elemento> remaining = new HashSet<>(left);
                        remaining.remove(a);
                        Set<DependenciaFuncional> alternative = new HashSet<>(fds);
                        alternative.remove(fd);
                        toAdd = new DependenciaFuncional.Constructor().izquierda(remaining).derecha(right).construir();
                        alternative.add(toAdd);
                        if (equivalent(alternative, fds)) {
                            found = true;
                            toRemove = fd;
                            ++count;
                            break;
                        }
                    }
                }

                if (found) {
                    break;
                }
                ++loop;
            }
            if (found) {
                fds.remove(toRemove);
                fds.add(toAdd);
            }
            if (loop == fds.size()) {
                break;
            }
        }
        return count;
    }

    public static <T> Set<Set<T>> setPotenciaSinNulo(Set<T> originalSet) {
        Set<Set<T>> result = setPotencia(originalSet);
        result.remove(new HashSet<T>());
        return result;
    }

    public static Set<Set<Elemento>> llaves(Set<Elemento> attrs, Set<DependenciaFuncional> fds) {
        Set<Set<Elemento>> keys = new HashSet<>();
        if (attrs.isEmpty()) {
            for (DependenciaFuncional fd : fds) {
                attrs.addAll(fd.left);
                attrs.addAll(fd.right);
            }
        }
        Set<Set<Elemento>> powerset = setPotenciaSinNulo(attrs);
        for (Set<Elemento> sa : powerset) {
            if (dependientes(sa, fds).equals(attrs)) {
                keys.add(sa);
            }
        }
        return keys;
    }

    public static boolean equivalent(Set<DependenciaFuncional> a, Set<DependenciaFuncional> b) {
        Set<Elemento> names = new HashSet<>();
        for (DependenciaFuncional fd : a) {
            names.addAll(fd.getLeft());
            names.addAll(fd.getRight());
        }
        for (DependenciaFuncional fd : b) {
            names.addAll(fd.getLeft());
            names.addAll(fd.getRight());
        }

        Set<Set<Elemento>> powerset = setPotenciaSinNulo(names);
        for (Set<Elemento> sa : powerset) {
            Set<Elemento> closureInA = closure(sa, a);
            Set<Elemento> closureInB = closure(sa, b);
            if (!closureInA.equals(closureInB)) {
                return false;
            }
        }
        return true;
    }

    public static Set<Elemento> closure(Set<Elemento> attrs, Set<DependenciaFuncional> fds) {
        Set<Elemento> result = new HashSet<>(attrs);

        boolean found = true;
        while (found) {
            found = false;
            for (DependenciaFuncional fd : fds) {
                if (result.containsAll(fd.left) && !result.containsAll(fd.right)) {
                    result.addAll(fd.right);
                    found = true;
                }
            }
        }

        return result;
    }

    public static Set<DependenciaFuncional> check3NF(Set<Elemento> attrs, Set<DependenciaFuncional> fds) {
        Set<Set<Elemento>> keys = llavesCandidatas(attrs, fds);
        Set<Elemento> primes = new HashSet<>();
        for (Set<Elemento> k : keys) {
            primes.addAll(k);
        }

        Set<DependenciaFuncional> violating = new HashSet<>();
        for (DependenciaFuncional fd : fds) {
            if (!primes.containsAll(fd.getRight())) {
                boolean contains = false;
                for (Set<Elemento> k : keys) {
                    if (fd.getLeft().containsAll(k)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    violating.add(fd);
                }
            }

        }
        return violating;
    }

    public static Set<DependenciaFuncional> projection(Set<Elemento> attrs, Set<DependenciaFuncional> fds) {
        Set<Elemento> appeared = new HashSet<>();
        for (DependenciaFuncional fd : fds) {
            appeared.addAll(fd.getLeft());
            appeared.addAll(fd.getRight());
        }
        if (attrs.containsAll(appeared)) {
            return new HashSet<>(fds);
        }
        Set<Elemento> notin = new HashSet<>(appeared);
        notin.removeAll(attrs);
        Set<Set<Elemento>> powerset = setPotenciaSinNulo(attrs);
        Set<DependenciaFuncional> result = new HashSet<>();
        for (Set<Elemento> sa : powerset) {
            Set<Elemento> closure = closure(sa, fds);
            closure.removeAll(notin);
            //closure.removeAll(sa);
            result.add(new DependenciaFuncional.Constructor().izquierda(sa).derecha(closure).construir());
        }
        //return result;
        return minimalBasis(result);
    }

}
