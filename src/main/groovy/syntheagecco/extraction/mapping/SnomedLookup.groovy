package syntheagecco.extraction.mapping

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import syntheagecco.extraction.GeccoCategory
import syntheagecco.utility.FileManipulation

class SnomedLookup extends GeccoCategoryLookup{

    private final static Logger logger = LogManager.getLogger(SnomedLookup.class)

    SnomedLookup(){
        super({
            logger.info("[#]Creating SNOMED-CT code lookup ...")
            return "src/main/resources/maps/snomed"
        }.call())
    }

}
