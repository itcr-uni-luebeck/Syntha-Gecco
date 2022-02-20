class RunGenerator {

    static void main(String[] args){

        def optFolder = new File("src/main/resources/openehr/opt")

        optFolder.traverse {file ->
            println file.getName()
            def process = "java -jar E:\\Studium\\Semi_6\\BA\\ehrBase\\openEHR_SDK\\generator\\target\\generator-1.5.0.jar -opt \"E:\\Studium\\Semi_6\\BA\\syntheagecco\\src\\main\\resources\\openehr\\opt\\${file.getName()}\" -out E:\\Studium\\Semi_6\\BA\\syntheagecco\\generated -package syntheagecco.openehr.sdk.model.generated"
                    .execute()
            process.waitFor()
            println process.err.text
        }

    }

}
