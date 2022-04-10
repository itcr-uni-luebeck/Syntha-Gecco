package org.uzl.syntheagecco

import org.uzl.syntheagecco.utility.FileManipulation

import java.nio.file.Path
import java.nio.file.Paths

class GenerateResourceIndex {

    private static pathToResources = Paths.get("src", "main", "resources")

    static void main(String[] args){

        generateSingleIndex(Paths.get("maps", "diagnosis_to_diagnosis_category"), "OpenEhrDiagnosisCategoryIndex")

        generateSingleIndex(Paths.get("maps", "openehr_category", "diagnosis_category"), "OpenEhrNameOfDiagnosisIndex")

        generateSingleIndex(Paths.get("maps", "openehr_category", "procedure_category"), "OpenEhrNameOfProcedureIndex")

        generateSingleIndex(Paths.get("maps", "procedure_to_procedure_category"), "OpenEhrProcedureCategoryIndex")

        generateSingleIndex(Paths.get("maps", "openehr_category", "diagnosis_transplant_category"), "OpenEhrTransplantIndex")

        generateSingleIndex(Paths.get("maps", "rxnorm"), "RxNormIndex")

        generateSingleIndex(Paths.get("maps", "snomed"), "SnomedIndex")

        generateSingleIndex(Paths.get("maps", "loinc"), "LoincIndex")

    }

    private static void generateSingleIndex(Path toDir, String indexName){
        def fullPath = pathToResources.resolve(toDir)
        def files = FileManipulation.getFilesInDir(fullPath.toString(), FileManipulation.FileExtension.JSON)
        def indexFile = new File(pathToResources.resolve(indexName + '.txt').toString())
        if(indexFile.exists()) indexFile.text = ''
        indexFile.withOutputStream {oStream ->
            files.each {file ->
                oStream << toDir.resolve(file.getName()).toString() + "\n"
            }
        }
    }

}
