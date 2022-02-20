package syntheagecco.utility

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateManipulation {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")

    /**
     * Method for converting dates in Synthea resources to Date instances
     * @param date String representing a date and time with offset
     * @return Date instance representing the date and time of the input string or null if the input is null
     */
    static Date dateFromSyntheaDate(String date){
        if(date == null){
            return null
        }
        else{
            OffsetDateTime syntheaDateTime = OffsetDateTime.parse(date, formatter)
            return Date.from(syntheaDateTime.toInstant())
        }
    }

    static LocalDateTime localDateTimeFromDate(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
    }

    static LocalDate localDateFromDate(Date date){
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault())
    }

}
