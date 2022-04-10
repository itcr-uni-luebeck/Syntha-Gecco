package org.uzl.syntheagecco.extraction.mapping

abstract class Lookup<K, V> {

    private Map<K, V> map

    Lookup(Map<K, V> map){
        this.map = map
    }

    Set<Map.Entry<K, V>> getView(){
        return map.entrySet()
    }

    V get(K key){
        return map[key]
    }

    boolean containsKey(K key){
        return map.containsKey(key)
    }

    boolean containsValue(V value){
        return map.containsValue(value)
    }

}
