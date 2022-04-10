package org.uzl.syntheagecco.fhir.validation

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.uzl.syntheagecco.utility.FileManipulation

class FhirFileValidator {

    private static final Logger logger = LogManager.getLogger(FhirFileValidator.class)

    static void validateBundles(String pathToDir, String outputDir){
        def files = FileManipulation.getFilesInDirRecursive(pathToDir, FileManipulation.FileExtension.JSON)
        files.each {file ->
            validateBundle(file.getAbsolutePath(), outputDir)
        }
    }

    static void validateBundle(String pathToFile, String outputDir){
        //Logging
        logger.debug("[?]Validating file at '${pathToFile}'...")

        ProcessBuilder processBuilder = new ProcessBuilder("python", "main.py", pathToFile, outputDir)
        processBuilder.directory(new File("src/main/codex_testdata_to_sq"))
        processBuilder.redirectErrorStream(true)

        Process validationProcess = processBuilder.start()
        String messages = validationProcess.getInputStream().getText("UTF-8")

        switch(validationProcess.waitFor()){
            case 0:
                logger.debug("[?]File validation successful!\nMessage: ${messages}")
                break
            default:
                logger.warn("[?]File validation failed!\nMessage: ${messages}")
        }
    }

    static void postFhirFilesToServer(String pathToDir){
        logger.debug("[?]Posting files to server @ http://localhost/8081/fhir ...")
        postFilesToServer("http://localhost/8081/fhir", pathToDir, FileManipulation.FileExtension.JSON)
        logger.debug("[?]Files were posted.")
    }

    static void postFhirFilesToServer(File pathToDir){
        logger.debug("[?]Posting files to server @ http://localhost/8081/fhir ...")
        postFilesToServer("http://localhost/8081/fhir", pathToDir.getAbsolutePath(), FileManipulation.FileExtension.JSON)
        logger.debug("[?]Files were posted.")
    }

    private static void postFilesToServer(String url, String pathToDir, FileManipulation.FileExtension ext){
        def fileList = FileManipulation.getFilesInDirRecursive(pathToDir, ext)

        ((HttpURLConnection) new URL(url).openConnection()).with({
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/json")

            fileList.each {file ->
                outputStream.withPrintWriter {printWriter ->
                    printWriter.write(file.getText("UTF-8"))
                }
            }

            logger.debug(inputStream.getText("UTF-8"))
            if(errorStream){
                logger.error(errorStream.getText("UTF-8"))
            }

        })
    }

}
