import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import syntheagecco.extraction.OpenEhrDiagnosisCategory
import syntheagecco.extraction.mapping.OpenEhrDiagnosisCategoryLookup
import syntheagecco.extraction.mapping.OpenEhrNameOfDiagnosisLookup
import syntheagecco.extraction.mapping.OpenEhrNameOfProcedureLookup
import syntheagecco.extraction.mapping.OpenEhrProcedureCategoryLookup
import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.NameDesProblemsDerDiagnoseDefiningCode

class TestOverlap {

    private static final Logger logger = LogManager.getLogger(this.class)

    static void main(String[] args){

        def categoryMap = new OpenEhrProcedureCategoryLookup()
        def diagnosisMap = new OpenEhrNameOfProcedureLookup()

        diagnosisMap.getView().each {entry ->
            def values = entry.value
            def categorySet = new HashSet<OpenEhrDiagnosisCategory>()

            logger.info("==============")
            logger.info("${entry.key}")
            values.each {value ->
                def category = categoryMap.get(value) as OpenEhrDiagnosisCategory
                logger.debug("${value.toString()}: ${category.toString()}")
                if(!categorySet.add(category)){
                    logger.warn("Found overlap!")
                }
            }
        }
    }

}
