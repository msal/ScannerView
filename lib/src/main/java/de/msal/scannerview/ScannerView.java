/*
 * Copyright 2015 MSal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.msal.scannerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @since 17.11.15
 */
public class ScannerView extends RelativeLayout {

   private final Animation mSlideInAnimation;
   private final ImageView mScanner;
   private final BarcodeView mBarcode;

   public ScannerView(Context context) {
      this(context, null);
   }

   public ScannerView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public ScannerView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      LayoutInflater.from(context).inflate(R.layout.scannerview_layout, this, true);

      mSlideInAnimation = AnimationUtils.loadAnimation(context, R.anim.scan_animation);
      mScanner = (ImageView) findViewById(R.id.scanner_lib_scanner);
      mBarcode = (BarcodeView) findViewById(R.id.scanner_lib_barcode);

      TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScannerAnimation, 0, 0);
      try {
         // barcode color
         setBarcodeColor(a.getColor(R.styleable.ScannerAnimation_barcodeColor,
               ContextCompat.getColor(context, R.color.barcode_grey)));
         // scanner color
         setScannerColor(a.getColor(R.styleable.ScannerAnimation_scannerColor,
               ContextCompat.getColor(context, R.color.scanner_red)));
         // min barcode line width
         setMinBarcodeLineWidth(a.getDimension(R.styleable.ScannerAnimation_minBarcodeLineWidth,
               getResources().getDimension(R.dimen.minBarcodeLineWidth)));
         // max barcode line width
         setMaxBarcodeLineWidth(a.getDimension(R.styleable.ScannerAnimation_maxBarcodeLineWidth,
               getResources().getDimension(R.dimen.maxBarcodeLineWidth)));
         // horizontal barcode padding
         float paddingHorizontal = a.getDimension(R.styleable.ScannerAnimation_barcodePaddingHorizontal,
                 getResources().getDimension(R.dimen.barcodePadding));
         setBarcodePaddingHorizontal(paddingHorizontal);
         // vertical barcode padding
         float paddingVertical = a.getDimension(R.styleable.ScannerAnimation_barcodePaddingVertical,
                 getResources().getDimension(R.dimen.barcodePadding));
         setBarcodePaddingVertical(paddingVertical);
      } finally {
         a.recycle();
      }
   }

   /**
    * Start the scan animation.
    */
   public void scan() {
      mScanner.startAnimation(mSlideInAnimation);
   }

   /**
    * Set the color of the barcode lines.
    *
    * @param color the color of the barcode lines.
    */
   public void setBarcodeColor(int color) {
      mBarcode.setBarcodeColor(color);
   }

   /**
    * Set the color of the laser of the scanner.
    *
    * @param color the color of the laser.
    */
   public void setScannerColor(int color) {
      if (mScanner.getDrawable() instanceof ShapeDrawable) {
         ((ShapeDrawable) mScanner.getDrawable()).getPaint().setColor(color);
      } else if (mScanner.getDrawable() instanceof GradientDrawable) {
         ((GradientDrawable) mScanner.getDrawable()).setColor(color);
      }
   }

   /**
    * Set the width of the slimmest allowed barcode line.
    *
    * @param width the width in dp.
    */
   public void setMinBarcodeLineWidth(float width) {
      mBarcode.setMinLineWidth(width);
   }

   /**
    * Set the width of the widest allowed barcode line.
    *
    * @param width the width in dp.
    */
   public void setMaxBarcodeLineWidth(float width) {
      mBarcode.setMaxLineWidth(width);
   }

   /**
    * Sets the horizontal padding of the barcode.
    *
    * @param padding the top and bottom padding in dp
    */
   public void setBarcodePaddingHorizontal(float padding) {
      mBarcode.setPaddingHorizontal(padding);
   }

   /**
    * Sets the vertical padding of the barcode.
    *
    * @param padding the left and right padding in dp
    */
   public void setBarcodePaddingVertical(float padding) {
      mBarcode.setPaddingVertical(padding);
   }

}
