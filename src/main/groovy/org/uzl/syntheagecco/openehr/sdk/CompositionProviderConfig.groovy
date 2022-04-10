package org.uzl.syntheagecco.openehr.sdk

import com.nedap.archie.rm.generic.PartyIdentified
import org.ehrbase.client.classgenerator.shareddefinition.Language
import org.ehrbase.client.classgenerator.shareddefinition.Setting
import org.ehrbase.client.classgenerator.shareddefinition.Territory

class CompositionProviderConfig {

    private PartyIdentified partyProxy
    private Territory territory
    private Language language
    private Setting setting

    CompositionProviderConfig() {}

    PartyIdentified getPartyProxy() {
        return partyProxy
    }

    void setPartyProxy(PartyIdentified partyProxy) {
        this.partyProxy = partyProxy
    }

    Territory getTerritory() {
        return territory
    }

    void setTerritory(Territory territory) {
        this.territory = territory
    }

    Language getLanguage() {
        return language
    }

    void setLanguage(Language language) {
        this.language = language
    }

    Setting getSetting() {
        return setting
    }

    void setSetting(Setting setting) {
        this.setting = setting
    }
}
