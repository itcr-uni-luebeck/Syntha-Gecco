package syntheagecco.extraction.mapping

abstract class MultiMap<K, V, C extends Collection<V>> implements Map<K, C>{

    private final HashMap<K, C> map
    private final Class<C> collectionClass

    MultiMap(Class<C> clazz){
        map = new HashMap<>()
        collectionClass = clazz
    }

    List<Entry<String, C>> getAll(){
        return map.entrySet().toList() as List<Entry<String, C>>
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
    C get(Object key) {
        return map[key as K]
    }

    @Override
    C put(K key, C value){
        def coll = map[key]
        if(coll){
            coll.addAll(value)
        }
        else{
            map[key] = collectionClass.getDeclaredConstructor().newInstance().tap {addAll(value)} as C
        }
        return null
    }

    C put(K key, V value) {
        def coll = map[key]
        if(coll){
            coll.add(value)
        }
        else{
            map[key] = collectionClass.getDeclaredConstructor().newInstance().tap{add(value)} as C
        }
        return null
    }

    @Override
    C remove(Object key) {
        return map.remove(key)
    }

    boolean remove(K key, V value) {
        return map[key].remove(value)
    }

    @Override
    void putAll(Map<? extends K, ? extends C> m) {
        map.putAll(m as Map<? extends K, ? extends C>)
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
    Collection<C> values() {
        return map.values()
    }

    @Override
    Set<Entry<K, C>> entrySet() {
        return map.entrySet()
    }

}
