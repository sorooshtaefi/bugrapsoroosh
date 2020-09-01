package com.vaadin.bugrap.views.report;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

public class ReportDataFacade {
    private BugrapRepository repository;

    public ReportDataFacade() {
        this.repository = new BugrapRepository("/Users/soroosh/bugrap/test-data");
    }

    public Set<Reporter> getReporters() {
        return repository.findReporters();
    }

    public Set<ProjectVersion> getVersionsByProject(Project project) {
        return repository.findProjectVersions( project );
    }

    public List<Report> getReportsOrderedByPriority(Project project, ProjectVersion version, Set<Report.Status> statuses, Reporter reportAssignee) {
        BugrapRepository.ReportsQuery query = new BugrapRepository.ReportsQuery();
        if(project != null)
            query.project = project;
        if(version != null)
            query.projectVersion = version;
        if(statuses != null && !statuses.isEmpty())
            query.reportStatuses = statuses;
        if(reportAssignee != null)
            query.reportAssignee = reportAssignee;

        return repository.findReports( query )
                .stream()
                .sorted( Comparator.comparing( Report::getPriority ).reversed() )
                .collect( Collectors.toList() );
    }

    public Map<Report.Status,Long> getReportDistributionByStatus() {
        // TODO: This needs
        BugrapRepository.ReportsQuery query = new BugrapRepository.ReportsQuery();
        return repository.findReports( query )
                .stream()
                .collect( Collectors.groupingBy(Report::getStatus,
                                                Collectors.counting()));
    }

    public Set<Project> getProjects() {
        return repository.findProjects();
    }

    public Set<ProjectVersion> getProjectVersions(Project project){
        return repository.findProjectVersions( project );
    }
}
