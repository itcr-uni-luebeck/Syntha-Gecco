package org.uzl.syntheagecco.extraction.model

import org.hl7.fhir.r4.model.Coding

class SDiagnosticReport {

    private String id
    private Coding coding
    private Date effective
    private List<String> memberIds

    SDiagnosticReport(){
        this.memberIds = new ArrayList<>()
    }

    String getId() {
        return id
    }

    void setId(String id) {
        this.id = id
    }

    Coding getCoding() {
        return coding
    }

    void setCoding(Coding coding) {
        this.coding = coding
    }

    Date getEffective() {
        return effective
    }

    void setEffective(Date effective) {
        this.effective = effective
    }

    List<String> getMembers() {
        return memberIds
    }

    void setMembers(List<String> memberIds) {
        this.memberIds = memberIds
    }

    void addMember(String memberId){
        this.memberIds.add(memberId)
    }
}
