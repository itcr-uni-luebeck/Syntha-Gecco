package org.uzl.syntheagecco.openehr.sdk

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.ehrbase.client.classgenerator.interfaces.CompositionEntity
import org.uzl.syntheagecco.config.SyntheaGeccoConfig
import org.uzl.syntheagecco.extraction.OpenEhrProcedureCategory
import org.uzl.syntheagecco.extraction.model.CaseInformation
import org.uzl.syntheagecco.extraction.model.SOpenEhrProcedure
import org.uzl.syntheagecco.openehr.sdk.model.OptGeccoCase
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.GeccoProzedurKategorieElement
import org.uzl.syntheagecco.openehr.sdk.model.generated.geccoprozedurcomposition.definition.KategorieDefiningCode

class OptTherapyBuilder extends BaseOTBuilder{

    private static final Logger logger = LogManager.getLogger(OtpAnamnesisBuilder.class)

    OptTherapyBuilder(){
        super()
    }

    OptTherapyBuilder(SyntheaGeccoConfig config){
        super(config)
    }

    @Override
    void buildResources(CaseInformation caseInfo, OptGeccoCase geccoCase) {
        def provider = CompositionProvider.getInstance()

        def compositions = geccoCase.getCompositions()

        compositions.addAll(buildDiagnosticProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.DIAGNOSTIC_PROCEDURE), provider))
        compositions.addAll(buildImagingProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.IMAGING), provider))
        compositions.addAll(buildTherapeuticProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.THERAPEUTIC_PROCEDURE_PROCEDURE), provider))
        compositions.addAll(buildOtherCategoryProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.OTHER_CATEGORY), provider))
        compositions.addAll(buildRespiratoryTherapyProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.RESPIRATORY_THERAPY_PROCEDURE), provider))
        compositions.addAll(buildArtificialRespProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.ARTIFICIAL_RESPIRATION_PROCEDURE), provider))
        compositions.addAll(buildSurgicalProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.SURGICAL_PROCEDURE), provider))
        compositions.addAll(buildAdmOfMedProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.ADMINISTRATION_OF_MEDICINE), provider))
        compositions.addAll(buildVentilationProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.NONINVASIVE_VENTILATION_PROCEDURE), provider))
        compositions.addAll(buildPositioningProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.PROCEDURES_RELATING_TO_POSITIONING_AND_SUPPORT_PROCEDURE), provider))
        compositions.addAll(buildOxygenAdmByNasalCannulaProcedure(caseInfo.getProceduresForCategory(OpenEhrProcedureCategory.OXYGEN_ADMINISTRATION_BY_NASAL_CANNULA_PROCEDURE), provider))
    }

    private List<CompositionEntity> buildDiagnosticProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.DIAGNOSTIC_PROCEDURE
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.DIAGNOSTIC_PROCEDURE
            }

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildImagingProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.IMAGING
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.IMAGING
            }

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildTherapeuticProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.THERAPEUTIC_PROCEDURE_PROCEDURE
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.THERAPEUTIC_PROCEDURE_PROCEDURE
            }

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildOtherCategoryProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.OTHER_CATEGORY
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.OTHER_CATEGORY
            }

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildRespiratoryTherapyProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.RESPIRATORY_THERAPY_PROCEDURE
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.RESPIRATORY_THERAPY_PROCEDURE
            }

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildArtificialRespProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.ARTIFICIAL_RESPIRATION_PROCEDURE
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.ARTIFICIAL_RESPIRATION_PROCEDURE
            }

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildSurgicalProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.SURGICAL_PROCEDURE
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.SURGICAL_PROCEDURE
            }

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildOxygenAdmByNasalCannulaProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.OXYGEN_ADMINISTRATION_BY_NASAL_CANNULA_PROCEDURE
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.OXYGEN_ADMINISTRATION_BY_NASAL_CANNULA_PROCEDURE
            }

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildAdmOfMedProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.ADMINISTRATION_OF_MEDICINE
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.ADMINISTRATION_OF_MEDICINE
            }

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildPositioningProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.PROCEDURES_RELATING_TO_POSITIONING_AND_SUPPORT_PROCEDURE
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.PROCEDURES_RELATING_TO_POSITIONING_AND_SUPPORT_PROCEDURE
            }

            compositions.add(proc)
        }

        return compositions
    }

    private List<CompositionEntity> buildVentilationProcedure(List<SOpenEhrProcedure> procedures, CompositionProvider provider){
        def compositions = new ArrayList<CompositionEntity>()

        procedures.each {procedure ->
            def proc = provider.getProcedureComposition(procedure)

            proc.setKategorie(new ArrayList<GeccoProzedurKategorieElement>().tap{
                it.add(new GeccoProzedurKategorieElement().tap {element ->
                    element.value = KategorieDefiningCode.NONINVASIVE_VENTILATION_PROCEDURE
                })
            })

            proc.getProzedur().tap {it ->
                it.artDerProzedurDefiningCode = KategorieDefiningCode.NONINVASIVE_VENTILATION_PROCEDURE
            }

            compositions.add(proc)
        }

        return compositions
    }

}
