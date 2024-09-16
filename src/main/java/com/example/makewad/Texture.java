package com.example.makewad;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Arrays;

public class Texture {

    public String name;
    public BufferedImage image;
    public int width;
    public int height;
    public byte[] paletteIndices;
    public byte[][] mipmaps = new byte[4][];

    //Texture constructor
    public Texture(String name, BufferedImage file, byte[][] palette) throws IOException {
        this.name = name;
        this.image = file;
        this.width = image.getWidth();
        this.height = image.getHeight();

        // Ensure width and height are multiples of 2
        if (!isPowerOfTwo(width) || !isPowerOfTwo(height)) {
            int newwidth = nearestPowerOf2(width);
            int newheight = nearestPowerOf2(height);
            BufferedImage resizeimage = ResizeImage(file,newwidth,newheight);
            this.image = resizeimage;
        }

        this.paletteIndices = ConvertToPaletteIndices(image,palette);
        GenerateMipmaps(palette);
    }

    public int nearestPowerOf2(int value){
        int power = 1;
        while(power<value){
            power *=2;
        }
        return power;
    }

    public boolean isPowerOfTwo(int value){
        return (value & (value - 1)) == 0;
    }

    //Get the image, and the palette array, and make the image match the palette
    public byte[] ConvertToPaletteIndices(BufferedImage image, byte[][] palette) {
        System.out.println("Converting Palette to Indices");

        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        byte[] indices = new byte[width * height];

        // Populate the array with pixel data from the BufferedImage
        image.getRGB(0, 0, width, height, pixels, 0, width);

        // Process each pixel
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];

            // Extract the RGB values from the pixel
            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = pixel & 0xFF;

            // Find the nearest palette color
            int paletteIndex = FindNearestPaletteColor(r, g, b, palette);

            // Validate the palette index
            if (paletteIndex < 0 || paletteIndex >= palette.length) {
                System.err.println("Invalid palette index: " + paletteIndex);
            }

            // Store the palette index
            indices[i] = (byte) paletteIndex;

            // Debugging statements
            System.out.println("Pixel #" + i + " RGB: (" + r + ", " + g + ", " + b + ")");
            System.out.println("Palette Index: " + (indices[i] & 0xFF));
        }
        return indices;
    }


    //Finding the closest color in the palette as the original image colors
    public int FindNearestPaletteColor(int r, int g, int b, byte[][] palette) {
        int nearestIndex = 0;
        double nearestDistance = Double.MAX_VALUE;

        for (int i = 0; i < palette.length; i++) {
            int pr = palette[i][0] & 0xFF;
            int pg = palette[i][1] & 0xFF;
            int pb = palette[i][2] & 0xFF;

            double distance = Math.sqrt(Math.pow(r - pr, 2) + Math.pow(g - pg, 2) + Math.pow(b - pb, 2));
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = i;
            }
        }

        if (nearestIndex < 0 || nearestIndex >= palette.length) {
            System.err.println("Invalid nearest index: " + nearestIndex);
        }

        return nearestIndex;
    }



    //Generate mipmaps (4 different sizes)
    //level 0 : original texture size (eg. 128x128)
    //level 1 : half of texture size (eg. 64x64)
    //level 2 : half of level 1 (eg. 32x32)
    //level 3 : half of level 2 (eg. 16x16)
    public void GenerateMipmaps(byte[][] palette){
        BufferedImage currentImage = image;
        for (int level=0; level < 4; level++){
            int mipWidth = Math.max(1, width >> level);
            int mipHeight = Math.max(1, height >> level);

            currentImage = ResizeImage(currentImage,mipWidth,mipHeight);

            mipmaps[level] = ConvertToPaletteIndices(currentImage,palette);

            System.out.println("Mimap: " + Arrays.toString(mipmaps[level]));
            System.out.println("Mimap: " + currentImage);
        }
    }

    public static BufferedImage ResizeImage(BufferedImage original, int newWidth, int newHeight) {

        System.out.println("Texture Width: " + newWidth);
        System.out.println("Texture Height: " + newHeight);

        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        // Check the original image size
        System.out.println("Original Width: " + original.getWidth());
        System.out.println("Original Height: " + original.getHeight());

        // Create a Graphics2D object to draw the resized image
        Graphics2D g2d = resized.createGraphics();

        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the original image scaled to the new dimensions
        g2d.drawImage(original, 0, 0, newWidth, newHeight, null);

        g2d.dispose();

        System.out.println("Texture resize success " + resized);

        return resized;
    }

    //Getter methods
    public byte[][] GetMipmaps() { return mipmaps; }
    public byte[] GetPaletteIndices() { return paletteIndices; }
    public String GetName () { return name; }
}
