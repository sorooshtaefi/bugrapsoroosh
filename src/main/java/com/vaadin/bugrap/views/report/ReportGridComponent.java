package com.vaadin.bugrap.views.report;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.vaadin.bugrap.domain.entities.Report;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.selection.SelectionListener;

public class ReportGridComponent extends VerticalLayout {

    private static final String PRIORITY_LABEL = "PRIORITY";
    private static final String TYPE_LABEL = "TYPE";
    private static final String SUMMARY_LABEL = "SUMMARY";
    private static final String ASSIGNED_LABEL = "ASSIGNED";
    private static final String LAST_MODIFIED_LABEL = "LAST MODIFIED";
    private static final String REPORTED_LABEL = "REPORTED";

    private static final String PRIORITY_STYLE = "<div class-name=\"[[item.priority]]\"></div>";

    private Grid<Report> grid;

    private Set<SelectionListener<Grid<Report>, Report>> selectionListeners;

    public ReportGridComponent() {
        selectionListeners = new HashSet<>();
        initUI();
    }

    private void initUI() {
        grid = new Grid<>(Report.class, false);
        grid.addColumn(TemplateRenderer.<Report>of(PRIORITY_STYLE)
                .withProperty("priority", report -> "n"+(report.getPriority().ordinal()+1)))
                .setHeader(PRIORITY_LABEL).setSortable(true).setWidth("90px");
        grid.addColumn(Report::getType).setHeader(TYPE_LABEL).setWidth("70px");
        grid.addColumn(Report::getSummary).setHeader(SUMMARY_LABEL).setWidth("400px");
        grid.addColumn(Report::getAssigned).setHeader(ASSIGNED_LABEL).setWidth("150px");
        grid.addColumn(Report::getTimestamp).setHeader(LAST_MODIFIED_LABEL).setWidth("100px");
        grid.addColumn(Report::getReportedTimestamp).setHeader(REPORTED_LABEL).setWidth("100px");
        grid.setSizeFull();

        grid.addSelectionListener(event -> selectionListeners.forEach(sl -> sl.selectionChange(event)));

        add(grid);
        setSizeFull();
    }

    public void setItems(List<Report> items) {
        grid.setItems(items);
    }

    public void addSelectionListener(SelectionListener<Grid<Report>, Report> listener) {
        this.selectionListeners.add(listener);
    }

    public boolean removeSelectionListener(SelectionListener<Grid<Report>, Report> listener) {
        return selectionListeners.remove(listener);
    }
}
