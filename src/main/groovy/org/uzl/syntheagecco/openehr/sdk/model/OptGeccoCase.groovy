package org.uzl.syntheagecco.openehr.sdk.model

import org.ehrbase.client.classgenerator.interfaces.CompositionEntity

class OptGeccoCase {

    private List<CompositionEntity> compositions

    OptGeccoCase() {
        this.compositions = new ArrayList<>()
    }

    void addComposition(CompositionEntity composition){
        this.compositions.add(composition)
    }

    List<CompositionEntity> getCompositions() {
        return compositions
    }

    void setCompositions(List<CompositionEntity> compositions) {
        this.compositions = compositions
    }

}
