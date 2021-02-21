package com.newha.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

@Service
public class S3Service {
	@Autowired
	private AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	// S3에 파일을 업로드한다.
	public void uploadOnS3(String fileName, File file) {

		TransferManager transferManager = new TransferManager(this.amazonS3);
		PutObjectRequest request = new PutObjectRequest(bucket, fileName, file);
		Upload upload = transferManager.upload(request);

		/* 업로드 progress가 변할 때마다 발생하는 Event Listener */
		request.setGeneralProgressListener(new ProgressListener() {
			@Override
			public void progressChanged(ProgressEvent progressEvent) {
				double progress = progressEvent.getBytes() != 0
						? (double) progressEvent.getBytesTransferred() / (double) progressEvent.getBytes()
								* (double) 100
						: 0.0;
			}
		});

		/* 아래 내용을 활용하면, 파일 업로드가 성공할 때까지 block을 걸 수 있다. */
		try {

			upload.waitForCompletion();
		} catch (AmazonClientException amazonClientException) {

			amazonClientException.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();

		}
	}
}
