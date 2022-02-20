package syntheagecco.analysis.query

import java.util.function.Supplier

interface Queryable<T extends Query> {

    T query()

}