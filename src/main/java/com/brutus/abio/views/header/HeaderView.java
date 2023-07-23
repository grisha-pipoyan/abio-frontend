package com.brutus.abio.views.header;

import com.brutus.abio.data.entity.Header;
import com.brutus.abio.data.service.HeaderService;
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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Header")
@Route(value = "header/:headerID?/:action?(edit)", layout = MainLayout.class)
public class HeaderView extends Div implements BeforeEnterObserver {

    private final String HEADER_ID = "headerID";
    private final String HEADER_EDIT_ROUTE_TEMPLATE = "header/%s/edit";

    private final Grid<Header> grid = new Grid<>(Header.class, false);

    private TextField headLine_en;
    private TextField headLine_ru;
    private TextField headLine_am;
    private TextField description_en;
    private TextField description_ru;
    private TextField description_am;
    private TextField url;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Header> binder;

    private Header header;

    private final HeaderService headerService;

    public HeaderView(HeaderService headerService) {
        this.headerService = headerService;
        addClassNames("header-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("headLine_en").setAutoWidth(true);
        grid.addColumn("headLine_ru").setAutoWidth(true);
        grid.addColumn("headLine_am").setAutoWidth(true);
        grid.addColumn("description_en").setAutoWidth(true);
        grid.addColumn("description_ru").setAutoWidth(true);
        grid.addColumn("description_am").setAutoWidth(true);
        grid.addColumn("url").setAutoWidth(true);
        grid.setItems(query -> headerService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(HEADER_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(HeaderView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Header.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.header == null) {
                    this.header = new Header();
                }
                binder.writeBean(this.header);
                headerService.update(this.header);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(HeaderView.class);
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
        Optional<Long> headerId = event.getRouteParameters().get(HEADER_ID).map(Long::parseLong);
        if (headerId.isPresent()) {
            Optional<Header> headerFromBackend = headerService.get(headerId.get());
            if (headerFromBackend.isPresent()) {
                populateForm(headerFromBackend.get());
            } else {
                Notification.show(String.format("The requested header was not found, ID = %s", headerId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(HeaderView.class);
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
        headLine_en = new TextField("Head Line_en");
        headLine_ru = new TextField("Head Line_ru");
        headLine_am = new TextField("Head Line_am");
        description_en = new TextField("Description_en");
        description_ru = new TextField("Description_ru");
        description_am = new TextField("Description_am");
        url = new TextField("Url");
        formLayout.add(headLine_en, headLine_ru, headLine_am, description_en, description_ru, description_am, url);

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

    private void populateForm(Header value) {
        this.header = value;
        binder.readBean(this.header);

    }
}
