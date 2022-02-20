package syntheagecco.extraction.mapping

import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.NameDesProblemsDerDiagnoseDefiningCode

class MultiListMap<K, V> implements Map<K, List<V>>{

    private final HashMap<K, ArrayList<V>> map

    MultiListMap(){
        map = new HashMap<>()
    }

    List<Entry<String, List<NameDesProblemsDerDiagnoseDefiningCode>>> getAll(){
        return map.entrySet().toList() as List<Entry<String, List<NameDesProblemsDerDiagnoseDefiningCode>>>
    }

    int size(){
        return map.entrySet().stream()
                .mapToInt({ entry -> entry.value.size() })
                .sum()
    }

    @Override
    boolean isEmpty() {
        return map.values().isEmpty()
    }

    @Override
    boolean containsKey(Object key) {
        return map.keySet().contains(key)
    }

    @Override
    boolean containsValue(Object value) {
        return map.values().contains(value)
    }

    @Override
    List<V> get(Object key) {
        return map[key as K] ?: new ArrayList<V>()
    }

    @Override
    List<V> put(K key, List<V> value){
        def list = map[key]
        if(list){
            list.addAll(value)
        }
        else{
            map[key] = new ArrayList<V>().tap {addAll(value)}
        }
        return null
    }

    List<V> putOne(K key, V value) {
        def list = map[key]
        if(list != null){
            list.add(value)
        }
        else{
            map[key as K] = new ArrayList<V>().tap{add(value)}
        }
        return null
    }

    @Override
    List<V> remove(Object key) {
        return map.remove(key)
    }

    @Override
    void putAll(Map<? extends K, ? extends List<V>> m) {
        map.putAll(m as Map<? extends K, ? extends ArrayList<V>>)
    }

    @Override
    void clear() {
        map.clear()
    }

    @Override
    Set<K> keySet() {
        return map.keySet()
    }

    @Override
    Collection<List<V>> values() {
        return map.values()
    }

    @Override
    Set<Entry<K, List<V>>> entrySet() {
        return map.entrySet()
    }
}
