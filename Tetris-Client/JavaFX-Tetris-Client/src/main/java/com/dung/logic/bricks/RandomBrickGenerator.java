package com.dung.logic.bricks;

import com.dung.lib.EventBusCustom;
import com.google.common.eventbus.Subscribe;
import com.dung.eventRequest.ListBricks;

import java.util.*;

public class RandomBrickGenerator extends EventBusCustom implements BrickGenerator {
	private final List<Brick> brickList;
	private final Deque<Brick> nextBricks = new ArrayDeque<>();
	private final Deque<Integer> listBrickList = new ArrayDeque<>();
	private List<Integer> listBrick = new ArrayList<Integer>();
	public RandomBrickGenerator() {
		attach(this);
		brickList = new ArrayList<>();
		brickList.add(new IBrick());
		brickList.add(new JBrick());
		brickList.add(new LBrick());
		brickList.add(new OBrick());
		brickList.add(new SBrick());
		brickList.add(new TBrick());
		brickList.add(new ZBrick());
		nextBricks.add(brickList.get(1));
		nextBricks.add(brickList.get(2));
	}

	@Override
	public Brick getBrick() {
		if (nextBricks.size() <= 1) {
			nextBricks.add(brickList.get(listBrickList.poll()));
		}
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
    
    @Subscribe
    public void addQueueListBrick(ListBricks listBricks) {
		String list = listBricks.getList();
		System.out.println(list);
		int[] arr = Arrays.stream(list.substring(1, list.length()-1).split(","))
			.map(String::trim).mapToInt(Integer::parseInt).toArray();
		for(int i = 0; i < arr.length; i++) {
			listBrickList.add(arr[i]-1);
		}
	}
}
