package com.vaadin.bugrap.views.report;

import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.TemplateRenderer;

@CssImport("styles/views/report/priority-column-renderer.css")
public class PropertiesEditorComponent extends VerticalLayout {

    private ComboBox<Report.Priority> priorityComboBox;
    private Select<Report.Type> typeSelect;
    private Select<Report.Status> statusSelect;
    private Select<Reporter> assignedToSelect;
    private Select<ProjectVersion> versionSelect;
    private Button updateButton;
    private Button revertButton;
    private Button openInNewWindow;
    private H3 summaryLabel;
    private TextArea descriptionTextArea;

    private Report reportToPresent;

    private ReportDataFacade reportDataFacade;

    public PropertiesEditorComponent(ReportDataFacade reportDataFacade) {
        this.reportDataFacade = reportDataFacade;
        initUI();
    }

    private void initUI() {
        initPriorityComboBox();
        initTypeSelect();
        initStatusSelect();
        initAssignedTo();
        initVersionSelect();
        initUpdateButton();
        initRevertButton();
        initOpenInNewWindowButton();
        //
        summaryLabel = new H3();
        HorizontalLayout summary = new HorizontalLayout(openInNewWindow, summaryLabel);
        summary.setAlignItems(Alignment.END);
        summary.setWidthFull();
        add(summary);
        //
        HorizontalLayout controls = new HorizontalLayout(
                priorityComboBox, typeSelect, statusSelect, assignedToSelect,
                versionSelect, updateButton, revertButton
        );
        controls.setAlignItems(Alignment.END);
        controls.setWidthFull();
        add(controls);
        //
        descriptionTextArea = new TextArea();
        descriptionTextArea.setSizeFull();
        add(descriptionTextArea);
    }

    private void initPriorityComboBox() {
        priorityComboBox = new ComboBox<>("PRIORITY");
        priorityComboBox.setItems(Report.Priority.values());
        /*priorityComboBox.setItemLabelGenerator(priority -> "n"+(priority.ordinal()+1));
        priorityComboBox.setRenderer(TemplateRenderer.<Report.Priority>of(
                "<div class-name=\"[[item.s]]\" style=\"display: block; height: 25px; border: 1px solid\"></div>")
                .withProperty("s", priority -> "n"+(priority.ordinal()+1))
        );*/
    }

    private void initTypeSelect() {
        typeSelect = new Select<>();
        typeSelect.setLabel("TYPE");
        typeSelect.setItems(Report.Type.values());
    }

    private void initStatusSelect() {
        statusSelect = new Select<>();
        statusSelect.setLabel("STATUS");
        statusSelect.setItems(Report.Status.values());
    }

    private void initAssignedTo() {
        assignedToSelect = new Select<>();
        assignedToSelect.setLabel("ASSIGNED TO");
        assignedToSelect.setItems(reportDataFacade.getReporters());
    }

    private void initVersionSelect() {
        versionSelect = new Select<>();
        versionSelect.setLabel("VERSION");
    }

    public Report getReportToPresent() {
        return reportToPresent;
    }

    public void setReportToPresent(Report reportToPresent) {
        this.reportToPresent = reportToPresent;
        versionSelect.setItems(reportDataFacade.getVersionsByProject(reportToPresent.getProject()));
        refreshValues();
    }

    private void refreshValues() {
        priorityComboBox.setValue(reportToPresent.getPriority());
        typeSelect.setValue(reportToPresent.getType());
        statusSelect.setValue(reportToPresent.getStatus());
        assignedToSelect.setValue(reportToPresent.getAssigned());
        versionSelect.setValue(reportToPresent.getVersion());
        summaryLabel.setText(reportToPresent.getSummary());
        descriptionTextArea.setValue(reportToPresent.getDescription());
    }

    private void initRevertButton() {
        updateButton = new Button("Update");
    }

    private void initUpdateButton() {
        revertButton = new Button("Revert");
    }

    private void initOpenInNewWindowButton() {
        openInNewWindow = new Button(VaadinIcon.EXTERNAL_BROWSER.create());
    }
}
