package net.kr1v.worldpanorama.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

import java.util.function.Supplier;

import static java.lang.Math.exp;

public class Tweener {
	//TODO: add easings
	//TODO: have some sort of common interface for Tweener and ScrollTweener
	Supplier<Number> target;
	double value;
	float speed;

	public Tweener(Supplier<Number> target) {
		this(target, 20);
	}

	public Tweener(Supplier<Number> target, float speed) {
		this.target = target;
		value = target.get().doubleValue();
		this.speed = speed;
	}

	public void update() {
		value = ease(value, target.get().doubleValue(), speed);
		if (isAtTarget()) value = target.get().doubleValue();
	}

	public double get() {
		return value;
	}

	public float getF() {
		return (float) value;
	}

	public int getI() {
		return (int) value;
	}

	public float getFloatingRemainder() {
		return this.getF() - (int) this.get();
	}

	public boolean isAtTarget() {
		// approxEq takes too long to say true
		var targetV = target.get();
		return Math.abs(value - targetV.doubleValue()) < 1.0E-3F;
	}

	public double getLerped(double start, double end) {
		return Mth.lerp(value, start, end);
	}

	public void snapToTarget() {
		this.value = this.target.get().doubleValue();
	}
	public static double ease(double start, double end, float speed) {
		var dt = 1.0F / Minecraft.getInstance().getFps();
		return start + (end - start) * (1 - exp(-dt * speed));
	}
}
