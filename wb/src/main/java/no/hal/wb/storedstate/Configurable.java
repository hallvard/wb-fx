package no.hal.wb.storedstate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public interface Configurable {

    /**
     * Configures with the provided configuration. 
     *
     * @param configuration
     */
    void configure(JsonNode configuration);

    /**
     * Returns the current configuration.
     *
     * @return the current configuration
     */
    default JsonNode getConfiguration() {
        return null;
    }

    public static JsonNodeFactory getJsonNodeFactory() {
        return JsonNodeFactory.instance;
    }
}
