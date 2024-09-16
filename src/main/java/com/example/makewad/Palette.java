package com.example.makewad;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.Color;
public class Palette {

    // The palette is a 256-color array where each color is represented by three bytes (R, G, B)
    public static Color[] ReadPalette(File paletteFile) throws IOException {
        // Check if the palette file is of correct byte size
        int paletteBytesExpected = 768;
        if (paletteFile.length() != paletteBytesExpected) {
            throw new IOException("Palette file must be 768 bytes!");
        }

        // Create a new color array with 256 slots
        Color[] palette = new Color[256];

        // Loop through the palette and find each color (3 bytes per color)
        try (FileInputStream fis = new FileInputStream(paletteFile)) {
            for (int i = 0; i < 256; i++) {
                int r = fis.read();
                int g = fis.read();
                int b = fis.read();

                palette[i] = new Color(r, g, b);
            }
        }
        return palette;
    }

    // Convert Color[] to byte[][]
    public static byte[][] convertPaletteToByteArray(Color[] palette) {
        if (palette.length != 256) {
            throw new IllegalArgumentException("Palette must contain exactly 256 colors!");
        }

        byte[][] bytePalette = new byte[256][3];  // 256 colors, each with 3 bytes (R, G, B)

        for (int i = 0; i < palette.length; i++) {
            Color color = palette[i];
            bytePalette[i][0] = (byte) color.getRed();   // Red component
            bytePalette[i][1] = (byte) color.getGreen(); // Green component
            bytePalette[i][2] = (byte) color.getBlue();  // Blue component
        }

        return bytePalette;
    }
}
