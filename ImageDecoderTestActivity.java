/**
 * 
 *
 * @author Rodrigue Saoungoumi <saoungoumi@gmail.com>
 *
 * Copyright (c) 2016 SAOUNGOUMI SOURPELE Rodrigue
 *
 * ALL RIGHTS RESERVED.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name Rodrigue Saoungoumi nor the names of any contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MICHAEL ROLAND BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.example.saoungoumi.lecteurjp2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import com.example.saoungoumi.lecteurjp2.ImageDecoder;
import com.example.saoungoumi.lecteurjp2.BitmapImage;
import com.example.saoungoumi.lecteurjp2.R;
import utils.ArrayUtils;
import java.util.Arrays;

public class ImageDecoderTestActivity extends Activity {
	ImageDecoder mImageDecoder = null;
	ViewGroup mMainView = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("ImageDecoderTestActivity dans onCreate avant setContentView()");
		setContentView(R.layout.activity_main);
		
		System.out.println("ImageDecoderTestActivity dans onCreate après setContentView()");
		mMainView = (ViewGroup) this.findViewById(R.id.main);
		System.out.println("ImageDecoderTestActivity dans onCreate avant l'appel de ImageDecoderTestApplication.getImageDecoderInstance()");
		mImageDecoder = ImageDecoderTestApplication.getImageDecoderInstance();
	}

	@Override
	public void onResume() {
		super.onResume();

		mImageDecoder = ImageDecoderTestApplication.getImageDecoderInstance();
		mMainView.removeAllViews();

		if (ImageDecoderTestApplication.isImageDecoderSupported()) {
			Thread worker = new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(100);
					} catch (Exception e) {
					}

					ImageDecoderTestActivity.this.runOnUiThread(new Runnable() {
						@SuppressLint("NewApi")
						public void run() {
							int[] resRawIds = { R.raw.debugimg1,
									R.raw.debugimg2, R.raw.debugimg3, };

							for (int i = 0; i < resRawIds.length; ++i) {
								ArrayList<byte[]> chunks = new ArrayList<byte[]>();

								InputStream is = ImageDecoderTestActivity.this
										.getResources().openRawResource(
												resRawIds[i]);

								if (is != null) {
									int readBytes;

									do {
										byte[] chunk = new byte[1024];
										try {
											readBytes = is.read(chunk);
										} catch (Exception e) {
											readBytes = -1;
										}
										if (readBytes > 0) {
											chunks.add(Arrays.copyOf(chunk,
													readBytes));
										}
									} while (readBytes >= 0);
								}

								byte[] fileData = ArrayUtils
										.toByteArray(chunks);

								BitmapImage bm = null;
								if (mImageDecoder != null) {
									bm = mImageDecoder.decodeImage(fileData);
								} else {
									View view = LayoutInflater.from(
											ImageDecoderTestActivity.this)
											.inflate(R.layout.record, null);
									view.setBackgroundResource(R.drawable.record_area_error);
									TextView title = (TextView) view
											.findViewById(R.id.record_caption);
									title.setVisibility(View.VISIBLE);
									title.setText("ImageDecoderService handle not ready");
									mMainView.addView(view);
								}

								Bitmap bmimg = null;
								if (bm != null) {
									bmimg = bm.getImage();
								} else {
									View view = LayoutInflater.from(
											ImageDecoderTestActivity.this)
											.inflate(R.layout.record, null);
									view.setBackgroundResource(R.drawable.record_area_error);
									TextView title = (TextView) view
											.findViewById(R.id.record_caption);
									title.setVisibility(View.VISIBLE);
									title.setText("ImageDecoderService did not return a BitmapImage object");
									mMainView.addView(view);
								}

								if (bmimg != null) {
									View view = LayoutInflater.from(
											ImageDecoderTestActivity.this)
											.inflate(R.layout.item_image, null);
									ImageView img = (ImageView) view
											.findViewById(R.id.image);
									img.setImageBitmap(bmimg);
									mMainView.addView(view);
								} else {
									if (ImageDecoderTestApplication
											.isImageDecoderSupported()) {
										View view = LayoutInflater.from(
												ImageDecoderTestActivity.this)
												.inflate(R.layout.record, null);
										view.setBackgroundResource(R.drawable.record_area_error);
										TextView title = (TextView) view
												.findViewById(R.id.record_caption);
										title.setVisibility(View.VISIBLE);
										title.setText("Failed to decode");
										TextView value = (TextView) view
												.findViewById(R.id.record_text1);
										value.setVisibility(View.VISIBLE);
										value.setText("ImageDecoderService is available");
										mMainView.addView(view);
									} else {
										View view = LayoutInflater.from(
												ImageDecoderTestActivity.this)
												.inflate(R.layout.record, null);
										TextView title = (TextView) view
												.findViewById(R.id.record_caption);
										title.setVisibility(View.VISIBLE);
										title.setText("Failed to decode");
										TextView value1 = (TextView) view
												.findViewById(R.id.record_text1);
										value1.setVisibility(View.VISIBLE);
										value1.setText("ImageDecoderService not available");
										mMainView.addView(view);
									}
								}
							}
						}
					});
				}
			};

			worker.start();
		} else {
			View view = LayoutInflater.from(ImageDecoderTestActivity.this)
					.inflate(R.layout.record, null);
			TextView title = (TextView) view.findViewById(R.id.record_caption);
			title.setVisibility(View.VISIBLE);
			title.setText("ImageDecoderService not available, click to download from Android Market.");
			view.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					try {
						ImageDecoderTestActivity.this
								.startActivity(ImageDecoderTestApplication
										.getImageDecoderInstallIntent());
					} catch (Exception e) {
					}
				}
			});
			mMainView.addView(view);
		}
	}
}
