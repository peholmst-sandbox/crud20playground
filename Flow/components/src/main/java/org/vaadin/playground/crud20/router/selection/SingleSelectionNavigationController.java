package org.vaadin.playground.crud20.router.selection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.HighlightCondition;
import com.vaadin.flow.router.RouteParameters;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.vaadin.playground.crud20.data.selection.SelectionChangeEvent;
import org.vaadin.playground.crud20.data.selection.SingleSelectionModel;

import java.io.Serializable;
import java.util.*;

public final class SingleSelectionNavigationController<T> implements Serializable {

    private final Class<? extends Component> routingTarget;
    private final SingleSelectionModel<T> selectionModel;
    private final String selectionParameterName;
    private final SerializableFunction<String, T> parameterConverter;
    private SerializablePredicate<T> parameterValidator;
    private RouteParameters routeParameters;
    private SingleSelectionNavigationController<?> parentController;
    private final List<SingleSelectionNavigationController<?>> childControllers = new ArrayList<>();

    // TODO Create nicer API instead of this horrible constructor

    public SingleSelectionNavigationController(@Nonnull Component owner,
                                               @Nonnull SingleSelectionModel<T> selectionModel,
                                               @Nonnull String selectionParameterName,
                                               @Nonnull SerializableFunction<String, T> parameterConverter) {
        owner.addAttachListener(event -> onOwnerAttached());
        owner.addDetachListener(event -> onOwnerDetached());
        this.routingTarget = owner.getClass();
        this.selectionModel = selectionModel;
        this.selectionParameterName = selectionParameterName;
        this.parameterConverter = parameterConverter;
        this.selectionModel.addSelectionChangeListener(this::handleSelectionChangeEvent);
        setParameterValidator(null);
    }

    public void setParentController(@Nullable SingleSelectionNavigationController<?> parentController) {
        this.parentController = parentController;
        updateSelectionModel();
    }

    private void onOwnerAttached() {
        if (parentController != null) {
            parentController.childControllers.add(this);
        }
    }

    private void onOwnerDetached() {
        if (parentController != null) {
            parentController.childControllers.remove(this);
        }
    }

    public void setParameterValidator(@Nullable SerializablePredicate<T> parameterValidator) {
        this.parameterValidator = Objects.requireNonNullElseGet(parameterValidator, () -> (t) -> true);
    }

    private Class<? extends Component> getRoutingTarget() {
        if (parentController != null) {
            return parentController.getRoutingTarget();
        }
        return routingTarget;
    }

    private void handleSelectionChangeEvent(@Nonnull SelectionChangeEvent<T> event) {
        event.firstSelectedItem().ifPresentOrElse(this::navigateToSelection, this::navigateToEmptySelection);
    }

    public void navigateToSelection(@Nonnull T selection) {
        if (isSelected(selection)) {
            return;
        }
        UI.getCurrent().navigate(getRoutingTarget(), getRouteParametersForSelection(selection));
    }

    private @Nonnull RouteParameters getRouteParameters() {
        if (parentController != null) {
            return parentController.getRouteParameters();
        }
        return Optional.ofNullable(routeParameters).orElseGet(RouteParameters::empty);
    }

    public @Nonnull RouteParameters getRouteParametersForSelection(@Nonnull T selection) {
        var params = new HashMap<String, String>();
        // TODO Would be helpful if you could get the map directly from RouteParameters instead of having to construct it like this.
        var routeParameters = getRouteParameters();
        routeParameters.getParameterNames().forEach(name -> routeParameters.get(name).ifPresent(value -> params.put(name, value)));
        params.put(selectionParameterName, selection.toString());
        return new RouteParameters(params);
    }

    public <C> @Nonnull HighlightCondition<C> getHighlightConditionForSelection(@Nonnull T selection) {
        return (c, event) -> routeParametersContainSelection(event.getRouteParameters(), selection);
    }

    public boolean isSelected(@Nonnull T selection) {
        return routeParametersContainSelection(getRouteParameters(), selection);
    }

    private boolean routeParametersContainSelection(@Nonnull RouteParameters routeParameters, @Nonnull T selection) {
        return routeParameters.get(selectionParameterName)
                .map(parameterConverter)
                .filter(selection::equals)
                .isPresent();
    }

    public void navigateToEmptySelection() {
        UI.getCurrent().navigate(getRoutingTarget());
    }

    private void updateSelectionModel() {
        getRouteParameters().get(selectionParameterName)
                .map(parameterConverter)
                .filter(parameterValidator)
                .ifPresentOrElse(selectionModel::select, selectionModel::deselectAll);
        this.childControllers.forEach(SingleSelectionNavigationController::updateSelectionModel);

    }

    public void handleBeforeEnterEvent(@Nonnull BeforeEnterEvent event) {
        this.routeParameters = event.getRouteParameters();
        updateSelectionModel();
    }
}
