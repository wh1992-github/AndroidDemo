package com.example.helloworld;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class GenerateValueFiles {

    private final int baseW;
    private final int baseH;

    private static final String TARGET_DIR = "app/resource";
    private static final String W_TEMPLATE = "<dimen name=\"x{0}\">{1}px</dimen>\n";
    private static final String H_TEMPLATE = "<dimen name=\"y{0}\">{1}px</dimen>\n";

    private static final String VALUE_TEMPLATE = "values-{0}x{1}";

    private static final String SUPPORT_DIMESION = "720,1280;1080,1920;1440,2560;";

    private String supportStr = SUPPORT_DIMESION;

    public GenerateValueFiles(int baseX, int baseY) {
        this.baseW = baseX;
        this.baseH = baseY;
        if (!this.supportStr.contains(baseX + "," + baseY)) {
            this.supportStr += baseX + "," + baseY + ";";
        }
        File dir = new File(TARGET_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        System.out.println(dir.getAbsoluteFile());
    }

    public void generate() {
        String[] vals = supportStr.split(";");
        for (String val : vals) {
            String[] wh = val.split(",");
            generateXmlFile(Integer.parseInt(wh[0]), Integer.parseInt(wh[1]));
        }
    }

    private void generateXmlFile(int w, int h) {
        StringBuilder sbForWidth = new StringBuilder();
        sbForWidth.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForWidth.append("<resources>\n");
        float cellw = w * 1.0f / baseW;

        System.out.println("width : " + w + "," + baseW + "," + cellw);
        for (int i = 1; i < baseW; i++) {
            sbForWidth.append(W_TEMPLATE.replace("{0}", i + "").replace("{1}", change(cellw * i) + ""));
        }
        sbForWidth.append(W_TEMPLATE.replace("{0}", baseW + "").replace("{1}", w + ""));
        sbForWidth.append("</resources>\n");

        StringBuilder sbForHeight = new StringBuilder();
        sbForHeight.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForHeight.append("<resources>\n");
        float cellh = h * 1.0f / baseH;
        System.out.println("height : " + h + "," + baseH + "," + cellh);
        for (int i = 1; i < baseH; i++) {
            sbForHeight.append(H_TEMPLATE.replace("{0}", i + "").replace("{1}", change(cellh * i) + ""));
        }
        sbForHeight.append(H_TEMPLATE.replace("{0}", baseH + "").replace("{1}", h + ""));
        sbForHeight.append("</resources>");

        File fileDir = new File(TARGET_DIR + File.separator + VALUE_TEMPLATE.replace("{0}", w + "").replace("{1}", h + ""));
        fileDir.mkdir();

        File layxFile = new File(fileDir.getAbsolutePath(), "lay_x.xml");
        File layyFile = new File(fileDir.getAbsolutePath(), "lay_y.xml");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sbForWidth);
            pw.close();
            pw = new PrintWriter(new FileOutputStream(layyFile));
            pw.print(sbForHeight);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }

    public static void main(String[] args) {
        int baseW = 1440;
        int baseH = 2560;

        new GenerateValueFiles(baseW, baseH).generate();
    }

}