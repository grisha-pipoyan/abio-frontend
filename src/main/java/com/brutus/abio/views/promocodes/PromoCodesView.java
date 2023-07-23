package com.brutus.abio.views.promocodes;

import com.brutus.abio.data.entity.PromoCode;
import com.brutus.abio.data.service.PromoCodeService;
import com.brutus.abio.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Promo Codes")
@Route(value = "promo-codes/:promoCodeID?/:action?(edit)", layout = MainLayout.class)
public class PromoCodesView extends Div implements BeforeEnterObserver {

    private final String PROMOCODE_ID = "promoCodeID";
    private final String PROMOCODE_EDIT_ROUTE_TEMPLATE = "promo-codes/%s/edit";

    private final Grid<PromoCode> grid = new Grid<>(PromoCode.class, false);

    private TextField code;
    private TextField discount;
    private TextField promoCodeType;
    private DatePicker validFrom;
    private DatePicker validUntil;
    private TextField productCodes;
    private TextField minimumPurchase;
    private TextField maxApplications;
    private TextField currentApplications;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<PromoCode> binder;

    private PromoCode promoCode;

    private final PromoCodeService promoCodeService;

    public PromoCodesView(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
        addClassNames("promo-codes-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("code").setAutoWidth(true);
        grid.addColumn("discount").setAutoWidth(true);
        grid.addColumn("promoCodeType").setAutoWidth(true);
        grid.addColumn("validFrom").setAutoWidth(true);
        grid.addColumn("validUntil").setAutoWidth(true);
        grid.addColumn("productCodes").setAutoWidth(true);
        grid.addColumn("minimumPurchase").setAutoWidth(true);
        grid.addColumn("maxApplications").setAutoWidth(true);
        grid.addColumn("currentApplications").setAutoWidth(true);
        grid.setItems(query -> promoCodeService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PROMOCODE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PromoCodesView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(PromoCode.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(discount).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("discount");
        binder.forField(promoCodeType).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("promoCodeType");
        binder.forField(minimumPurchase).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("minimumPurchase");
        binder.forField(maxApplications).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("maxApplications");
        binder.forField(currentApplications).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("currentApplications");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.promoCode == null) {
                    this.promoCode = new PromoCode();
                }
                binder.writeBean(this.promoCode);
                promoCodeService.update(this.promoCode);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(PromoCodesView.class);
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
        Optional<Long> promoCodeId = event.getRouteParameters().get(PROMOCODE_ID).map(Long::parseLong);
        if (promoCodeId.isPresent()) {
            Optional<PromoCode> promoCodeFromBackend = promoCodeService.get(promoCodeId.get());
            if (promoCodeFromBackend.isPresent()) {
                populateForm(promoCodeFromBackend.get());
            } else {
                Notification.show(String.format("The requested promoCode was not found, ID = %s", promoCodeId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(PromoCodesView.class);
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
        code = new TextField("Code");
        discount = new TextField("Discount");
        promoCodeType = new TextField("Promo Code Type");
        validFrom = new DatePicker("Valid From");
        validUntil = new DatePicker("Valid Until");
        productCodes = new TextField("Product Codes");
        minimumPurchase = new TextField("Minimum Purchase");
        maxApplications = new TextField("Max Applications");
        currentApplications = new TextField("Current Applications");
        formLayout.add(code, discount, promoCodeType, validFrom, validUntil, productCodes, minimumPurchase,
                maxApplications, currentApplications);

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

    private void populateForm(PromoCode value) {
        this.promoCode = value;
        binder.readBean(this.promoCode);

    }
}
