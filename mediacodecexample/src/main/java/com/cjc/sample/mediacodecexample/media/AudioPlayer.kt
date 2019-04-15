package com.cjc.sample.mediacodecexample.media

import android.annotation.SuppressLint
import android.content.res.AssetFileDescriptor
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import android.os.Build
import java.nio.ByteBuffer
import java.io.ByteArrayOutputStream
import java.io.IOException


/**
 * Created by ChenJiancan on 2019/4/4.
 */
class AudioPlayer {

    companion object {
        const val TAG= "AudioPlayer"
    }

    fun play(afd:AssetFileDescriptor) {
        var mediaCodec: MediaCodec? = null
        val extractor = MediaExtractor()
        Log.e(TAG, "$afd")
        extractor.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)

        asyncMode(extractor)
//        for (i in 0 until mediaExtractor.trackCount) {
//            val trackFormat  = mediaExtractor.getTrackFormat(i)
//            val mineType = trackFormat.getString(MediaFormat.KEY_MIME)
//            if (mineType.startsWith("audio")) {
//                mediaExtractor.selectTrack(i)
//
//                mediaCodec = MediaCodec.createDecoderByType(mineType);
//                mediaCodec.configure(trackFormat, null, null, 0);
//                break
//            }
//
//        }
//
//        if (mediaCodec == null) {
//            Log.e(TAG, "mediaCodec == null")
//            return
//        }
//
//
//        mediaCodec.start();
//
//        val startMs = System.currentTimeMillis()
//
////        while (!decodOnce(mediaCodec, mediaExtractor)) {
////
////        }
//        work(mediaCodec, mediaExtractor)
//
//        val costMs = System.currentTimeMillis() - startMs
//        Log.e(TAG, "done: $decodeSize bytes, cost: $costMs")
//
//        mediaCodec.stop()
//        mediaCodec.release()
//
//        mediaExtractor.release()

    }

    @SuppressLint("ObsoleteSdkInt")
    private fun readData(extractor: MediaExtractor, buffer: ByteBuffer, size: Int): Int {
        var totalSize = 0
        do {
            val sampleSize: Int
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                sampleSize = extractor.readSampleData(buffer, totalSize)
            } else {
                sampleSize = extractor.readSampleData(buffer, 0)
            }
            if (sampleSize < 0) {
                return totalSize
            }
            totalSize += sampleSize
            //Advance to the next sample.
            extractor.advance()
        } while (totalSize < size)
        return totalSize
    }

    private var decodeSize = 0
    private var pcmData: ByteArrayOutputStream? = null
    private var TIMEOUT = 10 * 1000L
    private val DEFAULT_BUFFER_SIZE = 4096

    @Throws(IOException::class)
    private fun decode(codec: MediaCodec, extractor: MediaExtractor): Boolean {
        val inputDatas = codec.inputBuffers
        val outputDatas = codec.outputBuffers

        val bufferInfo = MediaCodec.BufferInfo()
        //Get the max input size.
        var size = DEFAULT_BUFFER_SIZE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        }
        for (i in 0 until inputDatas.size - 1) {
            val inputIndex = codec.dequeueInputBuffer(-1)
            if (inputIndex < 0) {
                return true
            }
            //Input data
            val inputBuffer = inputDatas[inputIndex]
            inputBuffer.clear()
            //Request mp3 data chunk.
            val sampleSize = readData(extractor, inputBuffer, size)
            if (sampleSize <= 0) {
                return true
            } else {
                //Have it decoded.
                codec.queueInputBuffer(inputIndex, 0, sampleSize, 0, 0)
                decodeSize += sampleSize
                println("decode size:$sampleSize,total size:$decodeSize")
            }
            //Get output buffer
            var outputIndex = codec.dequeueOutputBuffer(bufferInfo, TIMEOUT)
            var outputBuffer: ByteBuffer
            var chunkPCM: ByteArray
            while (outputIndex >= 0) {
                outputBuffer = outputDatas[outputIndex]
                chunkPCM = ByteArray(bufferInfo.size)
                outputBuffer.get(chunkPCM)
                outputBuffer.clear()

                //Write to a byte array.
                pcmData?.write(chunkPCM)

                codec.releaseOutputBuffer(outputIndex, false)
                //Get next PCM chuck.
                outputIndex = codec.dequeueOutputBuffer(bufferInfo, TIMEOUT)
            }
        }
        //Have not decoded the whole file.
        return false
    }

    // 速度慢，分开两个线程会不会更快
    fun decodOnce(codec:MediaCodec, extractor: MediaExtractor):Boolean {

        val inputBufferIndex = codec.dequeueInputBuffer(10)
        if (inputBufferIndex >=0) {

            val inputBuffer = codec.getInputBuffer(inputBufferIndex)

            val readSize = extractor.readSampleData(inputBuffer, 0)
            if (readSize < 0) {
                codec.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                return true
            } else {
                codec.queueInputBuffer(inputBufferIndex, 0, readSize, extractor.sampleTime, 0)
                extractor.advance()
            }

            val outputBufferInfo = MediaCodec.BufferInfo()
            val outputBufferIndex = codec.dequeueOutputBuffer(outputBufferInfo, 10)
            when (outputBufferIndex) {
                MediaCodec.INFO_OUTPUT_FORMAT_CHANGED-> {
                    Log.e(TAG, "new format: ${codec.outputFormat}")
                }

                MediaCodec.INFO_TRY_AGAIN_LATER -> {
                    Log.e(TAG, "try again")
                }

                MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED -> {
                    //
                }

                else ->{
                    Log.e(TAG, "output")
                    val outputBuffer = codec.getOutputBuffer(outputBufferIndex)

                    val bufferSize = outputBufferInfo.size

                    outputBuffer?.clear()
                    codec.releaseOutputBuffer(outputBufferIndex, false)
                    Log.e(TAG, "release")

                }


            }
        }


        return false
    }

    fun extractWork(codec: MediaCodec, extractor: MediaExtractor) {
        var extractCount = 0
        var extractSize = 0
        while (true) {
            val inputBufferIndex = codec.dequeueInputBuffer(100)
            if (inputBufferIndex >= 0) {

                val inputBuffer = codec.getInputBuffer(inputBufferIndex)

                inputBuffer?.let {
                    val readSize = extractor.readSampleData(inputBuffer, 0)
                    if (readSize < 0) {
                        codec.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        Log.e(TAG, "extractWork done")

                        Log.e(TAG, "extractCount: $extractCount  $extractSize")

                        return
                    } else {
                        codec.queueInputBuffer(inputBufferIndex, 0, readSize, extractor.sampleTime, 0)
                        Log.e(TAG, "extract ${extractor.sampleTime}")
                        extractor.advance()

                        extractCount+=1
                        extractSize += readSize

                    }
                }


            } else {
            }
        }
    }

    fun decodeWork(codec: MediaCodec, extractor: MediaExtractor) {
        var outputCount = 0
        var outputSize = 0
        while (true) {
            val outputBufferInfo = MediaCodec.BufferInfo()
            val outputBufferIndex = codec.dequeueOutputBuffer(outputBufferInfo, 100)
            when (outputBufferIndex) {
                MediaCodec.INFO_OUTPUT_FORMAT_CHANGED-> {
                    Log.e(TAG, "new format: ${codec.outputFormat}")
                }

                MediaCodec.INFO_TRY_AGAIN_LATER -> {
//                    Log.e(TAG, "try again")

                }

                MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED -> {
                    //
                }

                else ->{
                    val outputBuffer = codec.getOutputBuffer(outputBufferIndex)

                    val bufferSize = outputBufferInfo.size
                    Log.e(TAG, "output ${outputBufferInfo.presentationTimeUs}")

                    outputBuffer?.clear()
                    codec.releaseOutputBuffer(outputBufferIndex, false)

                    outputCount += 1
                    outputSize += bufferSize

                }


            }

            if (outputBufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                Log.e(TAG, "decodeWork done")

                Log.e(TAG, "outputCount: $outputCount  $outputSize")

                return
            }
        }
    }

    // 双线程： 10s
    fun work(codec: MediaCodec, extractor: MediaExtractor) {
        val thread1 = Thread {
            extractWork(codec, extractor)
        }

        val thread2 = Thread{
            decodeWork(codec, extractor)
        }

        thread1.start()
        thread2.start()

        thread1.join()
        thread2.join()
//        quickDecode(codec, extractor)
    }

    // 单线程： 9m mp3 20s
    fun quickDecode(codec: MediaCodec, extractor: MediaExtractor) {
        var extractDone = false
        var decodeDone = false
        while (!decodeDone) {

            if (!extractDone) {
                val inputBufferIndex = codec.dequeueInputBuffer(10)
                if (inputBufferIndex >= 0) {

                    val inputBuffer = codec.getInputBuffer(inputBufferIndex)

                    inputBuffer?.let {
                        val readSize = extractor.readSampleData(inputBuffer, 0)
                        if (readSize < 0) {
                            codec.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                            Log.e(TAG, "extractWork done")
                            extractDone = true
                        } else {
                            Log.e(TAG, "extract")
                            codec.queueInputBuffer(inputBufferIndex, 0, readSize, extractor.sampleTime, 0)
                            extractor.advance()
                        }
                    }


                } else {
                }
            }

            if (!decodeDone) {
                val outputBufferInfo = MediaCodec.BufferInfo()
                val outputBufferIndex = codec.dequeueOutputBuffer(outputBufferInfo, 10)
                when (outputBufferIndex) {
                    MediaCodec.INFO_OUTPUT_FORMAT_CHANGED-> {
                        Log.e(TAG, "new format: ${codec.outputFormat}")
                    }

                    MediaCodec.INFO_TRY_AGAIN_LATER -> {
                        Log.e(TAG, "try again")

                    }

                    MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED -> {
                        //
                    }

                    else ->{
                        Log.e(TAG, "output")
                        val outputBuffer = codec.getOutputBuffer(outputBufferIndex)

                        val bufferSize = outputBufferInfo.size

                        outputBuffer?.clear()
                        codec.releaseOutputBuffer(outputBufferIndex, false)


                    }


                }

                if (outputBufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                    Log.e(TAG, "decodeWork done")

                    decodeDone = true
                }
            }
        }
    }

    fun asyncMode(extractor: MediaExtractor) {
        var mediaCodec: MediaCodec? = null
        var done = false
        var outputCount = 0
        var outputSize = 0
        var extractCount = 0
        var extractSize = 0

        for (i in 0 until extractor.trackCount) {
            val trackFormat  = extractor.getTrackFormat(i)
            val mineType = trackFormat.getString(MediaFormat.KEY_MIME)
            if (mineType.startsWith("audio")) {
                extractor.selectTrack(i)

                mediaCodec = MediaCodec.createDecoderByType(mineType);
                mediaCodec?.setCallback(object :MediaCodec.Callback() {
                    override fun onOutputBufferAvailable(codec: MediaCodec, outputBufferIndex: Int, outputBufferInfo: MediaCodec.BufferInfo) {

                        when (outputBufferIndex) {
                            MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                                Log.e(TAG, "new format: ${codec.outputFormat}")
                            }

                            MediaCodec.INFO_TRY_AGAIN_LATER -> {
                                Log.e(TAG, "try again")

                            }

                            MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED -> {
                                //
                            }

                            else -> {
                                val outputBuffer = codec.getOutputBuffer(outputBufferIndex)

                                val bufferSize = outputBufferInfo.size
                                Log.e(TAG, "output ${outputBufferInfo.presentationTimeUs}")

                                outputBuffer?.clear()
                                codec.releaseOutputBuffer(outputBufferIndex, false)

                                outputCount += 1
                                outputSize += bufferSize

                            }


                        }

                        if (outputBufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                            Log.e(TAG, "decodeWork done")

                            Log.e(TAG, "outputCount: $outputCount  $outputSize")
                            done = true
                            return
                        }

                    }


                    override fun onInputBufferAvailable(codec: MediaCodec, inputBufferIndex: Int) {
                        val inputBuffer = codec.getInputBuffer(inputBufferIndex)
                        inputBuffer?.let {
                            inputBuffer.clear()
                            val readSize = extractor.readSampleData(inputBuffer, 0)
                            if (readSize < 0) {
                                codec.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                                Log.e(TAG, "extractWork done")

                                Log.e(TAG, "extractCount: $extractCount  $extractSize")

                                return
                            } else {
                                codec.queueInputBuffer(inputBufferIndex, 0, readSize, extractor.sampleTime, 0)
                                Log.e(TAG, "extract ${extractor.sampleTime}")
                                extractor.advance()

                                extractCount+=1
                                extractSize += readSize

                            }
                        }
                    }

                    override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
                    }

                    override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                        Log.e(TAG, "error: $e")
                        done = true
                    }
                })

                mediaCodec.configure(trackFormat, null, null, 0);

                break
            }

        }

        if (mediaCodec == null) {
            Log.e(TAG, "mediaCodec == null")
            return
        }


        mediaCodec.start();

        val startMs = System.currentTimeMillis()

        while (!done) {

        }

        val costMs = System.currentTimeMillis() - startMs
        Log.e(TAG, "done: $decodeSize bytes, cost: $costMs")

        mediaCodec.stop()
        mediaCodec.release()

        extractor.release()
    }
}