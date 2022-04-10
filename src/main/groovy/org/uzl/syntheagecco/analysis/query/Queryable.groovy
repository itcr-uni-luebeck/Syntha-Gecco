package org.uzl.syntheagecco.analysis.query

interface Queryable<T extends Query> {

    T query()

}