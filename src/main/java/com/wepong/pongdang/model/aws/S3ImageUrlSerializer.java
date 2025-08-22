package com.wepong.pongdang.model.aws;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class S3ImageUrlSerializer extends JsonSerializer<String> {

	private static final String S3_BASE_URL = "https://bettopia-s3-bucket.s3.ap-northeast-2.amazonaws.com/";
	
	@Override
	public void serialize(String img_path, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (img_path == null || img_path.isBlank()) {
            gen.writeNull();
        } else {
            gen.writeString(S3_BASE_URL + img_path);
        }
		
	}
	
}
