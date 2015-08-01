package com.together.model.webdata;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

import com.together.model.Audio;

public interface AudioSvc {
	
	public static final String UPLOAD_PATH = "/audios";
	public static final String DATA_PARAMETER = "data";
	public static final String VIDEO_DATA_PATH = "/audios/{id}/data";
	public static final String ID_PARAMETER = "id";
	
	
	@Multipart
	@POST(UPLOAD_PATH)
	public Audio uploadFile(@Part(DATA_PARAMETER) TypedFile data);
	
	@Streaming
    @GET(VIDEO_DATA_PATH)
    Response getData(@Path(ID_PARAMETER) int id);

	
}
