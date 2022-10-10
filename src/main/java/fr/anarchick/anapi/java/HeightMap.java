package fr.anarchick.anapi.java;

import org.bukkit.util.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HeightMap {

	private final int height;
	private final int width;
	private final float widthScale = 1.0f;
	private float heightScale = 1.0f;
	/** 2d array of heights for each data point */
	private final Float[][] heights;
	private Vector highest = new Vector(0, 0, 0.0F);
	private boolean dirty = false;
	
	public HeightMap(int height, int width) {
		this.height = height;
		this.width = width;
		this.heights = new Float[height][width];
		
	}
	
	public boolean add(int x, int y, Float value) {
		if ( contains(x, y) && value > 0 ) {
			if (this.heights[x][y] == null) this.heights[x][y] = 0.0F;
			this.heights[x][y] += value;
			if ((this.heights[x][y] > this.highest.getZ()) && !this.dirty) this.highest = new Vector(x, y, this.heights[x][y]);
			return true;
		}
		return false;
	}
	
	public boolean remove(int x, int y, Float value) {
		if ( contains(x, y) && value < 0) {
			if (this.heights[x][y] == null) this.heights[x][y] = 0.0F;
			this.heights[x][y] -= value;
			if (this.heights[x][y] < 0) this.heights[x][y] = 0.0F;
			this.dirty = true;
			return true;
		}
		return false;
		
	}
	
	public boolean set(int x, int y, Float value) {
		if (contains(x, y)) {
			this.heights[x][y] = value;
			if (value < 0) this.heights[x][y] = 0.0F;
			if ((this.heights[x][y] > this.highest.getZ()) && !this.dirty) this.highest = new Vector(x, y, this.heights[x][y]);
			return true;
		}
		return false;
	}

	public boolean setHeightScale(float scale) {
		if (scale > 0) {
			this.heightScale = scale;
			return true;
		}
		return false;
	}
	
	public Float getHeight(int x, int y) {
		if (contains(x, y)) {
			if (this.heights[x][y] == null) return 0.0F;
			return this.heights[x][y] * this.heightScale;
		}
		return 0.0F;
	}
	
	public Float getHeight(float x, float y) {
		return getHeight(Float.valueOf(x).intValue(), Float.valueOf(y).intValue());
	}

	public Float[][] getData() {
		return this.heights;
	}

	public Vector[] toVectors() {
		ArrayList<Vector> vecs = new ArrayList<>();
		int offsetX = (int)Math.ceil(this.width/2);
		int offsetY = (int)Math.ceil(this.height/2);
		for (int x = 0; x <= this.width; x++) {
			for (int y = 0; y <= this.height; y++) {
				float height = getHeight(x, y);
				if (height > 0) {
					Vector vec = new Vector(x - offsetX, y - offsetY, height);
					vecs.add(vec);
				}
			}
		}
		return vecs.toArray(new Vector[0]);
	}
	
	/** Z-value is the height */
	public Vector getHighest() {
		if (this.dirty) {
			Float high = 0.0F;
			for (int x = 0; x <= this.height; x++) {
				for (int y = 0; y <= this.width; y++) {
					if (this.heights[x][y] > high) {
						high = this.heights[x][y];
						this.highest = new Vector(x, y, high);
					}
				}
			}
			this.dirty = false;
		}
		return this.highest;
	}
	
	public int getH() {
		return this.height;
	}
	
	public int getW() {
		return this.width;
	}
	
	public boolean contains(float x, float y) {
		return (x >= 0 && y >= 0 && x < this.height && y < this.width);
		/*
		int h = this.height/2;
		int w = this.width/2;
		return (x >= h * -1 && y >= w * -1 && x <= h && y <= w);
		*/
	}
	
	public boolean contains(int x, int y) {
		return (x >= 0 && y >= 0 && x < this.height && y < this.width);
		/*
		int h = this.height/2;
		int w = this.width/2;
		return (x >= h * -1 && y >= w * -1 && x <= h && y <= w);
		*/
	}

	/**
	 * Get the interpolated height for x,z coords, accounting for scale, interpolated using neighbors.
	 * This will give the interpolated height when the parameters lie somewhere between actual heightmap data points.
	 * parameters assume heightmap's origin is at world coordinates x:0, z: 0
	 * @return the scale-adjusted interpolated height at specified world coordinates */
	public float getInterpolatedHeight(float xf, float zf) {
		Vector a, b, c, d;

		float baseX = (float) Math.floor(xf / widthScale);
		float baseZ = (float) Math.floor(zf / widthScale);
		float x = baseX * this.widthScale;
		float z = baseZ * this.widthScale;
		float x2 = x + this.widthScale;
		float z2 = z + this.widthScale;

		a = new Vector(x,  getHeight(x,   z2), z2);
		b = new Vector(x,  getHeight(x,   z),  z);
		c = new Vector(x2, getHeight(x2,  z),  z);
		d = new Vector(x2, getHeight(x2,  z2), z2);

		float zFrac = 1f - (zf - z) / this.widthScale;
		float xFrac = (xf - x) / this.widthScale;

		float y = (float) ((1f - zFrac) * ((1-xFrac) * a.getY() + xFrac * d.getY())
				+ zFrac * ((1-xFrac) * b.getY() + xFrac * c.getY()));

		return y;
	}
	
	public BufferedImage toImage() {
		BufferedImage image = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
		int i = 0;
		int[] grayscale = new int[height * width];
		float h, colorInt;
		float highest = (float) getHighest().getZ();
		for (int y = 0; y < this.width; y++) {
			for (int x = 0; x < this.width; x++) {
				h = getHeight(x, y);
				colorInt = h / highest;
				grayscale[i++] = new Color(colorInt, colorInt, colorInt).getRGB();
			}
		}
		image.setRGB(0, 0, height, width, grayscale, 0, width);
		return image;
	}
	
}
