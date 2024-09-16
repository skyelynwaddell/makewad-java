package com.example.makewad;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class WAD {

    // Wad Properties
    File textureDir = null;
    File palette = null;
    File outputWad = null;

    // Wad Constructor
    public WAD(File textureDir, File palette, File outputWad) {
        this.textureDir = textureDir;
        this.palette = palette;
        this.outputWad = outputWad;
    }

    // Make Wad method, this runs when we execute "MAKE WAD" button
    public static String MakeWad(File textureDir, File palette, File outputWad) throws IOException {

        if (!textureDir.exists() || !textureDir.isDirectory() || !palette.exists() || !outputWad.exists()){
            System.out.println("Error reading files, make sure you selected the proper files!");
        }

        //Create new wad object
        WAD currentWad = new WAD(textureDir,palette,outputWad);
        Color[] paletteArray = Palette.ReadPalette(palette);
        List<BufferedImage> images = FileManager.LoadAllImagesFromDirectory(textureDir);
        List<String> textureNames = FileManager.LoadAllImageNamesFromDirectory(textureDir);
        byte[][] paletteByteArray = Palette.convertPaletteToByteArray(paletteArray);

        System.out.println(currentWad.textureDir);

        for (int i = 0; i < 256; i++){
            System.out.println("Processing Color #" + i + ": " + paletteArray[i]);
            System.out.println("Color[R]: " + paletteByteArray[i][0]);
            System.out.println("Color[G]: " + paletteByteArray[i][1]);
            System.out.println("Color[B]: " + paletteByteArray[i][2]);
        }

        long[] textureOffsets = new long[images.size()];
        long currentOffset = 4 + images.size() * 12;

        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputWad))) {

            System.out.println("Writing WAD Header...");
            //Write wad Header
            WriteWadHeader(dos,images.size());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (int i=0; i < images.toArray().length; i++){
            BufferedImage textureFile = images.get(i);
            String textureName = textureNames.get(i);

            System.out.println(textureName);
            System.out.println(images.get(i));

            Texture newTexture = new Texture(textureName,textureFile,paletteByteArray);
        }

        //Texture newTexture = new Texture()

        return "";
    }

    public static void WriteWadHeader(DataOutputStream dos, int numTextures) throws IOException {
        dos.writeBytes("WAD2");

        dos.writeInt(numTextures);
        dos.writeInt(0);
        dos.writeInt(0);
    }
}
