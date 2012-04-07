package com.dreamboxx.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.MediaElement;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.RootPanel;

public class SoundManager {

	final static String AUDIO_FORMAT_MP3 = "audio/mpeg";
	final static String AUDIO_FORMAT_OGG = "audio/ogg";
	final static String AUDIO_FORMAT_WAV = "audio/wav";

	private static String MP3_SUFFIX = ".mp3";
	private static String OGG_SUFFIX = ".ogg";
	private static String WAV_SUFFIX = ".wav";


	
	Audio audio;
	private String canPlayMp3;
	private String canPlayOgg;
	private String canPlayWav;

	public SoundManager() {
		super();

		audio = Audio.createIfSupported();

		if (audio == null) {
			System.out.println("AUDIO IS NOT SUPPORTED" );
			return; // don't continue if not supported
		}

		canPlayMp3 = audio.canPlayType(AUDIO_FORMAT_MP3);
		canPlayOgg = audio.canPlayType(AUDIO_FORMAT_OGG);
		canPlayWav = audio.canPlayType(AUDIO_FORMAT_WAV);

	}

	public Audio initSound(String audioFilename) { 


		Audio audio = Audio.createIfSupported();
		//TODO: error handling if not supported

		if (canPlayMp3.equals(MediaElement.CAN_PLAY_PROBABLY)) {

			audio.setSrc(audioFilename + MP3_SUFFIX);
		} else if (canPlayOgg.equals(MediaElement.CAN_PLAY_PROBABLY)) {
			audio.setSrc(audioFilename + OGG_SUFFIX);
		} else if (canPlayMp3.equals(MediaElement.CAN_PLAY_MAYBE)) {
			audio.setSrc(audioFilename + MP3_SUFFIX);
		} else if (canPlayOgg.equals(MediaElement.CAN_PLAY_MAYBE)) {
			audio.setSrc(audioFilename + OGG_SUFFIX);
		} else {
			//TODO: handle case that no audio format is present
			Log.warn("No suitable audio format", new Exception("Could not find suitable audio format to play " + audioFilename));
		}

		audio.load();
		RootPanel.get().add(audio);
		Log.debug("audio source = " + audio.getSrc() + " added");

		return audio;
	}

	public void play(Audio audio) {
		Log.trace("playing sound " + audio.getSrc());
		audio.play();
		//TODO: remove this hack. Is required since GWT plays a sound only once
		//audio.setSrc(audio.getSrc());
 	}
	
	// old code for using GWT Voices
		//	SoundController soundController = new SoundController();
		//	Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_UNKNOWN,
		//			"sounds/plop.wav");
		//	sound.setVolume(20);
		//	sound.play();

}
