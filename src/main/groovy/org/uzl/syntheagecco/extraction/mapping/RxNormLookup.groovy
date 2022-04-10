package org.uzl.syntheagecco.extraction.mapping

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class RxNormLookup extends GeccoCategoryLookup{

    private final static Logger logger = LogManager.getLogger(RxNormLookup.class)

    RxNormLookup(){
        super({
            logger.info("[#]Creating rxNorm code lookup ...")
            return  "src/main/resources/maps/rxnorm"
        }.call())
    }

}
