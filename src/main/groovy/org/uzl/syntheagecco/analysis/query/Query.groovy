package org.uzl.syntheagecco.analysis.query


import java.util.function.Supplier
import java.util.stream.Stream

class Query<T, U extends Query> {

    private final Supplier<Stream<T>> stream
    private final Class<T> classT
    private final Class<U> classU

    Query(Supplier<Stream<T>> supplier, Class<T> classT, Class<U> classU) {
        this.stream = supplier
        this.classT = classT
        this.classU = classU
    }

    Query(Supplier<Stream<T>> supplier){
        this.stream = supplier
        this.classT = Object.class as Class<T>
        this.classU = Query.class as Class<U>
    }

    Collection<T> execute(){
        return {this.stream.get().collect()}.call()
    }

    U filter(Closure filter){
        return classU.getDeclaredConstructor(Supplier.class, Class.class, Class.class)
                .newInstance((Supplier<Stream<T>>) {this.stream.get().filter(filter)}, classT, classU)
    }

    public <V extends T> U filter(Class<V> classV){
        return classU.getDeclaredConstructor(Supplier.class, Class.class, Class.class)
                .newInstance((Supplier<Stream<V>>) {this.stream.get().filter({classV.isInstance(it)})}, classV, classU)
    }

    /**
     * Note: Automatically applies the distinct method on the resulting stream!
     * @return
     */
    U union(Query<T, U>... queries){
        def stream = Stream.concat(this.stream.get(), queries.collect({query -> query.stream.get()}).stream()
                .reduce(Stream.&concat)
                .get())
                .distinct()
        return classU.getDeclaredConstructor(Supplier.class, Class.class, Class.class)
                .newInstance((Supplier<Stream<T>>) {stream}, classT, classU)
    }

    /**
     * 'Converts' the Query instance on which the method is called to an instance of the class represented by the Class
     * instance parameter. No cast takes place here.
     * IMPORTANT: Assumes the presence of a constructor in all subclasses with the signature:
     * Constructor(Supplier<Stream<V>> supplier){...}
     * @param classV represents the class of which an instance will be created
     * @return new instance of the class described by the parameter
     */
    public <V extends T, W extends U> W to(Class<V> typeClass, Class<W> queryClass){
        return queryClass.getDeclaredConstructor(Supplier.class, Class.class, Class.class)
                .newInstance((Supplier<Stream<V>>) {stream.get().filter({typeClass.isInstance(it)})}, typeClass, queryClass)
    }

    protected Supplier<Stream<T>> getStream() {
        return stream
    }

    protected Class<T> getClassT() {
        return classT
    }

    protected Class<U> getClassU() {
        return classU
    }

    /*
    * Necessary since a Class object of a generic type can not be created in a static context without parsing it to
    * the constructor first, which means that the attribute might not contain a value at some point. The solution
    * requires the subclasses to implement a method that gets this base type. This means that some boiler plate
    * code has to be written since every subclass is required to return its own proper value.
    *
    * IMPORTANT: The value should be the Class object whose instances are contained in the stream attribute. A subclass
    * of Query<T, U> that specifies T as <T extends SomeClass> should return SomeClass.class.
    *
    * NOTE: This is used in the to() method for converting a Query instance (or an instance of a subclass of Query) to
    * an instance of a subclass of that class.
    *
    * This method has to be overridden in order to obtain sensible behavior. See the comment on the baseClass attribute
    * for further information.
    */
    protected Class<?> getBaseClass(){
        return Object.class
    }

}
