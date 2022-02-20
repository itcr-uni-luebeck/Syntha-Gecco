package syntheagecco.extraction.model

import syntheagecco.openehr.sdk.model.generated.geccodiagnosecomposition.definition.NameDesProblemsDerDiagnoseDefiningCode

abstract class CategorizedResource extends BaseResource {

    private Set<NameDesProblemsDerDiagnoseDefiningCode> openEhrDiagnosisTypes

    CategorizedResource(){ }

    CategorizedResource(String id){
        super(id)
        this.openEhrDiagnosisTypes = new HashSet<>()
    }

    boolean addToDiagnosisTypes(NameDesProblemsDerDiagnoseDefiningCode type){
        return this.openEhrDiagnosisTypes.add(type)
    }

    boolean addAllDiagnosisTypes(List<NameDesProblemsDerDiagnoseDefiningCode> types){
        return this.openEhrDiagnosisTypes.addAll(types)
    }

    List<NameDesProblemsDerDiagnoseDefiningCode> getDiagnosisTypes(){
        return this.openEhrDiagnosisTypes.collect()
    }

}
