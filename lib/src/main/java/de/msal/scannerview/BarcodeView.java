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
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Random;

/**
 * @since 17.11.15
 */
class BarcodeView extends View {

   private final Paint mPaint;
   private int mMinLineWidth;
   private int mMaxLineWidth;
   private int mBarcodeColor;

   public BarcodeView(Context context) {
      this(context, null);
   }

   public BarcodeView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public BarcodeView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);

      mPaint = new Paint();

      Resources resources  = getResources();
      mMinLineWidth = (int) resources.getDimension(R.dimen.minBarcodeLineWidth);
      mMaxLineWidth = (int) resources.getDimension(R.dimen.maxBarcodeLineWidth);
      mBarcodeColor = Color.BLACK;
   }

   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      int defaultWidth = getPixels(50);
      int defaultHeight = getPixels(50);

      int widthMode = MeasureSpec.getMode(widthMeasureSpec);
      int widthSize = MeasureSpec.getSize(widthMeasureSpec);
      int heightMode = MeasureSpec.getMode(heightMeasureSpec);
      int heightSize = MeasureSpec.getSize(heightMeasureSpec);

      int width;
      int height;

      //Measure Width
      if (widthMode == MeasureSpec.EXACTLY) {
         //Must be this size
         width = widthSize;
      } else if (widthMode == MeasureSpec.AT_MOST) {
         //Can't be bigger than...
         width = Math.min(defaultWidth, widthSize);
      } else {
         //Be whatever you want
         width = defaultWidth;
      }

      //Measure Height
      if (heightMode == MeasureSpec.EXACTLY) {
         //Must be this size
         height = heightSize;
      } else if (heightMode == MeasureSpec.AT_MOST) {
         //Can't be bigger than...
         height = Math.min(defaultHeight, heightSize);
      } else {
         //Be whatever you want
         height = defaultHeight;
      }

      //needed?
      width -= getPaddingLeft() - getPaddingRight();
      height -= getPaddingTop() - getPaddingBottom();

      //MUST CALL THIS
      setMeasuredDimension(width, height);
   }

   @Override
   protected void onDraw(Canvas canvas) {
      int currentPosition = getPaddingLeft();
      int random = getRandomLineSize();
      boolean isTransparent = false;

      while (currentPosition + random < getMeasuredWidth() - getPaddingRight()) {
         // switch to the current line color (color <-> transparent)
         int lineColor = isTransparent ? Color.TRANSPARENT : mBarcodeColor;
         mPaint.setColor(lineColor);
         // draw a new line
         canvas.drawRect(
               currentPosition,
               getPaddingTop(),
               currentPosition + random,
               getMeasuredHeight() - getPaddingBottom(),
               mPaint);
         // store position next to the new line
         currentPosition += random;
         // generate a new line size
         random = getRandomLineSize();
         // switch the paint
         isTransparent = !isTransparent;
      }

      // add a last colored line at the end, to get a symmetrical barcode field
      mPaint.setColor(mBarcodeColor);
      canvas.drawRect(
            getMeasuredWidth() - getPaddingRight() - getRandomLineSize(),
            getPaddingTop(),
            getMeasuredWidth() - getPaddingRight(),
            getMeasuredHeight() - getPaddingBottom(),
            mPaint);
   }

   /**
    * Set the color of the barcode lines.
    *
    * @param color the color of the barcode lines.
    */
   void setBarcodeColor(int color) {
      mBarcodeColor = color;
      invalidate();
   }

   /**
    * Set the width of the slimmest allowed barcode line.
    *
    * @param minLineWidth the width in dp.
    */
   public void setMinLineWidth(float minLineWidth) {
      mMinLineWidth = getPixels(minLineWidth);
      invalidate();
   }

   /**
    * Set the width of the widest allowed barcode line.
    *
    * @param maxLineWidth the width in dp.
    */
   public void setMaxLineWidth(float maxLineWidth) {
      mMaxLineWidth = getPixels(maxLineWidth);
      invalidate();
   }

   /**
    * Sets the horizontal padding.
    *
    * @param padding the top and bottom padding in dp
    */
   public void setPaddingHorizontal(float padding) {
      super.setPadding(getPixels(padding), getPaddingTop(), getPixels(padding), getPaddingBottom());
   }

   /**
    * Sets the vertical padding.
    *
    * @param padding the left and right padding in dp
    */
   public void setPaddingVertical(float padding) {
      super.setPadding(getPaddingLeft(), getPixels(padding), getPaddingRight(), getPixels(padding));
   }

   private int getRandomLineSize() {
      Random r = new Random();
      // nextInt is normally exclusive of the top value, so add 1 to make it inclusive
      if (mMaxLineWidth < mMinLineWidth) {
         return r.nextInt((mMinLineWidth - mMaxLineWidth) + 1) + mMaxLineWidth;
      } else {
         return r.nextInt((mMaxLineWidth - mMinLineWidth) + 1) + mMinLineWidth;
      }
   }

   private int getPixels(float dp) {
      DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
      return (int) (dp * (metrics.densityDpi / 160f));
   }

}
