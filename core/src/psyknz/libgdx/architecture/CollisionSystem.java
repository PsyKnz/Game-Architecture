package psyknz.libgdx.architecture;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class CollisionSystem {
	
	public static final String DEFAULT_GROUP = "ALL";
	
	public final ObjectMap<String, Array<Collidable>> colliders;
	
	public final Array<CollisionRule> rules;
	
	public CollisionSystem() {
		this(true);
	}
	
	public CollisionSystem(boolean createDefaults) {
		colliders = new ObjectMap<String, Array<Collidable>>();
		rules = new Array<CollisionRule>();
		if(createDefaults) {
			createCollisionGroup(CollisionSystem.DEFAULT_GROUP);
			createCollisionRule(CollisionSystem.DEFAULT_GROUP, CollisionSystem.DEFAULT_GROUP);
		}
	}
	
	public void update(float delta) {
		for(int r = 0; r < rules.size; r++) {
			if(rules.get(r).groupA == rules.get(r).groupB) {
				for(int i = 0; i < colliders.get(rules.get(r).groupA).size; i++) {
					for(int j = i + 1; j < colliders.get(rules.get(r).groupA).size; j++) {
						if(colliders.get(rules.get(r).groupA).get(i).getBounds().overlaps(colliders.get(rules.get(r).groupA).get(j).getBounds())) {
							colliders.get(rules.get(r).groupA).get(i).collision(colliders.get(rules.get(r).groupA).get(j));
							colliders.get(rules.get(r).groupA).get(j).collision(colliders.get(rules.get(r).groupA).get(i));
						}
					}
				}
			}
			else {
				for(int i = 0; i < colliders.get(rules.get(r).groupA).size; i++) {
					for(int j = 0; j < colliders.get(rules.get(r).groupB).size; j++) {
						if(colliders.get(rules.get(r).groupA).get(i).getBounds().overlaps(colliders.get(rules.get(r).groupB).get(j).getBounds())) {
							colliders.get(rules.get(r).groupA).get(i).collision(colliders.get(rules.get(r).groupB).get(j));
							colliders.get(rules.get(r).groupB).get(j).collision(colliders.get(rules.get(r).groupA).get(i));
						}
					}
				}
			}
		}
	}
	
	public void createCollisionGroup(String id) {
		colliders.put(id, new Array<Collidable>());
	}
	
	public void createCollisionRule(String groupA, String groupB) {
		if(colliders.containsKey(groupA) && colliders.containsKey(groupB)) {
			rules.add(new CollisionRule(groupA, groupB));
		}
	}
	
	private class CollisionRule {
		
		public final String groupA, groupB;
		
		public CollisionRule(String groupA, String groupB) {
			this.groupA = groupA;
			this.groupB = groupB;
		}
	}

}
