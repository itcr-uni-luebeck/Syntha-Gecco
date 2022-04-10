package org.uzl.syntheagecco

import groovy.io.FileType

class PostToFhirServer {

    static void main(String[] args){

        def sourceDir = args[0]

        def fileList = new ArrayList<File>()
        def dir = new File(sourceDir)

        dir.traverse(type: FileType.FILES, nameFilter: ~"^.*\\.json\$"){
            file -> fileList << file
                println "Found JSON file: '${file.path}'"
        }

        fileList.each {file ->
            ((HttpURLConnection) new URL("https://fhir.imi.uni-luebeck.de/fhir").openConnection()).tap({
                requestMethod = "POST"
                doOutput = true
                setRequestProperty("Content-Type", "application/json")

                outputStream.withPrintWriter {printWriter ->
                    printWriter.write(file.getText("UTF-8"))
                }

                println inputStream.getText("UTF-8")
                if(errorStream){
                    println errorStream.getText("UTF-8")
                }
            })
        }

    }

}