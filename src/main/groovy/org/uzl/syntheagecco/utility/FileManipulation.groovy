package org.uzl.syntheagecco.utility

import groovy.io.FileType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class FileManipulation {

    private final static Logger logger = LogManager.getLogger(FileManipulation.class)

    enum FileExtension{
        JSON("json")

        final String extension

        private FileExtension(String extension){
            this.extension = extension
        }

        String toString(){
            return this.extension
        }
    }

    static List<File> getFilesInDirRecursive(String sourceDir, FileExtension fileExt){
        //Get all file paths from the files with the specified file type in the specified directory
        def fileList = new ArrayList<File>()
        def dir = new File(sourceDir)
        dir.traverse(type: FileType.FILES, nameFilter: ~"^.*\\.${fileExt.toString()}\$"){
            file -> fileList << file
                logger.debug("Found ${fileExt.toString().toUpperCase()} file: '${file.path}'")
        }
        return fileList
    }

    static List<File> getFilesInDir(String sourceDir, FileExtension fileExt){
        def fileList = new ArrayList<File>()
        def dir = new File(sourceDir)
        dir.eachFileMatch(FileType.FILES, ~"^.*\\.${fileExt.toString()}\$"){
            file -> fileList << file
                logger.debug("Found ${fileExt.toString().toUpperCase()} file: '${file.path}'")
        }
        return fileList
    }

    static void writeJsonString(String jsonString, String fileName, String outputPath){
        writeFile(jsonString, fileName, outputPath, FileManipulation.FileExtension.JSON)
    }

    private static void writeFile(String content, String fileName, String outputPath, FileManipulation.FileExtension ext){
        def newDir = new File("${outputPath}")
        if (!newDir.exists()) {
            newDir.mkdirs()
        }
        def newFile = new File("${newDir.getCanonicalPath()}/${fileName}.${ext.toString()}")
        newFile << content
    }

}
