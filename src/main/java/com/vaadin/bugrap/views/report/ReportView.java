package com.vaadin.bugrap.views.report;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;

import com.vaadin.bugrap.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "report", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Report")
@CssImport("styles/views/report/report-view.css")
@CssImport("styles/views/report/priority-column-renderer.css")
public class ReportView extends VerticalLayout {

    private ReportDataFacade reportDataFacade;
    private PropertiesEditorComponent propertiesEditorComponent;

    private Select<Project> projectsSelect;
    private Select<ProjectVersion> versionSelect;

    public ReportView() {
        setId("report-view");
        setSizeFull();
        //
        reportDataFacade = new ReportDataFacade();
        //
        add( createProjectsContainer() );
        add( createActionAndSearchBar() );
        add( createVersionAndDistributionContainer() );
        add( createAssigneeAndStatusContainer() );
        add( createReportGridContainer() );
    }

    private Component createProjectsContainer() {
        HorizontalLayout projectComboContainer = new HorizontalLayout();
        projectComboContainer.addAndExpand( createProjectsSelect() );
        projectComboContainer.setWidth("50%");
        return projectComboContainer;
    }

    private Select<Project> createProjectsSelect() {
        Project[] data = reportDataFacade.getProjects()
                .stream().sorted()
                .collect(Collectors.toList())
                .toArray( new Project[0] );
        projectsSelect = new Select<>( data );
        if(data.length > 0)
            projectsSelect.setValue( data[0] );
        projectsSelect.addValueChangeListener(event -> {
            createVersionSelect();
            createReportGrid();
        });
        return projectsSelect;
    }

    private Component createActionAndSearchBar() {
        Component actionButtons = createActionButtonContainer();
        Component searchBar = createSearchReportContainer();
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(actionButtons, searchBar);
        layout.setWidthFull();
        layout.setJustifyContentMode( JustifyContentMode.BETWEEN );
        layout.getElement().getStyle().set("border-bottom", "1px solid #E9E9E9");
        return layout;
    }

    private Component createActionButtonContainer() {
        Button reportBugButton = new Button("Report a bug", VaadinIcon.BUG.create() );
        Button requestFeatureButton = new Button("Request a feature", VaadinIcon.LIGHTBULB.create() );
        Button manageProjectButton = new Button("Manage Project", VaadinIcon.COG.create() );
        HorizontalLayout buttonsLayout = new HorizontalLayout(reportBugButton, requestFeatureButton, manageProjectButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setWidthFull();
        return buttonsLayout;
    }

    private Component createSearchReportContainer() {
        TextField searchField = new TextField("", "Search reports...");
        searchField.setWidthFull();
        searchField.getElement().getStyle().set("margin-left", "4px");
        searchField.getElement().getStyle().set("margin-right", "4px");
        Icon searchIcon = VaadinIcon.SEARCH.create();
        searchIcon.getStyle().set("margin-left", "20px");
        HorizontalLayout layout = new HorizontalLayout(searchIcon, searchField);
        layout.setJustifyContentMode( JustifyContentMode.AROUND );
        layout.getElement().getStyle().set("border", "1px solid #E9E9E9");
        layout.getElement().getStyle().set("border-radius", "10px");
        layout.setAlignItems( Alignment.CENTER );
        layout.setSpacing(true);
        layout.setWidth(40, Unit.PERCENTAGE);
        layout.setHeight(40, Unit.PIXELS);
        return layout;
    }

    private Component createVersionAndDistributionContainer() {
        Component versionSelect = createVersionSelect();
        Component distributionBar = createReportsDistributionBar();
        HorizontalLayout layout = new HorizontalLayout(versionSelect, distributionBar);
        layout.setFlexGrow(1, distributionBar);
        layout.setAlignItems( Alignment.END );
        layout.setWidthFull();
        return layout;
    }

    private Component createReportsDistributionBar() {
        // TODO: get report distributions from data
        DistributionBarComponent distributionBar = new DistributionBarComponent();
        distributionBar.setBars (5, 15, 180, 36, 350);
        distributionBar.getElement().getStyle().set("height","36px");
        distributionBar.getElement().getStyle().set("margin-bottom","4px");
        add( distributionBar );
        return distributionBar;
    }

    private Component createVersionSelect() {
        // TODO: use data-providers for future data refreshes
        versionSelect = new Select<>();
        versionSelect.setLabel("Reports for");
        versionSelect.getStyle().set("display", "flex");
        if (projectsSelect.getValue() != null) {
            Set<ProjectVersion> versions = reportDataFacade
                    .getProjectVersions(projectsSelect.getValue());
            if (versions.size() > 1) {
                ProjectVersion allVersions = new ProjectVersion();
                allVersions.setVersion("All Versions");
                versions.add(allVersions);
            }
            List<ProjectVersion> sortedVersions = versions.stream().sorted()
                                                .collect(Collectors.toList());
            versionSelect.setItems(sortedVersions.toArray(new ProjectVersion[0]));
            if (sortedVersions.size() > 0)
                versionSelect.setValue(sortedVersions.get(0));
        }
        return versionSelect;
    }

    private Component createAssigneeAndStatusContainer() {
        RadioButtonGroup<String> assignees = new RadioButtonGroup<>();
        assignees.setLabel("Assignees");
        assignees.setItems("Only me", "Everyone");
        assignees.setValue("Only me");

        RadioButtonGroup<String> statuses = new RadioButtonGroup<>();
        statuses.setLabel("Status");
        statuses.setItems("Open", "All Kinds", "Custom >");
        statuses.setValue("Open");

        Button customOptions = new Button("Options...");
        customOptions.setEnabled(false);

        statuses.addValueChangeListener( event -> customOptions.setEnabled( "Custom >".equals(event.getValue()) ) );

        CheckboxGroup<String> optionsCheckbox = new CheckboxGroup<>();
        optionsCheckbox.setItems("Open", "Fixed", "Invalid", "Won't fix", "Can't fix", "Duplicate", "Works for me", "Needs more information");
        optionsCheckbox.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        ContextMenu optionsPopup = new ContextMenu(customOptions);
        optionsPopup.setOpenOnClick(true);
        optionsPopup.add(optionsCheckbox);

        HorizontalLayout layout = new HorizontalLayout(assignees, statuses, customOptions);
        layout.setAlignItems(Alignment.END);
        return layout;
    }

    private Component createReportGridContainer() {
        ReportGridComponent reportGrid = createReportGrid();
        Component propertiesView = createPropertiesView();

        SplitLayout splitLayout = new SplitLayout(reportGrid, propertiesView);
        splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        splitLayout.setWidthFull();
        splitLayout.setHeight(75, Unit.PERCENTAGE);
        splitLayout.setSplitterPosition(100);

        reportGrid.addSelectionListener(event -> {
            splitLayout.setSplitterPosition(40);
            event.getFirstSelectedItem().ifPresent(report ->
                    propertiesEditorComponent.setReportToPresent(report));
        });
        return splitLayout;
    }

    private ReportGridComponent createReportGrid() {
        ReportGridComponent reportGrid = new ReportGridComponent();
        ProjectVersion queryVersion = null;
        if(versionSelect.getValue() != null && !"All Versions".equalsIgnoreCase(versionSelect.getValue().getVersion()))
            queryVersion = versionSelect.getValue();
        reportGrid.setItems(reportDataFacade.getReportsOrderedByPriority(projectsSelect.getValue(), queryVersion, null, null));
        return reportGrid;
    }

    private Component createPropertiesView() {
        propertiesEditorComponent = new PropertiesEditorComponent(reportDataFacade);
        return propertiesEditorComponent;
    }

    
}
