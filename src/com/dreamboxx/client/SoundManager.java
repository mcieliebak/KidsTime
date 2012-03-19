package com.dreamboxx.client;

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


	// old code for using GWT Voices
	//	SoundController soundController = new SoundController();
	//	Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_UNKNOWN,
	//			"sounds/plop.wav");
	//	sound.setVolume(20);
	//	sound.play();

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
			//throw new Exception("Could not find suitable audio format");
		}

		audio.load();
		RootPanel.get().add(audio);
		System.out.println("audio source = " + audio.getSrc() + "added");

		return audio;
	}

	public void play(Audio audio) {
		System.out.println("playing sound " + audio.getSrc());
		audio.play();
		//TODO: remove this hack. Is required since GWT plays a sound only once
		//audio.setSrc(audio.getSrc());
 	}
	
//	public void playAudio2() {
//		System.out.println("play audio2");
//		System.out.println("audio2 = " + audio2);
//		System.out.println("time = " + audio2.getCurrentTime());
//		System.out.println("ready state = " + audio2.getReadyState());
//		System.out.println("audio 2 lenght = " + audio2.getDuration());
//		audio2.pause();
//		audio2.setCurrentTime((double) 0.0);
//		System.out.println("audio2 time = " + audio2.getCurrentTime());
//		audio2.play();
//
//		//audio2 = loadSound(SOUND_TIME_SELECTION_CLICK);
//		//audio2.load();
//
//	}
}
