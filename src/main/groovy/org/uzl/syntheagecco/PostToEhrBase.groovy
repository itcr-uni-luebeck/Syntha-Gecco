package org.uzl.syntheagecco

import groovy.io.FileType

class PostToEhrBase {

    static void main(String[] args){

        def sourceDir = args[0]
        def uuid = UUID.randomUUID()
        def fileList = []

        new File(sourceDir).traverse(type: FileType.FILES, nameFilter: ~"^.*\\.json\$"){
            file -> fileList << file
                println "Found JSON file: '${file.path}'"
        }

        ((HttpURLConnection) new URL("http://ehrbase.imi.uni-luebeck.de/ehrbase/rest/openehr/v1/composition/" +
                "?format=FLAT&ehrId=${uuid}&templateId=").openConnection()).with({
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/json")

            fileList.each {file ->
                outputStream.withPrintWriter {printWriter ->
                    printWriter.write(file.getText("UTF-8"))
                }
            }

            println inputStream.getText("UTF-8")
            if(errorStream){
                println errorStream.getText("UTF-8")
            }

        })

    }

}
