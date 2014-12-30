package org.vocobox.apps.wav2synth;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.vocobox.apps.VocoboxControllerAbstract;
import org.vocobox.io.datasets.HumanVoiceDataset;
import org.vocobox.model.synth.MonitorSettings;
import org.vocobox.model.voice.analysis.VocoParseFileAndFFT;
import org.vocobox.synth.jsyn.circuits.JsynCircuitSynth;
import org.vocobox.synth.jsyn.dual.AbstractOcclusiveSynth;
import org.vocobox.synth.jsyn.dual.JsynOcclusiveNoiseSynth;
import org.vocobox.ui.charts.synth.SynthMonitorCharts;
import org.vocobox.ui.toolkit.transport.TransportPanel;
import org.vocobox.ui.toolkit.transport.TransportPanel.On;
import org.vocobox.voice.pitch.tarsos.VoiceFilePlay;

/**
 * Control synth while playing the input wav file. 
 * 
 * Synth controls are scheduled by file reading so might appear inapproriate.
 * @see VocoboxControllerFileRead that read and schedule events more smartly.
 * 
 * @author Martin Pernollet
 *
 */
public class VocoboxControllerFilePlay extends VocoboxControllerAbstract{
    public static void main(String[] args) throws Exception, UnsupportedAudioFileException {
        VocoboxControllerFilePlay controller = new VocoboxControllerFilePlay();
        String file = HumanVoiceDataset.NOTES.getNoteFilename("C3");
        file = "data/sound/doremi-mono.wav";
        controller.play(file);
    }

    public static int LENGTH = 10;
    
    protected VocoboxAppFile app;
    protected VocoboxPanelsFile panels;
    protected SynthMonitorCharts monitor;

    protected VoiceFilePlay voice;
    protected String vocoFilename;
    
    protected VocoParseFileAndFFT pitchAnalysis;
    protected float muteWinTime = -0.5f;
    protected double sourceGain = 0.0;

    /*public VocoboxControllerFile() {
        wireSynth();
        wireVoice();
        wireUI();
    }*/
    
    @Override
    public void wireUI() {
        this.monitorSettings = new MonitorSettings();
        monitorSettings.applyPalette = true;
        monitorSettings.timeMax = 20;
        //monitorSettings.
        this.app = new VocoboxAppFile();
    }

    @Override
    public void wireVoice() {
        this.voice = new VoiceFilePlay(synth);
    }

    @Override
    public void wireSynth() {
        this.synth = new JsynCircuitSynth();//makeOcclusiveSynth();
    }
    

    public void play(String file) throws Exception {
        loadFile(file);
        play();
    }
    
    /** Play song with synthetizer and source file at the same time */
    public void play() throws Exception {
        this.voice.play(new File(vocoFilename), 0, sourceGain);
    }

    public void stop() {
        this.synth.off();
    }
    
    public void loadFile(String waveFile){
        vocoFilename = waveFile;
        try {
            loadUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    public void loadUI() throws IOException {
        panels = new VocoboxPanelsFile(synth, pitchAnalysis, LENGTH, monitorSettings);
        panels.setInputControl(new TransportPanel(new On() {
            @Override
            public void play() {
                try {
                    VocoboxControllerFilePlay.this.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void stop() {
                VocoboxControllerFilePlay.this.stop();
                panels.getSynthMonitors().clear();
            }
        }));
        app.layout(panels);
        monitor = panels.getSynthMonitors();
        panels.getSynthMonitors().pitchCursorAnnotation.setWidth(muteWinTime);
    }
    
    public AbstractOcclusiveSynth makeOcclusiveSynth() {
        AbstractOcclusiveSynth synth = new JsynOcclusiveNoiseSynth();
        synth.setConfidenceTimeWindow(20);
        synth.setOctaveOffsetDetectionRatio(1);
        return synth;
    }
    
}
