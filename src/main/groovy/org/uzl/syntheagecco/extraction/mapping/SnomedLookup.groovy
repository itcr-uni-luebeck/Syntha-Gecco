package org.uzl.syntheagecco.extraction.mapping


import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class SnomedLookup extends GeccoCategoryLookup{

    private final static Logger logger = LogManager.getLogger(SnomedLookup.class)

    SnomedLookup(){
        super({
            logger.info("[#]Creating SNOMED-CT code lookup ...")
            return "src/main/resources/maps/snomed"
        }.call())
    }

}
