package com.brutus.abio.views.deliveryregions;

import com.brutus.abio.data.entity.DeliveryRegion;
import com.brutus.abio.data.service.DeliveryRegionService;
import com.brutus.abio.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Delivery Regions")
@Route(value = "delivery-regions/:deliveryRegionID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class DeliveryRegionsView extends Div implements BeforeEnterObserver {

    private final String DELIVERYREGION_ID = "deliveryRegionID";
    private final String DELIVERYREGION_EDIT_ROUTE_TEMPLATE = "delivery-regions/%s/edit";

    private final Grid<DeliveryRegion> grid = new Grid<>(DeliveryRegion.class, false);

    private TextField name_en;
    private TextField name_ru;
    private TextField name_am;
    private TextField price;
    private TextField currencyType;
    private TextField bulky;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<DeliveryRegion> binder;

    private DeliveryRegion deliveryRegion;

    private final DeliveryRegionService deliveryRegionService;

    public DeliveryRegionsView(DeliveryRegionService deliveryRegionService) {
        this.deliveryRegionService = deliveryRegionService;
        addClassNames("delivery-regions-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name_en").setAutoWidth(true);
        grid.addColumn("name_ru").setAutoWidth(true);
        grid.addColumn("name_am").setAutoWidth(true);
        grid.addColumn("price").setAutoWidth(true);
        grid.addColumn("currencyType").setAutoWidth(true);
        grid.addColumn("bulky").setAutoWidth(true);
        grid.setItems(query -> deliveryRegionService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(DELIVERYREGION_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(DeliveryRegionsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(DeliveryRegion.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(price).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("price");
        binder.forField(bulky).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("bulky");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.deliveryRegion == null) {
                    this.deliveryRegion = new DeliveryRegion();
                }
                binder.writeBean(this.deliveryRegion);
                deliveryRegionService.update(this.deliveryRegion);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(DeliveryRegionsView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> deliveryRegionId = event.getRouteParameters().get(DELIVERYREGION_ID).map(Long::parseLong);
        if (deliveryRegionId.isPresent()) {
            Optional<DeliveryRegion> deliveryRegionFromBackend = deliveryRegionService.get(deliveryRegionId.get());
            if (deliveryRegionFromBackend.isPresent()) {
                populateForm(deliveryRegionFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested deliveryRegion was not found, ID = %s", deliveryRegionId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(DeliveryRegionsView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        name_en = new TextField("Name_en");
        name_ru = new TextField("Name_ru");
        name_am = new TextField("Name_am");
        price = new TextField("Price");
        currencyType = new TextField("Currency Type");
        bulky = new TextField("Bulky");
        formLayout.add(name_en, name_ru, name_am, price, currencyType, bulky);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(DeliveryRegion value) {
        this.deliveryRegion = value;
        binder.readBean(this.deliveryRegion);

    }
}
