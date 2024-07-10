package no.hal.fx.bindings;

import javafx.beans.property.Property;
import javafx.scene.Node;

public record BindingTarget<T>(Node targetNode, Class<T> targetClass, Property<T> targetProperty, int maxBindings) {
    
    public BindingTarget(Node targetNode, Class<T> targetClass, Property<T> targetProperty) {
        this(targetNode, targetClass, targetProperty, 1);
    }

    public boolean acceptsBindings(long count) {
        return maxBindings < 0 || count <= maxBindings;
    }
}
