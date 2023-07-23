package com.brutus.abio.views.video;

import com.brutus.abio.data.entity.Video;
import com.brutus.abio.data.service.VideoService;
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

@PageTitle("Video")
@Route(value = "video/:videoID?/:action?(edit)", layout = MainLayout.class)
public class VideoView extends Div implements BeforeEnterObserver {

    private final String VIDEO_ID = "videoID";
    private final String VIDEO_EDIT_ROUTE_TEMPLATE = "video/%s/edit";

    private final Grid<Video> grid = new Grid<>(Video.class, false);

    private TextField title_en;
    private TextField title_ru;
    private TextField title_am;
    private TextField description_en;
    private TextField description_ru;
    private TextField description_am;
    private TextField url;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Video> binder;

    private Video video;

    private final VideoService videoService;

    public VideoView(VideoService videoService) {
        this.videoService = videoService;
        addClassNames("video-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("title_en").setAutoWidth(true);
        grid.addColumn("title_ru").setAutoWidth(true);
        grid.addColumn("title_am").setAutoWidth(true);
        grid.addColumn("description_en").setAutoWidth(true);
        grid.addColumn("description_ru").setAutoWidth(true);
        grid.addColumn("description_am").setAutoWidth(true);
        grid.addColumn("url").setAutoWidth(true);
        grid.setItems(query -> videoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(VIDEO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(VideoView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Video.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.video == null) {
                    this.video = new Video();
                }
                binder.writeBean(this.video);
                videoService.update(this.video);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(VideoView.class);
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
        Optional<Long> videoId = event.getRouteParameters().get(VIDEO_ID).map(Long::parseLong);
        if (videoId.isPresent()) {
            Optional<Video> videoFromBackend = videoService.get(videoId.get());
            if (videoFromBackend.isPresent()) {
                populateForm(videoFromBackend.get());
            } else {
                Notification.show(String.format("The requested video was not found, ID = %s", videoId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(VideoView.class);
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
        title_en = new TextField("Title_en");
        title_ru = new TextField("Title_ru");
        title_am = new TextField("Title_am");
        description_en = new TextField("Description_en");
        description_ru = new TextField("Description_ru");
        description_am = new TextField("Description_am");
        url = new TextField("Url");
        formLayout.add(title_en, title_ru, title_am, description_en, description_ru, description_am, url);

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

    private void populateForm(Video value) {
        this.video = value;
        binder.readBean(this.video);

    }
}
