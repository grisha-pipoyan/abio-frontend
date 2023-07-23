package com.brutus.abio.views.orders;

import com.brutus.abio.data.entity.SamplePerson;
import com.brutus.abio.data.service.SamplePersonService;
import com.brutus.abio.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Orders")
@Route(value = "orders", layout = MainLayout.class)
@Uses(Icon.class)
public class OrdersView extends Composite<VerticalLayout> {

    private HorizontalLayout layoutRow = new HorizontalLayout();

    private MenuBar menuBar = new MenuBar();

    private VerticalLayout layoutColumn2 = new VerticalLayout();

    private Grid basicGrid = new Grid(SamplePerson.class);

    public OrdersView() {
        getContent().setHeightFull();
        getContent().setWidthFull();
        layoutRow.setWidthFull();
        layoutRow.addClassName(Gap.MEDIUM);
        setMenuBarSampleData(menuBar);
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidthFull();
        setGridSampleData(basicGrid);
        getContent().add(layoutRow);
        layoutRow.add(menuBar);
        getContent().add(layoutColumn2);
        layoutColumn2.add(basicGrid);
    }

    private void setMenuBarSampleData(MenuBar menuBar) {
        menuBar.addItem("View");
        menuBar.addItem("Edit");
        menuBar.addItem("Share");
        menuBar.addItem("Move");
    }

    private void setGridSampleData(Grid grid) {
        grid.setItems(query -> samplePersonService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    @Autowired()
    private SamplePersonService samplePersonService;
}
