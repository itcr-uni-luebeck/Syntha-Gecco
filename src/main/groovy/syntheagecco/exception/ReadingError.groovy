package syntheagecco.exception

/**
 * This error is expected to be thrown if for example a JSON file couldn't be read correctly. This could be the case if
 * an expected entry isn't present.
 */
class ReadingError extends Error {

    ReadingError(String message){
        super(message)
    }

}
