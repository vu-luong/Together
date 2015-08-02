package com.together.model.mediator;

import java.io.File;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import android.util.Log;

import com.together.constants.Constants;
import com.together.model.Audio;
import com.together.model.webdata.AudioSvc;

public class AudioMediator {
	private AudioSvc audioSvc = new RestAdapter.Builder()
									.setEndpoint(Constants.AUDIO_ENDPOINT)
									.build()
									.create(AudioSvc.class);
	
	
	public Response uploadAudio(File file) {
		Log.i("TAG", "den day roi");
		return audioSvc.uploadFile(new TypedFile("audios/amr", file));
	}
									
}
