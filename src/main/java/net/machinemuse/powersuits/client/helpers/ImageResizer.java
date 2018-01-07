package net.machinemuse.powersuits.client.helpers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

/**
 * This program demonstrates how to resize an image.
 * http://www.codejava.net/java-se/graphics/how-to-resize-images-in-java
 * @author www.codejava.net
 *
 */
public class ImageResizer {

    /**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
    public static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight) throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);
//        Files.createTempDirectory();
//        Files.createTempFile();
// Path tempDir = Files.createTempDirectory("tempfiles");



//        Path tempFile = Files.createTempFile("tempfiles", ".tmp");


//        try {
//            // Create temp file.
//            File temp = File.createTempFile("pattern", ".suffix");
//
//            // Delete temp file when program exits.
//            temp.deleteOnExit();
//
//            // Write to temp file
//            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
//            out.write("aString");
//            out.close();
//        } catch (IOException e) {
//        }


//        File#createTempFile()
//        File#deleteOnExit()



        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    /**
     * Resizes an image by a percentage of original size (proportional).
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param percent a double number specifies percentage of the output image
     * over the input image.
     * @throws IOException
     */
    public static void resize(String inputImagePath,
                              String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }

//    /**
//     * Test resizing images
//     */
//    public static void main(String[] args) {
//        String inputImagePath = "D:/Photo/Puppy.jpg";
//        String outputImagePath1 = "D:/Photo/Puppy_Fixed.jpg";
//        String outputImagePath2 = "D:/Photo/Puppy_Smaller.jpg";
//        String outputImagePath3 = "D:/Photo/Puppy_Bigger.jpg";
//
//        try {
//            // resize to a fixed width (not proportional)
//            int scaledWidth = 1024;
//            int scaledHeight = 768;
//            ImageResizer.resize(inputImagePath, outputImagePath1, scaledWidth, scaledHeight);
//
//            // resize smaller by 50%
//            double percent = 0.5;
//            ImageResizer.resize(inputImagePath, outputImagePath2, percent);
//
//            // resize bigger by 50%
//            percent = 1.5;
//            ImageResizer.resize(inputImagePath, outputImagePath3, percent);
//
//        } catch (IOException ex) {
//            System.out.println("Error resizing the image.");
//            ex.printStackTrace();
//        }
//    }

}
