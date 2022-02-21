# SyntheaGecco

This project represents a patient data generator using the GECCO data set specification. Accordingly, data is 
generated representing a data set of COVID-19 patients. As the data is being created using the patient data 
generator [**Synthea**](https://github.com/synthetichealth/synthea) it can be safely employed without risking 
patient anonymity.

The project can be configured using command line parameters:

| Parameter          |   Values   |       Default       | Description                                                                                                                            |
|--------------------|:----------:|:-------------------:|----------------------------------------------------------------------------------------------------------------------------------------|
| -p                 |  Integer   |         100         | Specifies the number of patients generated                                                                                             |
| -s                 |    Long    | Current system time | Seed value for data generation                                                                                                         |
| -f                 |            |       Enabled       | Enables data generation in HL7 FHIR R4                                                                                                 |
| -o                 |            |      Disabled       | Enables data generation in openEHR                                                                                                     |
| -oUpload           |            |      Disabled       | Enables upload to an EHRbase server. If the option is disabled the composition instances will be exported locally as files on the disk |
| -oEndpoint         | Server URL |                     | For providing the URL of the EHRbase server if remote upload of openEHR data is enabled via **-oUpload**                               |
| -oUser             | User name  |                     | For providing the user name if remote upload of openEHR data is enabled via **-oUpload**                                               |
| -oPassw            |  Password  |                     | For providing the password if remote upload of openEHR data is enabled via **-oUpload**                                                |
| -ocIdValue         |   String   |    synthea-gecco    | The name value of the composer ID within the openEHR templates                                                                         |
| -ocIdScheme        |   String   |    synthea-gecco    | The scheme of the composer ID within the openEHR template                                                                              |
| -ocNamespace       |   String   |       synthea       | The namespace of the composer within the openEHR template                                                                              |
| -ocName            |   String   |    synthea-gecco    | The name of the composer within the openEHR template                                                                                   |
| -ocTerritory       |   String   |         DE          | The territory in which the composer resides, coded with using the [ISO_3166-1](https://en.wikipedia.org/wiki/ISO_3166-1) coding system                                         |
| -ocLanguage        |   String   |         de          | The language that is used in the template instances, coded with the [ISO_639-1](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) code system                                          |