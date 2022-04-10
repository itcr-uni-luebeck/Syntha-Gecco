package org.uzl.syntheagecco.analysis.module.state

class CodingCreationError extends Error {

    protected CodingCreationError(String message){
        super(message)
    }

    /**
     * Create CodingCreationError instance based on provided coding. The generated instances message field is dependant
     * on the presence of values for the system and code field of the provided Coding instance
     * @param coding
     * @return Optional instance which contains an CodingCreationError instance if there was an issue with the provided
     * Coding instance or is empty otherwise
     */
    protected static Optional<CodingCreationError> create(Coding coding){
        if(coding.system){
            if(coding.code) return Optional.empty()
            return Optional.of(missingCode(coding))
        }
        if(coding.code) return Optional.of(missingSystem(coding))
        return Optional.of(missingCodeAndSystem())
    }

    protected static CodingCreationError missingSystem(Coding coding){
        return new CodingCreationError("Coding with code '${coding.getCode()}' has no system!")
    }

    protected static CodingCreationError missingCode(Coding coding){
        return new CodingCreationError("Coding with system '${coding.getSystem()}' has no code!")
    }

    protected static CodingCreationError missingCodeAndSystem(){
        return new CodingCreationError("Coding has neither code nor system!")
    }

}
