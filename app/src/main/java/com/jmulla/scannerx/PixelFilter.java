package com.jmulla.scannerx;

import android.graphics.Color;
import android.opengl.GLES20;

import androidx.annotation.NonNull;

import com.otaliastudios.cameraview.filter.BaseFilter;
import com.otaliastudios.cameraview.filter.OneParameterFilter;

import java.util.ArrayList;
import java.util.List;


public class PixelFilter extends BaseFilter {

  //list of colours
  private static List<Colour> colours = new ArrayList<>();
  private static List<Palette> palettes = new ArrayList<>();

  private int[] locations = new int[8];

  static {
    colours.add(new Colour(114, 27, 101));
    colours.add(new Colour(184, 13, 87));
    colours.add(new Colour(248, 97, 90));
    colours.add(new Colour(255, 216, 104));

    colours.add(new Colour(255, 178, 167));
    colours.add(new Colour(230, 115, 159));
    colours.add(new Colour(204, 14, 116));
    colours.add(new Colour(121, 12, 90));

    colours.add(new Colour(110, 87, 115));
    colours.add(new Colour(212, 80, 121));
    colours.add(new Colour(234, 144, 133));
    colours.add(new Colour(233, 225, 204));

    colours.add(new Colour(248, 177, 149));
    colours.add(new Colour(246, 114, 128));
    colours.add(new Colour(192, 108, 132));
    colours.add(new Colour(108, 86, 123));

    colours.add(new Colour(173, 235, 190));
    colours.add(new Colour(238, 243, 173));
    colours.add(new Colour(81, 96, 145));
    colours.add(new Colour(116, 190, 193));

    colours.add(new Colour(214, 248, 184));
    colours.add(new Colour(172, 222, 170));
    colours.add(new Colour(143, 187, 175));
    colours.add(new Colour(107, 123, 142));

    colours.add(new Colour(215, 234, 234));
    colours.add(new Colour(150, 146, 175));
    colours.add(new Colour(172, 219, 223));
    colours.add(new Colour(215, 234, 234));

    colours.add(new Colour(214, 52, 71));
    colours.add(new Colour(245, 123, 81));
    colours.add(new Colour(246, 238, 223));
    colours.add(new Colour(209, 206, 189));

    colours.add(new Colour(27, 38, 44));
    colours.add(new Colour(15, 76, 129));
    colours.add(new Colour(237, 102, 99));
    colours.add(new Colour(255, 163, 114));


    Palette palette1 = new Palette(colours.subList(0, 8));
    palette1.setType(PaletteType.NORMAL);
    palettes.add(palette1);

    Palette palette2 = new Palette(colours.subList(8, 16));
    palette2.setType(PaletteType.NORMAL);
    palettes.add(palette2);

    Palette palette3 = new Palette();
    palette3.setType(PaletteType.MIX);

    palette3.addColours(colours.subList(2, 5));
    palette3.addColours(colours.subList(7, 10));
    palette3.addColours(colours.subList(14, 16));
    palettes.add(palette3);

    Palette palette4 = new Palette(colours.subList(20, 28));
    palette4.setType(PaletteType.NORMAL);
    palettes.add(palette4);

    Palette palette5 = new Palette(colours.subList(28, 36));
    palette5.setType(PaletteType.NORMAL);
    palettes.add(palette5);

  }


  private final static String FRAGMENT_SHADER =
      "#extension GL_OES_EGL_image_external : require\n"
          + "precision mediump float;\n"
          + "uniform vec3 palette[8];\n"
          + "varying vec2 " + DEFAULT_FRAGMENT_TEXTURE_COORDINATE_NAME + ";\n"
          + "uniform samplerExternalOES sTexture;\n"
          + "//vec3 palette[9];\n" +
          "float distanceRGB(vec3 col1, vec3 col2){\n" +
          "    float rmean = (float(col1.r + col2.r) / 2.);\n" +
          "    float r = col1.r - col2.r;\n" +
          "    float g = col1.g - col2.g;\n" +
          "    float b = col1.b - col2.b;\n" +
          "    return sqrt((((512.+rmean)*r*r)/256.) + 4.*g*g + (((767.-rmean)*b*b))/256.);\n" +
          "}\n\n"
          + "void main() {\n"
          + "  float dx = 8.*(1./1554.);\n"
          + "  float dy = 8.*(1./1080.);\n"
          + "  vec2 coord = vec2(dx*floor(" + DEFAULT_FRAGMENT_TEXTURE_COORDINATE_NAME + ".x/dx), dy*floor(" + DEFAULT_FRAGMENT_TEXTURE_COORDINATE_NAME + ".y/dy));\n"
          + "vec3 color;\n"
          + "int counter = 0;\n"
          + "float incX = (1./1554.);\n"
          + "float incY = (1./1080.);\n"
          + "for (float zz = coord.x; zz < coord.x + (dx); zz+=incX){\n"
          + "  for (float zy = coord.y; zy < coord.y + (dy); zy+=incY){\n"
          + "    color += texture2D(sTexture, vec2(zz, zy)).xyz;\n"
          + "    counter ++;\n"
          + "  }\n"
          + "}\n"
          + "color = color/float(counter);\n"
          + "float closest = 100000.;\n" +
          "vec3 picked = vec3(0.);\n" +
          "vec3 colour2 = color;\n" +
          "vec3 colour;\n" +
          "for (int i = 0; i < 8; i++){\n" +
          "colour = palette[i];\n" +
          "float dist = distanceRGB(colour, colour2);\n" +
          "if (dist < closest){\n" +
          "closest = dist;\n" +
          "picked = colour;\n" +
          "}\n" +
          "}\n"
          + "gl_FragColor = vec4(picked, 1.0);\n"
          + "}\n";


  public PixelFilter() {
  }


  @NonNull
  @Override
  public String getFragmentShader() {
    return FRAGMENT_SHADER;
  }

  private int currentPalette = 0;

  void selectColorPalette(int palette) {
    if (currentPalette + 1 < palettes.size()) {
      currentPalette++;
    } else {
      currentPalette = 0;
    }
  }

  @Override
  public void onCreate(int programHandle) {
    super.onCreate(programHandle);
    for (int i = 0; i < palettes.get(currentPalette).getSize(); i++) {
      locations[i] = GLES20.glGetUniformLocation(programHandle, "palette" + "[" + i + "]");
    }
  }


  @Override
  protected void onPreDraw(long timestampUs, @NonNull float[] transformMatrix) {
    super.onPreDraw(timestampUs, transformMatrix);
    Palette palette = palettes.get(currentPalette);
    int size = palette.getSize();
    float[][] colours = new float[size][3];

    List<Colour> paletteColours = palette.getColours();

    for (int i = 0; i < size; i++) {
      Colour colour = paletteColours.get(i);
      float[] c = new float[]{
          colour.getR() / 255f,
          colour.getG() / 255f,
          colour.getB() / 255f
      };
      colours[i] = c;
    }


    for (int i = 0; i < size; i++) {
      GLES20.glUniform3fv(locations[i], 1, colours[i], 0);
    }

  }


}
