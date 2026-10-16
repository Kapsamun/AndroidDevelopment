package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//import org.tensorflow.lite.Interpreter;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CelebrityClassifier {

    private static final int INPUT_SIZE = 224;
    private static final int NUM_CLASSES = 1001; // MobileNet quant labels

    private final Interpreter interpreter;
    private final List<String> labels;

    public CelebrityClassifier(Context context) throws IOException {
        interpreter = new Interpreter(loadModelFile(context, "mobilenet_v1_1.0_224_quant.tflite"));
        labels = loadLabels(context, "labels.txt");
    }

    private MappedByteBuffer loadModelFile(Context context, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private List<String> loadLabels(Context context, String labelPath) throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(labelPath)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    public String classify(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(INPUT_SIZE * INPUT_SIZE * 3);
        inputBuffer.order(ByteOrder.nativeOrder());

        int[] pixels = new int[INPUT_SIZE * INPUT_SIZE];
        resized.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE);

        for (int pixel : pixels) {
            inputBuffer.put((byte) ((pixel >> 16) & 0xFF)); // R
            inputBuffer.put((byte) ((pixel >> 8) & 0xFF));  // G
            inputBuffer.put((byte) (pixel & 0xFF));         // B
        }

        byte[][] output = new byte[1][NUM_CLASSES];
        interpreter.run(inputBuffer, output);

        // Топ-3 результата
        List<Result> results = new ArrayList<>();
        for (int i = 0; i < NUM_CLASSES && i < labels.size(); i++) {
            int score = output[0][i] & 0xFF; // quant 0–255
            if (score > 10) {
                results.add(new Result(labels.get(i), score));
            }
        }
        Collections.sort(results, (a, b) -> Integer.compare(b.score, a.score));

        StringBuilder sb = new StringBuilder("Топ результатов (MobileNet):\n\n");
        int limit = Math.min(3, results.size());
        for (int i = 0; i < limit; i++) {
            Result r = results.get(i);
            float conf = r.score / 255f * 100f;
            sb.append((i + 1)).append(". ").append(r.label)
                    .append(" — ").append(String.format("%.1f", conf)).append("%\n");
        }
        if (limit == 0) {
            sb.append("Объект не распознан с достаточной уверенностью.");
        }
        sb.append("\n(Для точного celebrity-ID нужна дообученная модель на датасете актёров)");
        return sb.toString();
    }

    public void close() {
        if (interpreter != null) interpreter.close();
    }

    private static class Result {
        final String label;
        final int score;
        Result(String label, int score) {
            this.label = label;
            this.score = score;
        }
    }
}
