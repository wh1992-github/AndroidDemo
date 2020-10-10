package com.example.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class RunCommand {
    public static StringBuilder getRunResult(StringBuilder resultBuilder) {
        if (resultBuilder.lastIndexOf("\n") != 0) {
            String result = resultBuilder.substring(resultBuilder.lastIndexOf("\n"));
            if (!"true".equalsIgnoreCase(result.trim())) {
                return null;
            }
        }
        return resultBuilder;
    }

    public static StringBuilder run(String TAG, String command) {
        return run(TAG, command.split(" "));
    }

    public static StringBuilder run(String TAG, String... args) {
        StringBuilder resultBuilder = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(" ");
        }
        LogUtil.i(TAG, "command: " + builder.toString());
        boolean success = false;
        Process proc = null;
        BufferedReader in = null;
        BufferedReader err = null;
        PrintWriter out = null;
        try {
            proc = Runtime.getRuntime().exec(args);
            if (proc != null) {
                in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        proc.getOutputStream())), true);
                String result;
                while ((result = in.readLine()) != null) {
                    resultBuilder.append(result).append("\n");
                    LogUtil.d(TAG, "run() result:" + result);
                }
                while ((result = err.readLine()) != null) {
                    resultBuilder.append(result).append("\n");
                    LogUtil.d(TAG, "run() err:" + result);
                }
            }
        } catch (IOException e) {
            LogUtil.e(TAG, "run() IOException: " + e.getMessage());
        } catch (Exception e) {
            LogUtil.e(TAG, "run() Exception: " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (err != null) {
                try {
                    err.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                out.close();
            }
            if (proc != null) {
                try {
                    int exitValue = proc.exitValue();
                    success = 0 == exitValue;
                    LogUtil.i(TAG, "run() exit: " + exitValue + ", success: " + success);
                } catch (IllegalThreadStateException e) {
                }
                proc = null;
            }
        }
        resultBuilder.append(success);
        return resultBuilder;
    }
}
