package common;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static boolean save(String path, String data) {
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write(data);
        } catch (Exception exception) {
            return false;
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    public static String read(String path) {
        File file = new File(path);
        if (!file.isFile()) return null;

        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        StringBuilder stringBuilder = new StringBuilder();

        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);

            String lineString = null;
            while ((lineString = bufferedReader.readLine()) != null) {
                stringBuilder.append(lineString).append("\r\n");
            }

        } catch (Exception exception) {
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return stringBuilder.toString();
    }

}
