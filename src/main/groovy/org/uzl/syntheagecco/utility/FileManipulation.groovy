package org.uzl.syntheagecco.utility

import com.google.common.base.Charsets
import com.google.common.io.Resources
import groovy.io.FileType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths

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

    static InputStream getResourceFile(String... pathComponents){
        def path = Paths.get(pathComponents)
        getResourceFile(path)
    }

    static InputStream getResourceFile(Path path){
        def classLoader = Thread.currentThread().getContextClassLoader()
        return classLoader.getResourceAsStream(path.toString())
    }

    /*
    static List<InputStream> getResoureFilesInDir(Path path){
        def stream = getResourceFile(path)
        def files = []

        try {
            def reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
            def fileName = "" as String
            while((fileName = reader.readLine()) != null) {
                files << getResourceFile(path.resolve(fileName))
            }
        }
        catch (IOException e){
            logger.error(e.getMessage())
            return null
        }
        return files
    }
     */

    static List<String> getResourceFilesInDir(Path path){
        //def classLoader = FileManipulation.class.getClassLoader()
        //def stream = classLoader.getResourceAsStream(path.toString())
        //System.out.println("Stream: " + stream)
        def url = Resources.getResource(path.toString())
        def s = Resources.toString(url, Charsets.UTF_8)
        def fileNames = s.split("\\n").findAll {it -> it.contains('.json')}
        def files = []
        fileNames.each {fileName ->
            def fileUrl = Resources.getResource(path.resolve(fileName).toString())
            files << Resources.toString(fileUrl, Charsets.UTF_8)
        }
        return files
    }

    static String getResource(Path path){
        System.out.println(path.toString())
        def url = Resources.getResource(path.toString())
        return Resources.toString(url, Charsets.UTF_8)
    }

}
