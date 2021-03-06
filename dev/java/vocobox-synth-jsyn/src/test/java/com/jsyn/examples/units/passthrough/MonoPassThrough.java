package com.jsyn.examples.units.passthrough;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.devices.AudioDeviceManager;
import com.jsyn.unitgen.ChannelIn;
import com.jsyn.unitgen.ChannelOut;

/**
 * Pass audio input to audio output.
 * @author  Phil Burk (C) 2010 Mobileer Inc
 *
 */
public class MonoPassThrough
{
	Synthesizer synth;
	ChannelIn channelIn;
	ChannelOut channelOut;

	private void test()
	{
		// Create a context for the synthesizer.
		synth = JSyn.createSynthesizer();
		synth.add( channelIn = new ChannelIn() );
		synth.add( channelOut = new ChannelOut() );
		// Connect the input to the output.
		channelIn.output.connect( channelOut.input );
		
		// Both stereo.
		int numInputChannels = 2;
		int numOutputChannels = 2;
		synth.start( 48000, AudioDeviceManager.USE_DEFAULT_DEVICE, numInputChannels, AudioDeviceManager.USE_DEFAULT_DEVICE,
				numOutputChannels );
		
		// We only need to start the ChannelOut. It will pull data from the ChannelIn.
		channelOut.start();
		// Sleep a while.
		try
		{
			double time = synth.getCurrentTime();
			// Sleep for a few seconds.
			synth.sleepUntil( time + 4.0 );
		} catch( InterruptedException e )
		{
			e.printStackTrace();
		}
		// Stop everything.
		synth.stop();
	}

	public static void main( String[] args )
	{
		new MonoPassThrough().test();
	}
}
