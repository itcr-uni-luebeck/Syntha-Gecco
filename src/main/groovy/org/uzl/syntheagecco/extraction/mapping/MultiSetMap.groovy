package org.uzl.syntheagecco.extraction.mapping

class MultiSetMap<K, V> extends MultiMap<K, V, Set<V>> {

    MultiSetMap(){
        super(HashSet.class)
    }

}
