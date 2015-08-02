package com.together.model.webdata;

import retrofit.client.Response;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

public interface AudioSvc {
	
	public static final String UPLOAD_PATH = "/hackacademy/Together/Server/recording.php";
	public static final String DATA_PARAMETER = "data";
	
	
	
	@Multipart
	@POST(UPLOAD_PATH)
	Response uploadFile(@Part(DATA_PARAMETER) TypedFile data);

	
}
