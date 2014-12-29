package org.vocobox.apps.benchmark;

import java.io.File;
import java.io.IOException;

import org.jzy3d.chart.Chart;
import org.jzy3d.ui.MultiChartPanel;
import org.vocobox.apps.benchmark.charts.NoteMozaic;
import org.vocobox.io.datasets.HumanVoiceDataset;

public class BenchmarkHumanVoiceNotesPitchEvaluation {
    public static void main(String[] args) throws Exception {
        HumanVoiceDataset voice = HumanVoiceDataset.NOTES;
        
        System.out.println(new File(".").getAbsolutePath());
        Chart[][] charts = NoteMozaic.evalChartsPitch(voice.getNoteMatrix(), 0, 0.5f);
        ui(voice.getNoteHeaders(), charts);
    }

    public static void ui(String[] headers, Chart[][] charts) throws IOException {
        MultiChartPanel monitorPanel = new MultiChartPanel(charts, headers, null, false, 100, 100, false, false);
        monitorPanel.ui();
    }
}
