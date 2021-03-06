package RayTracing;
 
import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import RayTracing.PrimitiveFactory.PrimitiveType;
 
/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {
 
	public static boolean shouldPrint = false;
	
    public int imageWidth;
    public int imageHeight;
 
    /**
     * Runs the ray tracer. Takes scene file, output image file and image size as input.
     */
    public static void main(String[] args) {
 
        try {
 
            RayTracer tracer = new RayTracer();
 
            // Default values:
            tracer.imageWidth = 500;
            tracer.imageHeight = 500;
 
            if (args.length < 2)
                throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");
 
            String sceneFileName = args[0];
            String outputFileName = args[1];

            if (args.length > 3) {
                tracer.imageWidth = Integer.parseInt(args[2]);
                tracer.imageHeight = Integer.parseInt(args[3]);
            }
 
 
            // Parse scene file:
            Scene scene = tracer.parseScene(sceneFileName);
 
            // Render scene:
            tracer.renderScene(scene, outputFileName);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
 
    }
 
    /**
     * Parses the scene file and creates the scene. Change this function so it generates the required objects.
     */
    public Scene parseScene(String sceneFileName) throws IOException, RayTracingParseException {
        FileReader fr = new FileReader(sceneFileName);
 
        BufferedReader r = new BufferedReader(fr);
        String line = null;
        int lineNum = 0;
        
        System.out.println("Started parsing scene file " + sceneFileName);
 
        Camera camera = null;
        Settings settings = null;
        
        List<Light> lights = new ArrayList<Light>();
        List<Material> materials = new ArrayList<Material>();
        List<Primitive> primitives = new ArrayList<Primitive>();
        
        PrimitiveFactory primitiveFactory = new PrimitiveFactory();
        
        while ((line = r.readLine()) != null) {
            line = line.trim();
            ++lineNum;
 
            if (line.isEmpty() || (line.charAt(0) == '#')) {  // This line in the scene file is a comment
                continue;
            }
            else {
                String code = line.substring(0, 3).toLowerCase();
                // Split according to white space characters:
                String[] params = line.substring(3).trim().toLowerCase().split("\\s+");
 
                if (code.equals("cam")) {
                    camera = Camera.parse(params);
                    System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
                } else if (code.equals("set"))  {
                    settings = Settings.parse(params);
                    System.out.println(String.format("Parsed general settings (line %d)", lineNum));
                } else if (code.equals("mtl")) {
                    materials.add(Material.parse(params));
                    System.out.println(String.format("Parsed material (line %d)", lineNum));
                } else if (code.equals("sph")) {
                    primitives.add(primitiveFactory.create(PrimitiveType.SPHERE, params));
                    System.out.println(String.format("Parsed sphere (line %d)", lineNum));
                } else if (code.equals("pln")) {
                	primitives.add(primitiveFactory.create(PrimitiveType.PLANE, params));
                    System.out.println(String.format("Parsed plane (line %d)", lineNum));
                } else if (code.equals("trg")) {
                	primitives.add(primitiveFactory.create(PrimitiveType.TRIANGLE, params));
                	System.out.println(String.format("Parsed triangle (line %d)", lineNum));
                } else if (code.equals("cdr")){
                	primitives.add(primitiveFactory.create(PrimitiveType.CYLINDER, params));
                	System.out.println(String.format("Parsed cylinder (line %d)", lineNum));
                } else if (code.equals("dsc")) {
                	primitives.add(primitiveFactory.create(PrimitiveType.DISC, params));
                	System.out.println(String.format("Parsed disc (line %d)", lineNum));
                } else if (code.equals("lgt")) {
                    lights.add(Light.parse(params));
                    System.out.println(String.format("Parsed light (line %d)", lineNum));
                } else {
                	r.close();
                    throw new RayTracingParseException(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
                }
            }
        }
 
        if (camera == null || settings == null || materials.isEmpty() || lights.isEmpty() || primitives.isEmpty()) {
        	r.close();
        	throw new RayTracingParseException("Did not parse all of the required scene parameters");
        }
        System.out.println("Finished parsing scene file " + sceneFileName);
        r.close();
        return new Scene(camera, settings, materials, lights, primitives);
    }
 
    /**
     * Renders the loaded scene and saves it to the specified file location.
     */
    public void renderScene(Scene scene, String outputFileName) {
        
    	System.out.println("Rendering scene");
    	long startTime = System.currentTimeMillis();
 
        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];
 
        scene.setImageContext(imageWidth, imageHeight);
        
        for (int i = 0; i < imageWidth; i++) {
        	for (int j = 0; j < imageHeight; j++) {
        		
        		Color pixelColor = scene.calculateColorForPixel(i, j);
        		
        		rgbData[(j * this.imageWidth + i) * 3] = (byte)(pixelColor.getRed() & 0xFF);
        		rgbData[(j * this.imageWidth + i) * 3 + 1] = (byte)(pixelColor.getGreen() & 0xFF);
        		rgbData[(j * this.imageWidth + i) * 3 + 2] = (byte)(pixelColor.getBlue() & 0xFF);
        	}
        } 
 
        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;
 
                // The time is measured for your own conveniece, rendering speed will not affect your score
                // unless it is exceptionally slow (more than a couple of minutes)
        System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");
 
                // This is already implemented, and should work without adding any code.
        saveImage(this.imageWidth, rgbData, outputFileName);
 
        System.out.println("Saved file " + outputFileName);
 
    }
 
 
    //////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////
 
    /*
     * Saves RGB data as an image in png format to the specified location.
     */
    public static void saveImage(int width, byte[] rgbData, String fileName)
    {
        try {
 
            BufferedImage image = bytes2RGB(width, rgbData);
            ImageIO.write(image, "png", new File(fileName));
 
        } catch (IOException e) {
            System.out.println("ERROR SAVING FILE: " + e.getMessage());
        }
 
    }
 
    /*
     * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
     */
    public static BufferedImage bytes2RGB(int width, byte[] buffer) {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);
 
        return result;
    }
 
    public static class RayTracerException extends Exception {
        public RayTracerException(String msg) {  super(msg); }
    }
 
 
}