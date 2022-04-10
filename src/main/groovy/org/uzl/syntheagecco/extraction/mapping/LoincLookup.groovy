package org.uzl.syntheagecco.extraction.mapping


import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class LoincLookup extends GeccoCategoryLookup {

    private final static Logger logger = LogManager.getLogger(RxNormLookup.class)

    LoincLookup(){
        super({
            logger.info("[#]Creating LOINC code lookup ...")
            return  "src/main/resources/maps/loinc"
        }.call())
    }

}
