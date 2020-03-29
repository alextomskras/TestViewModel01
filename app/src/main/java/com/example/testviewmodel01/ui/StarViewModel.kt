/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.example.testviewmodel01.ui


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testviewmodel01.model.Star
import com.example.testviewmodel01.model.Vector
import com.example.testviewmodel01.utils.rand
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.max

class StarViewModel : ViewModel() {

    private var sizeX = 0.0
    private var sizeY = 0.0
    private var origin: Vector = Vector(sizeX / 2.0, sizeY / 2.0)
    private var starVectorMagnitude = max(sizeX, sizeY)
    private var timer = Timer("Stars", false)

    val starLiveData = MutableLiveData<Star>()
    val emittingLiveData = MutableLiveData<Boolean>()

    fun startEmittingStars() {
        emittingLiveData.postValue(true)
        // Emit a new star every 20 milliseconds
        timer.schedule(0, 20) {
            // Choose random start point
            val x = rand(0, sizeX.toInt()).toDouble()
            val y = rand(0, sizeY.toInt()).toDouble()
            val starEndPosition = calculateStarEndPosition(x, y)

            val star =
                Star(x.toInt(), y.toInt(), starEndPosition.x.toInt(), starEndPosition.y.toInt())

            starLiveData.postValue(star)
        }
    }

    fun stopEmittingStars() {
        emittingLiveData.postValue(false)
        timer.cancel()
        timer = Timer("Stars", false)
    }

    fun setupDisplay(sizeX: Double, sizeY: Double) {
        this.sizeX = sizeX
        this.sizeY = sizeY
        origin = Vector(sizeX / 2.0, sizeY / 2.0)
        starVectorMagnitude = max(sizeX, sizeY)
    }

    private fun calculateStarEndPosition(x: Double, y: Double): Vector {
        // Get end position as vector from origin of magnitude starVectorMagnitude
        // and in the same direction as (x, y).
        // If unitVector is null, then just use original position.
        val position = Vector(x, y) - origin
        val unitVector = position.unitVector()
        return if (unitVector != null) {
            unitVector * starVectorMagnitude + origin
        } else {
            position + origin
        }
    }
}