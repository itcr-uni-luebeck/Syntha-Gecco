package org.uzl.syntheagecco

import org.uzl.syntheagecco.analysis.module.ModuleParser
import org.uzl.syntheagecco.analysis.module.state.CodingState
import org.uzl.syntheagecco.analysis.module.state.StateType
import org.uzl.syntheagecco.analysis.query.CodingStateQuery
import org.uzl.syntheagecco.utility.FileManipulation

import java.nio.file.Path

class Script{

    static void main(String[] args){

        def dir = Path.of("src", "main", "resources", "modules").toString()
        def files = FileManipulation.getFilesInDirRecursive(dir, FileManipulation.FileExtension.JSON)

        def parser = new ModuleParser()
        def result = parser.parseAll(files)

        def query = result.query()

        def start = System.currentTimeMillis()

        //Query 1
        def result1 = query.execute()
        println "Query 1: ${result1.size()}"

        //Query 2
        def result2_1 = query.filterByType(StateType.INITIAL).execute()
        def result2_2 = query.filterByType(StateType.TERMINAL).execute()
        println "Query 2: ${result2_1.size()} and ${result2_2.size()}"

        //Query 3
        def result3 = query.filterByType(StateType.INITIAL)
                .union(query.filterByType(StateType.TERMINAL))
                .execute()
        println "Query 3: ${result3.size()}"

        //Query 4
        def result4 = query.filterByType(StateType.INITIAL, StateType.TERMINAL).execute()
        println "Query 4: ${result4.size()}"

        //Query 5
        def result5 = query.filterByOrigin("covid19").execute()
        println "Query 5: ${result5.size()}"

        //Query 6
        def result6 = query.filterByOrigin("covid19")
                .filterByType(StateType.INITIAL, StateType.TERMINAL)
                .execute()
        println "Query 6: ${result6.size()}"

        //Query 7
        def result7_1 = query.to(CodingState.class, CodingStateQuery.class).execute()
        def result7_2 = query.filter(CodingState.class).execute()
        def result7_3 = query.to(CodingState.class, CodingStateQuery.class).filter(CodingState.class).execute()
        println "Query 7: ${result7_1.size()} and ${result7_2.size()} and ${result7_3.size()}"

        //Query 8
        def result8 = query.to(CodingState.class, CodingStateQuery.class)
                .filterByOrigin("infection")
                .filterByType(StateType.CONDITIONONSET)
                .filterByCoding("SNOMED-CT" ,"840539006")
                .execute()
        println "Query 8: ${result8.size()}"

        def end = System.currentTimeMillis()

        println "Time elapsed: ${end - start} ms"

    }

}