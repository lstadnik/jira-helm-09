package test.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.vavr.collection.Array;
import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import io.vavr.jackson.datatype.VavrModule;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Represents a collection of {@link KubeResources}, e.g. the YAML document generated by a Helm chart.
 */
public final class KubeResources {
    private final Map<Kind, Array<KubeResource>> map;

    private KubeResources(Map<Kind, Array<KubeResource>> map) {
        this.map = map;
    }

    public Traversable<StatefulSet> getStatefulSets() {
        return getAll(Kind.StatefulSet, StatefulSet.class);
    }

    public <T extends KubeResource> Traversable<T> getAll(Kind kind, Class<T> type) {
        return getAll(kind).map(type::cast);
    }

    public StatefulSet getStatefulSet(final String name) {
        return getStatefulSets().find(ss -> name.equals(ss.getName()))
                .getOrElseThrow(() -> new AssertionError("No StatefulSet found with name " + name));
    }

    public KubeResource get(Kind kind) {
        return getAll(kind)
                .singleOption()
                .getOrElseThrow(() -> new AssertionError("No single " + kind + " found"));
    }

    public <T extends KubeResource> T get(Kind kind, Class<T> type, String name) {
        return getAll(kind)
                .find(resource -> name.equals(resource.getName()))
                .map(type::cast)
                .getOrElseThrow(() -> new AssertionError("No " + kind + " found with name " + name));
    }

    public KubeResources assertContains(Kind kind, String name) {
        get(kind, name);
        return this;
    }

    public KubeResource get(Kind kind, String name) {
        return getAll(kind)
                .find(resource -> name.equals(resource.getName()))
                .getOrElseThrow(() -> new AssertionError("No " + kind + " found with name " + name));
    }

    public Traversable<KubeResource> getAll(Kind kind) {
        return map.get(kind)
                .getOrElse(Array::empty);
    }

    public static KubeResources parse(Path outputFile) throws IOException {
        final var yamlParser = new YAMLFactory().createParser(outputFile.toFile());
        final var objectMapper = new ObjectMapper().registerModule(new VavrModule());

        var map = Array.ofAll(objectMapper
                .readValues(yamlParser, new TypeReference<ObjectNode>() {
                })
                .readAll())
                .map(KubeResource::wrap)
                .groupBy(KubeResource::getKind);

        return new KubeResources(map);
    }
}
