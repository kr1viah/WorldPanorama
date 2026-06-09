package net.kr1v.worldpanorama.client.util;

import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

import static java.lang.Math.exp;

public class Tweener {
	private final Supplier<Number> target;
	private final Supplier<Float> speed;
	private double value;

	public Tweener(Supplier<Number> target) {
		this(target, () -> 20F);
	}

	public Tweener(Supplier<Number> target, Supplier<Float> speed) {
		this.speed = speed;
		this.target = target;
		snapToTarget();
	}

	public void update() {
		value = ease(value, target.get().doubleValue(), speed.get());
		if (isAtTarget()) snapToTarget();
	}

	public double get() {
		return value;
	}

	public boolean isAtTarget() {
		// approxEq takes too long to say true
		return Math.abs(value - target.get().doubleValue()) < 1.0E-3F;
	}

	public void snapToTarget() {
		this.value = this.target.get().doubleValue();
	}

	public void snapToValue(double yeah) {
		this.value = yeah;
	}

	public static double ease(double start, double end, float speed) {
		var dt = 1.0F / Minecraft.getInstance().getFps();
		return start + (end - start) * (1 - exp(-dt * speed));
	}

	public double updateThenGet() {
		update();
		return get();
	}
}
