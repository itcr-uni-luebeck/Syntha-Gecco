package org.uzl.syntheagecco

import org.uzl.syntheagecco.extraction.mapping.OpenEhrNameOfDiagnosisLookup

class CheckContainment {

    static void main(String[] args){

        def diagNameLookup = new OpenEhrNameOfDiagnosisLookup()
        def code = args[0]
        def answer = "Code ${code} "

        answer += diagNameLookup.containsKey(code) ? "contained" : "not contained"

        println answer
    }

}
