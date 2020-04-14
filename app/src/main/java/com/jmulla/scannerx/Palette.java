package com.jmulla.scannerx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Palette {

  private final List<Colour> colours;
  private String description = "Nothing here yet";
  private PaletteType type = PaletteType.NONE;
  private int size = 0;


  Palette(Colour... colours){
    ArrayList<Colour> cs = new ArrayList<>(Arrays.asList(colours));
    this.colours = cs;
    this.size = cs.size();
  }

  Palette(List<Colour> colours){
    this.colours = colours;
    this.size = colours.size();
  }

  public void addColour(Colour colour){
    this.colours.add(colour);
    this.size = this.colours.size();
  }

  public void addColours(List<Colour> colours){
    this.colours.addAll(colours);
    this.size = this.colours.size();
  }

  public List<Colour> getColours() {
    return colours;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PaletteType getType() {
    return type;
  }

  public void setType(PaletteType type) {
    this.type = type;
  }

  public int getSize() {
    return this.size;
  }
}

