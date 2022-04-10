package org.uzl.syntheagecco.openehr.sdk.client.endpoint


import com.nedap.archie.rm.composition.Composition
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.ehrbase.client.annotations.Id
import org.ehrbase.client.exception.ClientException
import org.ehrbase.client.flattener.Flattener
import org.ehrbase.client.flattener.Unflattener
import org.ehrbase.client.openehrclient.CompositionEndpoint
import org.ehrbase.client.openehrclient.VersionUid
import org.uzl.syntheagecco.openehr.sdk.client.AuthRestClient

import java.beans.IntrospectionException
import java.beans.PropertyDescriptor
import java.lang.reflect.InvocationTargetException

import static org.ehrbase.client.openehrclient.defaultrestclient.DefaultRestEhrEndpoint.EHR_PATH

class AuthRestCompositionEndpoint implements CompositionEndpoint{

    private static final Logger logger = LogManager.getLogger(AuthRestCompositionEndpoint.class)

    public static final String COMPOSITION_PATH = "/composition/";
    private final AuthRestClient authRestClient
    private final UUID ehrId

    AuthRestCompositionEndpoint(AuthRestClient authRestClient, UUID ehrId) {
        this.authRestClient = authRestClient
        this.ehrId = ehrId
    }

    static Optional<VersionUid> extractVersionUid(Object entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter({f -> f.isAnnotationPresent(Id.class)})
                .findAny()
                .map({idField ->
                    try {
                        PropertyDescriptor propertyDescriptor =
                                new PropertyDescriptor(idField.getName(), entity.getClass());
                        return (VersionUid) propertyDescriptor.getReadMethod().invoke(entity);
                    } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
                        throw new ClientException(e.getMessage(), e);
                    }
                })
    }

    @Override
    <T> T mergeCompositionEntity(T entity) {
        logger.debug("mergeComposition: ${entity.dump()}")
        Composition composition =
        new Unflattener(
                authRestClient.getTemplateProvider(),
                authRestClient.getDefaultValuesProvider())
                .unflatten(entity) as Composition

        Optional<VersionUid> versionUid = extractVersionUid(entity)

        final VersionUid updatedVersion
        if (versionUid.isEmpty()) {
            updatedVersion =
                    authRestClient.httpPost(
                            authRestClient
                                    .getConfig()
                                    .getBaseUri()
                                    .resolve(EHR_PATH + ehrId.toString() + COMPOSITION_PATH),
                            composition)
        } else {
            updatedVersion =
                    authRestClient.httpPut(
                            authRestClient
                                    .getConfig()
                                    .getBaseUri()
                                    .resolve(
                                            EHR_PATH + ehrId.toString() + COMPOSITION_PATH + versionUid.get().getUuid()),
                            composition,
                            versionUid.get())
        }
        Flattener.addVersion(entity, updatedVersion)
        entity = new Flattener(authRestClient.getTemplateProvider())
                .flatten(composition, entity.getClass()) as T
        Flattener.addVersion(entity, updatedVersion)

        return entity
    }

    @Override
    <T> Optional<T> find(UUID compositionId, Class<T> clazz) {
        Optional<Composition> composition =
                authRestClient.httpGet(
                        authRestClient
                                .getConfig()
                                .getBaseUri()
                                .resolve(EHR_PATH + ehrId.toString() + COMPOSITION_PATH + compositionId.toString()),
                        Composition.class)

        return composition.map(
                {c -> new Flattener(authRestClient.getTemplateProvider()).flatten(c, clazz)})
    }

}
