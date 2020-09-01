package com.vaadin.bugrap.views.report;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.vaadin.bugrap.domain.entities.Report;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class DistributionBarComponent extends HorizontalLayout {

    private static final String[] COLORS = {"0162A2", "016EB7", "017ACB", "0186DF", "0193F4", "0B9DFE",
                                            "20A5EF", "34ADFE", "48B5FE", "5DBEFE", "71C6FE", "85CEFE",
                                            "9AD6FE", "AEDEFF", "C2E6FF", "6DEFFF", "EBF7FF", "FFFFFF"};
    private List<Div> partitions;
    private int[] bars;
    private Collection<DistributionBarData<Report.Status>> data;

    public DistributionBarComponent () {
    }

    public DistributionBarComponent (int... bars) {
        this.bars = bars;
        initUI();
    }

    public DistributionBarComponent (Collection<DistributionBarData<Report.Status>> data) {
        this.data = data;
        initUI(data);
    }

    private void initUI (Collection<DistributionBarData<Report.Status>> data) {

    }

    private void initUI () {
        int[] counter = {0};
        int step = COLORS.length / bars.length;
        partitions = Arrays.stream(bars).sorted().boxed().map(i -> {
            Div div = new Div();
            div.setText(i.toString());
            setFlexGrow(i, div);
            div.getElement().getStyle().set("background-color", "#"+COLORS[counter[0]]);
            div.getElement().getStyle().set("min-width", "30px");
            div.getElement().getStyle().set("padding-left", "5px");
            div.getElement().getStyle().set("color", counter[0] > COLORS.length / 2 ? "#01568E" : "#FFFFFF");
            counter[0] += step;
            counter[0] = counter[0] < COLORS.length ? counter[0] : COLORS.length - 1;
            return div;
        }).collect(Collectors.toList());

        partitions.get(0).getElement().getStyle().set("border-radius", "0.25em 0em 0em 0.25em");
        partitions.get(bars.length-1).getElement().getStyle().set("border-radius", "0em 0.25em 0.25em 0em");

        setWidthFull();
        setSpacing(false);
        add(partitions.toArray(new Div[0]));
    }

    public void setBars (int... bars) {
        this.bars = bars;
        initUI();
    }

    public int[] getBars() {
        return bars;
    }

    public Collection<DistributionBarData<Report.Status>> getData() {
        return data;
    }

    public void setData(Collection<DistributionBarData<Report.Status>> data) {
        this.data = data;
        initUI(data);
    }
}


