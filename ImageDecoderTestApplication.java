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

import cm.ssr.DecodeurJpeg2000.ImageDecoder;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

/**
 * Application class to hold global application state.
 */
public class ImageDecoderTestApplication extends Application {
  private static Context sApplication = null;
	@Override
	public void onCreate()
	{
		super.onCreate();
		  System.out.println("ImageDecoderTestApplication dans onCreate avant ImageDecoderTestApplication.getImageDecoderInstance()");
    sApplication = this;
    ImageDecoderTestApplication.getImageDecoderInstance();
	}

  public static String getStringRes(int resId) {
    if (sApplication != null) {
      return sApplication.getString(resId);
    } else {
      return "";
    }
  }

  public static Resources getResourcesInstance() {
    if (sApplication != null) {
      return sApplication.getResources();
    } else {
      return null;
    }
  }

  private static ImageDecoder sImageDecoder = null;
  
  public static ImageDecoder getImageDecoderInstance() {
    if (sImageDecoder == null) {
      if (sApplication != null) {
        sImageDecoder = new ImageDecoder(sApplication);
      }
    }
    
    if (sImageDecoder != null) {
      sImageDecoder.connect();
    }
    
    return sImageDecoder;
  }

  public static boolean isImageDecoderSupported() {
    if (sImageDecoder == null) {
      if (sApplication != null) {
        sImageDecoder = new ImageDecoder(sApplication);
      }
    }
    
    if (sImageDecoder != null) {
      return sImageDecoder.isSupported();
    }
    
    return false;
  }
  
  public static Intent getImageDecoderInstallIntent() {
    return new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=at.mroland.android.apps.imagedecoder"));
  }
}
