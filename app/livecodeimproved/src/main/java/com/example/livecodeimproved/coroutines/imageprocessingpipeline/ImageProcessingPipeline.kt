package com.example.livecodeimproved.coroutines.imageprocessingpipeline

import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.Bitmap
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.DownloadedImage
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.Image
import com.example.livecodeimproved.coroutines.imageprocessingpipeline.models.SavedImage


interface ImageProcessingPipeline {

    /**
     * Особенности:
     *
     * занимает 300–800 мс;
     * является suspend-функцией.
     */
    suspend fun download(image: Image): DownloadedImage


    /**
     * Особенности:
     *
     * занимает около 200 мс CPU;
     * НЕ является suspend;
     * является тяжелой CPU-задачей.
     */
    fun decode(image: DownloadedImage): Bitmap

    /**
     * Особенности:
     *
     * занимает 100–300 мс;
     * является suspend-функцией.
     */
    suspend fun save(bitmap: Bitmap): SavedImage

    /**
     * все изображения должны быть обработаны;
     * каждое изображение должно пройти все три стадии;
     * программа должна корректно завершиться;
     * порядок возвращаемых результатов должен соответствовать входному списку;
     * если обработка одного изображения завершилась ошибкой, остальные изображения должны продолжить обработку;
     * для каждого изображения необходимо сохранить информацию об ошибке.
     */
    suspend fun processImages(images: List<Image>): List<SavedImage>
}

