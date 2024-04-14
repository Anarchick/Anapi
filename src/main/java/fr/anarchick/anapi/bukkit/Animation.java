package fr.anarchick.anapi.bukkit;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Animation extends Scheduling {
	
	private final Map<Integer, Runnable> anim = new HashMap<>();
	private int time, duration;
	private boolean pause = true;
	private final boolean async;
	
	public Animation(boolean async) {
		this.async = async;
	}
	
	public Animation put(int tick, final Runnable runnable) {
		anim.put(tick, runnable);
		if (tick > this.duration) {
			this.duration = tick;
		}
		return this;
	}
	
	public void play() {
		if (pause) {
			this.pause = false;
			tick();
		}
		
	}
	public void play(int tick) {
		this.time = tick;
		play();
	}
	
	public void pause() {
		this.pause = true;
	}
	
	private void tick() {
		if (!pause && time <= duration) {
			Runnable runnable = anim.get(time);
			if (runnable != null) runnable.run();
			this.time++;
			if (async) {
				asyncDelay(1, this::tick);
			} else {
				syncDelay(1, this::tick);
			}
		}
	}

	public void stop() {
		pause();
		this.time = duration;
		anim.clear();
	}
    
	
	public Integer getDuration() {
		return duration;
	}

}
